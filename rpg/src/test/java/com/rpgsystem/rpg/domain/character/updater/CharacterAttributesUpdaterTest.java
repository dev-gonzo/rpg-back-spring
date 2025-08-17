package com.rpgsystem.rpg.domain.character.updater;

import com.rpgsystem.rpg.api.dto.character.CharacterAttributeRequest;
import com.rpgsystem.rpg.domain.entity.AttributeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("CharacterAttributesUpdater Tests")
class CharacterAttributesUpdaterTest {

    private CharacterAttributeRequest request;
    private AttributeEntity entity;

    @BeforeEach
    void setUp() {
        entity = new AttributeEntity();
    }

    @Nested
    @DisplayName("Apply Method Tests")
    class ApplyMethodTests {

        @Test
        @DisplayName("Should update all attributes when all values are present")
        void shouldUpdateAllAttributesWhenAllValuesArePresent() {
            // Given
            request = CharacterAttributeRequest.builder()
                    .con(15)
                    .fr(12)
                    .dex(14)
                    .agi(13)
                    .intel(16)
                    .will(11)
                    .per(10)
                    .car(9)
                    .conMod(2)
                    .frMod(1)
                    .dexMod(2)
                    .agiMod(1)
                    .intMod(3)
                    .willMod(0)
                    .perMod(-1)
                    .carMod(-1)
                    .build();

            CharacterAttributesUpdater updater = new CharacterAttributesUpdater(request);

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getCon()).isEqualTo(15);
            assertThat(entity.getFr()).isEqualTo(12);
            assertThat(entity.getDex()).isEqualTo(14);
            assertThat(entity.getAgi()).isEqualTo(13);
            assertThat(entity.getIntel()).isEqualTo(16);
            assertThat(entity.getWill()).isEqualTo(11);
            assertThat(entity.getPer()).isEqualTo(10);
            assertThat(entity.getCar()).isEqualTo(9);
            
            assertThat(entity.getConMod()).isEqualTo(2);
            assertThat(entity.getFrMod()).isEqualTo(1);
            assertThat(entity.getDexMod()).isEqualTo(2);
            assertThat(entity.getAgiMod()).isEqualTo(1);
            assertThat(entity.getIntMod()).isEqualTo(3);
            assertThat(entity.getWillMod()).isEqualTo(0);
            assertThat(entity.getPerMod()).isEqualTo(-1);
            assertThat(entity.getCarMod()).isEqualTo(-1);
        }

        @Test
        @DisplayName("Should handle zero values for all attributes")
        void shouldHandleZeroValuesForAllAttributes() {
            // Given
            request = CharacterAttributeRequest.builder()
                    .con(0)
                    .fr(0)
                    .dex(0)
                    .agi(0)
                    .intel(0)
                    .will(0)
                    .per(0)
                    .car(0)
                    .conMod(0)
                    .frMod(0)
                    .dexMod(0)
                    .agiMod(0)
                    .intMod(0)
                    .willMod(0)
                    .perMod(0)
                    .carMod(0)
                    .build();

            CharacterAttributesUpdater updater = new CharacterAttributesUpdater(request);

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getCon()).isEqualTo(0);
            assertThat(entity.getFr()).isEqualTo(0);
            assertThat(entity.getDex()).isEqualTo(0);
            assertThat(entity.getAgi()).isEqualTo(0);
            assertThat(entity.getIntel()).isEqualTo(0);
            assertThat(entity.getWill()).isEqualTo(0);
            assertThat(entity.getPer()).isEqualTo(0);
            assertThat(entity.getCar()).isEqualTo(0);
            
            assertThat(entity.getConMod()).isEqualTo(0);
            assertThat(entity.getFrMod()).isEqualTo(0);
            assertThat(entity.getDexMod()).isEqualTo(0);
            assertThat(entity.getAgiMod()).isEqualTo(0);
            assertThat(entity.getIntMod()).isEqualTo(0);
            assertThat(entity.getWillMod()).isEqualTo(0);
            assertThat(entity.getPerMod()).isEqualTo(0);
            assertThat(entity.getCarMod()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should handle negative modifier values")
        void shouldHandleNegativeModifierValues() {
            // Given
            request = CharacterAttributeRequest.builder()
                    .con(15)
                    .fr(14)
                    .dex(13)
                    .agi(12)
                    .intel(16)
                    .will(11)
                    .per(10)
                    .car(9)
                    .conMod(-2)
                    .frMod(-1)
                    .dexMod(-3)
                    .agiMod(-4)
                    .intMod(-5)
                    .willMod(-6)
                    .perMod(-7)
                    .carMod(-8)
                    .build();

            CharacterAttributesUpdater updater = new CharacterAttributesUpdater(request);

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getCon()).isEqualTo(15);
            assertThat(entity.getFr()).isEqualTo(14);
            assertThat(entity.getDex()).isEqualTo(13);
            assertThat(entity.getAgi()).isEqualTo(12);
            assertThat(entity.getIntel()).isEqualTo(16);
            assertThat(entity.getWill()).isEqualTo(11);
            assertThat(entity.getPer()).isEqualTo(10);
            assertThat(entity.getCar()).isEqualTo(9);
            
            assertThat(entity.getConMod()).isEqualTo(-2);
            assertThat(entity.getFrMod()).isEqualTo(-1);
            assertThat(entity.getDexMod()).isEqualTo(-3);
            assertThat(entity.getAgiMod()).isEqualTo(-4);
            assertThat(entity.getIntMod()).isEqualTo(-5);
            assertThat(entity.getWillMod()).isEqualTo(-6);
            assertThat(entity.getPerMod()).isEqualTo(-7);
            assertThat(entity.getCarMod()).isEqualTo(-8);
        }

