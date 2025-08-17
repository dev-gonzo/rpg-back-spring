package com.rpgsystem.rpg.domain.character.updater;

import com.rpgsystem.rpg.domain.character.CharacterPoints;
import com.rpgsystem.rpg.domain.character.valueObject.Point;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@DisplayName("CharacterPointsUpdater Tests")
class CharacterPointsUpdaterTest {

    private CharacterPoints points;
    private CharacterEntity entity;

    @BeforeEach
    void setUp() {
        points = mock(CharacterPoints.class);
        entity = new CharacterEntity();
    }

    @Nested
    @DisplayName("Apply Method Tests")
    class ApplyMethodTests {

        @Test
        @DisplayName("Should apply all points when all values are present")
        void shouldApplyAllPointsWhenAllValuesArePresent() {
            // Given
            when(points.getHitPoints()).thenReturn(Point.of(100));
            when(points.getInitiative()).thenReturn(Point.of(15));
            when(points.getHeroPoints()).thenReturn(Point.of(5));
            when(points.getMagicPoints()).thenReturn(Point.of(20));
            when(points.getFaithPoints()).thenReturn(Point.of(10));
            when(points.getProtectionIndex()).thenReturn(Point.of(8));

            CharacterPointsUpdater updater = new CharacterPointsUpdater(points);

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getHitPoints()).isEqualTo(100);
            assertThat(entity.getInitiative()).isEqualTo(15);
            assertThat(entity.getHeroPoints()).isEqualTo(5);
            assertThat(entity.getMagicPoints()).isEqualTo(20);
            assertThat(entity.getFaithPoints()).isEqualTo(10);
            assertThat(entity.getProtectionIndex()).isEqualTo(8);
        }

