package com.rpgsystem.rpg.application.builder;

import com.rpgsystem.rpg.api.dto.character.CharacterPathsAndFormsResponse;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.PathsAndFormsEntity;
import com.rpgsystem.rpg.domain.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("CharacterPathsAndFormsDtoBuilder Tests")
class CharacterPathsAndFormsDtoBuilderTest {

    @Test
    @DisplayName("Should build CharacterPathsAndFormsResponse from PathsAndFormsEntity with all fields")
    void shouldBuildCharacterPathsAndFormsResponseFromPathsAndFormsEntityWithAllFields() {
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

        PathsAndFormsEntity pathsAndFormsEntity = PathsAndFormsEntity.builder()
                .characterId("character-id")
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
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        // When
        CharacterPathsAndFormsResponse result = CharacterPathsAndFormsDtoBuilder.from(pathsAndFormsEntity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getUnderstandForm()).isEqualTo(8);
        assertThat(result.getCreateForm()).isEqualTo(6);
        assertThat(result.getControlForm()).isEqualTo(7);
        assertThat(result.getFire()).isEqualTo(4);
        assertThat(result.getWater()).isEqualTo(3);
        assertThat(result.getEarth()).isEqualTo(2);
        assertThat(result.getAir()).isEqualTo(1);
        assertThat(result.getLight()).isEqualTo(4);
        assertThat(result.getDarkness()).isEqualTo(0);
        assertThat(result.getPlants()).isEqualTo(2);
        assertThat(result.getAnimals()).isEqualTo(3);
        assertThat(result.getHumans()).isEqualTo(4);
        assertThat(result.getSpiritum()).isEqualTo(1);
        assertThat(result.getArkanun()).isEqualTo(3);
        assertThat(result.getMetamagic()).isEqualTo(2);
        assertThat(result.getCreatedAt()).isEqualTo(createdAt);
        assertThat(result.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("Should build CharacterPathsAndFormsResponse from PathsAndFormsEntity with null values")
    void shouldBuildCharacterPathsAndFormsResponseFromPathsAndFormsEntityWithNullValues() {
        // Given
        CharacterEntity character = CharacterEntity.builder()
                .id("character-null")
                .name("Null Character")
                .build();

        PathsAndFormsEntity pathsAndFormsEntity = PathsAndFormsEntity.builder()
                .characterId("character-null")
                .character(character)
                .understandForm(null)
                .createForm(null)
                .controlForm(null)
                .fire(null)
                .water(null)
                .earth(null)
                .air(null)
                .light(null)
                .darkness(null)
                .plants(null)
                .animals(null)
                .humans(null)
                .spiritum(null)
                .arkanun(null)
                .metamagic(null)
                .createdAt(null)
                .updatedAt(null)
                .build();

        // When
        CharacterPathsAndFormsResponse result = CharacterPathsAndFormsDtoBuilder.from(pathsAndFormsEntity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCharacterId()).isEqualTo("character-null");
        assertThat(result.getUnderstandForm()).isNull();
        assertThat(result.getCreateForm()).isNull();
        assertThat(result.getControlForm()).isNull();
        assertThat(result.getFire()).isNull();
        assertThat(result.getWater()).isNull();
        assertThat(result.getEarth()).isNull();
        assertThat(result.getAir()).isNull();
        assertThat(result.getLight()).isNull();
        assertThat(result.getDarkness()).isNull();
        assertThat(result.getPlants()).isNull();
        assertThat(result.getAnimals()).isNull();
        assertThat(result.getHumans()).isNull();
        assertThat(result.getSpiritum()).isNull();
        assertThat(result.getArkanun()).isNull();
        assertThat(result.getMetamagic()).isNull();
        assertThat(result.getCreatedAt()).isNull();
        assertThat(result.getUpdatedAt()).isNull();
    }

    @Test
    @DisplayName("Should build CharacterPathsAndFormsResponse with zero values")
    void shouldBuildCharacterPathsAndFormsResponseWithZeroValues() {
        // Given
        CharacterEntity character = CharacterEntity.builder()
                .id("character-zero")
                .name("Zero Character")
                .build();

        Instant createdAt = Instant.parse("2023-01-01T00:00:00Z");
        Instant updatedAt = Instant.parse("2023-01-01T00:00:00Z");

        PathsAndFormsEntity pathsAndFormsEntity = PathsAndFormsEntity.builder()
                .characterId("character-zero")
                .character(character)
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
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        // When
        CharacterPathsAndFormsResponse result = CharacterPathsAndFormsDtoBuilder.from(pathsAndFormsEntity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCharacterId()).isEqualTo("character-zero");
        assertThat(result.getUnderstandForm()).isEqualTo(0);
        assertThat(result.getCreateForm()).isEqualTo(0);
        assertThat(result.getControlForm()).isEqualTo(0);
        assertThat(result.getFire()).isEqualTo(0);
        assertThat(result.getWater()).isEqualTo(0);
        assertThat(result.getEarth()).isEqualTo(0);
        assertThat(result.getAir()).isEqualTo(0);
        assertThat(result.getLight()).isEqualTo(0);
        assertThat(result.getDarkness()).isEqualTo(0);
        assertThat(result.getPlants()).isEqualTo(0);
        assertThat(result.getAnimals()).isEqualTo(0);
        assertThat(result.getHumans()).isEqualTo(0);
        assertThat(result.getSpiritum()).isEqualTo(0);
        assertThat(result.getArkanun()).isEqualTo(0);
        assertThat(result.getMetamagic()).isEqualTo(0);
        assertThat(result.getCreatedAt()).isEqualTo(createdAt);
        assertThat(result.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("Should build CharacterPathsAndFormsResponse with maximum values")
    void shouldBuildCharacterPathsAndFormsResponseWithMaximumValues() {
        // Given
        CharacterEntity character = CharacterEntity.builder()
                .id("character-max")
                .name("Maximum Character")
                .build();

        Instant createdAt = Instant.parse("2023-12-31T23:59:59Z");
        Instant updatedAt = Instant.parse("2023-12-31T23:59:59Z");

        PathsAndFormsEntity pathsAndFormsEntity = PathsAndFormsEntity.builder()
                .characterId("character-max")
                .character(character)
                .understandForm(Integer.MAX_VALUE)
                .createForm(Integer.MAX_VALUE)
                .controlForm(Integer.MAX_VALUE)
                .fire(Integer.MAX_VALUE)
                .water(Integer.MAX_VALUE)
                .earth(Integer.MAX_VALUE)
                .air(Integer.MAX_VALUE)
                .light(Integer.MAX_VALUE)
                .darkness(Integer.MAX_VALUE)
                .plants(Integer.MAX_VALUE)
                .animals(Integer.MAX_VALUE)
                .humans(Integer.MAX_VALUE)
                .spiritum(Integer.MAX_VALUE)
                .arkanun(Integer.MAX_VALUE)
                .metamagic(Integer.MAX_VALUE)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        // When
        CharacterPathsAndFormsResponse result = CharacterPathsAndFormsDtoBuilder.from(pathsAndFormsEntity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCharacterId()).isEqualTo("character-max");
        assertThat(result.getUnderstandForm()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getCreateForm()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getControlForm()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getFire()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getWater()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getEarth()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getAir()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getLight()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getDarkness()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getPlants()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getAnimals()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getHumans()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getSpiritum()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getArkanun()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getMetamagic()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getCreatedAt()).isEqualTo(createdAt);
        assertThat(result.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("Should not allow instantiation")
    void shouldNotAllowInstantiation() throws Exception {
        // Given
        Constructor<CharacterPathsAndFormsDtoBuilder> constructor = CharacterPathsAndFormsDtoBuilder.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // When & Then
        assertThrows(InvocationTargetException.class, () -> {
            constructor.newInstance();
        });
    }
}