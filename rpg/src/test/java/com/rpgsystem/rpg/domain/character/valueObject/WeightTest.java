package com.rpgsystem.rpg.domain.character.valueObject;

import com.rpgsystem.rpg.domain.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Weight Value Object Tests")
class WeightTest {

    @Nested
    @DisplayName("Weight Creation with 'of' method Tests")
    class WeightOfMethodTests {

        @Test
        @DisplayName("Should create Weight with positive value using of method")
        void shouldCreateWeightWithPositiveValueUsingOfMethod() {
            // Given
            Integer value = 70;

            // When
            Weight weight = Weight.of(value);

            // Then
            assertThat(weight).isNotNull();
            assertThat(weight.getKilograms()).isEqualTo(value);
        }

        @Test
        @DisplayName("Should create Weight with minimum valid value using of method")
        void shouldCreateWeightWithMinimumValidValueUsingOfMethod() {
            // Given
            Integer value = 1;

            // When
            Weight weight = Weight.of(value);

            // Then
            assertThat(weight).isNotNull();
            assertThat(weight.getKilograms()).isEqualTo(value);
        }

        @Test
        @DisplayName("Should create Weight with large value using of method")
        void shouldCreateWeightWithLargeValueUsingOfMethod() {
            // Given
            Integer value = 200;

            // When
            Weight weight = Weight.of(value);

            // Then
            assertThat(weight).isNotNull();
            assertThat(weight.getKilograms()).isEqualTo(value);
        }

        @Test
        @DisplayName("Should return null when value is null using of method")
        void shouldReturnNullWhenValueIsNullUsingOfMethod() {
            // Given
            Integer nullValue = null;

            // When
            Weight weight = Weight.of(nullValue);

            // Then
            assertThat(weight).isNull();
        }

        @Test
        @DisplayName("Should throw exception when value is zero using of method")
        void shouldThrowExceptionWhenValueIsZeroUsingOfMethod() {
            // Given
            Integer zeroValue = 0;

            // When & Then
            assertThatThrownBy(() -> Weight.of(zeroValue))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Weight must be greater than 0 kg.");
        }

        @Test
        @DisplayName("Should throw exception when value is negative using of method")
        void shouldThrowExceptionWhenValueIsNegativeUsingOfMethod() {
            // Given
            Integer negativeValue = -10;

            // When & Then
            assertThatThrownBy(() -> Weight.of(negativeValue))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Weight must be greater than 0 kg.");
        }
    }

    @Nested
    @DisplayName("Weight Creation with 'from' method Tests")
    class WeightFromMethodTests {

        @Test
        @DisplayName("Should create Weight with valid string value using from method")
        void shouldCreateWeightWithValidStringValueUsingFromMethod() {
            // Given
            String value = "85";

            // When
            Weight weight = Weight.from(value);

            // Then
            assertThat(weight).isNotNull();
            assertThat(weight.getKilograms()).isEqualTo(85);
        }

        @Test
        @DisplayName("Should create Weight with minimum valid string value using from method")
        void shouldCreateWeightWithMinimumValidStringValueUsingFromMethod() {
            // Given
            String value = "1";

            // When
            Weight weight = Weight.from(value);

            // Then
            assertThat(weight).isNotNull();
            assertThat(weight.getKilograms()).isEqualTo(1);
        }

        @Test
        @DisplayName("Should return null when string is null using from method")
        void shouldReturnNullWhenStringIsNullUsingFromMethod() {
            // Given
            String nullValue = null;

            // When
            Weight weight = Weight.from(nullValue);

            // Then
            assertThat(weight).isNull();
        }

        @Test
        @DisplayName("Should return null when string is blank using from method")
        void shouldReturnNullWhenStringIsBlankUsingFromMethod() {
            // Given
            String blankValue = "   ";

            // When
            Weight weight = Weight.from(blankValue);

            // Then
            assertThat(weight).isNull();
        }

        @Test
        @DisplayName("Should return null when string is empty using from method")
        void shouldReturnNullWhenStringIsEmptyUsingFromMethod() {
            // Given
            String emptyValue = "";

            // When
            Weight weight = Weight.from(emptyValue);

            // Then
            assertThat(weight).isNull();
        }

        @Test
        @DisplayName("Should throw exception when string is not a number using from method")
        void shouldThrowExceptionWhenStringIsNotANumberUsingFromMethod() {
            // Given
            String invalidValue = "abc";

            // When & Then
            assertThatThrownBy(() -> Weight.from(invalidValue))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Weight must be a valid number.");
        }

        @Test
        @DisplayName("Should throw exception when string represents zero using from method")
        void shouldThrowExceptionWhenStringRepresentsZeroUsingFromMethod() {
            // Given
            String zeroValue = "0";

            // When & Then
            assertThatThrownBy(() -> Weight.from(zeroValue))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Weight must be greater than 0 kg.");
        }

