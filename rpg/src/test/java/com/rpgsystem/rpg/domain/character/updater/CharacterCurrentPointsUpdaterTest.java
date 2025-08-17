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

@DisplayName("CharacterCurrentPointsUpdater Tests")
class CharacterCurrentPointsUpdaterTest {

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
        @DisplayName("Should apply all current points when all values are present")
        void shouldApplyAllCurrentPointsWhenAllValuesArePresent() {
            // Given
            when(points.getCurrentHitPoints()).thenReturn(Point.of(80));
            when(points.getCurrentInitiative()).thenReturn(Point.of(12));
            when(points.getCurrentHeroPoints()).thenReturn(Point.of(3));
            when(points.getCurrentMagicPoints()).thenReturn(Point.of(15));
            when(points.getCurrentFaithPoints()).thenReturn(Point.of(8));
            when(points.getCurrentProtectionIndex()).thenReturn(Point.of(6));

            CharacterCurrentPointsUpdater updater = new CharacterCurrentPointsUpdater(points);

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getCurrentHitPoints()).isEqualTo(80);
            assertThat(entity.getCurrentInitiative()).isEqualTo(12);
            assertThat(entity.getCurrentHeroPoints()).isEqualTo(3);
            assertThat(entity.getCurrentMagicPoints()).isEqualTo(15);
            assertThat(entity.getCurrentFaithPoints()).isEqualTo(8);
            assertThat(entity.getCurrentProtectionIndex()).isEqualTo(6);
        }

        @Test
        @DisplayName("Should not update when points is null")
        void shouldNotUpdateWhenPointsIsNull() {
            // Given
            CharacterCurrentPointsUpdater updater = new CharacterCurrentPointsUpdater(null);
            entity.setCurrentHitPoints(50); // Set initial value

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getCurrentHitPoints()).isEqualTo(50); // Should remain unchanged
        }

        @Test
        @DisplayName("Should not update when entity is null")
        void shouldNotUpdateWhenEntityIsNull() {
            // Given
            CharacterCurrentPointsUpdater updater = new CharacterCurrentPointsUpdater(points);

            // When & Then
            assertDoesNotThrow(() -> updater.apply(null));
        }

        @Test
        @DisplayName("Should not update when both points and entity are null")
        void shouldNotUpdateWhenBothPointsAndEntityAreNull() {
            // Given
            CharacterCurrentPointsUpdater updater = new CharacterCurrentPointsUpdater(null);

            // When & Then
            assertDoesNotThrow(() -> updater.apply(null));
        }

        @Test
        @DisplayName("Should handle null current hit points")
        void shouldHandleNullCurrentHitPoints() {
            // Given
            when(points.getCurrentHitPoints()).thenReturn(null);
            when(points.getCurrentInitiative()).thenReturn(Point.of(12));
            when(points.getCurrentHeroPoints()).thenReturn(Point.of(3));
            when(points.getCurrentMagicPoints()).thenReturn(Point.of(15));
            when(points.getCurrentFaithPoints()).thenReturn(Point.of(8));
            when(points.getCurrentProtectionIndex()).thenReturn(Point.of(6));

            CharacterCurrentPointsUpdater updater = new CharacterCurrentPointsUpdater(points);
            entity.setCurrentHitPoints(50); // Set initial value

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getCurrentHitPoints()).isEqualTo(50); // Should remain unchanged
            assertThat(entity.getCurrentInitiative()).isEqualTo(12);
            assertThat(entity.getCurrentHeroPoints()).isEqualTo(3);
            assertThat(entity.getCurrentMagicPoints()).isEqualTo(15);
            assertThat(entity.getCurrentFaithPoints()).isEqualTo(8);
            assertThat(entity.getCurrentProtectionIndex()).isEqualTo(6);
        }

        @Test
        @DisplayName("Should handle null current initiative")
        void shouldHandleNullCurrentInitiative() {
            // Given
            when(points.getCurrentHitPoints()).thenReturn(Point.of(80));
            when(points.getCurrentInitiative()).thenReturn(null);
            when(points.getCurrentHeroPoints()).thenReturn(Point.of(3));
            when(points.getCurrentMagicPoints()).thenReturn(Point.of(15));
            when(points.getCurrentFaithPoints()).thenReturn(Point.of(8));
            when(points.getCurrentProtectionIndex()).thenReturn(Point.of(6));

            CharacterCurrentPointsUpdater updater = new CharacterCurrentPointsUpdater(points);
            entity.setCurrentInitiative(20); // Set initial value

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getCurrentHitPoints()).isEqualTo(80);
            assertThat(entity.getCurrentInitiative()).isEqualTo(20); // Should remain unchanged
            assertThat(entity.getCurrentHeroPoints()).isEqualTo(3);
            assertThat(entity.getCurrentMagicPoints()).isEqualTo(15);
            assertThat(entity.getCurrentFaithPoints()).isEqualTo(8);
            assertThat(entity.getCurrentProtectionIndex()).isEqualTo(6);
        }

        @Test
        @DisplayName("Should handle null current hero points")
        void shouldHandleNullCurrentHeroPoints() {
            // Given
            when(points.getCurrentHitPoints()).thenReturn(Point.of(80));
            when(points.getCurrentInitiative()).thenReturn(Point.of(12));
            when(points.getCurrentHeroPoints()).thenReturn(null);
            when(points.getCurrentMagicPoints()).thenReturn(Point.of(15));
            when(points.getCurrentFaithPoints()).thenReturn(Point.of(8));
            when(points.getCurrentProtectionIndex()).thenReturn(Point.of(6));

            CharacterCurrentPointsUpdater updater = new CharacterCurrentPointsUpdater(points);
            entity.setCurrentHeroPoints(2); // Set initial value

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getCurrentHitPoints()).isEqualTo(80);
            assertThat(entity.getCurrentInitiative()).isEqualTo(12);
            assertThat(entity.getCurrentHeroPoints()).isEqualTo(2); // Should remain unchanged
            assertThat(entity.getCurrentMagicPoints()).isEqualTo(15);
            assertThat(entity.getCurrentFaithPoints()).isEqualTo(8);
            assertThat(entity.getCurrentProtectionIndex()).isEqualTo(6);
        }

        @Test
        @DisplayName("Should handle null current magic points")
        void shouldHandleNullCurrentMagicPoints() {
            // Given
            when(points.getCurrentHitPoints()).thenReturn(Point.of(80));
            when(points.getCurrentInitiative()).thenReturn(Point.of(12));
            when(points.getCurrentHeroPoints()).thenReturn(Point.of(3));
            when(points.getCurrentMagicPoints()).thenReturn(null);
            when(points.getCurrentFaithPoints()).thenReturn(Point.of(8));
            when(points.getCurrentProtectionIndex()).thenReturn(Point.of(6));

            CharacterCurrentPointsUpdater updater = new CharacterCurrentPointsUpdater(points);
            entity.setCurrentMagicPoints(25); // Set initial value

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getCurrentHitPoints()).isEqualTo(80);
            assertThat(entity.getCurrentInitiative()).isEqualTo(12);
            assertThat(entity.getCurrentHeroPoints()).isEqualTo(3);
            assertThat(entity.getCurrentMagicPoints()).isEqualTo(25); // Should remain unchanged
            assertThat(entity.getCurrentFaithPoints()).isEqualTo(8);
            assertThat(entity.getCurrentProtectionIndex()).isEqualTo(6);
        }

        @Test
        @DisplayName("Should handle null current faith points")
        void shouldHandleNullCurrentFaithPoints() {
            // Given
            when(points.getCurrentHitPoints()).thenReturn(Point.of(80));
            when(points.getCurrentInitiative()).thenReturn(Point.of(12));
            when(points.getCurrentHeroPoints()).thenReturn(Point.of(3));
            when(points.getCurrentMagicPoints()).thenReturn(Point.of(15));
            when(points.getCurrentFaithPoints()).thenReturn(null);
            when(points.getCurrentProtectionIndex()).thenReturn(Point.of(6));

            CharacterCurrentPointsUpdater updater = new CharacterCurrentPointsUpdater(points);
            entity.setCurrentFaithPoints(12); // Set initial value

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getCurrentHitPoints()).isEqualTo(80);
            assertThat(entity.getCurrentInitiative()).isEqualTo(12);
            assertThat(entity.getCurrentHeroPoints()).isEqualTo(3);
            assertThat(entity.getCurrentMagicPoints()).isEqualTo(15);
            assertThat(entity.getCurrentFaithPoints()).isEqualTo(12); // Should remain unchanged
            assertThat(entity.getCurrentProtectionIndex()).isEqualTo(6);
        }

        @Test
        @DisplayName("Should handle null current protection index")
        void shouldHandleNullCurrentProtectionIndex() {
            // Given
            when(points.getCurrentHitPoints()).thenReturn(Point.of(80));
            when(points.getCurrentInitiative()).thenReturn(Point.of(12));
            when(points.getCurrentHeroPoints()).thenReturn(Point.of(3));
            when(points.getCurrentMagicPoints()).thenReturn(Point.of(15));
            when(points.getCurrentFaithPoints()).thenReturn(Point.of(8));
            when(points.getCurrentProtectionIndex()).thenReturn(null);

            CharacterCurrentPointsUpdater updater = new CharacterCurrentPointsUpdater(points);
            entity.setCurrentProtectionIndex(10); // Set initial value

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getCurrentHitPoints()).isEqualTo(80);
            assertThat(entity.getCurrentInitiative()).isEqualTo(12);
            assertThat(entity.getCurrentHeroPoints()).isEqualTo(3);
            assertThat(entity.getCurrentMagicPoints()).isEqualTo(15);
            assertThat(entity.getCurrentFaithPoints()).isEqualTo(8);
            assertThat(entity.getCurrentProtectionIndex()).isEqualTo(10); // Should remain unchanged
        }

        @Test
        @DisplayName("Should handle all null current points")
        void shouldHandleAllNullCurrentPoints() {
            // Given
            when(points.getCurrentHitPoints()).thenReturn(null);
            when(points.getCurrentInitiative()).thenReturn(null);
            when(points.getCurrentHeroPoints()).thenReturn(null);
            when(points.getCurrentMagicPoints()).thenReturn(null);
            when(points.getCurrentFaithPoints()).thenReturn(null);
            when(points.getCurrentProtectionIndex()).thenReturn(null);

            CharacterCurrentPointsUpdater updater = new CharacterCurrentPointsUpdater(points);
            
            // Set initial values
            entity.setCurrentHitPoints(50);
            entity.setCurrentInitiative(20);
            entity.setCurrentHeroPoints(2);
            entity.setCurrentMagicPoints(25);
            entity.setCurrentFaithPoints(12);
            entity.setCurrentProtectionIndex(10);

            // When
            updater.apply(entity);

            // Then - All values should remain unchanged
            assertThat(entity.getCurrentHitPoints()).isEqualTo(50);
            assertThat(entity.getCurrentInitiative()).isEqualTo(20);
            assertThat(entity.getCurrentHeroPoints()).isEqualTo(2);
            assertThat(entity.getCurrentMagicPoints()).isEqualTo(25);
            assertThat(entity.getCurrentFaithPoints()).isEqualTo(12);
            assertThat(entity.getCurrentProtectionIndex()).isEqualTo(10);
        }

        @Test
        @DisplayName("Should handle mixed null and valid current points")
        void shouldHandleMixedNullAndValidCurrentPoints() {
            // Given
            when(points.getCurrentHitPoints()).thenReturn(Point.of(80));
            when(points.getCurrentInitiative()).thenReturn(null);
            when(points.getCurrentHeroPoints()).thenReturn(Point.of(3));
            when(points.getCurrentMagicPoints()).thenReturn(null);
            when(points.getCurrentFaithPoints()).thenReturn(Point.of(8));
            when(points.getCurrentProtectionIndex()).thenReturn(null);

            CharacterCurrentPointsUpdater updater = new CharacterCurrentPointsUpdater(points);
            
            // Set initial values
            entity.setCurrentInitiative(20);
            entity.setCurrentMagicPoints(25);
            entity.setCurrentProtectionIndex(10);

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getCurrentHitPoints()).isEqualTo(80); // Updated
            assertThat(entity.getCurrentInitiative()).isEqualTo(20); // Unchanged
            assertThat(entity.getCurrentHeroPoints()).isEqualTo(3); // Updated
            assertThat(entity.getCurrentMagicPoints()).isEqualTo(25); // Unchanged
            assertThat(entity.getCurrentFaithPoints()).isEqualTo(8); // Updated
            assertThat(entity.getCurrentProtectionIndex()).isEqualTo(10); // Unchanged
        }

        @Test
        @DisplayName("Should handle zero values")
        void shouldHandleZeroValues() {
            // Given
            when(points.getCurrentHitPoints()).thenReturn(Point.of(0));
            when(points.getCurrentInitiative()).thenReturn(Point.of(0));
            when(points.getCurrentHeroPoints()).thenReturn(Point.of(0));
            when(points.getCurrentMagicPoints()).thenReturn(Point.of(0));
            when(points.getCurrentFaithPoints()).thenReturn(Point.of(0));
            when(points.getCurrentProtectionIndex()).thenReturn(Point.of(0));

            CharacterCurrentPointsUpdater updater = new CharacterCurrentPointsUpdater(points);

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getCurrentHitPoints()).isEqualTo(0);
            assertThat(entity.getCurrentInitiative()).isEqualTo(0);
            assertThat(entity.getCurrentHeroPoints()).isEqualTo(0);
            assertThat(entity.getCurrentMagicPoints()).isEqualTo(0);
            assertThat(entity.getCurrentFaithPoints()).isEqualTo(0);
            assertThat(entity.getCurrentProtectionIndex()).isEqualTo(0);
        }
    }
}