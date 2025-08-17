package com.rpgsystem.rpg.application.builder;

import com.rpgsystem.rpg.api.dto.character.CharacterRitualPowerResponse;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.RitualPowerEntity;
import com.rpgsystem.rpg.domain.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("CharacterRitualPowerDtoBuilder Tests")
class CharacterRitualPowerDtoBuilderTest {

    @Test
    @DisplayName("Should build CharacterRitualPowerResponse from RitualPowerEntity with all fields")
    void shouldBuildCharacterRitualPowerResponseFromRitualPowerEntityWithAllFields() {
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

        RitualPowerEntity ritualPowerEntity = RitualPowerEntity.builder()
                .id("ritual-power-id")
                .character(character)
                .name("Fireball")
                .pathsForms("Fire, Destruction")
                .description("A powerful fire spell that creates a ball of flame")
                .bookPage("Page 123")
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        // When
        CharacterRitualPowerResponse result = CharacterRitualPowerDtoBuilder.from(ritualPowerEntity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("ritual-power-id");
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getName()).isEqualTo("Fireball");
        assertThat(result.getPathsForms()).isEqualTo("Fire, Destruction");
        assertThat(result.getDescription()).isEqualTo("A powerful fire spell that creates a ball of flame");
        assertThat(result.getBookPage()).isEqualTo("Page 123");
        assertThat(result.getCreatedAt()).isEqualTo(createdAt);
        assertThat(result.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("Should build CharacterRitualPowerResponse from RitualPowerEntity with null values")
    void shouldBuildCharacterRitualPowerResponseFromRitualPowerEntityWithNullValues() {
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

        RitualPowerEntity ritualPowerEntity = RitualPowerEntity.builder()
                .id("ritual-power-id")
                .character(character)
                .name(null)
                .pathsForms(null)
                .description(null)
                .bookPage(null)
                .createdAt(null)
                .updatedAt(null)
                .build();

        // When
        CharacterRitualPowerResponse result = CharacterRitualPowerDtoBuilder.from(ritualPowerEntity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("ritual-power-id");
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getName()).isNull();
        assertThat(result.getPathsForms()).isNull();
        assertThat(result.getDescription()).isNull();
        assertThat(result.getBookPage()).isNull();
        assertThat(result.getCreatedAt()).isNull();
        assertThat(result.getUpdatedAt()).isNull();
    }

    @Test
    @DisplayName("Should build CharacterRitualPowerResponse from RitualPowerEntity with empty strings")
    void shouldBuildCharacterRitualPowerResponseFromRitualPowerEntityWithEmptyStrings() {
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

        Instant now = Instant.now();
        RitualPowerEntity ritualPowerEntity = RitualPowerEntity.builder()
                .id("ritual-power-id")
                .character(character)
                .name("")
                .pathsForms("")
                .description("")
                .bookPage("")
                .createdAt(now)
                .updatedAt(now)
                .build();

        // When
        CharacterRitualPowerResponse result = CharacterRitualPowerDtoBuilder.from(ritualPowerEntity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("ritual-power-id");
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getName()).isEmpty();
        assertThat(result.getPathsForms()).isEmpty();
        assertThat(result.getDescription()).isEmpty();
        assertThat(result.getBookPage()).isEmpty();
        assertThat(result.getCreatedAt()).isEqualTo(now);
        assertThat(result.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Should build CharacterRitualPowerResponse from RitualPowerEntity with maximum values")
    void shouldBuildCharacterRitualPowerResponseFromRitualPowerEntityWithMaximumValues() {
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

        String longText = "A".repeat(1000); // Very long text
        Instant now = Instant.now();
        RitualPowerEntity ritualPowerEntity = RitualPowerEntity.builder()
                .id("ritual-power-id")
                .character(character)
                .name(longText)
                .pathsForms(longText)
                .description(longText)
                .bookPage(longText)
                .createdAt(now)
                .updatedAt(now)
                .build();

        // When
        CharacterRitualPowerResponse result = CharacterRitualPowerDtoBuilder.from(ritualPowerEntity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("ritual-power-id");
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getName()).isEqualTo(longText);
        assertThat(result.getPathsForms()).isEqualTo(longText);
        assertThat(result.getDescription()).isEqualTo(longText);
        assertThat(result.getBookPage()).isEqualTo(longText);
        assertThat(result.getCreatedAt()).isEqualTo(now);
        assertThat(result.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Should build CharacterRitualPowerResponse from RitualPowerEntity with Unicode characters")
    void shouldBuildCharacterRitualPowerResponseFromRitualPowerEntityWithUnicodeCharacters() {
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

        Instant now = Instant.now();
        RitualPowerEntity ritualPowerEntity = RitualPowerEntity.builder()
                .id("ritual-power-id")
                .character(character)
                .name("火球術") // Japanese for "Fireball"
                .pathsForms("火, 破壊") // Japanese for "Fire, Destruction"
                .description("Une boule de feu puissante 🔥") // French with emoji
                .bookPage("Página 123") // Portuguese
                .createdAt(now)
                .updatedAt(now)
                .build();

        // When
        CharacterRitualPowerResponse result = CharacterRitualPowerDtoBuilder.from(ritualPowerEntity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("ritual-power-id");
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getName()).isEqualTo("火球術");
        assertThat(result.getPathsForms()).isEqualTo("火, 破壊");
        assertThat(result.getDescription()).isEqualTo("Une boule de feu puissante 🔥");
        assertThat(result.getBookPage()).isEqualTo("Página 123");
        assertThat(result.getCreatedAt()).isEqualTo(now);
        assertThat(result.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Should not allow instantiation of CharacterRitualPowerDtoBuilder")
    void shouldNotAllowInstantiation() throws NoSuchMethodException {
        // Given
        Constructor<CharacterRitualPowerDtoBuilder> constructor = CharacterRitualPowerDtoBuilder.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // When & Then
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);
        assertThat(exception.getCause()).isInstanceOf(UnsupportedOperationException.class);
        assertThat(exception.getCause().getMessage()).isEqualTo("Utility class");
    }
}