        @Test
        @DisplayName("Should throw exception when string represents negative value using from method")
        void shouldThrowExceptionWhenStringRepresentsNegativeValueUsingFromMethod() {
            // Given
            String negativeValue = "-5";

            // When & Then
            assertThatThrownBy(() -> Weight.from(negativeValue))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Weight must be greater than 0 kg.");
        }

        @Test
        @DisplayName("Should throw exception for decimal string values")
        void shouldThrowExceptionForDecimalStringValues() {
            // Given
            String decimalValue = "75.5";

            // When & Then
            assertThatThrownBy(() -> Weight.from(decimalValue))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Weight must be a valid number.");
        }
    }

    @Nested
    @DisplayName("Weight inGrams() method Tests")
    class WeightInGramsMethodTests {

        @Test
        @DisplayName("Should convert weight to grams correctly")
        void shouldConvertWeightToGramsCorrectly() {
            // Given
            Weight weight = Weight.of(70);

            // When
            double weightInGrams = weight.inGrams();

            // Then
            assertThat(weightInGrams).isEqualTo(70000.0); // 70 kg = 70,000 grams
        }

        @Test
        @DisplayName("Should convert minimum weight to grams correctly")
        void shouldConvertMinimumWeightToGramsCorrectly() {
            // Given
            Weight weight = Weight.of(1);

            // When
            double weightInGrams = weight.inGrams();

            // Then
            assertThat(weightInGrams).isEqualTo(1000.0); // 1 kg = 1,000 grams
        }

        @Test
        @DisplayName("Should convert large weight to grams correctly")
        void shouldConvertLargeWeightToGramsCorrectly() {
            // Given
            Weight weight = Weight.of(200);

            // When
            double weightInGrams = weight.inGrams();

            // Then
            assertThat(weightInGrams).isEqualTo(200000.0); // 200 kg = 200,000 grams
        }

        @Test
        @DisplayName("Should handle typical human weights in grams conversion")
        void shouldHandleTypicalHumanWeightsInGramsConversion() {
            // Given - typical human weights in kg
            int[] typicalWeights = {50, 60, 70, 80, 90, 100};

            // When & Then
            for (int weightKg : typicalWeights) {
                Weight weight = Weight.of(weightKg);
                double expectedGrams = weightKg * 1000.0;
                assertThat(weight.inGrams()).isEqualTo(expectedGrams);
            }
        }
    }

    @Nested
    @DisplayName("Weight Equality and Behavior Tests")
    class WeightEqualityAndBehaviorTests {

