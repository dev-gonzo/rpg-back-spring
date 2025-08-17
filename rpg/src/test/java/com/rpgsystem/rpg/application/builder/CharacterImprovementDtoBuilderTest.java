package com.rpgsystem.rpg.application.builder;

import com.rpgsystem.rpg.api.dto.character.CharacterImprovementResponse;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.ImprovementEntity;
import com.rpgsystem.rpg.domain.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CharacterImprovementDtoBuilder Tests")
class CharacterImprovementDtoBuilderTest {

    @Test
    @DisplayName("Should build CharacterImprovementResponse from ImprovementEntity with all fields")
    void shouldBuildCharacterImprovementResponseFromImprovementEntityWithAllFields() {
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

        ImprovementEntity improvementEntity = ImprovementEntity.builder()
                .id("improvement-id")
                .character(character)
                .cost(15)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        // When
        CharacterImprovementResponse result = CharacterImprovementDtoBuilder.from(improvementEntity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getCost()).isEqualTo(15);
        assertThat(result.getKitCost()).isEqualTo(15); // Note: kitCost is set to same value as cost
        assertThat(result.getCreatedAt()).isEqualTo(createdAt);
        assertThat(result.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("Should build CharacterImprovementResponse from ImprovementEntity with null cost")
    void shouldBuildCharacterImprovementResponseFromImprovementEntityWithNullCost() {
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

        Instant timestamp = Instant.now();

        ImprovementEntity improvementEntity = ImprovementEntity.builder()
                .id("improvement-null-cost")
                .character(character)
                .cost(null)
                .createdAt(timestamp)
                .updatedAt(timestamp)
                .build();

        // When
        CharacterImprovementResponse result = CharacterImprovementDtoBuilder.from(improvementEntity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getCost()).isNull();
        assertThat(result.getKitCost()).isNull(); // kitCost follows cost value
        assertThat(result.getCreatedAt()).isEqualTo(timestamp);
        assertThat(result.getUpdatedAt()).isEqualTo(timestamp);
    }

    @Test
    @DisplayName("Should build CharacterImprovementResponse from ImprovementEntity with zero cost")
    void shouldBuildCharacterImprovementResponseFromImprovementEntityWithZeroCost() {
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

        Instant timestamp = Instant.now();

        ImprovementEntity improvementEntity = ImprovementEntity.builder()
                .id("improvement-zero-cost")
                .character(character)
                .cost(0)
                .createdAt(timestamp)
                .updatedAt(timestamp)
                .build();

        // When
        CharacterImprovementResponse result = CharacterImprovementDtoBuilder.from(improvementEntity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getCost()).isEqualTo(0);
        assertThat(result.getKitCost()).isEqualTo(0);
        assertThat(result.getCreatedAt()).isEqualTo(timestamp);
        assertThat(result.getUpdatedAt()).isEqualTo(timestamp);
    }

    @Test
    @DisplayName("Should build CharacterImprovementResponse from ImprovementEntity with high cost")
    void shouldBuildCharacterImprovementResponseFromImprovementEntityWithHighCost() {
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

        Instant timestamp = Instant.now();
        Integer highCost = 999;

        ImprovementEntity improvementEntity = ImprovementEntity.builder()
                .id("improvement-high-cost")
                .character(character)
                .cost(highCost)
                .createdAt(timestamp)
                .updatedAt(timestamp)
                .build();

        // When
        CharacterImprovementResponse result = CharacterImprovementDtoBuilder.from(improvementEntity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getCost()).isEqualTo(highCost);
        assertThat(result.getKitCost()).isEqualTo(highCost);
        assertThat(result.getCreatedAt()).isEqualTo(timestamp);
        assertThat(result.getUpdatedAt()).isEqualTo(timestamp);
    }

    @Test
    @DisplayName("Should build CharacterImprovementResponse from ImprovementEntity with negative cost")
    void shouldBuildCharacterImprovementResponseFromImprovementEntityWithNegativeCost() {
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

        Instant timestamp = Instant.now();
        Integer negativeCost = -5;

        ImprovementEntity improvementEntity = ImprovementEntity.builder()
                .id("improvement-negative-cost")
                .character(character)
                .cost(negativeCost)
                .createdAt(timestamp)
                .updatedAt(timestamp)
                .build();

        // When
        CharacterImprovementResponse result = CharacterImprovementDtoBuilder.from(improvementEntity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getCost()).isEqualTo(negativeCost);
        assertThat(result.getKitCost()).isEqualTo(negativeCost);
        assertThat(result.getCreatedAt()).isEqualTo(timestamp);
        assertThat(result.getUpdatedAt()).isEqualTo(timestamp);
    }

    @Test
    @DisplayName("Should build CharacterImprovementResponse from ImprovementEntity with null timestamps")
    void shouldBuildCharacterImprovementResponseFromImprovementEntityWithNullTimestamps() {
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

        ImprovementEntity improvementEntity = ImprovementEntity.builder()
                .id("improvement-no-timestamps")
                .character(character)
                .cost(10)
                .createdAt(null)
                .updatedAt(null)
                .build();

        // When
        CharacterImprovementResponse result = CharacterImprovementDtoBuilder.from(improvementEntity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getCost()).isEqualTo(10);
        assertThat(result.getKitCost()).isEqualTo(10);
        assertThat(result.getCreatedAt()).isNull();
        assertThat(result.getUpdatedAt()).isNull();
    }

    @Test
    @DisplayName("Should build CharacterImprovementResponse from ImprovementEntity with different character IDs")
    void shouldBuildCharacterImprovementResponseFromImprovementEntityWithDifferentCharacterIds() {
        // Given
        User user = User.builder()
                .id("user-id")
                .name("Test User")
                .email("test@example.com")
                .build();

        CharacterEntity character = CharacterEntity.builder()
                .id("different-character-id-12345")
                .name("Different Character")
                .controlUser(user)
                .build();

        Instant timestamp = Instant.now();

        ImprovementEntity improvementEntity = ImprovementEntity.builder()
                .id("improvement-different-char")
                .character(character)
                .cost(25)
                .createdAt(timestamp)
                .updatedAt(timestamp)
                .build();

        // When
        CharacterImprovementResponse result = CharacterImprovementDtoBuilder.from(improvementEntity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCharacterId()).isEqualTo("different-character-id-12345");
        assertThat(result.getCost()).isEqualTo(25);
        assertThat(result.getKitCost()).isEqualTo(25);
        assertThat(result.getCreatedAt()).isEqualTo(timestamp);
        assertThat(result.getUpdatedAt()).isEqualTo(timestamp);
    }

    @Test
    @DisplayName("Should throw NullPointerException when ImprovementEntity is null")
    void shouldThrowNullPointerExceptionWhenImprovementEntityIsNull() {
        // Given
        ImprovementEntity nullEntity = null;

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            CharacterImprovementDtoBuilder.from(nullEntity);
        });
    }

    @Test
    @DisplayName("Should throw NullPointerException when character is null")
    void shouldThrowNullPointerExceptionWhenCharacterIsNull() {
        // Given
        ImprovementEntity improvementEntity = ImprovementEntity.builder()
                .id("improvement-no-char")
                .character(null)
                .cost(10)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            CharacterImprovementDtoBuilder.from(improvementEntity);
        });
    }

    @Test
    @DisplayName("Should not allow instantiation of utility class")
    void shouldNotAllowInstantiationOfUtilityClass() throws NoSuchMethodException {
        // Given
        Constructor<CharacterImprovementDtoBuilder> constructor = CharacterImprovementDtoBuilder.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // When & Then
        assertThrows(InvocationTargetException.class, constructor::newInstance);
    }
}