package com.rpgsystem.rpg.domain.character.valueObject;

import com.rpgsystem.rpg.domain.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Attribute Value Object Tests")
class AttributeTest {

    @Test
    @DisplayName("Should create attribute with valid positive value")
    void shouldCreateAttributeWithValidPositiveValue() {
        // Given
        int value = 15;

        // When
        Attribute attribute = Attribute.of(value);

        // Then
        assertThat(attribute).isNotNull();
        assertThat(attribute.getValue()).isEqualTo(value);
    }

    @Test
    @DisplayName("Should create attribute with zero value")
    void shouldCreateAttributeWithZeroValue() {
        // Given
        int value = 0;

        // When
        Attribute attribute = Attribute.of(value);

        // Then
        assertThat(attribute).isNotNull();
        assertThat(attribute.getValue()).isEqualTo(value);
    }

    @Test
    @DisplayName("Should throw exception when value is negative")
    void shouldThrowExceptionWhenValueIsNegative() {
        // Given
        int negativeValue = -1;

        // When & Then
        assertThatThrownBy(() -> Attribute.of(negativeValue))
                .isInstanceOf(DomainException.class)
                .hasMessage("Attribute value must be zero or positive.");
    }

    @Test
    @DisplayName("Should throw exception when value is very negative")
    void shouldThrowExceptionWhenValueIsVeryNegative() {
        // Given
        int veryNegativeValue = -100;

        // When & Then
        assertThatThrownBy(() -> Attribute.of(veryNegativeValue))
                .isInstanceOf(DomainException.class)
                .hasMessage("Attribute value must be zero or positive.");
    }

    @Test
    @DisplayName("Should create attribute with maximum integer value")
    void shouldCreateAttributeWithMaximumIntegerValue() {
        // Given
        int maxValue = Integer.MAX_VALUE;

        // When
        Attribute attribute = Attribute.of(maxValue);

        // Then
        assertThat(attribute).isNotNull();
        assertThat(attribute.getValue()).isEqualTo(maxValue);
    }

    @Test
    @DisplayName("Should return string representation of value")
    void shouldReturnStringRepresentationOfValue() {
        // Given
        int value = 18;
        Attribute attribute = Attribute.of(value);

        // When
        String result = attribute.toString();

        // Then
        assertThat(result).isEqualTo("18");
    }

    @Test
    @DisplayName("Should be equal when values are the same")
    void shouldBeEqualWhenValuesAreTheSame() {
        // Given
        int value = 12;
        Attribute attribute1 = Attribute.of(value);
        Attribute attribute2 = Attribute.of(value);

        // When & Then
        assertThat(attribute1).isEqualTo(attribute2);
        assertThat(attribute1.hashCode()).isEqualTo(attribute2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when values are different")
    void shouldNotBeEqualWhenValuesAreDifferent() {
        // Given
        Attribute attribute1 = Attribute.of(10);
        Attribute attribute2 = Attribute.of(15);

        // When & Then
        assertThat(attribute1).isNotEqualTo(attribute2);
        assertThat(attribute1.hashCode()).isNotEqualTo(attribute2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal to null")
    void shouldNotBeEqualToNull() {
        // Given
        Attribute attribute = Attribute.of(10);

        // When & Then
        assertThat(attribute).isNotEqualTo(null);
    }

    @Test
    @DisplayName("Should not be equal to different class")
    void shouldNotBeEqualToDifferentClass() {
        // Given
        Attribute attribute = Attribute.of(10);
        String differentClass = "10";

        // When & Then
        assertThat(attribute).isNotEqualTo(differentClass);
    }

    @Test
    @DisplayName("Should be equal to itself")
    void shouldBeEqualToItself() {
        // Given
        Attribute attribute = Attribute.of(10);

        // When & Then
        assertThat(attribute).isEqualTo(attribute);
    }
}