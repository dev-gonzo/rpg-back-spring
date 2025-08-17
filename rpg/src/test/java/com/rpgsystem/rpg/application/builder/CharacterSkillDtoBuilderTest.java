package com.rpgsystem.rpg.application.builder;

import com.rpgsystem.rpg.api.dto.character.CharacterSkillResponse;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.SkillEntity;
import com.rpgsystem.rpg.domain.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CharacterSkillDtoBuilder Tests")
class CharacterSkillDtoBuilderTest {

    @Test
    @DisplayName("Should build CharacterSkillResponse from SkillEntity with all fields")
    void shouldBuildCharacterSkillResponseFromSkillEntityWithAllFields() {
        // Given
        User user = User.builder()
                .id("user-id")
                .name("Test User")
                .email("test@example.com")
                .build();

        CharacterEntity character = CharacterEntity.builder()
                .id("character-id")
                .name("Test Character")
                .controlUser(user)
                .build();

        Instant createdAt = Instant.parse("2023-01-01T10:00:00Z");
        Instant updatedAt = Instant.parse("2023-01-02T15:30:00Z");

        SkillEntity skillEntity = SkillEntity.builder()
                .id("skill-id")
                .character(character)
                .name("Sword Fighting")
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        // When
        CharacterSkillResponse result = CharacterSkillDtoBuilder.build(skillEntity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("skill-id");
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getSkill()).isEqualTo("Sword Fighting");
        assertThat(result.getGroup()).isNull();
        assertThat(result.getAttribute()).isNull();
        assertThat(result.getCost()).isNull();
        assertThat(result.getKitValue()).isNull();
        assertThat(result.getCreatedAt()).isEqualTo(createdAt);
        assertThat(result.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("Should build CharacterSkillResponse from SkillEntity with null name")
    void shouldBuildCharacterSkillResponseFromSkillEntityWithNullName() {
        // Given
        User user = User.builder()
                .id("user-id")
                .name("Test User")
                .email("test@example.com")
                .build();

        CharacterEntity character = CharacterEntity.builder()
                .id("character-id")
                .name("Test Character")
                .controlUser(user)
                .build();

        SkillEntity skillEntity = SkillEntity.builder()
                .id("skill-id")
                .character(character)
                .name(null)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        // When
        CharacterSkillResponse result = CharacterSkillDtoBuilder.build(skillEntity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("skill-id");
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getSkill()).isNull();
    }

    @Test
    @DisplayName("Should build CharacterSkillResponse from SkillEntity with null timestamps")
    void shouldBuildCharacterSkillResponseFromSkillEntityWithNullTimestamps() {
        // Given
        User user = User.builder()
                .id("user-id")
                .name("Test User")
                .email("test@example.com")
                .build();

        CharacterEntity character = CharacterEntity.builder()
                .id("character-id")
                .name("Test Character")
                .controlUser(user)
                .build();

        SkillEntity skillEntity = SkillEntity.builder()
                .id("skill-id")
                .character(character)
                .name("Magic")
                .createdAt(null)
                .updatedAt(null)
                .build();

        // When
        CharacterSkillResponse result = CharacterSkillDtoBuilder.build(skillEntity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("skill-id");
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getSkill()).isEqualTo("Magic");
        assertThat(result.getCreatedAt()).isNull();
        assertThat(result.getUpdatedAt()).isNull();
    }

    @Test
    @DisplayName("Should throw NullPointerException when SkillEntity is null")
    void shouldThrowNullPointerExceptionWhenSkillEntityIsNull() {
        // When & Then
        assertThrows(NullPointerException.class, () -> CharacterSkillDtoBuilder.build(null));
    }

    @Test
    @DisplayName("Should throw NullPointerException when character is null")
    void shouldThrowNullPointerExceptionWhenCharacterIsNull() {
        // Given
        SkillEntity skillEntity = SkillEntity.builder()
                .id("skill-id")
                .character(null)
                .name("Test Skill")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            CharacterSkillDtoBuilder.build(skillEntity);
        });
    }

    @Test
    @DisplayName("Should not allow instantiation of utility class")
    void shouldNotAllowInstantiationOfUtilityClass() throws NoSuchMethodException {
        // Given
        Constructor<CharacterSkillDtoBuilder> constructor = CharacterSkillDtoBuilder.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // When & Then
        assertThrows(InvocationTargetException.class, constructor::newInstance);
    }
}