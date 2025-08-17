package com.rpgsystem.rpg.application.service.character;

import com.rpgsystem.rpg.api.dto.character.CharacterCombatSkillRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterCombatSkillResponse;
import com.rpgsystem.rpg.domain.common.CharacterAccessValidator;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.CombatSkillEntity;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.domain.repository.character.CombatSkillRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CharacterCombatSkillService Tests")
class CharacterCombatSkillServiceTest {

    @Mock
    private CombatSkillRepository repository;

    @Mock
    private CharacterAccessValidator accessValidator;

    @Mock
    private CharacterService characterService;

    @InjectMocks
    private CharacterCombatSkillService characterCombatSkillService;

    private String characterId;
    private String skillId;
    private User mockUser;
    private CharacterEntity mockCharacter;
    private CombatSkillEntity mockCombatSkill;

    @BeforeEach
    void setUp() {
        characterId = UUID.randomUUID().toString();
        skillId = UUID.randomUUID().toString();
        
        mockUser = User.builder()
                .id(UUID.randomUUID().toString())
                .name("Test User")
                .email("testuser@example.com")
                .password("password123")
                .build();

        mockCharacter = CharacterEntity.builder()
                .id(characterId)
                .name("Test Character")
                .build();

        mockCombatSkill = CombatSkillEntity.builder()
                .id(skillId)
                .character(mockCharacter)
                .name("Firearms")
                .skillValue(50)
                .bookPage("Page 100")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    @Test
    @DisplayName("Should get skill successfully")
    void shouldGetSkillSuccessfully() {
        // Given
        when(characterService.getById(characterId)).thenReturn(mockCharacter);
        when(repository.findById(skillId)).thenReturn(Optional.of(mockCombatSkill));
        doNothing().when(accessValidator).validateControlAccess(mockCharacter, mockUser);

        // When
        CharacterCombatSkillResponse response = characterCombatSkillService.getSkill(skillId, characterId, mockUser);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(skillId);
        assertThat(response.getCharacterId()).isEqualTo(characterId);
        assertThat(response.getSkill()).isEqualTo("Firearms");
        assertThat(response.getCreatedAt()).isEqualTo(mockCombatSkill.getCreatedAt());
        assertThat(response.getUpdatedAt()).isEqualTo(mockCombatSkill.getUpdatedAt());

        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(mockCharacter, mockUser);
        verify(repository).findById(skillId);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when skill not found in getSkill")
    void shouldThrowEntityNotFoundExceptionWhenSkillNotFoundInGetSkill() {
        // Given
        when(characterService.getById(characterId)).thenReturn(mockCharacter);
        when(repository.findById(skillId)).thenReturn(Optional.empty());
        doNothing().when(accessValidator).validateControlAccess(mockCharacter, mockUser);

        // When & Then
        assertThatThrownBy(() -> characterCombatSkillService.getSkill(skillId, characterId, mockUser))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(skillId);

        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(mockCharacter, mockUser);
        verify(repository).findById(skillId);
    }

    @Test
    @DisplayName("Should validate character access before getting skill")
    void shouldValidateCharacterAccessBeforeGettingSkill() {
        // Given
        when(characterService.getById(characterId)).thenReturn(mockCharacter);
        doThrow(new RuntimeException("Access denied")).when(accessValidator).validateControlAccess(mockCharacter, mockUser);

        // When & Then
        assertThatThrownBy(() -> characterCombatSkillService.getSkill(skillId, characterId, mockUser))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Access denied");

        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(mockCharacter, mockUser);
        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("Should get all skills successfully")
    void shouldGetAllSkillsSuccessfully() {
        // Given
        CombatSkillEntity skill2 = CombatSkillEntity.builder()
                .id(UUID.randomUUID().toString())
                .character(mockCharacter)
                .name("Melee")
                .skillValue(40)
                .bookPage("Page 101")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        List<CombatSkillEntity> skills = Arrays.asList(mockCombatSkill, skill2);

        when(characterService.getById(characterId)).thenReturn(mockCharacter);
        when(repository.findAllByCharacter_Id(characterId)).thenReturn(skills);
        doNothing().when(accessValidator).validateControlAccess(mockCharacter, mockUser);

        // When
        List<CharacterCombatSkillResponse> responses = characterCombatSkillService.getSkills(characterId, mockUser);

        // Then
        assertThat(responses).isNotNull();
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getSkill()).isEqualTo("Firearms");
        assertThat(responses.get(1).getSkill()).isEqualTo("Melee");

        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(mockCharacter, mockUser);
        verify(repository).findAllByCharacter_Id(characterId);
    }

    @Test
    @DisplayName("Should return empty list when no skills found")
    void shouldReturnEmptyListWhenNoSkillsFound() {
        // Given
        when(characterService.getById(characterId)).thenReturn(mockCharacter);
        when(repository.findAllByCharacter_Id(characterId)).thenReturn(Collections.emptyList());
        doNothing().when(accessValidator).validateControlAccess(mockCharacter, mockUser);

        // When
        List<CharacterCombatSkillResponse> responses = characterCombatSkillService.getSkills(characterId, mockUser);

        // Then
        assertThat(responses).isNotNull();
        assertThat(responses).isEmpty();

        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(mockCharacter, mockUser);
        verify(repository).findAllByCharacter_Id(characterId);
    }

    @Test
    @DisplayName("Should validate character access before getting skills")
    void shouldValidateCharacterAccessBeforeGettingSkills() {
        // Given
        when(characterService.getById(characterId)).thenReturn(mockCharacter);
        doThrow(new RuntimeException("Access denied")).when(accessValidator).validateControlAccess(mockCharacter, mockUser);

        // When & Then
        assertThatThrownBy(() -> characterCombatSkillService.getSkills(characterId, mockUser))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Access denied");

        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(mockCharacter, mockUser);
        verify(repository, never()).findAllByCharacter_Id(characterId);
    }

    @Test
    @DisplayName("Should save skill successfully")
    void shouldSaveSkillSuccessfully() {
        // Given
        CharacterCombatSkillRequest request = CharacterCombatSkillRequest.builder()
                .skill("Updated Firearms")
                .group("Combat")
                .attribute("DEX")
                .attackCost(60)
                .defenseCost(40)
                .attackKitValue(5)
                .defenseKitValue(3)
                .build();

        CombatSkillEntity updatedSkill = CombatSkillEntity.builder()
                .id(skillId)
                .character(mockCharacter)
                .name("Updated Firearms")
                .skillValue(0) // Default value from updater
                .bookPage(null) // Default value from updater
                .createdAt(mockCombatSkill.getCreatedAt())
                .updatedAt(Instant.now())
                .build();

        when(characterService.getById(characterId)).thenReturn(mockCharacter);
        when(repository.findById(skillId)).thenReturn(Optional.of(mockCombatSkill));
        when(repository.save(any(CombatSkillEntity.class))).thenReturn(updatedSkill);
        doNothing().when(accessValidator).validateControlAccess(mockCharacter, mockUser);

        // When
        CharacterCombatSkillResponse response = characterCombatSkillService.save(request, skillId, characterId, mockUser);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(skillId);
        assertThat(response.getSkill()).isEqualTo("Updated Firearms");
        assertThat(response.getCharacterId()).isEqualTo(characterId);

        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(mockCharacter, mockUser);
        verify(repository).findById(skillId);
        verify(repository).save(any(CombatSkillEntity.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when skill not found in save")
    void shouldThrowEntityNotFoundExceptionWhenSkillNotFoundInSave() {
        // Given
        CharacterCombatSkillRequest request = CharacterCombatSkillRequest.builder()
                .skill("Updated Firearms")
                .attackCost(60)
                .defenseCost(40)
                .attackKitValue(5)
                .defenseKitValue(3)
                .build();

        when(characterService.getById(characterId)).thenReturn(mockCharacter);
        when(repository.findById(skillId)).thenReturn(Optional.empty());
        doNothing().when(accessValidator).validateControlAccess(mockCharacter, mockUser);

        // When & Then
        assertThatThrownBy(() -> characterCombatSkillService.save(request, skillId, characterId, mockUser))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(skillId);

        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(mockCharacter, mockUser);
        verify(repository).findById(skillId);
        verify(repository, never()).save(any(CombatSkillEntity.class));
    }

    @Test
    @DisplayName("Should validate character access before saving skill")
    void shouldValidateCharacterAccessBeforeSavingSkill() {
        // Given
        CharacterCombatSkillRequest request = CharacterCombatSkillRequest.builder()
                .skill("Updated Firearms")
                .attackCost(60)
                .defenseCost(40)
                .attackKitValue(5)
                .defenseKitValue(3)
                .build();

        when(characterService.getById(characterId)).thenReturn(mockCharacter);
        doThrow(new RuntimeException("Access denied")).when(accessValidator).validateControlAccess(mockCharacter, mockUser);

        // When & Then
        assertThatThrownBy(() -> characterCombatSkillService.save(request, skillId, characterId, mockUser))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Access denied");

        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(mockCharacter, mockUser);
        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("Should preserve timestamps when saving skill")
    void shouldPreserveTimestampsWhenSavingSkill() {
        // Given
        CharacterCombatSkillRequest request = CharacterCombatSkillRequest.builder()
                .skill("Updated Firearms")
                .attackCost(60)
                .defenseCost(40)
                .attackKitValue(5)
                .defenseKitValue(3)
                .build();

        Instant originalCreatedAt = Instant.now().minusSeconds(3600);
        Instant originalUpdatedAt = Instant.now().minusSeconds(1800);

        mockCombatSkill.setCreatedAt(originalCreatedAt);
        mockCombatSkill.setUpdatedAt(originalUpdatedAt);

        when(characterService.getById(characterId)).thenReturn(mockCharacter);
        when(repository.findById(skillId)).thenReturn(Optional.of(mockCombatSkill));
        when(repository.save(any(CombatSkillEntity.class))).thenAnswer(invocation -> {
            CombatSkillEntity savedEntity = invocation.getArgument(0);
            // Simulate that createdAt is preserved and updatedAt is updated by @UpdateTimestamp
            savedEntity.setUpdatedAt(Instant.now());
            return savedEntity;
        });
        doNothing().when(accessValidator).validateControlAccess(mockCharacter, mockUser);

        // When
        CharacterCombatSkillResponse response = characterCombatSkillService.save(request, skillId, characterId, mockUser);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getCreatedAt()).isEqualTo(originalCreatedAt);
        assertThat(response.getUpdatedAt()).isAfter(originalUpdatedAt);

        verify(repository).save(any(CombatSkillEntity.class));
    }

    @Test
    @DisplayName("Should apply CharacterCombatSkillUpdater when saving")
    void shouldApplyCharacterCombatSkillUpdaterWhenSaving() {
        // Given
        CharacterCombatSkillRequest request = CharacterCombatSkillRequest.builder()
                .skill("Archery")
                .group("Ranged")
                .attribute("DEX")
                .attackCost(50)
                .defenseCost(30)
                .attackKitValue(4)
                .defenseKitValue(2)
                .build();

        when(characterService.getById(characterId)).thenReturn(mockCharacter);
        when(repository.findById(skillId)).thenReturn(Optional.of(mockCombatSkill));
        when(repository.save(any(CombatSkillEntity.class))).thenAnswer(invocation -> {
            CombatSkillEntity entity = invocation.getArgument(0);
            // Verify that the updater was applied
            assertThat(entity.getName()).isEqualTo("Archery");
            assertThat(entity.getSkillValue()).isEqualTo(0); // Default from updater
            assertThat(entity.getBookPage()).isNull(); // Default from updater
            return entity;
        });
        doNothing().when(accessValidator).validateControlAccess(mockCharacter, mockUser);

        // When
        characterCombatSkillService.save(request, skillId, characterId, mockUser);

        // Then
        verify(repository).save(any(CombatSkillEntity.class));
    }

    @Test
    @DisplayName("Should handle multiple skills with different attributes")
    void shouldHandleMultipleSkillsWithDifferentAttributes() {
        // Given
        CombatSkillEntity meleeSkill = CombatSkillEntity.builder()
                .id(UUID.randomUUID().toString())
                .character(mockCharacter)
                .name("Melee")
                .skillValue(45)
                .bookPage("Page 102")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        CombatSkillEntity archerySkill = CombatSkillEntity.builder()
                .id(UUID.randomUUID().toString())
                .character(mockCharacter)
                .name("Archery")
                .skillValue(35)
                .bookPage("Page 103")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        List<CombatSkillEntity> skills = Arrays.asList(mockCombatSkill, meleeSkill, archerySkill);

        when(characterService.getById(characterId)).thenReturn(mockCharacter);
        when(repository.findAllByCharacter_Id(characterId)).thenReturn(skills);
        doNothing().when(accessValidator).validateControlAccess(mockCharacter, mockUser);

        // When
        List<CharacterCombatSkillResponse> responses = characterCombatSkillService.getSkills(characterId, mockUser);

        // Then
        assertThat(responses).hasSize(3);
        assertThat(responses.get(0).getSkill()).isEqualTo("Firearms");
        assertThat(responses.get(1).getSkill()).isEqualTo("Melee");
        assertThat(responses.get(2).getSkill()).isEqualTo("Archery");

        verify(repository).findAllByCharacter_Id(characterId);
    }
}