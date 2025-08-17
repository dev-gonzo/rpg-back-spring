package com.rpgsystem.rpg.domain.character.valueObject;

import com.rpgsystem.rpg.domain.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Height Value Object Tests")
class HeightTest {

    @Nested
    @DisplayName("Height Creation with 'of' method Tests")
    class HeightOfMethodTests {

        @Test
        @DisplayName("Should create Height with positive value using of method")
        void shouldCreateHeightWithPositiveValueUsingOfMethod() {
            // Given
            int value = 180;

            // When
            Height height = Height.of(value);

            // Then
            assertThat(height).isNotNull();
            assertThat(height.getCentimeters()).isEqualTo(value);
        }

        @Test
        @DisplayName("Should create Height with minimum valid value using of method")
        void shouldCreateHeightWithMinimumValidValueUsingOfMethod() {
            // Given
            int value = 1;

            // When
            Height height = Height.of(value);

            // Then
            assertThat(height).isNotNull();
            assertThat(height.getCentimeters()).isEqualTo(value);
        }

        @Test
        @DisplayName("Should create Height with large value using of method")
        void shouldCreateHeightWithLargeValueUsingOfMethod() {
            // Given
            int value = 300;

            // When
            Height height = Height.of(value);

            // Then
            assertThat(height).isNotNull();
            assertThat(height.getCentimeters()).isEqualTo(value);
        }

        @Test
        @DisplayName("Should throw exception when value is zero using of method")
        void shouldThrowExceptionWhenValueIsZeroUsingOfMethod() {
            // Given
            int zeroValue = 0;

            // When & Then
            assertThatThrownBy(() -> Height.of(zeroValue))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Height must be greater than 0 cm.");
        }

        @Test
        @DisplayName("Should throw exception when value is negative using of method")
        void shouldThrowExceptionWhenValueIsNegativeUsingOfMethod() {
            // Given
            int negativeValue = -10;

            // When & Then
            assertThatThrownBy(() -> Height.of(negativeValue))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Height must be greater than 0 cm.");
        }

        @Test
        @DisplayName("Should throw exception when value is very negative using of method")
        void shouldThrowExceptionWhenValueIsVeryNegativeUsingOfMethod() {
            // Given
            int veryNegativeValue = Integer.MIN_VALUE;

            // When & Then
            assertThatThrownBy(() -> Height.of(veryNegativeValue))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Height must be greater than 0 cm.");
        }

        @Test
        @DisplayName("Should return null when value is null using of method")
        void shouldReturnNullWhenValueIsNullUsingOfMethod() {
            // Given
            Integer nullValue = null;

            // When
            Height height = Height.of(nullValue);

            // Then
            assertThat(height).isNull();
        }
    }

    @Nested
    @DisplayName("Height Creation with 'from' method Tests")
    class HeightFromMethodTests {

        @Test
        @DisplayName("Should create Height with positive value using from method")
        void shouldCreateHeightWithPositiveValueUsingFromMethod() {
            // Given
            String value = "175";

            // When
            Height height = Height.from(value);

            // Then
            assertThat(height).isNotNull();
            assertThat(height.getCentimeters()).isEqualTo(175);
        }

        @Test
        @DisplayName("Should create Height with minimum valid value using from method")
        void shouldCreateHeightWithMinimumValidValueUsingFromMethod() {
            // Given
            String value = "1";

            // When
            Height height = Height.from(value);

            // Then
            assertThat(height).isNotNull();
            assertThat(height.getCentimeters()).isEqualTo(1);
        }

        @Test
        @DisplayName("Should create Height with large value using from method")
        void shouldCreateHeightWithLargeValueUsingFromMethod() {
            // Given
            String value = "250";

            // When
            Height height = Height.from(value);

            // Then
            assertThat(height).isNotNull();
            assertThat(height.getCentimeters()).isEqualTo(250);
        }

        @Test
        @DisplayName("Should throw exception when value is zero using from method")
        void shouldThrowExceptionWhenValueIsZeroUsingFromMethod() {
            // Given
            int zeroValue = 0;

            // When & Then
            assertThatThrownBy(() -> Height.from(String.valueOf(zeroValue)))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Height must be greater than 0 cm.");
        }

        @Test
        @DisplayName("Should throw exception when value is negative using from method")
        void shouldThrowExceptionWhenValueIsNegativeUsingFromMethod() {
            // Given
            int negativeValue = -5;

            // When & Then
            assertThatThrownBy(() -> Height.from(String.valueOf(negativeValue)))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Height must be greater than 0 cm.");
        }

