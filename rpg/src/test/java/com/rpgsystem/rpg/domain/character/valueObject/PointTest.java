package com.rpgsystem.rpg.domain.character.valueObject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Point Value Object Tests")
class PointTest {

    @Test
    @DisplayName("Should create Point with positive value")
    void shouldCreatePointWithPositiveValue() {
        // Given
        int value = 10;

        // When
        Point point = Point.of(value);

        // Then
        assertThat(point).isNotNull();
        assertThat(point.getValue()).isEqualTo(value);
    }

    @Test
    @DisplayName("Should create Point with zero value")
    void shouldCreatePointWithZeroValue() {
        // Given
        int value = 0;

        // When
        Point point = Point.of(value);

        // Then
        assertThat(point).isNotNull();
        assertThat(point.getValue()).isEqualTo(value);
    }

    @Test
    @DisplayName("Should create Point with negative value")
    void shouldCreatePointWithNegativeValue() {
        // Given
        int value = -5;

        // When
        Point point = Point.of(value);

        // Then
        assertThat(point).isNotNull();
        assertThat(point.getValue()).isEqualTo(value);
    }

    @Test
    @DisplayName("Should create Point with maximum integer value")
    void shouldCreatePointWithMaximumIntegerValue() {
        // Given
        int value = Integer.MAX_VALUE;

        // When
        Point point = Point.of(value);

        // Then
        assertThat(point).isNotNull();
        assertThat(point.getValue()).isEqualTo(value);
    }

    @Test
    @DisplayName("Should create Point with minimum integer value")
    void shouldCreatePointWithMinimumIntegerValue() {
        // Given
        int value = Integer.MIN_VALUE;

        // When
        Point point = Point.of(value);

        // Then
        assertThat(point).isNotNull();
        assertThat(point.getValue()).isEqualTo(value);
    }

    @Test
    @DisplayName("Should return value as string in toString method")
    void shouldReturnValueAsStringInToStringMethod() {
        // Given
        int value = 25;
        Point point = Point.of(value);

        // When
        String result = point.toString();

        // Then
        assertThat(result).isEqualTo("25");
    }

    @Test
    @DisplayName("Should return zero as string for zero value")
    void shouldReturnZeroAsStringForZeroValue() {
        // Given
        Point point = Point.of(0);

        // When
        String result = point.toString();

        // Then
        assertThat(result).isEqualTo("0");
    }

    @Test
    @DisplayName("Should return negative value as string")
    void shouldReturnNegativeValueAsString() {
        // Given
        Point point = Point.of(-10);

        // When
        String result = point.toString();

        // Then
        assertThat(result).isEqualTo("-10");
    }

    @Test
    @DisplayName("Should be equal when values are the same")
    void shouldBeEqualWhenValuesAreTheSame() {
        // Given
        Point point1 = Point.of(15);
        Point point2 = Point.of(15);

        // When & Then
        assertThat(point1).isEqualTo(point2);
        assertThat(point1.hashCode()).isEqualTo(point2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when values are different")
    void shouldNotBeEqualWhenValuesAreDifferent() {
        // Given
        Point point1 = Point.of(15);
        Point point2 = Point.of(20);

        // When & Then
        assertThat(point1).isNotEqualTo(point2);
        assertThat(point1.hashCode()).isNotEqualTo(point2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal to null")
    void shouldNotBeEqualToNull() {
        // Given
        Point point = Point.of(5);

        // When & Then
        assertThat(point).isNotEqualTo(null);
    }

    @Test
    @DisplayName("Should not be equal to different class")
    void shouldNotBeEqualToDifferentClass() {
        // Given
        Point point = Point.of(5);
        Integer differentClass = 5;

        // When & Then
        assertThat(point).isNotEqualTo(differentClass);
    }

    @Test
    @DisplayName("Should be equal to itself")
    void shouldBeEqualToItself() {
        // Given
        Point point = Point.of(100);

        // When & Then
        assertThat(point).isEqualTo(point);
    }

    @Test
    @DisplayName("Should create different instances with same value")
    void shouldCreateDifferentInstancesWithSameValue() {
        // Given
        Point point1 = Point.of(50);
        Point point2 = Point.of(50);

        // When & Then
        assertThat(point1).isNotSameAs(point2);
        assertThat(point1).isEqualTo(point2);
    }

    @Test
    @DisplayName("Should handle typical RPG point values")
    void shouldHandleTypicalRpgPointValues() {
        // Test common RPG point values
        int[] typicalValues = {1, 5, 10, 15, 20, 25, 50, 100};
        
        for (int value : typicalValues) {
            Point point = Point.of(value);
            assertThat(point).isNotNull();
            assertThat(point.getValue()).isEqualTo(value);
            assertThat(point.toString()).isEqualTo(String.valueOf(value));
        }
    }

    @Test
    @DisplayName("Should maintain immutability")
    void shouldMaintainImmutability() {
        // Given
        int originalValue = 42;
        Point point = Point.of(originalValue);

        // When
        int retrievedValue = point.getValue();

        // Then
        assertThat(retrievedValue).isEqualTo(originalValue);
        // Verify that the value cannot be changed (immutable)
        assertThat(point.getValue()).isEqualTo(originalValue);
    }

    @Test
    @DisplayName("Should work with large positive values")
    void shouldWorkWithLargePositiveValues() {
        // Given
        int[] largeValues = {1000, 10000, 100000, 1000000};

        // When & Then
        for (int value : largeValues) {
            Point point = Point.of(value);
            assertThat(point).isNotNull();
            assertThat(point.getValue()).isEqualTo(value);
            assertThat(point.toString()).isEqualTo(String.valueOf(value));
        }
    }

    @Test
    @DisplayName("Should work with large negative values")
    void shouldWorkWithLargeNegativeValues() {
        // Given
        int[] largeNegativeValues = {-1000, -10000, -100000, -1000000};

        // When & Then
        for (int value : largeNegativeValues) {
            Point point = Point.of(value);
            assertThat(point).isNotNull();
            assertThat(point.getValue()).isEqualTo(value);
            assertThat(point.toString()).isEqualTo(String.valueOf(value));
        }
    }

    @Test
    @DisplayName("Should handle boundary integer values correctly")
    void shouldHandleBoundaryIntegerValuesCorrectly() {
        // Test Integer.MAX_VALUE
        Point maxPoint = Point.of(Integer.MAX_VALUE);
        assertThat(maxPoint.getValue()).isEqualTo(Integer.MAX_VALUE);
        assertThat(maxPoint.toString()).isEqualTo(String.valueOf(Integer.MAX_VALUE));

        // Test Integer.MIN_VALUE
        Point minPoint = Point.of(Integer.MIN_VALUE);
        assertThat(minPoint.getValue()).isEqualTo(Integer.MIN_VALUE);
        assertThat(minPoint.toString()).isEqualTo(String.valueOf(Integer.MIN_VALUE));
    }

    @Test
    @DisplayName("Should have consistent toString and getValue behavior")
    void shouldHaveConsistentToStringAndGetValueBehavior() {
        // Given
        int[] testValues = {-100, -1, 0, 1, 100, 999, -999};

        // When & Then
        for (int value : testValues) {
            Point point = Point.of(value);
            assertThat(point.toString()).isEqualTo(String.valueOf(point.getValue()));
        }
    }
}