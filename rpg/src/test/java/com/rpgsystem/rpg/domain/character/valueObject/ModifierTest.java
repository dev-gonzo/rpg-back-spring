package com.rpgsystem.rpg.domain.character.valueObject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Modifier Value Object Tests")
class ModifierTest {

    @Test
    @DisplayName("Should create Modifier with positive value")
    void shouldCreateModifierWithPositiveValue() {
        // Given
        int value = 5;

        // When
        Modifier modifier = Modifier.of(value);

        // Then
        assertThat(modifier).isNotNull();
        assertThat(modifier.getValue()).isEqualTo(value);
    }

    @Test
    @DisplayName("Should create Modifier with zero value")
    void shouldCreateModifierWithZeroValue() {
        // Given
        int value = 0;

        // When
        Modifier modifier = Modifier.of(value);

        // Then
        assertThat(modifier).isNotNull();
        assertThat(modifier.getValue()).isEqualTo(value);
    }

    @Test
    @DisplayName("Should create Modifier with negative value")
    void shouldCreateModifierWithNegativeValue() {
        // Given
        int value = -3;

        // When
        Modifier modifier = Modifier.of(value);

        // Then
        assertThat(modifier).isNotNull();
        assertThat(modifier.getValue()).isEqualTo(value);
    }

    @Test
    @DisplayName("Should create Modifier with maximum integer value")
    void shouldCreateModifierWithMaximumIntegerValue() {
        // Given
        int value = Integer.MAX_VALUE;

        // When
        Modifier modifier = Modifier.of(value);

        // Then
        assertThat(modifier).isNotNull();
        assertThat(modifier.getValue()).isEqualTo(value);
    }

    @Test
    @DisplayName("Should create Modifier with minimum integer value")
    void shouldCreateModifierWithMinimumIntegerValue() {
        // Given
        int value = Integer.MIN_VALUE;

        // When
        Modifier modifier = Modifier.of(value);

        // Then
        assertThat(modifier).isNotNull();
        assertThat(modifier.getValue()).isEqualTo(value);
    }

    @Test
    @DisplayName("Should be equal when values are the same")
    void shouldBeEqualWhenValuesAreTheSame() {
        // Given
        Modifier modifier1 = Modifier.of(2);
        Modifier modifier2 = Modifier.of(2);

        // When & Then
        assertThat(modifier1).isEqualTo(modifier2);
        assertThat(modifier1.hashCode()).isEqualTo(modifier2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when values are different")
    void shouldNotBeEqualWhenValuesAreDifferent() {
        // Given
        Modifier modifier1 = Modifier.of(2);
        Modifier modifier2 = Modifier.of(3);

        // When & Then
        assertThat(modifier1).isNotEqualTo(modifier2);
        assertThat(modifier1.hashCode()).isNotEqualTo(modifier2.hashCode());
    }

    @Test
    @DisplayName("Should have proper toString representation")
    void shouldHaveProperToStringRepresentation() {
        // Given
        Modifier modifier = Modifier.of(5);

        // When
        String result = modifier.toString();

        // Then
        assertThat(result).isEqualTo("5");
    }

    @Test
    @DisplayName("Should not be equal to null")
    void shouldNotBeEqualToNull() {
        // Given
        Modifier modifier = Modifier.of(1);

        // When & Then
        assertThat(modifier).isNotEqualTo(null);
    }

    @Test
    @DisplayName("Should not be equal to different class")
    void shouldNotBeEqualToDifferentClass() {
        // Given
        Modifier modifier = Modifier.of(5);
        Integer differentClass = 5;

        // When & Then
        assertThat(modifier).isNotEqualTo(differentClass);
    }

    @Test
    @DisplayName("Should be equal to itself")
    void shouldBeEqualToItself() {
        // Given
        Modifier modifier = Modifier.of(10);

        // When & Then
        assertThat(modifier).isEqualTo(modifier);
    }

    @Test
    @DisplayName("Should create different instances with same value")
    void shouldCreateDifferentInstancesWithSameValue() {
        // Given
        Modifier modifier1 = Modifier.of(7);
        Modifier modifier2 = Modifier.of(7);

        // When & Then
        assertThat(modifier1).isNotSameAs(modifier2);
        assertThat(modifier1).isEqualTo(modifier2);
    }

    @Test
    @DisplayName("Should handle typical RPG modifier values")
    void shouldHandleTypicalRpgModifierValues() {
        // Test common RPG modifier values (-5 to +5)
        for (int i = -5; i <= 5; i++) {
            Modifier modifier = Modifier.of(i);
            assertThat(modifier).isNotNull();
            assertThat(modifier.getValue()).isEqualTo(i);
        }
    }

    @Test
    @DisplayName("Should maintain immutability")
    void shouldMaintainImmutability() {
        // Given
        int originalValue = 3;
        Modifier modifier = Modifier.of(originalValue);

        // When
        int retrievedValue = modifier.getValue();

        // Then
        assertThat(retrievedValue).isEqualTo(originalValue);
        // Verify that the value cannot be changed (immutable)
        assertThat(modifier.getValue()).isEqualTo(originalValue);
    }

    @Test
    @DisplayName("Should work with negative modifiers")
    void shouldWorkWithNegativeModifiers() {
        // Given
        int[] negativeValues = {-1, -2, -5, -10, -100};

        // When & Then
        for (int value : negativeValues) {
            Modifier modifier = Modifier.of(value);
            assertThat(modifier).isNotNull();
            assertThat(modifier.getValue()).isEqualTo(value);
        }
    }

    @Test
    @DisplayName("Should work with positive modifiers")
    void shouldWorkWithPositiveModifiers() {
        // Given
        int[] positiveValues = {1, 2, 5, 10, 100};

        // When & Then
        for (int value : positiveValues) {
            Modifier modifier = Modifier.of(value);
            assertThat(modifier).isNotNull();
            assertThat(modifier.getValue()).isEqualTo(value);
        }
    }
}