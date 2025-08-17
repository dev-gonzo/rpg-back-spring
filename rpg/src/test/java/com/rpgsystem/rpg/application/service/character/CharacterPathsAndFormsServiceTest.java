package com.rpgsystem.rpg.application.service.character;

import com.rpgsystem.rpg.api.dto.character.CharacterPathsAndFormsRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterPathsAndFormsResponse;
import com.rpgsystem.rpg.domain.common.CharacterAccessValidator;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.PathsAndFormsEntity;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.domain.repository.character.PathsAndFormsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CharacterPathsAndFormsServiceTest {

    @Mock
    private PathsAndFormsRepository repository;

    @Mock
    private CharacterService characterService;

    @Mock
    private CharacterAccessValidator accessValidator;

    @InjectMocks
    private CharacterPathsAndFormsService service;

    private User user;
    private CharacterEntity character;
    private PathsAndFormsEntity pathsAndForms;
    private CharacterPathsAndFormsRequest request;

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

        pathsAndForms = PathsAndFormsEntity.builder()
                .characterId("character-id")
                .character(character)
                .understandForm(5)
                .createForm(3)
                .controlForm(2)
                .fire(2)
                .water(1)
                .earth(3)
                .air(0)
                .light(1)
                .darkness(2)
                .plants(0)
                .animals(1)
                .humans(3)
                .spiritum(2)
                .arkanun(1)
                .metamagic(0)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        request = CharacterPathsAndFormsRequest.builder()
                .understandForm(7)
                .createForm(5)
                .controlForm(4)
                .fire(3)
                .water(2)
                .earth(4)
                .air(1)
                .light(2)
                .darkness(3)
                .plants(1)
                .animals(2)
                .humans(4)
                .spiritum(3)
                .arkanun(2)
                .metamagic(1)
                .build();
    }

    @Test
    void get_ShouldReturnPathsAndForms_WhenEntityExists() {
        // Given
        String characterId = "character-id";

        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(accessValidator).validateControlAccess(character, user);
        when(repository.findById(characterId)).thenReturn(Optional.of(pathsAndForms));

        // When
        CharacterPathsAndFormsResponse result = service.get(characterId, user);

        // Then
        assertNotNull(result);
        assertEquals(pathsAndForms.getCharacter().getId(), result.getCharacterId());
        assertEquals(pathsAndForms.getUnderstandForm(), result.getUnderstandForm());
        assertEquals(pathsAndForms.getCreateForm(), result.getCreateForm());
        assertEquals(pathsAndForms.getControlForm(), result.getControlForm());
        assertEquals(pathsAndForms.getFire(), result.getFire());
        assertEquals(pathsAndForms.getWater(), result.getWater());
        assertEquals(pathsAndForms.getEarth(), result.getEarth());
        assertEquals(pathsAndForms.getAir(), result.getAir());
        assertEquals(pathsAndForms.getLight(), result.getLight());
        assertEquals(pathsAndForms.getDarkness(), result.getDarkness());
        assertEquals(pathsAndForms.getPlants(), result.getPlants());
        assertEquals(pathsAndForms.getAnimals(), result.getAnimals());
        assertEquals(pathsAndForms.getHumans(), result.getHumans());
        assertEquals(pathsAndForms.getSpiritum(), result.getSpiritum());
        assertEquals(pathsAndForms.getArkanun(), result.getArkanun());
        assertEquals(pathsAndForms.getMetamagic(), result.getMetamagic());
        assertEquals(pathsAndForms.getCreatedAt(), result.getCreatedAt());
        assertEquals(pathsAndForms.getUpdatedAt(), result.getUpdatedAt());

        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository).findById(characterId);
    }

    @Test
    void get_ShouldThrowEntityNotFoundException_WhenPathsAndFormsDoesNotExist() {
        // Given
        String characterId = "character-id";

        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(accessValidator).validateControlAccess(character, user);
        when(repository.findById(characterId)).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> service.get(characterId, user)
        );

        assertEquals(characterId, exception.getMessage());

        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository).findById(characterId);
    }

    @Test
    void get_ShouldValidateCharacterAccess_BeforeRetrieving() {
        // Given
        String characterId = "character-id";

        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(accessValidator).validateControlAccess(character, user);
        when(repository.findById(characterId)).thenReturn(Optional.of(pathsAndForms));

        // When
        service.get(characterId, user);

        // Then
        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(character, user);
    }

    @Test
    void save_ShouldUpdateAndReturnPathsAndForms_WhenValidRequest() {
        // Given
        String characterId = "character-id";
        Instant originalCreatedAt = pathsAndForms.getCreatedAt();
        Instant originalUpdatedAt = pathsAndForms.getUpdatedAt();

        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(accessValidator).validateControlAccess(character, user);
        when(repository.findById(characterId)).thenReturn(Optional.of(pathsAndForms));
        when(repository.save(any(PathsAndFormsEntity.class))).thenReturn(pathsAndForms);

        // When
        CharacterPathsAndFormsResponse result = service.save(characterId, request, user);

        // Then
        assertNotNull(result);
        assertEquals(pathsAndForms.getCharacter().getId(), result.getCharacterId());
        
        // Verify that the updater was applied
        assertEquals(7, pathsAndForms.getUnderstandForm());
        assertEquals(5, pathsAndForms.getCreateForm());
        assertEquals(4, pathsAndForms.getControlForm());
        assertEquals(3, pathsAndForms.getFire());
        assertEquals(2, pathsAndForms.getWater());
        assertEquals(4, pathsAndForms.getEarth());
        assertEquals(1, pathsAndForms.getAir());
        assertEquals(2, pathsAndForms.getLight());
        assertEquals(3, pathsAndForms.getDarkness());
        assertEquals(1, pathsAndForms.getPlants());
        assertEquals(2, pathsAndForms.getAnimals());
        assertEquals(4, pathsAndForms.getHumans());
        assertEquals(3, pathsAndForms.getSpiritum());
        assertEquals(2, pathsAndForms.getArkanun());
        assertEquals(1, pathsAndForms.getMetamagic());
        
        // Verify timestamps are preserved
        assertEquals(originalCreatedAt, pathsAndForms.getCreatedAt());

        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository).findById(characterId);
        verify(repository).save(pathsAndForms);
    }

    @Test
    void save_ShouldThrowEntityNotFoundException_WhenPathsAndFormsDoesNotExist() {
        // Given
        String characterId = "character-id";

        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(accessValidator).validateControlAccess(character, user);
        when(repository.findById(characterId)).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> service.save(characterId, request, user)
        );

        assertEquals(characterId, exception.getMessage());

        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository).findById(characterId);
        verify(repository, never()).save(any());
    }

    @Test
    void save_ShouldValidateCharacterAccess_BeforeProcessing() {
        // Given
        String characterId = "character-id";

        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(accessValidator).validateControlAccess(character, user);
        when(repository.findById(characterId)).thenReturn(Optional.of(pathsAndForms));
        when(repository.save(any(PathsAndFormsEntity.class))).thenReturn(pathsAndForms);

        // When
        service.save(characterId, request, user);

        // Then
        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(character, user);
    }

    @Test
    void save_ShouldApplyCharacterPathsAndFormsUpdater_WhenSaving() {
        // Given
        String characterId = "character-id";
        Integer originalUnderstandForm = pathsAndForms.getUnderstandForm();
        Integer originalCreateForm = pathsAndForms.getCreateForm();
        Integer originalControlForm = pathsAndForms.getControlForm();
        Integer originalFire = pathsAndForms.getFire();
        Integer originalWater = pathsAndForms.getWater();
        Integer originalEarth = pathsAndForms.getEarth();
        Integer originalAir = pathsAndForms.getAir();
        Integer originalLight = pathsAndForms.getLight();
        Integer originalDarkness = pathsAndForms.getDarkness();
        Integer originalPlants = pathsAndForms.getPlants();
        Integer originalAnimals = pathsAndForms.getAnimals();
        Integer originalHumans = pathsAndForms.getHumans();
        Integer originalSpiritum = pathsAndForms.getSpiritum();
        Integer originalArkanun = pathsAndForms.getArkanun();
        Integer originalMetamagic = pathsAndForms.getMetamagic();

        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(accessValidator).validateControlAccess(character, user);
        when(repository.findById(characterId)).thenReturn(Optional.of(pathsAndForms));
        when(repository.save(any(PathsAndFormsEntity.class))).thenReturn(pathsAndForms);

        // When
        service.save(characterId, request, user);

        // Then
        // Verify that the entity was updated with request values
        assertNotEquals(originalUnderstandForm, pathsAndForms.getUnderstandForm());
        assertNotEquals(originalCreateForm, pathsAndForms.getCreateForm());
        assertNotEquals(originalControlForm, pathsAndForms.getControlForm());
        assertNotEquals(originalFire, pathsAndForms.getFire());
        assertNotEquals(originalWater, pathsAndForms.getWater());
        assertNotEquals(originalEarth, pathsAndForms.getEarth());
        assertNotEquals(originalAir, pathsAndForms.getAir());
        assertNotEquals(originalLight, pathsAndForms.getLight());
        assertNotEquals(originalDarkness, pathsAndForms.getDarkness());
        assertNotEquals(originalPlants, pathsAndForms.getPlants());
        assertNotEquals(originalAnimals, pathsAndForms.getAnimals());
        assertNotEquals(originalHumans, pathsAndForms.getHumans());
        assertNotEquals(originalSpiritum, pathsAndForms.getSpiritum());
        assertNotEquals(originalArkanun, pathsAndForms.getArkanun());
        assertNotEquals(originalMetamagic, pathsAndForms.getMetamagic());
        
        assertEquals(request.getUnderstandForm(), pathsAndForms.getUnderstandForm());
        assertEquals(request.getCreateForm(), pathsAndForms.getCreateForm());
        assertEquals(request.getControlForm(), pathsAndForms.getControlForm());
        assertEquals(request.getFire(), pathsAndForms.getFire());
        assertEquals(request.getWater(), pathsAndForms.getWater());
        assertEquals(request.getEarth(), pathsAndForms.getEarth());
        assertEquals(request.getAir(), pathsAndForms.getAir());
        assertEquals(request.getLight(), pathsAndForms.getLight());
        assertEquals(request.getDarkness(), pathsAndForms.getDarkness());
        assertEquals(request.getPlants(), pathsAndForms.getPlants());
        assertEquals(request.getAnimals(), pathsAndForms.getAnimals());
        assertEquals(request.getHumans(), pathsAndForms.getHumans());
        assertEquals(request.getSpiritum(), pathsAndForms.getSpiritum());
        assertEquals(request.getArkanun(), pathsAndForms.getArkanun());
        assertEquals(request.getMetamagic(), pathsAndForms.getMetamagic());

        verify(repository).save(pathsAndForms);
    }

    @Test
    void save_ShouldHandleMinimumValues_WhenRequestHasZeros() {
        // Given
        String characterId = "character-id";
        CharacterPathsAndFormsRequest minRequest = CharacterPathsAndFormsRequest.builder()
                .understandForm(0)
                .createForm(0)
                .controlForm(0)
                .fire(0)
                .water(0)
                .earth(0)
                .air(0)
                .light(0)
                .darkness(0)
                .plants(0)
                .animals(0)
                .humans(0)
                .spiritum(0)
                .arkanun(0)
                .metamagic(0)
                .build();

        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(accessValidator).validateControlAccess(character, user);
        when(repository.findById(characterId)).thenReturn(Optional.of(pathsAndForms));
        when(repository.save(any(PathsAndFormsEntity.class))).thenReturn(pathsAndForms);

        // When
        CharacterPathsAndFormsResponse result = service.save(characterId, minRequest, user);

        // Then
        assertNotNull(result);
        assertEquals(0, pathsAndForms.getUnderstandForm());
        assertEquals(0, pathsAndForms.getCreateForm());
        assertEquals(0, pathsAndForms.getControlForm());
        assertEquals(0, pathsAndForms.getFire());
        assertEquals(0, pathsAndForms.getWater());
        assertEquals(0, pathsAndForms.getEarth());
        assertEquals(0, pathsAndForms.getAir());
        assertEquals(0, pathsAndForms.getLight());
        assertEquals(0, pathsAndForms.getDarkness());
        assertEquals(0, pathsAndForms.getPlants());
        assertEquals(0, pathsAndForms.getAnimals());
        assertEquals(0, pathsAndForms.getHumans());
        assertEquals(0, pathsAndForms.getSpiritum());
        assertEquals(0, pathsAndForms.getArkanun());
        assertEquals(0, pathsAndForms.getMetamagic());

        verify(repository).save(pathsAndForms);
    }

    @Test
    void save_ShouldHandleMaximumValues_WhenRequestHasMaxValues() {
        // Given
        String characterId = "character-id";
        CharacterPathsAndFormsRequest maxRequest = CharacterPathsAndFormsRequest.builder()
                .understandForm(10)
                .createForm(10)
                .controlForm(10)
                .fire(4)
                .water(4)
                .earth(4)
                .air(4)
                .light(4)
                .darkness(4)
                .plants(4)
                .animals(4)
                .humans(4)
                .spiritum(4)
                .arkanun(4)
                .metamagic(4)
                .build();

        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(accessValidator).validateControlAccess(character, user);
        when(repository.findById(characterId)).thenReturn(Optional.of(pathsAndForms));
        when(repository.save(any(PathsAndFormsEntity.class))).thenReturn(pathsAndForms);

        // When
        CharacterPathsAndFormsResponse result = service.save(characterId, maxRequest, user);

        // Then
        assertNotNull(result);
        assertEquals(10, pathsAndForms.getUnderstandForm());
        assertEquals(10, pathsAndForms.getCreateForm());
        assertEquals(10, pathsAndForms.getControlForm());
        assertEquals(4, pathsAndForms.getFire());
        assertEquals(4, pathsAndForms.getWater());
        assertEquals(4, pathsAndForms.getEarth());
        assertEquals(4, pathsAndForms.getAir());
        assertEquals(4, pathsAndForms.getLight());
        assertEquals(4, pathsAndForms.getDarkness());
        assertEquals(4, pathsAndForms.getPlants());
        assertEquals(4, pathsAndForms.getAnimals());
        assertEquals(4, pathsAndForms.getHumans());
        assertEquals(4, pathsAndForms.getSpiritum());
        assertEquals(4, pathsAndForms.getArkanun());
        assertEquals(4, pathsAndForms.getMetamagic());

        verify(repository).save(pathsAndForms);
    }

    @Test
    void get_ShouldReturnCorrectResponse_WhenPathsAndFormsHasAllFields() {
        // Given
        String characterId = "character-id";
        PathsAndFormsEntity fullEntity = PathsAndFormsEntity.builder()
                .characterId(characterId)
                .character(character)
                .understandForm(8)
                .createForm(6)
                .controlForm(7)
                .fire(4)
                .water(3)
                .earth(2)
                .air(1)
                .light(4)
                .darkness(0)
                .plants(2)
                .animals(3)
                .humans(4)
                .spiritum(1)
                .arkanun(3)
                .metamagic(2)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(accessValidator).validateControlAccess(character, user);
        when(repository.findById(characterId)).thenReturn(Optional.of(fullEntity));

        // When
        CharacterPathsAndFormsResponse result = service.get(characterId, user);

        // Then
        assertNotNull(result);
        assertEquals(characterId, result.getCharacterId());
        assertEquals(8, result.getUnderstandForm());
        assertEquals(6, result.getCreateForm());
        assertEquals(7, result.getControlForm());
        assertEquals(4, result.getFire());
        assertEquals(3, result.getWater());
        assertEquals(2, result.getEarth());
        assertEquals(1, result.getAir());
        assertEquals(4, result.getLight());
        assertEquals(0, result.getDarkness());
        assertEquals(2, result.getPlants());
        assertEquals(3, result.getAnimals());
        assertEquals(4, result.getHumans());
        assertEquals(1, result.getSpiritum());
        assertEquals(3, result.getArkanun());
        assertEquals(2, result.getMetamagic());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getUpdatedAt());

        verify(repository).findById(characterId);
    }
}