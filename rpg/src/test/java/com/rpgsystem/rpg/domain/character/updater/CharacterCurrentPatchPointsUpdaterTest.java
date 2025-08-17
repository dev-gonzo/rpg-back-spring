package com.rpgsystem.rpg.domain.character.updater;

import com.rpgsystem.rpg.api.dto.character.CharacterCurrentPointsRequest;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("CharacterCurrentPatchPointsUpdater Tests")
class CharacterCurrentPatchPointsUpdaterTest {

    private CharacterCurrentPointsRequest request;
    private CharacterEntity entity;

    @BeforeEach
    void setUp() {
        entity = new CharacterEntity();
    }

    @Nested
    @DisplayName("Apply Method Tests")
    class ApplyMethodTests {

        @Test
        @DisplayName("Should apply all current points when all values are present")
        void shouldApplyAllCurrentPointsWhenAllValuesArePresent() {
            // Given
            request = CharacterCurrentPointsRequest.builder()
                    .currentHitPoints(75)
                    .currentInitiative(10)
                    .currentHeroPoints(2)
                    .currentMagicPoints(18)
                    .currentFaithPoints(7)
                    .currentProtectionIndex(5)
                    .build();

            CharacterCurrentPatchPointsUpdater updater = new CharacterCurrentPatchPointsUpdater(request);

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getCurrentHitPoints()).isEqualTo(75);
            assertThat(entity.getCurrentInitiative()).isEqualTo(10);
            assertThat(entity.getCurrentHeroPoints()).isEqualTo(2);
            assertThat(entity.getCurrentMagicPoints()).isEqualTo(18);
            assertThat(entity.getCurrentFaithPoints()).isEqualTo(7);
            assertThat(entity.getCurrentProtectionIndex()).isEqualTo(5);
        }

        @Test
        @DisplayName("Should not update when request is null")
        void shouldNotUpdateWhenRequestIsNull() {
            // Given
            CharacterCurrentPatchPointsUpdater updater = new CharacterCurrentPatchPointsUpdater(null);
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
            request = CharacterCurrentPointsRequest.builder()
                    .currentHitPoints(75)
                    .currentInitiative(10)
                    .currentHeroPoints(2)
                    .currentMagicPoints(18)
                    .currentFaithPoints(7)
                    .currentProtectionIndex(5)
                    .build();
            CharacterCurrentPatchPointsUpdater updater = new CharacterCurrentPatchPointsUpdater(request);

            // When & Then
            assertDoesNotThrow(() -> updater.apply(null));
        }

        @Test
        @DisplayName("Should not update when both request and entity are null")
        void shouldNotUpdateWhenBothRequestAndEntityAreNull() {
            // Given
            CharacterCurrentPatchPointsUpdater updater = new CharacterCurrentPatchPointsUpdater(null);

            // When & Then
            assertDoesNotThrow(() -> updater.apply(null));
        }

