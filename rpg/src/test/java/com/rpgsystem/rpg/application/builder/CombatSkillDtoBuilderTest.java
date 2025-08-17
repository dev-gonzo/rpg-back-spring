package com.rpgsystem.rpg.application.builder;

import com.rpgsystem.rpg.api.dto.character.CharacterCombatSkillResponse;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.CombatSkillEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CombatSkillDtoBuilder Tests")
class CombatSkillDtoBuilderTest {

    @Test
    @DisplayName("Should build CharacterCombatSkillResponse with all fields correctly")
    void shouldBuildCharacterCombatSkillResponseWithAllFields() {
        // Given
        String skillId = "skill-123";
        String characterId = "char-456";
        String skillName = "Espadas";
        Integer skillValue = 15;
        String bookPage = "Página 42";
        Instant createdAt = Instant.now().minusSeconds(3600);
        Instant updatedAt = Instant.now();

        CharacterEntity character = CharacterEntity.builder()
                .id(characterId)
                .build();

        CombatSkillEntity combatSkill = CombatSkillEntity.builder()
                .id(skillId)
                .character(character)
                .name(skillName)
                .skillValue(skillValue)
                .bookPage(bookPage)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        // When
        CharacterCombatSkillResponse response = CombatSkillDtoBuilder.build(combatSkill);

        // Then
        assertNotNull(response);
        assertEquals(skillId, response.getId());
        assertEquals(characterId, response.getCharacterId());
        assertEquals(skillName, response.getSkill());
        assertNull(response.getGroup()); // Campo não existe na entidade
        assertNull(response.getAttribute()); // Campo não existe na entidade
        assertNull(response.getAttackCost()); // Campo não existe na entidade
        assertNull(response.getDefenseCost()); // Campo não existe na entidade
        assertNull(response.getAttackKitValue()); // Campo não existe na entidade
        assertNull(response.getDefenseKitValue()); // Campo não existe na entidade
        assertEquals(createdAt, response.getCreatedAt());
        assertEquals(updatedAt, response.getUpdatedAt());
    }

    @Test
    @DisplayName("Should build response with null optional fields")
    void shouldBuildResponseWithNullOptionalFields() {
        // Given
        String skillId = "skill-789";
        String characterId = "char-101";
        String skillName = "Arcos";
        Instant createdAt = Instant.now().minusSeconds(1800);
        Instant updatedAt = Instant.now();

        CharacterEntity character = CharacterEntity.builder()
                .id(characterId)
                .build();

        CombatSkillEntity combatSkill = CombatSkillEntity.builder()
                .id(skillId)
                .character(character)
                .name(skillName)
                .skillValue(10)
                .bookPage(null) // Campo opcional nulo
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        // When
        CharacterCombatSkillResponse response = CombatSkillDtoBuilder.build(combatSkill);

        // Then
        assertNotNull(response);
        assertEquals(skillId, response.getId());
        assertEquals(characterId, response.getCharacterId());
        assertEquals(skillName, response.getSkill());
        assertEquals(createdAt, response.getCreatedAt());
        assertEquals(updatedAt, response.getUpdatedAt());
    }

    @Test
    @DisplayName("Should build response with minimum required fields")
    void shouldBuildResponseWithMinimumRequiredFields() {
        // Given
        String skillId = "skill-min";
        String characterId = "char-min";
        String skillName = "Habilidade Mínima";
        Instant timestamp = Instant.now();

        CharacterEntity character = CharacterEntity.builder()
                .id(characterId)
                .build();

        CombatSkillEntity combatSkill = CombatSkillEntity.builder()
                .id(skillId)
                .character(character)
                .name(skillName)
                .skillValue(1)
                .createdAt(timestamp)
                .updatedAt(timestamp)
                .build();

        // When
        CharacterCombatSkillResponse response = CombatSkillDtoBuilder.build(combatSkill);

        // Then
        assertNotNull(response);
        assertEquals(skillId, response.getId());
        assertEquals(characterId, response.getCharacterId());
        assertEquals(skillName, response.getSkill());
        assertEquals(timestamp, response.getCreatedAt());
        assertEquals(timestamp, response.getUpdatedAt());
    }

    @Test
    @DisplayName("Should build response with special characters in skill name")
    void shouldBuildResponseWithSpecialCharactersInSkillName() {
        // Given
        String skillId = "skill-special";
        String characterId = "char-special";
        String skillName = "Armas de Fogo & Explosivos (Avançado)";
        Instant createdAt = Instant.now().minusSeconds(7200);
        Instant updatedAt = Instant.now();

        CharacterEntity character = CharacterEntity.builder()
                .id(characterId)
                .build();

        CombatSkillEntity combatSkill = CombatSkillEntity.builder()
                .id(skillId)
                .character(character)
                .name(skillName)
                .skillValue(20)
                .bookPage("Pág. 123-125")
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        // When
        CharacterCombatSkillResponse response = CombatSkillDtoBuilder.build(combatSkill);

        // Then
        assertNotNull(response);
        assertEquals(skillName, response.getSkill());
        assertEquals(skillId, response.getId());
        assertEquals(characterId, response.getCharacterId());
    }