        @Test
        @DisplayName("Should throw exception when value is very negative using from method")
        void shouldThrowExceptionWhenValueIsVeryNegativeUsingFromMethod() {
            // Given
            int veryNegativeValue = Integer.MIN_VALUE;

            // When & Then
            assertThatThrownBy(() -> Height.from(String.valueOf(veryNegativeValue)))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Height must be greater than 0 cm.");
        }

        @Test
        @DisplayName("Should return null when value is null using from method")
        void shouldReturnNullWhenValueIsNullUsingFromMethod() {
            // Given
            String nullValue = null;

            // When
            Height height = Height.from(nullValue);

            // Then
            assertThat(height).isNull();
        }

        @Test
        @DisplayName("Should return null when value is blank using from method")
        void shouldReturnNullWhenValueIsBlankUsingFromMethod() {
            // Given
            String blankValue = "   ";

            // When
            Height height = Height.from(blankValue);

            // Then
            assertThat(height).isNull();
        }

        @Test
        @DisplayName("Should return null when value is empty using from method")
        void shouldReturnNullWhenValueIsEmptyUsingFromMethod() {
            // Given
            String emptyValue = "";

            // When
            Height height = Height.from(emptyValue);

            // Then
            assertThat(height).isNull();
        }

        @Test
        @DisplayName("Should throw exception when value is not a valid number using from method")
        void shouldThrowExceptionWhenValueIsNotValidNumberUsingFromMethod() {
            // Given
            String invalidValue = "abc";

            // When & Then
            assertThatThrownBy(() -> Height.from(invalidValue))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Height must be a valid number.");
        }

        @Test
        @DisplayName("Should throw exception when value contains special characters using from method")
        void shouldThrowExceptionWhenValueContainsSpecialCharactersUsingFromMethod() {
            // Given
            String invalidValue = "12.5";

            // When & Then
            assertThatThrownBy(() -> Height.from(invalidValue))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Height must be a valid number.");
        }

        @Test
        @DisplayName("Should throw exception when value is alphanumeric using from method")
        void shouldThrowExceptionWhenValueIsAlphanumericUsingFromMethod() {
            // Given
            String invalidValue = "180cm";

            // When & Then
            assertThatThrownBy(() -> Height.from(invalidValue))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Height must be a valid number.");
        }
    }

    @Nested
    @DisplayName("Height Equality and Behavior Tests")
    class HeightEqualityAndBehaviorTests {