        @Test
        @DisplayName("Should be equal when values are the same regardless of creation method")
        void shouldBeEqualWhenValuesAreTheSameRegardlessOfCreationMethod() {
            // Given
            Weight weightFromOf = Weight.of(75);
            Weight weightFromFrom = Weight.from("75");

            // When & Then
            assertThat(weightFromOf).isEqualTo(weightFromFrom);
            assertThat(weightFromOf.hashCode()).isEqualTo(weightFromFrom.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when values are different")
        void shouldNotBeEqualWhenValuesAreDifferent() {
            // Given
            Weight weight1 = Weight.of(70);
            Weight weight2 = Weight.of(80);

            // When & Then
            assertThat(weight1).isNotEqualTo(weight2);
            assertThat(weight1.hashCode()).isNotEqualTo(weight2.hashCode());
        }

        @Test
        @DisplayName("Should have proper toString representation")
        void shouldHaveProperToStringRepresentation() {
            // Given
            Weight weight = Weight.of(65);

            // When
            String result = weight.toString();

            // Then
            assertThat(result).contains("65");
            assertThat(result).contains("Weight");
        }

        @Test
        @DisplayName("Should not be equal to null")
        void shouldNotBeEqualToNull() {
            // Given
            Weight weight = Weight.of(60);

            // When & Then
            assertThat(weight).isNotEqualTo(null);
        }

        @Test
        @DisplayName("Should not be equal to different class")
        void shouldNotBeEqualToDifferentClass() {
            // Given
            Weight weight = Weight.of(60);
            Integer differentClass = 60;

            // When & Then
            assertThat(weight).isNotEqualTo(differentClass);
        }

        @Test
        @DisplayName("Should be equal to itself")
        void shouldBeEqualToItself() {
            // Given
            Weight weight = Weight.of(90);

            // When & Then
            assertThat(weight).isEqualTo(weight);
        }

        @Test
        @DisplayName("Should create different instances with same value")
        void shouldCreateDifferentInstancesWithSameValue() {
            // Given
            Weight weight1 = Weight.of(75);
            Weight weight2 = Weight.of(75);

            // When & Then
            assertThat(weight1).isNotSameAs(weight2);
            assertThat(weight1).isEqualTo(weight2);
        }
    }

    @Nested
    @DisplayName("Weight Realistic Values Tests")
    class WeightRealisticValuesTests {

        @Test
        @DisplayName("Should handle typical human weight values in kilograms")
        void shouldHandleTypicalHumanWeightValuesInKilograms() {
            // Given - typical human weights in kg
            int[] typicalWeights = {40, 50, 60, 70, 80, 90, 100, 120};

            // When & Then
            for (int weightValue : typicalWeights) {
                Weight weight = Weight.of(weightValue);
                assertThat(weight).isNotNull();
                assertThat(weight.getKilograms()).isEqualTo(weightValue);
                assertThat(weight.inGrams()).isEqualTo(weightValue * 1000.0);
            }
        }

        @Test
        @DisplayName("Should handle extreme but valid weight values")
        void shouldHandleExtremeButValidWeightValues() {
            // Given - extreme but theoretically possible weights
            int[] extremeWeights = {1, 10, 300, 500}; // very light to very heavy

            // When & Then
            for (int weightValue : extremeWeights) {
                Weight weight = Weight.of(weightValue);
                assertThat(weight).isNotNull();
                assertThat(weight.getKilograms()).isEqualTo(weightValue);
                assertThat(weight.inGrams()).isEqualTo(weightValue * 1000.0);
            }
        }

        @Test
        @DisplayName("Should handle maximum integer value")
        void shouldHandleMaximumIntegerValue() {
            // Given
            int maxValue = Integer.MAX_VALUE;

            // When
            Weight weight = Weight.of(maxValue);

            // Then
            assertThat(weight).isNotNull();
            assertThat(weight.getKilograms()).isEqualTo(maxValue);
        }
    }

    @Nested
    @DisplayName("Weight Method Equivalence Tests")
    class WeightMethodEquivalenceTests {

        @Test
        @DisplayName("Should produce equivalent results from 'of' and 'from' methods")
        void shouldProduceEquivalentResultsFromOfAndFromMethods() {
            // Given
            int[] testValues = {1, 25, 50, 75, 100, 150, 200};

            // When & Then
            for (int value : testValues) {
                Weight weightFromOf = Weight.of(value);
                Weight weightFromFrom = Weight.from(String.valueOf(value));
                
                assertThat(weightFromOf).isEqualTo(weightFromFrom);
                assertThat(weightFromOf.getKilograms()).isEqualTo(weightFromFrom.getKilograms());
                assertThat(weightFromOf.hashCode()).isEqualTo(weightFromFrom.hashCode());
                assertThat(weightFromOf.inGrams()).isEqualTo(weightFromFrom.inGrams());
            }
        }

        @Test
        @DisplayName("Should throw same exception for invalid values in both methods")
        void shouldThrowSameExceptionForInvalidValuesInBothMethods() {
            // Given
            int[] invalidValues = {0, -1, -10, -100};

            // When & Then
            for (int invalidValue : invalidValues) {
                // Test 'of' method
                assertThatThrownBy(() -> Weight.of(invalidValue))
                        .isInstanceOf(DomainException.class)
                        .hasMessage("Weight must be greater than 0 kg.");
                
                // Test 'from' method
                assertThatThrownBy(() -> Weight.from(String.valueOf(invalidValue)))
                        .isInstanceOf(DomainException.class)
                        .hasMessage("Weight must be greater than 0 kg.");
            }
        }
    }

    @Nested
    @DisplayName("Weight Immutability Tests")
    class WeightImmutabilityTests {

        @Test
        @DisplayName("Should maintain immutability")
        void shouldMaintainImmutability() {
            // Given
            int originalValue = 75;
            Weight weight = Weight.of(originalValue);

            // When
            int retrievedValue = weight.getKilograms();
            double gramsValue = weight.inGrams();

            // Then
            assertThat(retrievedValue).isEqualTo(originalValue);
            assertThat(gramsValue).isEqualTo(originalValue * 1000.0);
            // Verify that the value cannot be changed (immutable)
            assertThat(weight.getKilograms()).isEqualTo(originalValue);
            assertThat(weight.inGrams()).isEqualTo(originalValue * 1000.0);
        }

        @Test
        @DisplayName("Should create independent instances")
        void shouldCreateIndependentInstances() {
            // Given
            int value = 80;
            Weight weight1 = Weight.of(value);
            Weight weight2 = Weight.from(String.valueOf(value));
            Weight weight3 = Weight.of(value);

            // When & Then
            assertThat(weight1).isNotSameAs(weight2);
            assertThat(weight1).isNotSameAs(weight3);
            assertThat(weight2).isNotSameAs(weight3);
            
            // But they should be equal
            assertThat(weight1).isEqualTo(weight2);
            assertThat(weight1).isEqualTo(weight3);
            assertThat(weight2).isEqualTo(weight3);
            
            // And have same grams conversion
            assertThat(weight1.inGrams()).isEqualTo(weight2.inGrams());
            assertThat(weight1.inGrams()).isEqualTo(weight3.inGrams());
        }
    }
}