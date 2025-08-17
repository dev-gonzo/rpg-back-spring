package com.rpgsystem.rpg.application.builder;

import com.rpgsystem.rpg.api.dto.character.CharacterPointsResponse;
import com.rpgsystem.rpg.domain.character.CharacterPoints;
import com.rpgsystem.rpg.domain.character.valueObject.Point;
import com.rpgsystem.rpg.domain.common.CodigoId;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CharacterPointsDtoBuilderTest {

    @Test
    @DisplayName("Should build CharacterPointsResponse from CharacterEntity with all values")
    void shouldBuildFromCharacterEntityWithAllValues() {
        // Given
        User user = User.builder()
                .id("user-id")
                .name("Test User")
                .email("test@example.com")
                .build();

        CharacterEntity entity = CharacterEntity.builder()
                .id("character-id")
                .name("Test Character")
                .hitPoints(120)
                .currentHitPoints(100)
                .initiative(18)
                .currentInitiative(15)
                .heroPoints(8)
                .currentHeroPoints(6)
                .magicPoints(30)
                .currentMagicPoints(25)
                .faithPoints(20)
                .currentFaithPoints(18)
                .protectionIndex(7)
                .currentProtectionIndex(5)
                .controlUser(user)
                .build();

        // When
        CharacterPointsResponse result = CharacterPointsDtoBuilder.from(entity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getHitPoints()).isEqualTo(120);
        assertThat(result.getCurrentHitPoints()).isEqualTo(100);
        assertThat(result.getInitiative()).isEqualTo(18);
        assertThat(result.getCurrentInitiative()).isEqualTo(15);
        assertThat(result.getHeroPoints()).isEqualTo(8);
        assertThat(result.getCurrentHeroPoints()).isEqualTo(6);
        assertThat(result.getMagicPoints()).isEqualTo(30);
        assertThat(result.getCurrentMagicPoints()).isEqualTo(25);
        assertThat(result.getFaithPoints()).isEqualTo(20);
        assertThat(result.getCurrentFaithPoints()).isEqualTo(18);
        assertThat(result.getProtectionIndex()).isEqualTo(7);
        assertThat(result.getCurrentProtectionIndex()).isEqualTo(5);
    }

    @Test
    @DisplayName("Should build CharacterPointsResponse from CharacterEntity with null values")
    void shouldBuildFromCharacterEntityWithNullValues() {
        // Given
        User user = User.builder()
                .id("user-id")
                .name("Test User")
                .email("test@example.com")
                .build();

        CharacterEntity entity = CharacterEntity.builder()
                .id("character-id")
                .name("Test Character")
                .hitPoints(null)
                .currentHitPoints(null)
                .initiative(null)
                .currentInitiative(null)
                .heroPoints(null)
                .currentHeroPoints(null)
                .magicPoints(null)
                .currentMagicPoints(null)
                .faithPoints(null)
                .currentFaithPoints(null)
                .protectionIndex(null)
                .currentProtectionIndex(null)
                .controlUser(user)
                .build();

        // When
        CharacterPointsResponse result = CharacterPointsDtoBuilder.from(entity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getHitPoints()).isNull();
        assertThat(result.getCurrentHitPoints()).isNull();
        assertThat(result.getInitiative()).isNull();
        assertThat(result.getCurrentInitiative()).isNull();
        assertThat(result.getHeroPoints()).isNull();
        assertThat(result.getCurrentHeroPoints()).isNull();
        assertThat(result.getMagicPoints()).isNull();
        assertThat(result.getCurrentMagicPoints()).isNull();
        assertThat(result.getFaithPoints()).isNull();
        assertThat(result.getCurrentFaithPoints()).isNull();
        assertThat(result.getProtectionIndex()).isNull();
        assertThat(result.getCurrentProtectionIndex()).isNull();
    }

    @Test
    @DisplayName("Should build CharacterPointsResponse from CharacterPoints with all values")
    void shouldBuildFromCharacterPointsWithAllValues() {
        // Given
        CharacterPoints points = new CharacterPoints(
                new CodigoId("character-id"),
                Point.of(120),
                Point.of(100),
                Point.of(18),
                Point.of(15),
                Point.of(8),
                Point.of(6),
                Point.of(30),
                Point.of(25),
                Point.of(20),
                Point.of(18),
                Point.of(7),
                Point.of(5)
        );

        // When
        CharacterPointsResponse result = CharacterPointsDtoBuilder.from(points);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getHitPoints()).isEqualTo(120);
        assertThat(result.getCurrentHitPoints()).isEqualTo(100);
        assertThat(result.getInitiative()).isEqualTo(18);
        assertThat(result.getCurrentInitiative()).isEqualTo(15);
        assertThat(result.getHeroPoints()).isEqualTo(8);
        assertThat(result.getCurrentHeroPoints()).isEqualTo(6);
        assertThat(result.getMagicPoints()).isEqualTo(30);
        assertThat(result.getCurrentMagicPoints()).isEqualTo(25);
        assertThat(result.getFaithPoints()).isEqualTo(20);
        assertThat(result.getCurrentFaithPoints()).isEqualTo(18);
        assertThat(result.getProtectionIndex()).isEqualTo(7);
        assertThat(result.getCurrentProtectionIndex()).isEqualTo(5);
    }

    @Test
    @DisplayName("Should build CharacterPointsResponse from CharacterPoints with null values")
    void shouldBuildFromCharacterPointsWithNullValues() {
        // Given
        CharacterPoints points = new CharacterPoints(
                new CodigoId("character-id"),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        // When
        CharacterPointsResponse result = CharacterPointsDtoBuilder.from(points);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getHitPoints()).isNull();
        assertThat(result.getCurrentHitPoints()).isNull();
        assertThat(result.getInitiative()).isNull();
        assertThat(result.getCurrentInitiative()).isNull();
        assertThat(result.getHeroPoints()).isNull();
        assertThat(result.getCurrentHeroPoints()).isNull();
        assertThat(result.getMagicPoints()).isNull();
        assertThat(result.getCurrentMagicPoints()).isNull();
        assertThat(result.getFaithPoints()).isNull();
        assertThat(result.getCurrentFaithPoints()).isNull();
        assertThat(result.getProtectionIndex()).isNull();
        assertThat(result.getCurrentProtectionIndex()).isNull();
    }

    @Test
    @DisplayName("Should build CharacterPointsResponse from CharacterPoints with mixed null values")
    void shouldBuildFromCharacterPointsWithMixedNullValues() {
        // Given
        CharacterPoints points = new CharacterPoints(
                new CodigoId("character-id"),
                Point.of(120),
                null,
                Point.of(18),
                null,
                null,
                Point.of(6),
                Point.of(30),
                null,
                null,
                Point.of(18),
                Point.of(7),
                null
        );

        // When
        CharacterPointsResponse result = CharacterPointsDtoBuilder.from(points);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getHitPoints()).isEqualTo(120);
        assertThat(result.getCurrentHitPoints()).isNull();
        assertThat(result.getInitiative()).isEqualTo(18);
        assertThat(result.getCurrentInitiative()).isNull();
        assertThat(result.getHeroPoints()).isNull();
        assertThat(result.getCurrentHeroPoints()).isEqualTo(6);
        assertThat(result.getMagicPoints()).isEqualTo(30);
        assertThat(result.getCurrentMagicPoints()).isNull();
        assertThat(result.getFaithPoints()).isNull();
        assertThat(result.getCurrentFaithPoints()).isEqualTo(18);
        assertThat(result.getProtectionIndex()).isEqualTo(7);
        assertThat(result.getCurrentProtectionIndex()).isNull();
    }

    @Test
    @DisplayName("Should build CharacterPointsResponse from CharacterPoints with zero values")
    void shouldBuildFromCharacterPointsWithZeroValues() {
        // Given
        CharacterPoints points = new CharacterPoints(
                new CodigoId("character-id"),
                Point.of(0),
                Point.of(0),
                Point.of(0),
                Point.of(0),
                Point.of(0),
                Point.of(0),
                Point.of(0),
                Point.of(0),
                Point.of(0),
                Point.of(0),
                Point.of(0),
                Point.of(0)
        );

        // When
        CharacterPointsResponse result = CharacterPointsDtoBuilder.from(points);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getHitPoints()).isEqualTo(0);
        assertThat(result.getCurrentHitPoints()).isEqualTo(0);
        assertThat(result.getInitiative()).isEqualTo(0);
        assertThat(result.getCurrentInitiative()).isEqualTo(0);
        assertThat(result.getHeroPoints()).isEqualTo(0);
        assertThat(result.getCurrentHeroPoints()).isEqualTo(0);
        assertThat(result.getMagicPoints()).isEqualTo(0);
        assertThat(result.getCurrentMagicPoints()).isEqualTo(0);
        assertThat(result.getFaithPoints()).isEqualTo(0);
        assertThat(result.getCurrentFaithPoints()).isEqualTo(0);
        assertThat(result.getProtectionIndex()).isEqualTo(0);
        assertThat(result.getCurrentProtectionIndex()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should build CharacterPointsResponse from CharacterEntity with maximum values")
    void shouldBuildFromCharacterEntityWithMaximumValues() {
        // Given
        User user = User.builder()
                .id("user-id")
                .name("Test User")
                .email("test@example.com")
                .build();

        CharacterEntity entity = CharacterEntity.builder()
                .id("character-id")
                .name("Test Character")
                .hitPoints(Integer.MAX_VALUE)
                .currentHitPoints(Integer.MAX_VALUE)
                .initiative(Integer.MAX_VALUE)
                .currentInitiative(Integer.MAX_VALUE)
                .heroPoints(Integer.MAX_VALUE)
                .currentHeroPoints(Integer.MAX_VALUE)
                .magicPoints(Integer.MAX_VALUE)
                .currentMagicPoints(Integer.MAX_VALUE)
                .faithPoints(Integer.MAX_VALUE)
                .currentFaithPoints(Integer.MAX_VALUE)
                .protectionIndex(Integer.MAX_VALUE)
                .currentProtectionIndex(Integer.MAX_VALUE)
                .controlUser(user)
                .build();

        // When
        CharacterPointsResponse result = CharacterPointsDtoBuilder.from(entity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getHitPoints()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getCurrentHitPoints()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getInitiative()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getCurrentInitiative()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getHeroPoints()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getCurrentHeroPoints()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getMagicPoints()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getCurrentMagicPoints()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getFaithPoints()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getCurrentFaithPoints()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getProtectionIndex()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getCurrentProtectionIndex()).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    @DisplayName("Should not allow instantiation of CharacterPointsDtoBuilder")
    void shouldNotAllowInstantiation() throws NoSuchMethodException {
        // Given
        Constructor<CharacterPointsDtoBuilder> constructor = CharacterPointsDtoBuilder.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // When & Then
        assertThrows(InvocationTargetException.class, constructor::newInstance);
    }
}