        @Test
        @DisplayName("Should not update when points is null")
        void shouldNotUpdateWhenPointsIsNull() {
            // Given
            CharacterPointsUpdater updater = new CharacterPointsUpdater(null);
            entity.setHitPoints(50); // Set initial value

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getHitPoints()).isEqualTo(50); // Should remain unchanged
        }

        @Test
        @DisplayName("Should not update when entity is null")
        void shouldNotUpdateWhenEntityIsNull() {
            // Given
            CharacterPointsUpdater updater = new CharacterPointsUpdater(points);

            // When & Then
            assertDoesNotThrow(() -> updater.apply(null));
        }

        @Test
        @DisplayName("Should not update when both points and entity are null")
        void shouldNotUpdateWhenBothPointsAndEntityAreNull() {
            // Given
            CharacterPointsUpdater updater = new CharacterPointsUpdater(null);

            // When & Then
            assertDoesNotThrow(() -> updater.apply(null));
        }

        @Test
        @DisplayName("Should handle null hit points")
        void shouldHandleNullHitPoints() {
            // Given
            when(points.getHitPoints()).thenReturn(null);
            when(points.getInitiative()).thenReturn(Point.of(15));
            when(points.getHeroPoints()).thenReturn(Point.of(5));
            when(points.getMagicPoints()).thenReturn(Point.of(20));
            when(points.getFaithPoints()).thenReturn(Point.of(10));
            when(points.getProtectionIndex()).thenReturn(Point.of(8));

            CharacterPointsUpdater updater = new CharacterPointsUpdater(points);
            entity.setHitPoints(50); // Set initial value

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getHitPoints()).isEqualTo(50); // Should remain unchanged
            assertThat(entity.getInitiative()).isEqualTo(15);
            assertThat(entity.getHeroPoints()).isEqualTo(5);
            assertThat(entity.getMagicPoints()).isEqualTo(20);
            assertThat(entity.getFaithPoints()).isEqualTo(10);
            assertThat(entity.getProtectionIndex()).isEqualTo(8);
        }

        @Test
        @DisplayName("Should handle null initiative")
        void shouldHandleNullInitiative() {
            // Given
            when(points.getHitPoints()).thenReturn(Point.of(100));
            when(points.getInitiative()).thenReturn(null);
            when(points.getHeroPoints()).thenReturn(Point.of(5));
            when(points.getMagicPoints()).thenReturn(Point.of(20));
            when(points.getFaithPoints()).thenReturn(Point.of(10));
            when(points.getProtectionIndex()).thenReturn(Point.of(8));

            CharacterPointsUpdater updater = new CharacterPointsUpdater(points);
            entity.setInitiative(25); // Set initial value

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getHitPoints()).isEqualTo(100);
            assertThat(entity.getInitiative()).isEqualTo(25); // Should remain unchanged
            assertThat(entity.getHeroPoints()).isEqualTo(5);
            assertThat(entity.getMagicPoints()).isEqualTo(20);
            assertThat(entity.getFaithPoints()).isEqualTo(10);
            assertThat(entity.getProtectionIndex()).isEqualTo(8);
        }

        @Test
        @DisplayName("Should handle null hero points")
        void shouldHandleNullHeroPoints() {
            // Given
            when(points.getHitPoints()).thenReturn(Point.of(100));
            when(points.getInitiative()).thenReturn(Point.of(15));
            when(points.getHeroPoints()).thenReturn(null);
            when(points.getMagicPoints()).thenReturn(Point.of(20));
            when(points.getFaithPoints()).thenReturn(Point.of(10));
            when(points.getProtectionIndex()).thenReturn(Point.of(8));

            CharacterPointsUpdater updater = new CharacterPointsUpdater(points);
            entity.setHeroPoints(3); // Set initial value

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getHitPoints()).isEqualTo(100);
            assertThat(entity.getInitiative()).isEqualTo(15);
            assertThat(entity.getHeroPoints()).isEqualTo(3); // Should remain unchanged
            assertThat(entity.getMagicPoints()).isEqualTo(20);
            assertThat(entity.getFaithPoints()).isEqualTo(10);
            assertThat(entity.getProtectionIndex()).isEqualTo(8);
        }

        @Test
        @DisplayName("Should handle null magic points")
        void shouldHandleNullMagicPoints() {
            // Given
            when(points.getHitPoints()).thenReturn(Point.of(100));
            when(points.getInitiative()).thenReturn(Point.of(15));
            when(points.getHeroPoints()).thenReturn(Point.of(5));
            when(points.getMagicPoints()).thenReturn(null);
            when(points.getFaithPoints()).thenReturn(Point.of(10));
            when(points.getProtectionIndex()).thenReturn(Point.of(8));

            CharacterPointsUpdater updater = new CharacterPointsUpdater(points);
            entity.setMagicPoints(30); // Set initial value

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getHitPoints()).isEqualTo(100);
            assertThat(entity.getInitiative()).isEqualTo(15);
            assertThat(entity.getHeroPoints()).isEqualTo(5);
            assertThat(entity.getMagicPoints()).isEqualTo(30); // Should remain unchanged
            assertThat(entity.getFaithPoints()).isEqualTo(10);
            assertThat(entity.getProtectionIndex()).isEqualTo(8);
        }

        @Test
        @DisplayName("Should handle null faith points")
        void shouldHandleNullFaithPoints() {
            // Given
            when(points.getHitPoints()).thenReturn(Point.of(100));
            when(points.getInitiative()).thenReturn(Point.of(15));
            when(points.getHeroPoints()).thenReturn(Point.of(5));
            when(points.getMagicPoints()).thenReturn(Point.of(20));
            when(points.getFaithPoints()).thenReturn(null);
            when(points.getProtectionIndex()).thenReturn(Point.of(8));

            CharacterPointsUpdater updater = new CharacterPointsUpdater(points);
            entity.setFaithPoints(15); // Set initial value

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getHitPoints()).isEqualTo(100);
            assertThat(entity.getInitiative()).isEqualTo(15);
            assertThat(entity.getHeroPoints()).isEqualTo(5);
            assertThat(entity.getMagicPoints()).isEqualTo(20);
            assertThat(entity.getFaithPoints()).isEqualTo(15); // Should remain unchanged
            assertThat(entity.getProtectionIndex()).isEqualTo(8);
        }

        @Test
        @DisplayName("Should handle null protection index")
        void shouldHandleNullProtectionIndex() {
            // Given
            when(points.getHitPoints()).thenReturn(Point.of(100));
            when(points.getInitiative()).thenReturn(Point.of(15));
            when(points.getHeroPoints()).thenReturn(Point.of(5));
            when(points.getMagicPoints()).thenReturn(Point.of(20));
            when(points.getFaithPoints()).thenReturn(Point.of(10));
            when(points.getProtectionIndex()).thenReturn(null);

            CharacterPointsUpdater updater = new CharacterPointsUpdater(points);
            entity.setProtectionIndex(12); // Set initial value

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getHitPoints()).isEqualTo(100);
            assertThat(entity.getInitiative()).isEqualTo(15);
            assertThat(entity.getHeroPoints()).isEqualTo(5);
            assertThat(entity.getMagicPoints()).isEqualTo(20);
            assertThat(entity.getFaithPoints()).isEqualTo(10);
            assertThat(entity.getProtectionIndex()).isEqualTo(12); // Should remain unchanged
        }

        @Test
        @DisplayName("Should handle all null points")
        void shouldHandleAllNullPoints() {
            // Given
            when(points.getHitPoints()).thenReturn(null);
            when(points.getInitiative()).thenReturn(null);
            when(points.getHeroPoints()).thenReturn(null);
            when(points.getMagicPoints()).thenReturn(null);
            when(points.getFaithPoints()).thenReturn(null);
            when(points.getProtectionIndex()).thenReturn(null);

            CharacterPointsUpdater updater = new CharacterPointsUpdater(points);
            
            // Set initial values
            entity.setHitPoints(50);
            entity.setInitiative(25);
            entity.setHeroPoints(3);
            entity.setMagicPoints(30);
            entity.setFaithPoints(15);
            entity.setProtectionIndex(12);

            // When
            updater.apply(entity);

            // Then - All values should remain unchanged
            assertThat(entity.getHitPoints()).isEqualTo(50);
            assertThat(entity.getInitiative()).isEqualTo(25);
            assertThat(entity.getHeroPoints()).isEqualTo(3);
            assertThat(entity.getMagicPoints()).isEqualTo(30);
            assertThat(entity.getFaithPoints()).isEqualTo(15);
            assertThat(entity.getProtectionIndex()).isEqualTo(12);
        }

        @Test
        @DisplayName("Should handle mixed null and valid points")
        void shouldHandleMixedNullAndValidPoints() {
            // Given
            when(points.getHitPoints()).thenReturn(Point.of(100));
            when(points.getInitiative()).thenReturn(null);
            when(points.getHeroPoints()).thenReturn(Point.of(5));
            when(points.getMagicPoints()).thenReturn(null);
            when(points.getFaithPoints()).thenReturn(Point.of(10));
            when(points.getProtectionIndex()).thenReturn(null);

            CharacterPointsUpdater updater = new CharacterPointsUpdater(points);
            
            // Set initial values
            entity.setInitiative(25);
            entity.setMagicPoints(30);
            entity.setProtectionIndex(12);

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getHitPoints()).isEqualTo(100); // Updated
            assertThat(entity.getInitiative()).isEqualTo(25); // Unchanged
            assertThat(entity.getHeroPoints()).isEqualTo(5); // Updated
            assertThat(entity.getMagicPoints()).isEqualTo(30); // Unchanged
            assertThat(entity.getFaithPoints()).isEqualTo(10); // Updated
            assertThat(entity.getProtectionIndex()).isEqualTo(12); // Unchanged
        }
    }
}