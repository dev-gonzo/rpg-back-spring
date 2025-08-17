package com.rpgsystem.rpg.application.service.character;

import com.rpgsystem.rpg.api.dto.character.CharacterWeaponRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterWeaponResponse;
import com.rpgsystem.rpg.application.builder.CharacterWeaponDtoBuilder;
import com.rpgsystem.rpg.domain.character.updater.CharacterWeaponUpdater;
import com.rpgsystem.rpg.domain.common.CharacterAccessValidator;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.domain.entity.WeaponEntity;
import com.rpgsystem.rpg.domain.repository.character.WeaponRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CharacterWeaponServiceTest {

    @Mock
    private WeaponRepository repository;

    @Mock
    private CharacterService characterService;

    @Mock
    private CharacterAccessValidator accessValidator;

    @InjectMocks
    private CharacterWeaponService service;

    private User user;
    private CharacterEntity character;
    private WeaponEntity weapon;
    private CharacterWeaponRequest request;
    private CharacterWeaponResponse response;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id("user-id")
                .name("Test User")
                .email("test@example.com")
                .password("password")
                .isMaster(false)
                .build();

        character = new CharacterEntity();
        character.setId("character-id");
        character.setName("Test Character");

        weapon = new WeaponEntity();
        weapon.setId("weapon-id");
        weapon.setCharacter(character);
        weapon.setName("Test Weapon");
        weapon.setDescription("Test Description");
        weapon.setDamage("1d6");
        weapon.setInitiative(5);
        weapon.setRange("10m");
        weapon.setRof("1");
        weapon.setAmmunition("30");
        weapon.setBookPage("100");
        weapon.setCreatedAt(Instant.now());
        weapon.setUpdatedAt(Instant.now());

        request = CharacterWeaponRequest.builder()
                .name("Updated Weapon")
                .description("Updated Description")
                .damage("2d6")
                .initiative(7)
                .range("15m")
                .rof("2")
                .ammunition("40")
                .bookPage("150")
                .build();

        response = CharacterWeaponResponse.builder()
                .id(weapon.getId())
                .characterId(character.getId())
                .name(weapon.getName())
                .description(weapon.getDescription())
                .damage(weapon.getDamage())
                .initiative(weapon.getInitiative())
                .range(weapon.getRange())
                .rof(weapon.getRof())
                .ammunition(weapon.getAmmunition())
                .bookPage(weapon.getBookPage())
                .createdAt(weapon.getCreatedAt())
                .updatedAt(weapon.getUpdatedAt())
                .build();
    }

    @Test
    void getAll_ShouldReturnAllWeapons_WhenUserHasAccess() {
        // Given
        String characterId = "character-id";
        List<WeaponEntity> weapons = Arrays.asList(weapon);
        
        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(accessValidator).validateControlAccess(character, user);
        when(repository.findAllByCharacter_Id(characterId)).thenReturn(weapons);

        try (MockedStatic<CharacterWeaponDtoBuilder> builderMock = mockStatic(CharacterWeaponDtoBuilder.class)) {
            builderMock.when(() -> CharacterWeaponDtoBuilder.from(weapon)).thenReturn(response);

            // When
            List<CharacterWeaponResponse> result = service.getAll(characterId, user);

            // Then
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(response, result.get(0));
            verify(characterService).getById(characterId);
            verify(accessValidator).validateControlAccess(character, user);
            verify(repository).findAllByCharacter_Id(characterId);
        }
    }

    @Test
    void getAll_ShouldReturnEmptyList_WhenNoWeaponsFound() {
        // Given
        String characterId = "character-id";
        
        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(accessValidator).validateControlAccess(character, user);
        when(repository.findAllByCharacter_Id(characterId)).thenReturn(Arrays.asList());

        // When
        List<CharacterWeaponResponse> result = service.getAll(characterId, user);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository).findAllByCharacter_Id(characterId);
    }

    @Test
    void get_ShouldReturnWeapon_WhenUserHasAccessAndWeaponExists() {
        // Given
        String characterId = "character-id";
        String weaponId = "weapon-id";
        
        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(accessValidator).validateControlAccess(character, user);
        when(repository.findById(weaponId)).thenReturn(Optional.of(weapon));

        try (MockedStatic<CharacterWeaponDtoBuilder> builderMock = mockStatic(CharacterWeaponDtoBuilder.class)) {
            builderMock.when(() -> CharacterWeaponDtoBuilder.from(weapon)).thenReturn(response);

            // When
            CharacterWeaponResponse result = service.get(characterId, weaponId, user);

            // Then
            assertNotNull(result);
            assertEquals(response, result);
            verify(characterService).getById(characterId);
            verify(accessValidator).validateControlAccess(character, user);
            verify(repository).findById(weaponId);
        }
    }

    @Test
    void get_ShouldThrowEntityNotFoundException_WhenWeaponNotFound() {
        // Given
        String characterId = "character-id";
        String weaponId = "non-existent-id";
        
        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(accessValidator).validateControlAccess(character, user);
        when(repository.findById(weaponId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            service.get(characterId, weaponId, user);
        });
        
        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository).findById(weaponId);
    }

    @Test
    void save_ShouldUpdateAndReturnWeapon_WhenValidRequest() {
        // Given
        String characterId = "character-id";
        String weaponId = "weapon-id";
        WeaponEntity updatedWeapon = new WeaponEntity();
        updatedWeapon.setId(weaponId);
        updatedWeapon.setCharacter(character);
        updatedWeapon.setName(request.getName());
        updatedWeapon.setDescription(request.getDescription());
        updatedWeapon.setDamage(request.getDamage());
        updatedWeapon.setInitiative(request.getInitiative());
        updatedWeapon.setRange(request.getRange());
        updatedWeapon.setRof(request.getRof());
        updatedWeapon.setAmmunition(request.getAmmunition());
        updatedWeapon.setBookPage(request.getBookPage());
        updatedWeapon.setCreatedAt(weapon.getCreatedAt());
        updatedWeapon.setUpdatedAt(Instant.now());
        
        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(accessValidator).validateControlAccess(character, user);
        when(repository.findById(weaponId)).thenReturn(Optional.of(weapon));
        when(repository.save(any(WeaponEntity.class))).thenReturn(updatedWeapon);

        CharacterWeaponResponse updatedResponse = CharacterWeaponResponse.builder()
                .id(updatedWeapon.getId())
                .characterId(character.getId())
                .name(updatedWeapon.getName())
                .description(updatedWeapon.getDescription())
                .damage(updatedWeapon.getDamage())
                .initiative(updatedWeapon.getInitiative())
                .range(updatedWeapon.getRange())
                .rof(updatedWeapon.getRof())
                .ammunition(updatedWeapon.getAmmunition())
                .bookPage(updatedWeapon.getBookPage())
                .createdAt(updatedWeapon.getCreatedAt())
                .updatedAt(updatedWeapon.getUpdatedAt())
                .build();

        try (MockedStatic<CharacterWeaponDtoBuilder> builderMock = mockStatic(CharacterWeaponDtoBuilder.class)) {
            builderMock.when(() -> CharacterWeaponDtoBuilder.from(updatedWeapon)).thenReturn(updatedResponse);

            // When
            CharacterWeaponResponse result = service.save(characterId, weaponId, request, user);

            // Then
            assertNotNull(result);
            assertEquals(updatedResponse, result);
            verify(characterService).getById(characterId);
            verify(accessValidator).validateControlAccess(character, user);
            verify(repository).findById(weaponId);
            verify(repository).save(any(WeaponEntity.class));
        }
    }

    @Test
    void save_ShouldThrowEntityNotFoundException_WhenWeaponNotFound() {
        // Given
        String characterId = "character-id";
        String weaponId = "non-existent-id";
        
        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(accessValidator).validateControlAccess(character, user);
        when(repository.findById(weaponId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            service.save(characterId, weaponId, request, user);
        });
        
        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository).findById(weaponId);
        verify(repository, never()).save(any(WeaponEntity.class));
    }

    // Método getById é privado, testado indiretamente através dos métodos públicos

    @Test
    void save_ShouldApplyUpdaterCorrectly_WhenValidRequest() {
        // Given
        String characterId = "character-id";
        String weaponId = "weapon-id";
        
        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(accessValidator).validateControlAccess(character, user);
        when(repository.findById(weaponId)).thenReturn(Optional.of(weapon));
        when(repository.save(any(WeaponEntity.class))).thenReturn(weapon);

        try (MockedStatic<CharacterWeaponDtoBuilder> builderMock = mockStatic(CharacterWeaponDtoBuilder.class)) {
            builderMock.when(() -> CharacterWeaponDtoBuilder.from(weapon)).thenReturn(response);

            // When
            service.save(characterId, weaponId, request, user);

            // Then
            // Verify that the updater was applied by checking the weapon entity was saved
            verify(repository).save(weapon);
            // The CharacterWeaponUpdater should have been instantiated and applied
            // This is verified indirectly through the save operation
        }
    }

    @Test
    void getAll_ShouldHandleMultipleWeapons_WhenCharacterHasMultipleWeapons() {
        // Given
        String characterId = "character-id";
        WeaponEntity weapon2 = new WeaponEntity();
        weapon2.setId("weapon-2");
        weapon2.setCharacter(character);
        weapon2.setName("Second Weapon");
        weapon2.setDamage("1d8");
        weapon2.setInitiative(3);
        
        List<WeaponEntity> weapons = Arrays.asList(weapon, weapon2);
        
        CharacterWeaponResponse response2 = CharacterWeaponResponse.builder()
                .id(weapon2.getId())
                .characterId(character.getId())
                .name(weapon2.getName())
                .damage(weapon2.getDamage())
                .initiative(weapon2.getInitiative())
                .build();
        
        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(accessValidator).validateControlAccess(character, user);
        when(repository.findAllByCharacter_Id(characterId)).thenReturn(weapons);

        try (MockedStatic<CharacterWeaponDtoBuilder> builderMock = mockStatic(CharacterWeaponDtoBuilder.class)) {
            builderMock.when(() -> CharacterWeaponDtoBuilder.from(weapon)).thenReturn(response);
            builderMock.when(() -> CharacterWeaponDtoBuilder.from(weapon2)).thenReturn(response2);

            // When
            List<CharacterWeaponResponse> result = service.getAll(characterId, user);

            // Then
            assertNotNull(result);
            assertEquals(2, result.size());
            assertTrue(result.contains(response));
            assertTrue(result.contains(response2));
            verify(characterService).getById(characterId);
            verify(accessValidator).validateControlAccess(character, user);
            verify(repository).findAllByCharacter_Id(characterId);
        }
    }
}