        @Test
        @DisplayName("Should handle null current hit points")
        void shouldHandleNullCurrentHitPoints() {
            // Given
            request = CharacterCurrentPointsRequest.builder()
                    .currentHitPoints(null)
                    .currentInitiative(10)
                    .currentHeroPoints(2)
                    .currentMagicPoints(18)
                    .currentFaithPoints(7)
                    .currentProtectionIndex(5)
                    .build();

            CharacterCurrentPatchPointsUpdater updater = new CharacterCurrentPatchPointsUpdater(request);
            entity.setCurrentHitPoints(50); // Set initial value

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getCurrentHitPoints()).isEqualTo(50); // Should remain unchanged
            assertThat(entity.getCurrentInitiative()).isEqualTo(10);
            assertThat(entity.getCurrentHeroPoints()).isEqualTo(2);
            assertThat(entity.getCurrentMagicPoints()).isEqualTo(18);
            assertThat(entity.getCurrentFaithPoints()).isEqualTo(7);
            assertThat(entity.getCurrentProtectionIndex()).isEqualTo(5);
        }

        @Test
        @DisplayName("Should handle null current initiative")
        void shouldHandleNullCurrentInitiative() {
            // Given
            request = CharacterCurrentPointsRequest.builder()
                    .currentHitPoints(75)
                    .currentInitiative(null)
                    .currentHeroPoints(2)
                    .currentMagicPoints(18)
                    .currentFaithPoints(7)
                    .currentProtectionIndex(5)
                    .build();

            CharacterCurrentPatchPointsUpdater updater = new CharacterCurrentPatchPointsUpdater(request);
            entity.setCurrentInitiative(20); // Set initial value

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getCurrentHitPoints()).isEqualTo(75);
            assertThat(entity.getCurrentInitiative()).isEqualTo(20); // Should remain unchanged
            assertThat(entity.getCurrentHeroPoints()).isEqualTo(2);
            assertThat(entity.getCurrentMagicPoints()).isEqualTo(18);
            assertThat(entity.getCurrentFaithPoints()).isEqualTo(7);
            assertThat(entity.getCurrentProtectionIndex()).isEqualTo(5);
        }

        @Test
        @DisplayName("Should handle null current hero points")
        void shouldHandleNullCurrentHeroPoints() {
            // Given
            request = CharacterCurrentPointsRequest.builder()
                    .currentHitPoints(75)
                    .currentInitiative(10)
                    .currentHeroPoints(null)
                    .currentMagicPoints(18)
                    .currentFaithPoints(7)
                    .currentProtectionIndex(5)
                    .build();

            CharacterCurrentPatchPointsUpdater updater = new CharacterCurrentPatchPointsUpdater(request);
            entity.setCurrentHeroPoints(3); // Set initial value

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getCurrentHitPoints()).isEqualTo(75);
            assertThat(entity.getCurrentInitiative()).isEqualTo(10);
            assertThat(entity.getCurrentHeroPoints()).isEqualTo(3); // Should remain unchanged
            assertThat(entity.getCurrentMagicPoints()).isEqualTo(18);
            assertThat(entity.getCurrentFaithPoints()).isEqualTo(7);
            assertThat(entity.getCurrentProtectionIndex()).isEqualTo(5);
        }

        @Test
        @DisplayName("Should handle null current magic points")
        void shouldHandleNullCurrentMagicPoints() {
            // Given
            request = CharacterCurrentPointsRequest.builder()
                    .currentHitPoints(75)
                    .currentInitiative(10)
                    .currentHeroPoints(2)
                    .currentMagicPoints(null)
                    .currentFaithPoints(7)
                    .currentProtectionIndex(5)
                    .build();

            CharacterCurrentPatchPointsUpdater updater = new CharacterCurrentPatchPointsUpdater(request);
            entity.setCurrentMagicPoints(25); // Set initial value

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getCurrentHitPoints()).isEqualTo(75);
            assertThat(entity.getCurrentInitiative()).isEqualTo(10);
            assertThat(entity.getCurrentHeroPoints()).isEqualTo(2);
            assertThat(entity.getCurrentMagicPoints()).isEqualTo(25); // Should remain unchanged
            assertThat(entity.getCurrentFaithPoints()).isEqualTo(7);
            assertThat(entity.getCurrentProtectionIndex()).isEqualTo(5);
        }

        @Test
        @DisplayName("Should handle null current faith points")
        void shouldHandleNullCurrentFaithPoints() {
            // Given
            request = CharacterCurrentPointsRequest.builder()
                    .currentHitPoints(75)
                    .currentInitiative(10)
                    .currentHeroPoints(2)
                    .currentMagicPoints(18)
                    .currentFaithPoints(null)
                    .currentProtectionIndex(5)
                    .build();

            CharacterCurrentPatchPointsUpdater updater = new CharacterCurrentPatchPointsUpdater(request);
            entity.setCurrentFaithPoints(12); // Set initial value

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getCurrentHitPoints()).isEqualTo(75);
            assertThat(entity.getCurrentInitiative()).isEqualTo(10);
            assertThat(entity.getCurrentHeroPoints()).isEqualTo(2);
            assertThat(entity.getCurrentMagicPoints()).isEqualTo(18);
            assertThat(entity.getCurrentFaithPoints()).isEqualTo(12); // Should remain unchanged
            assertThat(entity.getCurrentProtectionIndex()).isEqualTo(5);
        }

        @Test
        @DisplayName("Should handle null current protection index")
        void shouldHandleNullCurrentProtectionIndex() {
            // Given
            request = CharacterCurrentPointsRequest.builder()
                    .currentHitPoints(75)
                    .currentInitiative(10)
                    .currentHeroPoints(2)
                    .currentMagicPoints(18)
                    .currentFaithPoints(7)
                    .currentProtectionIndex(null)
                    .build();

            CharacterCurrentPatchPointsUpdater updater = new CharacterCurrentPatchPointsUpdater(request);
            entity.setCurrentProtectionIndex(8); // Set initial value

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getCurrentHitPoints()).isEqualTo(75);
            assertThat(entity.getCurrentInitiative()).isEqualTo(10);
            assertThat(entity.getCurrentHeroPoints()).isEqualTo(2);
            assertThat(entity.getCurrentMagicPoints()).isEqualTo(18);
            assertThat(entity.getCurrentFaithPoints()).isEqualTo(7);
            assertThat(entity.getCurrentProtectionIndex()).isEqualTo(8); // Should remain unchanged
        }

        @Test
        @DisplayName("Should handle all null current points")
        void shouldHandleAllNullCurrentPoints() {
            // Given
            request = CharacterCurrentPointsRequest.builder()
                    .currentHitPoints(null)
                    .currentInitiative(null)
                    .currentHeroPoints(null)
                    .currentMagicPoints(null)
                    .currentFaithPoints(null)
                    .currentProtectionIndex(null)
                    .build();

            CharacterCurrentPatchPointsUpdater updater = new CharacterCurrentPatchPointsUpdater(request);
            
            // Set initial values
            entity.setCurrentHitPoints(50);
            entity.setCurrentInitiative(20);
            entity.setCurrentHeroPoints(3);
            entity.setCurrentMagicPoints(25);
            entity.setCurrentFaithPoints(12);
            entity.setCurrentProtectionIndex(8);

            // When
            updater.apply(entity);

            // Then - All values should remain unchanged
            assertThat(entity.getCurrentHitPoints()).isEqualTo(50);
            assertThat(entity.getCurrentInitiative()).isEqualTo(20);
            assertThat(entity.getCurrentHeroPoints()).isEqualTo(3);
            assertThat(entity.getCurrentMagicPoints()).isEqualTo(25);
            assertThat(entity.getCurrentFaithPoints()).isEqualTo(12);
            assertThat(entity.getCurrentProtectionIndex()).isEqualTo(8);
        }

        @Test
        @DisplayName("Should handle mixed null and valid current points")
        void shouldHandleMixedNullAndValidCurrentPoints() {
            // Given
            request = CharacterCurrentPointsRequest.builder()
                    .currentHitPoints(75)
                    .currentInitiative(null)
                    .currentHeroPoints(2)
                    .currentMagicPoints(null)
                    .currentFaithPoints(7)
                    .currentProtectionIndex(null)
                    .build();

            CharacterCurrentPatchPointsUpdater updater = new CharacterCurrentPatchPointsUpdater(request);
            
            // Set initial values
            entity.setCurrentInitiative(20);
            entity.setCurrentMagicPoints(25);
            entity.setCurrentProtectionIndex(8);

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getCurrentHitPoints()).isEqualTo(75); // Updated
            assertThat(entity.getCurrentInitiative()).isEqualTo(20); // Unchanged
            assertThat(entity.getCurrentHeroPoints()).isEqualTo(2); // Updated
            assertThat(entity.getCurrentMagicPoints()).isEqualTo(25); // Unchanged
            assertThat(entity.getCurrentFaithPoints()).isEqualTo(7); // Updated
            assertThat(entity.getCurrentProtectionIndex()).isEqualTo(8); // Unchanged
        }

        @Test
        @DisplayName("Should handle zero values")
        void shouldHandleZeroValues() {
            // Given
            request = CharacterCurrentPointsRequest.builder()
                    .currentHitPoints(0)
                    .currentInitiative(0)
                    .currentHeroPoints(0)
                    .currentMagicPoints(0)
                    .currentFaithPoints(0)
                    .currentProtectionIndex(0)
                    .build();

            CharacterCurrentPatchPointsUpdater updater = new CharacterCurrentPatchPointsUpdater(request);

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

        @Test
        @DisplayName("Should handle negative values")
        void shouldHandleNegativeValues() {
            // Given
            request = CharacterCurrentPointsRequest.builder()
                    .currentHitPoints(-5)
                    .currentInitiative(-2)
                    .currentHeroPoints(-1)
                    .currentMagicPoints(-10)
                    .currentFaithPoints(-3)
                    .currentProtectionIndex(-1)
                    .build();

            CharacterCurrentPatchPointsUpdater updater = new CharacterCurrentPatchPointsUpdater(request);

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getCurrentHitPoints()).isEqualTo(-5);
            assertThat(entity.getCurrentInitiative()).isEqualTo(-2);
            assertThat(entity.getCurrentHeroPoints()).isEqualTo(-1);
            assertThat(entity.getCurrentMagicPoints()).isEqualTo(-10);
            assertThat(entity.getCurrentFaithPoints()).isEqualTo(-3);
            assertThat(entity.getCurrentProtectionIndex()).isEqualTo(-1);
        }
    }
}