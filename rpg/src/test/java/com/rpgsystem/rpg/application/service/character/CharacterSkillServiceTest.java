package com.rpgsystem.rpg.application.service.character;

import com.rpgsystem.rpg.api.dto.character.CharacterSkillRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterSkillResponse;
import com.rpgsystem.rpg.domain.common.CharacterAccessValidator;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.SkillEntity;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.domain.repository.character.SkillRepository;
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
@DisplayName("CharacterSkillService Tests")
class CharacterSkillServiceTest {

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private CharacterAccessValidator characterAccessValidator;

    @Mock
    private CharacterService characterService;

    @InjectMocks
    private CharacterSkillService characterSkillService;

    private User mockUser;
    private CharacterEntity mockCharacter;
    private SkillEntity mockSkill;
    private String characterId;
    private String skillId;

    @BeforeEach
    void setUp() {
        characterId = UUID.randomUUID().toString();
        skillId = UUID.randomUUID().toString();
        
        mockUser = User.builder()
                .id(UUID.randomUUID().toString())
                .name("Test User")
                .email("testuser@example.com")
                .password("password")
                .isMaster(false)
                .build();

        mockCharacter = CharacterEntity.builder()
                .id(characterId)
                .name("Test Character")
                .controlUser(mockUser)
                .birthDate(java.time.LocalDate.now())
                .isKnown(true)
                .edit(true)
                .build();

        mockSkill = SkillEntity.builder()
                .id(skillId)
                .character(mockCharacter)
                .name("Firearms")
                .skillValue(50)
                .bookPage("Page 123")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    @Test
    @DisplayName("Should get skill by id successfully")
    void shouldGetSkillByIdSuccessfully() {
        // Given
        when(characterService.getById(characterId)).thenReturn(mockCharacter);
        when(skillRepository.findById(skillId)).thenReturn(Optional.of(mockSkill));
        doNothing().when(characterAccessValidator).validateControlAccess(mockCharacter, mockUser);

        // When
        CharacterSkillResponse response = characterSkillService.getSkill(skillId, characterId, mockUser);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(skillId);
        assertThat(response.getCharacterId()).isEqualTo(characterId);
        assertThat(response.getSkill()).isEqualTo("Firearms");
        assertThat(response.getCost()).isNull(); // CharacterSkillDtoBuilder returns null for cost
        assertThat(response.getKitValue()).isNull(); // CharacterSkillDtoBuilder returns null for kitValue
        assertThat(response.getCreatedAt()).isNotNull();
        assertThat(response.getUpdatedAt()).isNotNull();

        verify(characterService).getById(characterId);
        verify(characterAccessValidator).validateControlAccess(mockCharacter, mockUser);
        verify(skillRepository).findById(skillId);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when skill not found")
    void shouldThrowEntityNotFoundExceptionWhenSkillNotFound() {
        // Given
        when(characterService.getById(characterId)).thenReturn(mockCharacter);
        when(skillRepository.findById(skillId)).thenReturn(Optional.empty());
        doNothing().when(characterAccessValidator).validateControlAccess(mockCharacter, mockUser);

        // When & Then
        assertThatThrownBy(() -> characterSkillService.getSkill(skillId, characterId, mockUser))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(skillId);

        verify(characterService).getById(characterId);
        verify(characterAccessValidator).validateControlAccess(mockCharacter, mockUser);
        verify(skillRepository).findById(skillId);
    }

    @Test
    @DisplayName("Should get all character skills successfully")
    void shouldGetAllCharacterSkillsSuccessfully() {
        // Given
        SkillEntity skill2 = SkillEntity.builder()
                .id(UUID.randomUUID().toString())
                .character(mockCharacter)
                .name("Stealth")
                .skillValue(30)
                .bookPage("Page 456")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        List<SkillEntity> skills = Arrays.asList(mockSkill, skill2);

        when(characterService.getById(characterId)).thenReturn(mockCharacter);
        when(skillRepository.findAllByCharacter_Id(characterId)).thenReturn(skills);
        doNothing().when(characterAccessValidator).validateControlAccess(mockCharacter, mockUser);

        // When
        List<CharacterSkillResponse> responses = characterSkillService.getSkills(characterId, mockUser);

        // Then
        assertThat(responses).hasSize(2);
        assertThat(responses.get(0).getSkill()).isEqualTo("Firearms");
        assertThat(responses.get(1).getSkill()).isEqualTo("Stealth");

        verify(characterService).getById(characterId);
        verify(characterAccessValidator).validateControlAccess(mockCharacter, mockUser);
        verify(skillRepository).findAllByCharacter_Id(characterId);
    }

    @Test
    @DisplayName("Should return empty list when character has no skills")
    void shouldReturnEmptyListWhenCharacterHasNoSkills() {
        // Given
        when(characterService.getById(characterId)).thenReturn(mockCharacter);
        when(skillRepository.findAllByCharacter_Id(characterId)).thenReturn(Collections.emptyList());
        doNothing().when(characterAccessValidator).validateControlAccess(mockCharacter, mockUser);

        // When
        List<CharacterSkillResponse> responses = characterSkillService.getSkills(characterId, mockUser);

        // Then
        assertThat(responses).isEmpty();

        verify(characterService).getById(characterId);
        verify(characterAccessValidator).validateControlAccess(mockCharacter, mockUser);
        verify(skillRepository).findAllByCharacter_Id(characterId);
    }

    @Test
    @DisplayName("Should save skill successfully")
    void shouldSaveSkillSuccessfully() {
        // Given
        CharacterSkillRequest request = CharacterSkillRequest.builder()
                .skill("Updated Firearms")
                .group("Combat")
                .attribute("DEX")
                .cost(60)
                .kitValue(5)
                .build();

        SkillEntity updatedSkill = SkillEntity.builder()
                .id(skillId)
                .character(mockCharacter)
                .name("Updated Firearms")
                .skillValue(60)
                .bookPage("Page 123")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        when(characterService.getById(characterId)).thenReturn(mockCharacter);
        when(skillRepository.findById(skillId)).thenReturn(Optional.of(mockSkill));
        when(skillRepository.save(any(SkillEntity.class))).thenReturn(updatedSkill);
        doNothing().when(characterAccessValidator).validateControlAccess(mockCharacter, mockUser);

        // When
        CharacterSkillResponse response = characterSkillService.save(request, skillId, characterId, mockUser);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(skillId);
        assertThat(response.getSkill()).isEqualTo("Updated Firearms");
        assertThat(response.getCost()).isNull(); // CharacterSkillDtoBuilder returns null for cost
        assertThat(response.getKitValue()).isNull(); // CharacterSkillDtoBuilder returns null for kitValue

        verify(characterService).getById(characterId);
        verify(characterAccessValidator).validateControlAccess(mockCharacter, mockUser);
        verify(skillRepository).findById(skillId);
        verify(skillRepository).save(any(SkillEntity.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when saving non-existent skill")
    void shouldThrowEntityNotFoundExceptionWhenSavingNonExistentSkill() {
        // Given
        CharacterSkillRequest request = CharacterSkillRequest.builder()
                .skill("Updated Firearms")
                .group("Combat")
                .attribute("DEX")
                .cost(60)
                .kitValue(5)
                .build();

        when(characterService.getById(characterId)).thenReturn(mockCharacter);
        when(skillRepository.findById(skillId)).thenReturn(Optional.empty());
        doNothing().when(characterAccessValidator).validateControlAccess(mockCharacter, mockUser);

        // When & Then
        assertThatThrownBy(() -> characterSkillService.save(request, skillId, characterId, mockUser))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(skillId);

        verify(characterService).getById(characterId);
        verify(characterAccessValidator).validateControlAccess(mockCharacter, mockUser);
        verify(skillRepository).findById(skillId);
        verify(skillRepository, never()).save(any(SkillEntity.class));
    }

    @Test
    @DisplayName("Should handle skill with minimum values")
    void shouldHandleSkillWithMinimumValues() {
        // Given
        CharacterSkillRequest request = CharacterSkillRequest.builder()
                .skill("Basic Skill")
                .group(null)
                .attribute(null)
                .cost(0)
                .kitValue(0)
                .build();

        when(characterService.getById(characterId)).thenReturn(mockCharacter);
        when(skillRepository.findById(skillId)).thenReturn(Optional.of(mockSkill));
        when(skillRepository.save(any(SkillEntity.class))).thenReturn(mockSkill);
        doNothing().when(characterAccessValidator).validateControlAccess(mockCharacter, mockUser);

        // When
        CharacterSkillResponse response = characterSkillService.save(request, skillId, characterId, mockUser);

        // Then
        assertThat(response).isNotNull();
        verify(skillRepository).save(any(SkillEntity.class));
    }

    @Test
    @DisplayName("Should handle skill with maximum values")
    void shouldHandleSkillWithMaximumValues() {
        // Given
        CharacterSkillRequest request = CharacterSkillRequest.builder()
                .skill("Advanced Combat Skill with Very Long Name")
                .group("Advanced Combat Group")
                .attribute("DEXTERITY")
                .cost(999)
                .kitValue(100)
                .build();

        when(characterService.getById(characterId)).thenReturn(mockCharacter);
        when(skillRepository.findById(skillId)).thenReturn(Optional.of(mockSkill));
        when(skillRepository.save(any(SkillEntity.class))).thenReturn(mockSkill);
        doNothing().when(characterAccessValidator).validateControlAccess(mockCharacter, mockUser);

        // When
        CharacterSkillResponse response = characterSkillService.save(request, skillId, characterId, mockUser);

        // Then
        assertThat(response).isNotNull();
        verify(skillRepository).save(any(SkillEntity.class));
    }

    @Test
    @DisplayName("Should validate character access for all operations")
    void shouldValidateCharacterAccessForAllOperations() {
        // Given
        when(characterService.getById(characterId)).thenReturn(mockCharacter);
        when(skillRepository.findById(skillId)).thenReturn(Optional.of(mockSkill));
        when(skillRepository.findAllByCharacter_Id(characterId)).thenReturn(Arrays.asList(mockSkill));
        when(skillRepository.save(any(SkillEntity.class))).thenReturn(mockSkill);
        doNothing().when(characterAccessValidator).validateControlAccess(mockCharacter, mockUser);

        CharacterSkillRequest request = CharacterSkillRequest.builder()
                .skill("Test Skill")
                .cost(10)
                .kitValue(1)
                .build();

        // When
        characterSkillService.getSkill(skillId, characterId, mockUser);
        characterSkillService.getSkills(characterId, mockUser);
        characterSkillService.save(request, skillId, characterId, mockUser);

        // Then
        verify(characterAccessValidator, times(3)).validateControlAccess(mockCharacter, mockUser);
        verify(characterService, times(3)).getById(characterId);
    }
}