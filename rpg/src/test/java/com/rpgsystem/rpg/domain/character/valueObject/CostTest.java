package com.rpgsystem.rpg.domain.character.valueObject;

import com.rpgsystem.rpg.domain.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Cost Value Object Tests")
class CostTest {

    @Test
    @DisplayName("Should create Cost with valid positive value")
    void shouldCreateCostWithValidPositiveValue() {
        // Given
        int value = 10;

        // When
        Cost cost = Cost.of(value);

        // Then
        assertThat(cost).isNotNull();
        assertThat(cost.getValue()).isEqualTo(value);
    }

    @Test
    @DisplayName("Should create Cost with minimum valid value")
    void shouldCreateCostWithMinimumValidValue() {
        // Given
        int value = 1;

        // When
        Cost cost = Cost.of(value);

        // Then
        assertThat(cost).isNotNull();
        assertThat(cost.getValue()).isEqualTo(value);
    }

    @Test
    @DisplayName("Should create Cost with high value")
    void shouldCreateCostWithHighValue() {
        // Given
        int value = 1000;

        // When
        Cost cost = Cost.of(value);

        // Then
        assertThat(cost).isNotNull();
        assertThat(cost.getValue()).isEqualTo(value);
    }

    @Test
    @DisplayName("Should create Cost with maximum integer value")
    void shouldCreateCostWithMaximumIntegerValue() {
        // Given
        int value = Integer.MAX_VALUE;

        // When
        Cost cost = Cost.of(value);

        // Then
        assertThat(cost).isNotNull();
        assertThat(cost.getValue()).isEqualTo(value);
    }

    @Test
    @DisplayName("Should create Cost from valid string")
    void shouldCreateCostFromValidString() {
        // Given
        String value = "25";

        // When
        Cost cost = Cost.from(value);

        // Then
        assertThat(cost).isNotNull();
        assertThat(cost.getValue()).isEqualTo(25);
    }

    @Test
    @DisplayName("Should create Cost from string with leading/trailing spaces")
    void shouldCreateCostFromStringWithSpaces() {
        // Given
        String value = "  15  ";

        // When
        Cost cost = Cost.from(value);

        // Then
        assertThat(cost).isNotNull();
        assertThat(cost.getValue()).isEqualTo(15);
    }

    @Test
    @DisplayName("Should throw exception when value is zero")
    void shouldThrowExceptionWhenValueIsZero() {
        // Given
        int zeroValue = 0;

        // When & Then
        assertThatThrownBy(() -> Cost.of(zeroValue))
                .isInstanceOf(DomainException.class)
                .hasMessage("Cost must be greater than 0.");
    }

    @Test
    @DisplayName("Should throw exception when value is negative")
    void shouldThrowExceptionWhenValueIsNegative() {
        // Given
        int negativeValue = -5;

        // When & Then
        assertThatThrownBy(() -> Cost.of(negativeValue))
                .isInstanceOf(DomainException.class)
                .hasMessage("Cost must be greater than 0.");
    }

    @Test
    @DisplayName("Should throw exception when string value is zero")
    void shouldThrowExceptionWhenStringValueIsZero() {
        // Given
        String zeroValue = "0";

        // When & Then
        assertThatThrownBy(() -> Cost.from(zeroValue))
                .isInstanceOf(DomainException.class)
                .hasMessage("Cost must be greater than 0.");
    }

    @Test
    @DisplayName("Should throw exception when string value is negative")
    void shouldThrowExceptionWhenStringValueIsNegative() {
        // Given
        String negativeValue = "-10";

        // When & Then
        assertThatThrownBy(() -> Cost.from(negativeValue))
                .isInstanceOf(DomainException.class)
                .hasMessage("Cost must be greater than 0.");
    }

    @Test
    @DisplayName("Should return null when integer value is null")
    void shouldReturnNullWhenIntegerValueIsNull() {
        // Given
        Integer nullValue = null;

        // When
        Cost cost = Cost.of(nullValue);

        // Then
        assertThat(cost).isNull();
    }

    @Test
    @DisplayName("Should return null when string value is null")
    void shouldReturnNullWhenStringValueIsNull() {
        // Given
        String nullValue = null;

        // When
        Cost cost = Cost.from(nullValue);

        // Then
        assertThat(cost).isNull();
    }

    @Test
    @DisplayName("Should return null when string value is blank")
    void shouldReturnNullWhenStringValueIsBlank() {
        // Given
        String blankValue = "   ";

        // When
        Cost cost = Cost.from(blankValue);

        // Then
        assertThat(cost).isNull();
    }

    @Test
    @DisplayName("Should return null when string value is empty")
    void shouldReturnNullWhenStringValueIsEmpty() {
        // Given
        String emptyValue = "";

        // When
        Cost cost = Cost.from(emptyValue);

        // Then
        assertThat(cost).isNull();
    }

    @Test
    @DisplayName("Should throw exception for invalid string format")
    void shouldThrowExceptionForInvalidStringFormat() {
        // Given
        String invalidValue = "abc";

        // When & Then
        assertThatThrownBy(() -> Cost.from(invalidValue))
                .isInstanceOf(DomainException.class)
                .hasMessage("Cost must be a valid number.");
    }

    @Test
    @DisplayName("Should throw exception for decimal string")
    void shouldThrowExceptionForDecimalString() {
        // Given
        String decimalValue = "10.5";

        // When & Then
        assertThatThrownBy(() -> Cost.from(decimalValue))
                .isInstanceOf(DomainException.class)
                .hasMessage("Cost must be a valid number.");
    }

    @Test
    @DisplayName("Should throw exception for string with special characters")
    void shouldThrowExceptionForStringWithSpecialCharacters() {
        // Given
        String specialValue = "10$";

        // When & Then
        assertThatThrownBy(() -> Cost.from(specialValue))
                .isInstanceOf(DomainException.class)
                .hasMessage("Cost must be a valid number.");
    }

    @Test
    @DisplayName("Should be equal when values are the same")
    void shouldBeEqualWhenValuesAreTheSame() {
        // Given
        Cost cost1 = Cost.of(20);
        Cost cost2 = Cost.of(20);

        // When & Then
        assertThat(cost1).isEqualTo(cost2);
        assertThat(cost1.hashCode()).isEqualTo(cost2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when values are different")
    void shouldNotBeEqualWhenValuesAreDifferent() {
        // Given
        Cost cost1 = Cost.of(15);
        Cost cost2 = Cost.of(25);

        // When & Then
        assertThat(cost1).isNotEqualTo(cost2);
        assertThat(cost1.hashCode()).isNotEqualTo(cost2.hashCode());
    }

    @Test
    @DisplayName("Should have proper toString representation")
    void shouldHaveProperToStringRepresentation() {
        // Given
        Cost cost = Cost.of(30);

        // When
        String result = cost.toString();

        // Then
        assertThat(result).contains("30");
        assertThat(result).contains("Cost");
    }

    @Test
    @DisplayName("Should not be equal to null")
    void shouldNotBeEqualToNull() {
        // Given
        Cost cost = Cost.of(10);

        // When & Then
        assertThat(cost).isNotEqualTo(null);
    }

    @Test
    @DisplayName("Should not be equal to different class")
    void shouldNotBeEqualToDifferentClass() {
        // Given
        Cost cost = Cost.of(10);
        Integer differentClass = 10;

        // When & Then
        assertThat(cost).isNotEqualTo(differentClass);
    }

    @Test
    @DisplayName("Should be equal to itself")
    void shouldBeEqualToItself() {
        // Given
        Cost cost = Cost.of(50);

        // When & Then
        assertThat(cost).isEqualTo(cost);
    }
}