        @Test
        @DisplayName("Should be equal when values are the same regardless of creation method")
        void shouldBeEqualWhenValuesAreTheSameRegardlessOfCreationMethod() {
            // Given
            Height heightFromOf = Height.of(170);
            Height heightFromFrom = Height.from("170");

            // When & Then
            assertThat(heightFromOf).isEqualTo(heightFromFrom);
            assertThat(heightFromOf.hashCode()).isEqualTo(heightFromFrom.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when values are different")
        void shouldNotBeEqualWhenValuesAreDifferent() {
            // Given
            Height height1 = Height.of(170);
            Height height2 = Height.of(180);

            // When & Then
            assertThat(height1).isNotEqualTo(height2);
            assertThat(height1.hashCode()).isNotEqualTo(height2.hashCode());
        }

        @Test
        @DisplayName("Should have proper toString representation")
        void shouldHaveProperToStringRepresentation() {
            // Given
            Height height = Height.of(185);

            // When
            String result = height.toString();

            // Then
            assertThat(result).contains("185");
            assertThat(result).contains("Height");
        }

        @Test
        @DisplayName("Should not be equal to null")
        void shouldNotBeEqualToNull() {
            // Given
            Height height = Height.of(160);

            // When & Then
            assertThat(height).isNotEqualTo(null);
        }

        @Test
        @DisplayName("Should not be equal to different class")
        void shouldNotBeEqualToDifferentClass() {
            // Given
            Height height = Height.of(160);
            Integer differentClass = 160;

            // When & Then
            assertThat(height).isNotEqualTo(differentClass);
        }

        @Test
        @DisplayName("Should be equal to itself")
        void shouldBeEqualToItself() {
            // Given
            Height height = Height.of(190);

            // When & Then
            assertThat(height).isEqualTo(height);
        }

        @Test
        @DisplayName("Should create different instances with same value")
        void shouldCreateDifferentInstancesWithSameValue() {
            // Given
            Height height1 = Height.of(175);
            Height height2 = Height.of(175);

            // When & Then
            assertThat(height1).isNotSameAs(height2);
            assertThat(height1).isEqualTo(height2);
        }
    }

    @Nested
    @DisplayName("Height Realistic Values Tests")
    class HeightRealisticValuesTests {

        @Test
        @DisplayName("Should handle typical human height values in centimeters")
        void shouldHandleTypicalHumanHeightValuesInCentimeters() {
            // Given - typical human heights in cm
            int[] typicalHeights = {150, 160, 170, 180, 190, 200};

            // When & Then
            for (int heightValue : typicalHeights) {
                Height height = Height.of(heightValue);
                assertThat(height).isNotNull();
                assertThat(height.getCentimeters()).isEqualTo(heightValue);
            }
        }

        @Test
        @DisplayName("Should handle extreme but valid height values")
        void shouldHandleExtremeButValidHeightValues() {
            // Given - extreme but theoretically possible heights
            int[] extremeHeights = {50, 100, 250, 300}; // very short to very tall

            // When & Then
            for (int heightValue : extremeHeights) {
                Height height = Height.from(String.valueOf(heightValue));
                assertThat(height).isNotNull();
                assertThat(height.getCentimeters()).isEqualTo(heightValue);
            }
        }

        @Test
        @DisplayName("Should handle maximum integer value")
        void shouldHandleMaximumIntegerValue() {
            // Given
            int maxValue = Integer.MAX_VALUE;

            // When
            Height height = Height.of(maxValue);

            // Then
            assertThat(height).isNotNull();
            assertThat(height.getCentimeters()).isEqualTo(maxValue);
        }
    }

    @Nested
    @DisplayName("Height Method Equivalence Tests")
    class HeightMethodEquivalenceTests {

        @Test
        @DisplayName("Should produce equivalent results from 'of' and 'from' methods")
        void shouldProduceEquivalentResultsFromOfAndFromMethods() {
            // Given
            int[] testValues = {1, 50, 100, 150, 200, 250, 1000};

            // When & Then
            for (int value : testValues) {
                Height heightFromOf = Height.of(value);
                Height heightFromFrom = Height.from(String.valueOf(value));
                
                assertThat(heightFromOf).isEqualTo(heightFromFrom);
                assertThat(heightFromOf.getCentimeters()).isEqualTo(heightFromFrom.getCentimeters());
                assertThat(heightFromOf.hashCode()).isEqualTo(heightFromFrom.hashCode());
            }
        }

        @Test
        @DisplayName("Should throw same exception for invalid values in both methods")
        void shouldThrowSameExceptionForInvalidValuesInBothMethods() {
            // Given
            int[] invalidValues = {0, -1, -10, -100, Integer.MIN_VALUE};

            // When & Then
            for (int invalidValue : invalidValues) {
                // Test 'of' method
                assertThatThrownBy(() -> Height.of(invalidValue))
                        .isInstanceOf(DomainException.class)
                        .hasMessage("Height must be greater than 0 cm.");
                
                // Test 'from' method
                assertThatThrownBy(() -> Height.from(String.valueOf(invalidValue)))
                        .isInstanceOf(DomainException.class)
                        .hasMessage("Height must be greater than 0 cm.");
            }
        }
    }

    @Nested
    @DisplayName("Height Immutability Tests")
    class HeightImmutabilityTests {

        @Test
        @DisplayName("Should maintain immutability")
        void shouldMaintainImmutability() {
            // Given
            int originalValue = 175;
            Height height = Height.of(originalValue);

            // When
            int retrievedValue = height.getCentimeters();

            // Then
            assertThat(retrievedValue).isEqualTo(originalValue);
            // Verify that the value cannot be changed (immutable)
            assertThat(height.getCentimeters()).isEqualTo(originalValue);
        }

        @Test
        @DisplayName("Should create independent instances")
        void shouldCreateIndependentInstances() {
            // Given
            int value = 180;
            Height height1 = Height.of(value);
            Height height2 = Height.from(String.valueOf(value));
            Height height3 = Height.of(value);

            // When & Then
            assertThat(height1).isNotSameAs(height2);
            assertThat(height1).isNotSameAs(height3);
            assertThat(height2).isNotSameAs(height3);
            
            // But they should be equal
            assertThat(height1).isEqualTo(height2);
            assertThat(height1).isEqualTo(height3);
            assertThat(height2).isEqualTo(height3);
        }
    }
}