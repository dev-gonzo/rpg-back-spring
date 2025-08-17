package com.rpgsystem.rpg.application.builder;

import com.rpgsystem.rpg.api.dto.character.CharacterRelevantPeopleResponse;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.RelevantPeopleEntity;
import com.rpgsystem.rpg.domain.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CharacterRelevantPeopleDtoBuilderTest {

    @Test
    @DisplayName("Should build CharacterRelevantPeopleResponse from RelevantPeopleEntity with all values")
    void shouldBuildFromRelevantPeopleEntityWithAllValues() {
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
        RelevantPeopleEntity entity = RelevantPeopleEntity.builder()
                .id("person-id")
                .character(character)
                .category("Ally")
                .name("John Doe")
                .apparentAge(30)
                .city("New York")
                .profession("Detective")
                .briefDescription("A helpful detective who assists the party")
                .createdAt(now)
                .updatedAt(now)
                .build();

        // When
        CharacterRelevantPeopleResponse result = CharacterRelevantPeopleDtoBuilder.from(entity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("person-id");
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getCategory()).isEqualTo("Ally");
        assertThat(result.getName()).isEqualTo("John Doe");
        assertThat(result.getApparentAge()).isEqualTo(30);
        assertThat(result.getCity()).isEqualTo("New York");
        assertThat(result.getProfession()).isEqualTo("Detective");
        assertThat(result.getBriefDescription()).isEqualTo("A helpful detective who assists the party");
        assertThat(result.getCreatedAt()).isEqualTo(now);
        assertThat(result.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Should build CharacterRelevantPeopleResponse from RelevantPeopleEntity with null values")
    void shouldBuildFromRelevantPeopleEntityWithNullValues() {
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

        RelevantPeopleEntity entity = RelevantPeopleEntity.builder()
                .id("person-id")
                .character(character)
                .category(null)
                .name(null)
                .apparentAge(null)
                .city(null)
                .profession(null)
                .briefDescription(null)
                .createdAt(null)
                .updatedAt(null)
                .build();

        // When
        CharacterRelevantPeopleResponse result = CharacterRelevantPeopleDtoBuilder.from(entity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("person-id");
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getCategory()).isNull();
        assertThat(result.getName()).isNull();
        assertThat(result.getApparentAge()).isNull();
        assertThat(result.getCity()).isNull();
        assertThat(result.getProfession()).isNull();
        assertThat(result.getBriefDescription()).isNull();
        assertThat(result.getCreatedAt()).isNull();
        assertThat(result.getUpdatedAt()).isNull();
    }

    @Test
    @DisplayName("Should build CharacterRelevantPeopleResponse from RelevantPeopleEntity with empty strings")
    void shouldBuildFromRelevantPeopleEntityWithEmptyStrings() {
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
        RelevantPeopleEntity entity = RelevantPeopleEntity.builder()
                .id("person-id")
                .character(character)
                .category("")
                .name("")
                .apparentAge(0)
                .city("")
                .profession("")
                .briefDescription("")
                .createdAt(now)
                .updatedAt(now)
                .build();

        // When
        CharacterRelevantPeopleResponse result = CharacterRelevantPeopleDtoBuilder.from(entity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("person-id");
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getCategory()).isEmpty();
        assertThat(result.getName()).isEmpty();
        assertThat(result.getApparentAge()).isEqualTo(0);
        assertThat(result.getCity()).isEmpty();
        assertThat(result.getProfession()).isEmpty();
        assertThat(result.getBriefDescription()).isEmpty();
        assertThat(result.getCreatedAt()).isEqualTo(now);
        assertThat(result.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Should build CharacterRelevantPeopleResponse from RelevantPeopleEntity with maximum values")
    void shouldBuildFromRelevantPeopleEntityWithMaximumValues() {
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
        RelevantPeopleEntity entity = RelevantPeopleEntity.builder()
                .id("person-id")
                .character(character)
                .category(longText)
                .name(longText)
                .apparentAge(Integer.MAX_VALUE)
                .city(longText)
                .profession(longText)
                .briefDescription(longText)
                .createdAt(now)
                .updatedAt(now)
                .build();

        // When
        CharacterRelevantPeopleResponse result = CharacterRelevantPeopleDtoBuilder.from(entity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("person-id");
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getCategory()).isEqualTo(longText);
        assertThat(result.getName()).isEqualTo(longText);
        assertThat(result.getApparentAge()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getCity()).isEqualTo(longText);
        assertThat(result.getProfession()).isEqualTo(longText);
        assertThat(result.getBriefDescription()).isEqualTo(longText);
        assertThat(result.getCreatedAt()).isEqualTo(now);
        assertThat(result.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Should build CharacterRelevantPeopleResponse from RelevantPeopleEntity with Unicode characters")
    void shouldBuildFromRelevantPeopleEntityWithUnicodeCharacters() {
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
        RelevantPeopleEntity entity = RelevantPeopleEntity.builder()
                .id("person-id")
                .character(character)
                .category("敵対者") // Japanese for "adversary"
                .name("José María Azañón") // Spanish name with accents
                .apparentAge(25)
                .city("São Paulo") // Portuguese city name
                .profession("Développeur") // French profession
                .briefDescription("Un développeur très expérimenté 🚀")
                .createdAt(now)
                .updatedAt(now)
                .build();

        // When
        CharacterRelevantPeopleResponse result = CharacterRelevantPeopleDtoBuilder.from(entity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("person-id");
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getCategory()).isEqualTo("敵対者");
        assertThat(result.getName()).isEqualTo("José María Azañón");
        assertThat(result.getApparentAge()).isEqualTo(25);
        assertThat(result.getCity()).isEqualTo("São Paulo");
        assertThat(result.getProfession()).isEqualTo("Développeur");
        assertThat(result.getBriefDescription()).isEqualTo("Un développeur très expérimenté 🚀");
        assertThat(result.getCreatedAt()).isEqualTo(now);
        assertThat(result.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Should build CharacterRelevantPeopleResponse from RelevantPeopleEntity with negative apparent age")
    void shouldBuildFromRelevantPeopleEntityWithNegativeApparentAge() {
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
        RelevantPeopleEntity entity = RelevantPeopleEntity.builder()
                .id("person-id")
                .character(character)
                .category("Mysterious")
                .name("Timeless Being")
                .apparentAge(-1) // Negative age for supernatural beings
                .city("Unknown")
                .profession("Guardian")
                .briefDescription("An ancient entity with no apparent age")
                .createdAt(now)
                .updatedAt(now)
                .build();

        // When
        CharacterRelevantPeopleResponse result = CharacterRelevantPeopleDtoBuilder.from(entity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("person-id");
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getCategory()).isEqualTo("Mysterious");
        assertThat(result.getName()).isEqualTo("Timeless Being");
        assertThat(result.getApparentAge()).isEqualTo(-1);
        assertThat(result.getCity()).isEqualTo("Unknown");
        assertThat(result.getProfession()).isEqualTo("Guardian");
        assertThat(result.getBriefDescription()).isEqualTo("An ancient entity with no apparent age");
        assertThat(result.getCreatedAt()).isEqualTo(now);
        assertThat(result.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Should not allow instantiation of CharacterRelevantPeopleDtoBuilder")
    void shouldNotAllowInstantiation() throws NoSuchMethodException {
        // Given
        Constructor<CharacterRelevantPeopleDtoBuilder> constructor = CharacterRelevantPeopleDtoBuilder.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // When & Then
        InvocationTargetException exception = assertThrows(InvocationTargetException.class, constructor::newInstance);
        assertThat(exception.getCause()).isInstanceOf(UnsupportedOperationException.class);
        assertThat(exception.getCause().getMessage()).isEqualTo("Utility class");
    }
}