    @Test
    @DisplayName("Should build response with empty skill name")
    void shouldBuildResponseWithEmptySkillName() {
        // Given
        String skillId = "skill-empty";
        String characterId = "char-empty";
        String skillName = "";
        Instant timestamp = Instant.now();

        CharacterEntity character = CharacterEntity.builder()
                .id(characterId)
                .build();

        CombatSkillEntity combatSkill = CombatSkillEntity.builder()
                .id(skillId)
                .character(character)
                .name(skillName)
                .skillValue(0)
                .createdAt(timestamp)
                .updatedAt(timestamp)
                .build();

        // When
        CharacterCombatSkillResponse response = CombatSkillDtoBuilder.build(combatSkill);

        // Then
        assertNotNull(response);
        assertEquals("", response.getSkill());
        assertEquals(skillId, response.getId());
        assertEquals(characterId, response.getCharacterId());
    }

    @Test
    @DisplayName("Should build response with very long skill name")
    void shouldBuildResponseWithVeryLongSkillName() {
        // Given
        String skillId = "skill-long";
        String characterId = "char-long";
        String skillName = "A".repeat(255); // Nome muito longo
        Instant timestamp = Instant.now();

        CharacterEntity character = CharacterEntity.builder()
                .id(characterId)
                .build();

        CombatSkillEntity combatSkill = CombatSkillEntity.builder()
                .id(skillId)
                .character(character)
                .name(skillName)
                .skillValue(50)
                .createdAt(timestamp)
                .updatedAt(timestamp)
                .build();

        // When
        CharacterCombatSkillResponse response = CombatSkillDtoBuilder.build(combatSkill);

        // Then
        assertNotNull(response);
        assertEquals(skillName, response.getSkill());
        assertEquals(255, response.getSkill().length());
    }

    @Test
    @DisplayName("Should build response with Unicode characters in skill name")
    void shouldBuildResponseWithUnicodeCharactersInSkillName() {
        // Given
        String skillId = "skill-unicode";
        String characterId = "char-unicode";
        String skillName = "Técnicas de Combate 武術 🗡️";
        Instant timestamp = Instant.now();

        CharacterEntity character = CharacterEntity.builder()
                .id(characterId)
                .build();

        CombatSkillEntity combatSkill = CombatSkillEntity.builder()
                .id(skillId)
                .character(character)
                .name(skillName)
                .skillValue(25)
                .createdAt(timestamp)
                .updatedAt(timestamp)
                .build();

        // When
        CharacterCombatSkillResponse response = CombatSkillDtoBuilder.build(combatSkill);

        // Then
        assertNotNull(response);
        assertEquals(skillName, response.getSkill());
        assertTrue(response.getSkill().contains("武術"));
        assertTrue(response.getSkill().contains("🗡️"));
    }

    @Test
    @DisplayName("Should build response with extreme skill values")
    void shouldBuildResponseWithExtremeSkillValues() {
        // Given
        String skillId = "skill-extreme";
        String characterId = "char-extreme";
        String skillName = "Habilidade Extrema";
        Integer extremeValue = Integer.MAX_VALUE;
        Instant timestamp = Instant.now();

        CharacterEntity character = CharacterEntity.builder()
                .id(characterId)
                .build();

        CombatSkillEntity combatSkill = CombatSkillEntity.builder()
                .id(skillId)
                .character(character)
                .name(skillName)
                .skillValue(extremeValue)
                .createdAt(timestamp)
                .updatedAt(timestamp)
                .build();

        // When
        CharacterCombatSkillResponse response = CombatSkillDtoBuilder.build(combatSkill);

        // Then
        assertNotNull(response);
        assertEquals(skillName, response.getSkill());
        assertEquals(skillId, response.getId());
        assertEquals(characterId, response.getCharacterId());
    }

    @Test
    @DisplayName("Should build response with same created and updated timestamps")
    void shouldBuildResponseWithSameCreatedAndUpdatedTimestamps() {
        // Given
        String skillId = "skill-same-time";
        String characterId = "char-same-time";
        String skillName = "Nova Habilidade";
        Instant sameTimestamp = Instant.now();

        CharacterEntity character = CharacterEntity.builder()
                .id(characterId)
                .build();

        CombatSkillEntity combatSkill = CombatSkillEntity.builder()
                .id(skillId)
                .character(character)
                .name(skillName)
                .skillValue(5)
                .createdAt(sameTimestamp)
                .updatedAt(sameTimestamp)
                .build();

        // When
        CharacterCombatSkillResponse response = CombatSkillDtoBuilder.build(combatSkill);

        // Then
        assertNotNull(response);
        assertEquals(sameTimestamp, response.getCreatedAt());
        assertEquals(sameTimestamp, response.getUpdatedAt());
        assertEquals(response.getCreatedAt(), response.getUpdatedAt());
    }