        @Test
        @DisplayName("Should not throw exception when request is null")
        void shouldNotThrowExceptionWhenRequestIsNull() {
            // Given
            CharacterAttributesUpdater updater = new CharacterAttributesUpdater(null);

            // When & Then
            assertDoesNotThrow(() -> updater.apply(entity));
        }

        @Test
        @DisplayName("Should not throw exception when entity is null")
        void shouldNotThrowExceptionWhenEntityIsNull() {
            // Given
            CharacterAttributeRequest validRequest = CharacterAttributeRequest.builder()
                    .con(15)
                    .fr(12)
                    .build();
            CharacterAttributesUpdater updater = new CharacterAttributesUpdater(validRequest);

            // When & Then
            assertDoesNotThrow(() -> updater.apply(null));
        }

        @Test
        @DisplayName("Should not throw exception when both request and entity are null")
        void shouldNotThrowExceptionWhenBothRequestAndEntityAreNull() {
            // Given
            CharacterAttributesUpdater updater = new CharacterAttributesUpdater(null);

            // When & Then
            assertDoesNotThrow(() -> updater.apply(null));
        }

        @Test
        @DisplayName("Should handle null modifier values")
        void shouldHandleNullModifierValues() {
            // Given
            request = CharacterAttributeRequest.builder()
                    .con(15)
                    .fr(14)
                    .dex(13)
                    .agi(12)
                    .intel(16)
                    .will(11)
                    .per(10)
                    .car(9)
                    .build();
            
            // All modifiers are null by default
            CharacterAttributesUpdater updater = new CharacterAttributesUpdater(request);

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getCon()).isEqualTo(15);
            assertThat(entity.getFr()).isEqualTo(14);
            assertThat(entity.getDex()).isEqualTo(13);
            assertThat(entity.getAgi()).isEqualTo(12);
            assertThat(entity.getIntel()).isEqualTo(16);
            assertThat(entity.getWill()).isEqualTo(11);
            assertThat(entity.getPer()).isEqualTo(10);
            assertThat(entity.getCar()).isEqualTo(9);
            
            assertThat(entity.getConMod()).isNull();
            assertThat(entity.getFrMod()).isNull();
            assertThat(entity.getDexMod()).isNull();
            assertThat(entity.getAgiMod()).isNull();
            assertThat(entity.getIntMod()).isNull();
            assertThat(entity.getWillMod()).isNull();
            assertThat(entity.getPerMod()).isNull();
            assertThat(entity.getCarMod()).isNull();
        }

        @Test
        @DisplayName("Should handle mixed null and valid modifier values")
        void shouldHandleMixedNullAndValidModifierValues() {
            // Given
            request = CharacterAttributeRequest.builder()
                    .con(15)
                    .fr(14)
                    .dex(13)
                    .agi(12)
                    .intel(16)
                    .will(11)
                    .per(10)
                    .car(9)
                    .conMod(2)
                    .frMod(null)
                    .dexMod(0)
                    .agiMod(null)
                    .intMod(3)
                    .willMod(null)
                    .perMod(-3)
                    .carMod(null)
                    .build();

            CharacterAttributesUpdater updater = new CharacterAttributesUpdater(request);

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getConMod()).isEqualTo(2);
            assertThat(entity.getFrMod()).isNull();
            assertThat(entity.getDexMod()).isEqualTo(0);
            assertThat(entity.getAgiMod()).isNull();
            assertThat(entity.getIntMod()).isEqualTo(3);
            assertThat(entity.getWillMod()).isNull();
            assertThat(entity.getPerMod()).isEqualTo(-3);
            assertThat(entity.getCarMod()).isNull();
        }
    }

    @Nested
    @DisplayName("ToValue Method Tests")
    class ToValueMethodTests {

        @Test
        @DisplayName("Should return correct value for positive modifier")
        void shouldReturnCorrectValueForPositiveModifier() {
            // Given
            request = CharacterAttributeRequest.builder()
                    .conMod(5)
                    .build();
            CharacterAttributesUpdater updater = new CharacterAttributesUpdater(request);

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getConMod()).isEqualTo(5);
        }

        @Test
        @DisplayName("Should return correct value for negative modifier")
        void shouldReturnCorrectValueForNegativeModifier() {
            // Given
            request = CharacterAttributeRequest.builder()
                    .conMod(-5)
                    .build();
            CharacterAttributesUpdater updater = new CharacterAttributesUpdater(request);

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getConMod()).isEqualTo(-5);
        }

        @Test
        @DisplayName("Should return correct value for zero modifier")
        void shouldReturnCorrectValueForZeroModifier() {
            // Given
            request = CharacterAttributeRequest.builder()
                    .conMod(0)
                    .build();
            CharacterAttributesUpdater updater = new CharacterAttributesUpdater(request);

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getConMod()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should return null for null modifier")
        void shouldReturnNullForNullModifier() {
            // Given
            request = CharacterAttributeRequest.builder()
                    .conMod(null)
                    .build();
            CharacterAttributesUpdater updater = new CharacterAttributesUpdater(request);

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getConMod()).isNull();
        }
    }
}