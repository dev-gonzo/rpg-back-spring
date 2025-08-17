package com.rpgsystem.rpg.application.service.character;

import com.rpgsystem.rpg.api.dto.character.CharacterEquipmentRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterEquipmentResponse;
import com.rpgsystem.rpg.domain.common.CharacterAccessValidator;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.EquipmentEntity;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.domain.repository.character.EquipmentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CharacterEquipmentServiceTest {

    @Mock
    private EquipmentRepository repository;

    @Mock
    private CharacterService characterService;

    @Mock
    private CharacterAccessValidator accessValidator;

    @InjectMocks
    private CharacterEquipmentService service;

    private User user;
    private CharacterEntity character;
    private EquipmentEntity equipment;
    private CharacterEquipmentRequest request;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("Test User")
                .email("test@example.com")
                .password("password")
                .isMaster(false)
                .build();

        character = CharacterEntity.builder()
                .id("character-id")
                .controlUser(user)
                .birthDate(LocalDate.of(1990, 1, 1))
                .isKnown(true)
                .edit(true)
                .build();

        equipment = EquipmentEntity.builder()
                .id("equipment-id")
                .character(character)
                .name("Test Equipment")
                .quantity(1)
                .classification("Armor")
                .description("Test description")
                .kineticProtection(5)
                .ballisticProtection(3)
                .dexterityPenalty(1)
                .agilityPenalty(0)
                .initiative(2)
                .bookPage("Page 100")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        request = CharacterEquipmentRequest.builder()
                .name("Updated Equipment")
                .quantity(2)
                .classification("Weapon")
                .description("Updated description")
                .kineticProtection(7)
                .ballisticProtection(5)
                .dexterityPenalty(2)
                .agilityPenalty(1)
                .initiative(3)
                .bookPage("Page 200")
                .build();
    }

    @Test
    void shouldGetAllEquipmentsSuccessfully() {
        // Given
        String characterId = "character-id";
        List<EquipmentEntity> equipments = Arrays.asList(equipment);

        when(characterService.getById(characterId)).thenReturn(character);
        when(repository.findAllByCharacter_Id(characterId)).thenReturn(equipments);

        // When
        List<CharacterEquipmentResponse> result = service.getAll(characterId, user);

        // Then
        assertThat(result).hasSize(1);
        CharacterEquipmentResponse response = result.get(0);
        assertThat(response.getId()).isEqualTo("equipment-id");
        assertThat(response.getCharacterId()).isEqualTo("character-id");
        assertThat(response.getName()).isEqualTo("Test Equipment");
        assertThat(response.getQuantity()).isEqualTo(1);
        assertThat(response.getClassification()).isEqualTo("Armor");
        assertThat(response.getDescription()).isEqualTo("Test description");
        assertThat(response.getKineticProtection()).isEqualTo(5);
        assertThat(response.getBallisticProtection()).isEqualTo(3);
        assertThat(response.getDexterityPenalty()).isEqualTo(1);
        assertThat(response.getAgilityPenalty()).isEqualTo(0);
        assertThat(response.getInitiative()).isEqualTo(2);
        assertThat(response.getBookPage()).isEqualTo("Page 100");
        assertThat(response.getCreatedAt()).isNotNull();
        assertThat(response.getUpdatedAt()).isNotNull();

        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository).findAllByCharacter_Id(characterId);
    }

    @Test
    void shouldGetEquipmentByIdSuccessfully() {
        // Given
        String characterId = "character-id";
        String equipmentId = "equipment-id";

        when(characterService.getById(characterId)).thenReturn(character);
        when(repository.findById(equipmentId)).thenReturn(Optional.of(equipment));

        // When
        CharacterEquipmentResponse result = service.get(characterId, equipmentId, user);

        // Then
        assertThat(result.getId()).isEqualTo("equipment-id");
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getName()).isEqualTo("Test Equipment");
        assertThat(result.getQuantity()).isEqualTo(1);
        assertThat(result.getClassification()).isEqualTo("Armor");
        assertThat(result.getDescription()).isEqualTo("Test description");
        assertThat(result.getKineticProtection()).isEqualTo(5);
        assertThat(result.getBallisticProtection()).isEqualTo(3);
        assertThat(result.getDexterityPenalty()).isEqualTo(1);
        assertThat(result.getAgilityPenalty()).isEqualTo(0);
        assertThat(result.getInitiative()).isEqualTo(2);
        assertThat(result.getBookPage()).isEqualTo("Page 100");
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();

        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository).findById(equipmentId);
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenEquipmentNotFound() {
        // Given
        String characterId = "character-id";
        String equipmentId = "non-existent-id";

        when(characterService.getById(characterId)).thenReturn(character);
        when(repository.findById(equipmentId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> service.get(characterId, equipmentId, user))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(equipmentId);

        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository).findById(equipmentId);
    }

    @Test
    void shouldSaveEquipmentSuccessfully() {
        // Given
        String characterId = "character-id";
        String equipmentId = "equipment-id";
        EquipmentEntity savedEquipment = EquipmentEntity.builder()
                .id("equipment-id")
                .character(character)
                .name("Updated Equipment")
                .quantity(2)
                .classification("Weapon")
                .description("Updated description")
                .kineticProtection(7)
                .ballisticProtection(5)
                .dexterityPenalty(2)
                .agilityPenalty(1)
                .initiative(3)
                .bookPage("Page 200")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        when(characterService.getById(characterId)).thenReturn(character);
        when(repository.findById(equipmentId)).thenReturn(Optional.of(equipment));
        when(repository.save(any(EquipmentEntity.class))).thenReturn(savedEquipment);

        // When
        CharacterEquipmentResponse result = service.save(characterId, equipmentId, request, user);

        // Then
        assertThat(result.getId()).isEqualTo("equipment-id");
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getName()).isEqualTo("Updated Equipment");
        assertThat(result.getQuantity()).isEqualTo(2);
        assertThat(result.getClassification()).isEqualTo("Weapon");
        assertThat(result.getDescription()).isEqualTo("Updated description");
        assertThat(result.getKineticProtection()).isEqualTo(7);
        assertThat(result.getBallisticProtection()).isEqualTo(5);
        assertThat(result.getDexterityPenalty()).isEqualTo(2);
        assertThat(result.getAgilityPenalty()).isEqualTo(1);
        assertThat(result.getInitiative()).isEqualTo(3);
        assertThat(result.getBookPage()).isEqualTo("Page 200");
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();

        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository).findById(equipmentId);
        verify(repository).save(any(EquipmentEntity.class));
    }

    @Test
    void shouldHandleNullValuesInEquipmentRequest() {
        // Given
        String characterId = "character-id";
        String equipmentId = "equipment-id";
        CharacterEquipmentRequest nullRequest = CharacterEquipmentRequest.builder()
                .name("Minimal Equipment")
                .quantity(1)
                .classification("Basic")
                .description(null)
                .kineticProtection(null)
                .ballisticProtection(null)
                .dexterityPenalty(null)
                .agilityPenalty(null)
                .initiative(null)
                .bookPage(null)
                .build();

        EquipmentEntity savedEquipment = EquipmentEntity.builder()
                .id("equipment-id")
                .character(character)
                .name("Minimal Equipment")
                .quantity(1)
                .classification("Basic")
                .description(null)
                .kineticProtection(null)
                .ballisticProtection(null)
                .dexterityPenalty(null)
                .agilityPenalty(null)
                .initiative(null)
                .bookPage(null)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        when(characterService.getById(characterId)).thenReturn(character);
        when(repository.findById(equipmentId)).thenReturn(Optional.of(equipment));
        when(repository.save(any(EquipmentEntity.class))).thenReturn(savedEquipment);

        // When
        CharacterEquipmentResponse result = service.save(characterId, equipmentId, nullRequest, user);

        // Then
        assertThat(result.getId()).isEqualTo("equipment-id");
        assertThat(result.getName()).isEqualTo("Minimal Equipment");
        assertThat(result.getQuantity()).isEqualTo(1);
        assertThat(result.getClassification()).isEqualTo("Basic");
        assertThat(result.getDescription()).isNull();
        assertThat(result.getKineticProtection()).isNull();
        assertThat(result.getBallisticProtection()).isNull();
        assertThat(result.getDexterityPenalty()).isNull();
        assertThat(result.getAgilityPenalty()).isNull();
        assertThat(result.getInitiative()).isNull();
        assertThat(result.getBookPage()).isNull();

        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository).findById(equipmentId);
        verify(repository).save(any(EquipmentEntity.class));
    }

    @Test
    void shouldHandleMaximumValuesInEquipmentRequest() {
        // Given
        String characterId = "character-id";
        String equipmentId = "equipment-id";
        CharacterEquipmentRequest maxRequest = CharacterEquipmentRequest.builder()
                .name("Maximum Equipment")
                .quantity(Integer.MAX_VALUE)
                .classification("Legendary")
                .description("Maximum protection equipment")
                .kineticProtection(Integer.MAX_VALUE)
                .ballisticProtection(Integer.MAX_VALUE)
                .dexterityPenalty(Integer.MAX_VALUE)
                .agilityPenalty(Integer.MAX_VALUE)
                .initiative(Integer.MAX_VALUE)
                .bookPage("Page MAX")
                .build();

        EquipmentEntity savedEquipment = EquipmentEntity.builder()
                .id("equipment-id")
                .character(character)
                .name("Maximum Equipment")
                .quantity(Integer.MAX_VALUE)
                .classification("Legendary")
                .description("Maximum protection equipment")
                .kineticProtection(Integer.MAX_VALUE)
                .ballisticProtection(Integer.MAX_VALUE)
                .dexterityPenalty(Integer.MAX_VALUE)
                .agilityPenalty(Integer.MAX_VALUE)
                .initiative(Integer.MAX_VALUE)
                .bookPage("Page MAX")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        when(characterService.getById(characterId)).thenReturn(character);
        when(repository.findById(equipmentId)).thenReturn(Optional.of(equipment));
        when(repository.save(any(EquipmentEntity.class))).thenReturn(savedEquipment);

        // When
        CharacterEquipmentResponse result = service.save(characterId, equipmentId, maxRequest, user);

        // Then
        assertThat(result.getId()).isEqualTo("equipment-id");
        assertThat(result.getName()).isEqualTo("Maximum Equipment");
        assertThat(result.getQuantity()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getClassification()).isEqualTo("Legendary");
        assertThat(result.getDescription()).isEqualTo("Maximum protection equipment");
        assertThat(result.getKineticProtection()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getBallisticProtection()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getDexterityPenalty()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getAgilityPenalty()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getInitiative()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getBookPage()).isEqualTo("Page MAX");

        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository).findById(equipmentId);
        verify(repository).save(any(EquipmentEntity.class));
    }

    @Test
    void shouldHandleZeroValuesInEquipmentRequest() {
        // Given
        String characterId = "character-id";
        String equipmentId = "equipment-id";
        CharacterEquipmentRequest zeroRequest = CharacterEquipmentRequest.builder()
                .name("Zero Equipment")
                .quantity(0)
                .classification("None")
                .description("Zero protection equipment")
                .kineticProtection(0)
                .ballisticProtection(0)
                .dexterityPenalty(0)
                .agilityPenalty(0)
                .initiative(0)
                .bookPage("Page 0")
                .build();

        EquipmentEntity savedEquipment = EquipmentEntity.builder()
                .id("equipment-id")
                .character(character)
                .name("Zero Equipment")
                .quantity(0)
                .classification("None")
                .description("Zero protection equipment")
                .kineticProtection(0)
                .ballisticProtection(0)
                .dexterityPenalty(0)
                .agilityPenalty(0)
                .initiative(0)
                .bookPage("Page 0")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        when(characterService.getById(characterId)).thenReturn(character);
        when(repository.findById(equipmentId)).thenReturn(Optional.of(equipment));
        when(repository.save(any(EquipmentEntity.class))).thenReturn(savedEquipment);

        // When
        CharacterEquipmentResponse result = service.save(characterId, equipmentId, zeroRequest, user);

        // Then
        assertThat(result.getId()).isEqualTo("equipment-id");
        assertThat(result.getName()).isEqualTo("Zero Equipment");
        assertThat(result.getQuantity()).isEqualTo(0);
        assertThat(result.getClassification()).isEqualTo("None");
        assertThat(result.getDescription()).isEqualTo("Zero protection equipment");
        assertThat(result.getKineticProtection()).isEqualTo(0);
        assertThat(result.getBallisticProtection()).isEqualTo(0);
        assertThat(result.getDexterityPenalty()).isEqualTo(0);
        assertThat(result.getAgilityPenalty()).isEqualTo(0);
        assertThat(result.getInitiative()).isEqualTo(0);
        assertThat(result.getBookPage()).isEqualTo("Page 0");

        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository).findById(equipmentId);
        verify(repository).save(any(EquipmentEntity.class));
    }
}