package com.rpgsystem.rpg.application.service.character;

import com.rpgsystem.rpg.api.dto.character.CharacterRitualPowerRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterRitualPowerResponse;
import com.rpgsystem.rpg.domain.common.CharacterAccessValidator;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.RitualPowerEntity;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.domain.repository.character.RitualPowerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CharacterRitualPowerServiceTest {

    @Mock
    private RitualPowerRepository repository;

    @Mock
    private CharacterService characterService;

    @Mock
    private CharacterAccessValidator accessValidator;

    @InjectMocks
    private CharacterRitualPowerService service;

    private User user;
    private CharacterEntity character;
    private RitualPowerEntity ritualPower;
    private CharacterRitualPowerRequest request;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id("user-id")
                .email("test@example.com")
                .build();

        character = CharacterEntity.builder()
                .id("character-id")
                .name("Test Character")
                .build();

        ritualPower = RitualPowerEntity.builder()
                .id("ritual-power-id")
                .character(character)
                .name("Test Ritual Power")
                .pathsForms("Fire, Water")
                .description("A powerful ritual")
                .bookPage("Page 123")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        request = CharacterRitualPowerRequest.builder()
                .name("Updated Ritual Power")
                .pathsForms("Earth, Air")
                .description("An updated ritual")
                .bookPage("Page 456")
                .build();
    }

    @Test
    void getAll_ShouldReturnAllRitualPowers_WhenCharacterExists() {
        // Given
        String characterId = "character-id";
        List<RitualPowerEntity> ritualPowers = Arrays.asList(ritualPower);

        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(accessValidator).validateControlAccess(character, user);
        when(repository.findAllByCharacter_Id(characterId)).thenReturn(ritualPowers);

        // When
        List<CharacterRitualPowerResponse> result = service.getAll(characterId, user);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(ritualPower.getId(), result.get(0).getId());
        assertEquals(ritualPower.getName(), result.get(0).getName());
        assertEquals(ritualPower.getPathsForms(), result.get(0).getPathsForms());
        assertEquals(ritualPower.getDescription(), result.get(0).getDescription());
        assertEquals(ritualPower.getBookPage(), result.get(0).getBookPage());

        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository).findAllByCharacter_Id(characterId);
    }

    @Test
    void getAll_ShouldReturnEmptyList_WhenNoRitualPowersExist() {
        // Given
        String characterId = "character-id";

        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(accessValidator).validateControlAccess(character, user);
        when(repository.findAllByCharacter_Id(characterId)).thenReturn(Collections.emptyList());

        // When
        List<CharacterRitualPowerResponse> result = service.getAll(characterId, user);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository).findAllByCharacter_Id(characterId);
    }

    @Test
    void getAll_ShouldReturnMultipleRitualPowers_WhenMultipleExist() {
        // Given
        String characterId = "character-id";
        RitualPowerEntity secondRitualPower = RitualPowerEntity.builder()
                .id("ritual-power-2")
                .character(character)
                .name("Second Ritual Power")
                .pathsForms("Light, Darkness")
                .description("Another ritual")
                .bookPage("Page 789")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        List<RitualPowerEntity> ritualPowers = Arrays.asList(ritualPower, secondRitualPower);

        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(accessValidator).validateControlAccess(character, user);
        when(repository.findAllByCharacter_Id(characterId)).thenReturn(ritualPowers);

        // When
        List<CharacterRitualPowerResponse> result = service.getAll(characterId, user);

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(ritualPower.getId(), result.get(0).getId());
        assertEquals(secondRitualPower.getId(), result.get(1).getId());

        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository).findAllByCharacter_Id(characterId);
    }

    @Test
    void get_ShouldReturnRitualPower_WhenRitualPowerExists() {
        // Given
        String characterId = "character-id";
        String ritualPowerId = "ritual-power-id";

        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(accessValidator).validateControlAccess(character, user);
        when(repository.findById(ritualPowerId)).thenReturn(Optional.of(ritualPower));

        // When
        CharacterRitualPowerResponse result = service.get(characterId, ritualPowerId, user);

        // Then
        assertNotNull(result);
        assertEquals(ritualPower.getId(), result.getId());
        assertEquals(ritualPower.getName(), result.getName());
        assertEquals(ritualPower.getPathsForms(), result.getPathsForms());
        assertEquals(ritualPower.getDescription(), result.getDescription());
        assertEquals(ritualPower.getBookPage(), result.getBookPage());
        assertEquals(character.getId(), result.getCharacterId());

        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository).findById(ritualPowerId);
    }

    @Test
    void get_ShouldThrowEntityNotFoundException_WhenRitualPowerDoesNotExist() {
        // Given
        String characterId = "character-id";
        String ritualPowerId = "non-existent-id";

        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(accessValidator).validateControlAccess(character, user);
        when(repository.findById(ritualPowerId)).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> service.get(characterId, ritualPowerId, user)
        );

        assertEquals(ritualPowerId, exception.getMessage());

        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository).findById(ritualPowerId);
    }

    @Test
    void save_ShouldUpdateAndReturnRitualPower_WhenValidRequest() {
        // Given
        String characterId = "character-id";
        String ritualPowerId = "ritual-power-id";
        Instant originalCreatedAt = ritualPower.getCreatedAt();
        Instant originalUpdatedAt = ritualPower.getUpdatedAt();

        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(accessValidator).validateControlAccess(character, user);
        when(repository.findById(ritualPowerId)).thenReturn(Optional.of(ritualPower));
        when(repository.save(any(RitualPowerEntity.class))).thenReturn(ritualPower);

        // When
        CharacterRitualPowerResponse result = service.save(characterId, ritualPowerId, request, user);

        // Then
        assertNotNull(result);
        assertEquals(ritualPower.getId(), result.getId());
        assertEquals(character.getId(), result.getCharacterId());
        
        // Verify that the updater was applied
        assertEquals("Updated Ritual Power", ritualPower.getName());
        assertEquals("Earth, Air", ritualPower.getPathsForms());
        assertEquals("An updated ritual", ritualPower.getDescription());
        assertEquals("Page 456", ritualPower.getBookPage());
        
        // Verify timestamps are preserved
        assertEquals(originalCreatedAt, ritualPower.getCreatedAt());

        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository).findById(ritualPowerId);
        verify(repository).save(ritualPower);
    }

    @Test
    void save_ShouldThrowEntityNotFoundException_WhenRitualPowerDoesNotExist() {
        // Given
        String characterId = "character-id";
        String ritualPowerId = "non-existent-id";

        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(accessValidator).validateControlAccess(character, user);
        when(repository.findById(ritualPowerId)).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> service.save(characterId, ritualPowerId, request, user)
        );

        assertEquals(ritualPowerId, exception.getMessage());

        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository).findById(ritualPowerId);
        verify(repository, never()).save(any());
    }

    // Método getById é privado, testado indiretamente através dos métodos públicos

    @Test
    void save_ShouldValidateCharacterAccess_BeforeProcessing() {
        // Given
        String characterId = "character-id";
        String ritualPowerId = "ritual-power-id";

        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(accessValidator).validateControlAccess(character, user);
        when(repository.findById(ritualPowerId)).thenReturn(Optional.of(ritualPower));
        when(repository.save(any(RitualPowerEntity.class))).thenReturn(ritualPower);

        // When
        service.save(characterId, ritualPowerId, request, user);

        // Then
        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(character, user);
    }

    @Test
    void getAll_ShouldValidateCharacterAccess_BeforeRetrieving() {
        // Given
        String characterId = "character-id";

        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(accessValidator).validateControlAccess(character, user);
        when(repository.findAllByCharacter_Id(characterId)).thenReturn(Collections.emptyList());

        // When
        service.getAll(characterId, user);

        // Then
        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(character, user);
    }

    @Test
    void get_ShouldValidateCharacterAccess_BeforeRetrieving() {
        // Given
        String characterId = "character-id";
        String ritualPowerId = "ritual-power-id";

        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(accessValidator).validateControlAccess(character, user);
        when(repository.findById(ritualPowerId)).thenReturn(Optional.of(ritualPower));

        // When
        service.get(characterId, ritualPowerId, user);

        // Then
        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(character, user);
    }

    @Test
    void save_ShouldApplyCharacterRitualPowerUpdater_WhenSaving() {
        // Given
        String characterId = "character-id";
        String ritualPowerId = "ritual-power-id";
        String originalName = ritualPower.getName();
        String originalPathsForms = ritualPower.getPathsForms();
        String originalDescription = ritualPower.getDescription();
        String originalBookPage = ritualPower.getBookPage();

        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(accessValidator).validateControlAccess(character, user);
        when(repository.findById(ritualPowerId)).thenReturn(Optional.of(ritualPower));
        when(repository.save(any(RitualPowerEntity.class))).thenReturn(ritualPower);

        // When
        service.save(characterId, ritualPowerId, request, user);

        // Then
        // Verify that the entity was updated with request values
        assertNotEquals(originalName, ritualPower.getName());
        assertNotEquals(originalPathsForms, ritualPower.getPathsForms());
        assertNotEquals(originalDescription, ritualPower.getDescription());
        assertNotEquals(originalBookPage, ritualPower.getBookPage());
        
        assertEquals(request.getName(), ritualPower.getName());
        assertEquals(request.getPathsForms(), ritualPower.getPathsForms());
        assertEquals(request.getDescription(), ritualPower.getDescription());
        assertEquals(request.getBookPage(), ritualPower.getBookPage());

        verify(repository).save(ritualPower);
    }
}