    @Test
    @DisplayName("Should build response with different created and updated timestamps")
    void shouldBuildResponseWithDifferentCreatedAndUpdatedTimestamps() {
        // Given
        String skillId = "skill-diff-time";
        String characterId = "char-diff-time";
        String skillName = "Habilidade Atualizada";
        Instant createdAt = Instant.now().minusSeconds(86400); // 1 dia atrás
        Instant updatedAt = Instant.now();

        CharacterEntity character = CharacterEntity.builder()
                .id(characterId)
                .build();

        CombatSkillEntity combatSkill = CombatSkillEntity.builder()
                .id(skillId)
                .character(character)
                .name(skillName)
                .skillValue(12)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        // When
        CharacterCombatSkillResponse response = CombatSkillDtoBuilder.build(combatSkill);

        // Then
        assertNotNull(response);
        assertEquals(createdAt, response.getCreatedAt());
        assertEquals(updatedAt, response.getUpdatedAt());
        assertTrue(response.getUpdatedAt().isAfter(response.getCreatedAt()));
    }

    @Test
    @DisplayName("Should verify all null fields are correctly set")
    void shouldVerifyAllNullFieldsAreCorrectlySet() {
        // Given
        String skillId = "skill-null-check";
        String characterId = "char-null-check";
        String skillName = "Verificação de Nulos";
        Instant timestamp = Instant.now();

        CharacterEntity character = CharacterEntity.builder()
                .id(characterId)
                .build();

        CombatSkillEntity combatSkill = CombatSkillEntity.builder()
                .id(skillId)
                .character(character)
                .name(skillName)
                .skillValue(8)
                .createdAt(timestamp)
                .updatedAt(timestamp)
                .build();

        // When
        CharacterCombatSkillResponse response = CombatSkillDtoBuilder.build(combatSkill);

        // Then
        assertNotNull(response);
        // Verificar campos que devem ser nulos (não existem na entidade)
        assertNull(response.getGroup());
        assertNull(response.getAttribute());
        assertNull(response.getAttackCost());
        assertNull(response.getDefenseCost());
        assertNull(response.getAttackKitValue());
        assertNull(response.getDefenseKitValue());
        
        // Verificar campos que devem ter valores
        assertNotNull(response.getId());
        assertNotNull(response.getCharacterId());
        assertNotNull(response.getSkill());
        assertNotNull(response.getCreatedAt());
        assertNotNull(response.getUpdatedAt());
    }

    @Test
    @DisplayName("Should handle builder pattern correctly")
    void shouldHandleBuilderPatternCorrectly() {
        // Given
        String skillId = "skill-builder";
        String characterId = "char-builder";
        String skillName = "Teste Builder";
        Instant timestamp = Instant.now();

        CharacterEntity character = CharacterEntity.builder()
                .id(characterId)
                .build();

        CombatSkillEntity combatSkill = CombatSkillEntity.builder()
                .id(skillId)
                .character(character)
                .name(skillName)
                .skillValue(30)
                .bookPage("Teste")
                .createdAt(timestamp)
                .updatedAt(timestamp)
                .build();

        // When
        CharacterCombatSkillResponse response1 = CombatSkillDtoBuilder.build(combatSkill);
        CharacterCombatSkillResponse response2 = CombatSkillDtoBuilder.build(combatSkill);

        // Then
        assertNotNull(response1);
        assertNotNull(response2);
        assertNotSame(response1, response2); // Diferentes instâncias
        assertEquals(response1.getId(), response2.getId());
        assertEquals(response1.getCharacterId(), response2.getCharacterId());
        assertEquals(response1.getSkill(), response2.getSkill());
        assertEquals(response1.getCreatedAt(), response2.getCreatedAt());
        assertEquals(response1.getUpdatedAt(), response2.getUpdatedAt());
    }

    @Test
    @DisplayName("Should throw NullPointerException when combat skill entity is null")
    void shouldThrowNullPointerExceptionWhenCombatSkillEntityIsNull() {
        // Given
        CombatSkillEntity nullCombatSkill = null;

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            CombatSkillDtoBuilder.build(nullCombatSkill);
        });
    }

    @Test
    @DisplayName("Should throw NullPointerException when character is null")
    void shouldThrowNullPointerExceptionWhenCharacterIsNull() {
        // Given
        String skillId = "skill-null-char";
        String skillName = "Habilidade sem Personagem";
        Instant timestamp = Instant.now();

        CombatSkillEntity combatSkill = CombatSkillEntity.builder()
                .id(skillId)
                .character(null) // Character nulo
                .name(skillName)
                .skillValue(15)
                .createdAt(timestamp)
                .updatedAt(timestamp)
                .build();

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            CombatSkillDtoBuilder.build(combatSkill);
        });
    }

    @Test
    @DisplayName("Should not allow instantiation of CombatSkillDtoBuilder")
    void shouldNotAllowInstantiation() throws NoSuchMethodException {
        // Given
        Constructor<CombatSkillDtoBuilder> constructor = CombatSkillDtoBuilder.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // When & Then
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);
        assertThat(exception.getCause()).isInstanceOf(UnsupportedOperationException.class);
        assertThat(exception.getCause().getMessage()).isEqualTo("Utility class");
    }
}