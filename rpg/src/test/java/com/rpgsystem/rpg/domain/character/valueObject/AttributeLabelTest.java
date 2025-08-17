package com.rpgsystem.rpg.domain.character.valueObject;

import com.rpgsystem.rpg.domain.enums.AttributeEnum;
import com.rpgsystem.rpg.domain.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("AttributeLabel Value Object Tests")
class AttributeLabelTest {

    @Test
    @DisplayName("Should create AttributeLabel from valid string")
    void shouldCreateAttributeLabelFromValidString() {
        // Given
        String value = "FR";

        // When
        AttributeLabel attributeLabel = AttributeLabel.of(value);

        // Then
        assertThat(attributeLabel).isNotNull();
        assertThat(attributeLabel.getValue()).isEqualTo(AttributeEnum.FR);
    }

    @Test
    @DisplayName("Should create AttributeLabel from lowercase string")
    void shouldCreateAttributeLabelFromLowercaseString() {
        // Given
        String value = "agi";

        // When
        AttributeLabel attributeLabel = AttributeLabel.of(value);

        // Then
        assertThat(attributeLabel).isNotNull();
        assertThat(attributeLabel.getValue()).isEqualTo(AttributeEnum.AGI);
    }

    @Test
    @DisplayName("Should create AttributeLabel from string with spaces")
    void shouldCreateAttributeLabelFromStringWithSpaces() {
        // Given
        String value = "  INT  ";

        // When
        AttributeLabel attributeLabel = AttributeLabel.of(value);

        // Then
        assertThat(attributeLabel).isNotNull();
        assertThat(attributeLabel.getValue()).isEqualTo(AttributeEnum.INT);
    }

    @Test
    @DisplayName("Should create AttributeLabel from AttributeEnum")
    void shouldCreateAttributeLabelFromAttributeEnum() {
        // Given
        AttributeEnum enumValue = AttributeEnum.WILL;

        // When
        AttributeLabel attributeLabel = AttributeLabel.of(enumValue);

        // Then
        assertThat(attributeLabel).isNotNull();
        assertThat(attributeLabel.getValue()).isEqualTo(AttributeEnum.WILL);
    }

    @Test
    @DisplayName("Should return null when string is null")
    void shouldReturnNullWhenStringIsNull() {
        // Given
        String value = null;

        // When
        AttributeLabel attributeLabel = AttributeLabel.of(value);

        // Then
        assertThat(attributeLabel).isNull();
    }

    @Test
    @DisplayName("Should return null when string is blank")
    void shouldReturnNullWhenStringIsBlank() {
        // Given
        String value = "   ";

        // When
        AttributeLabel attributeLabel = AttributeLabel.of(value);

        // Then
        assertThat(attributeLabel).isNull();
    }

    @Test
    @DisplayName("Should return null when string is empty")
    void shouldReturnNullWhenStringIsEmpty() {
        // Given
        String value = "";

        // When
        AttributeLabel attributeLabel = AttributeLabel.of(value);

        // Then
        assertThat(attributeLabel).isNull();
    }

    @Test
    @DisplayName("Should return null when AttributeEnum is null")
    void shouldReturnNullWhenAttributeEnumIsNull() {
        // Given
        AttributeEnum enumValue = null;

        // When
        AttributeLabel attributeLabel = AttributeLabel.of(enumValue);

        // Then
        assertThat(attributeLabel).isNull();
    }

    @Test
    @DisplayName("Should throw exception for invalid attribute string")
    void shouldThrowExceptionForInvalidAttributeString() {
        // Given
        String invalidValue = "INVALID_ATTRIBUTE";

        // When & Then
        assertThatThrownBy(() -> AttributeLabel.of(invalidValue))
                .isInstanceOf(DomainException.class)
                .hasMessage("Invalid attribute label: INVALID_ATTRIBUTE");
    }

    @Test
    @DisplayName("Should throw exception for numeric string")
    void shouldThrowExceptionForNumericString() {
        // Given
        String numericValue = "123";

        // When & Then
        assertThatThrownBy(() -> AttributeLabel.of(numericValue))
                .isInstanceOf(DomainException.class)
                .hasMessage("Invalid attribute label: 123");
    }

    @Test
    @DisplayName("Should be equal when values are the same")
    void shouldBeEqualWhenValuesAreTheSame() {
        // Given
        AttributeLabel label1 = AttributeLabel.of(AttributeEnum.CON);
        AttributeLabel label2 = AttributeLabel.of("CON");

        // When & Then
        assertThat(label1).isEqualTo(label2);
        assertThat(label1.hashCode()).isEqualTo(label2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when values are different")
    void shouldNotBeEqualWhenValuesAreDifferent() {
        // Given
        AttributeLabel label1 = AttributeLabel.of(AttributeEnum.FR);
        AttributeLabel label2 = AttributeLabel.of(AttributeEnum.AGI);

        // When & Then
        assertThat(label1).isNotEqualTo(label2);
        assertThat(label1.hashCode()).isNotEqualTo(label2.hashCode());
    }

    @Test
    @DisplayName("Should have proper toString representation")
    void shouldHaveProperToStringRepresentation() {
        // Given
        AttributeLabel label = AttributeLabel.of(AttributeEnum.CAR);

        // When
        String result = label.toString();

        // Then
        assertThat(result).contains("CAR");
        assertThat(result).contains("AttributeLabel");
    }

    @Test
    @DisplayName("Should not be equal to null")
    void shouldNotBeEqualToNull() {
        // Given
        AttributeLabel label = AttributeLabel.of(AttributeEnum.INT);

        // When & Then
        assertThat(label).isNotEqualTo(null);
    }

    @Test
    @DisplayName("Should not be equal to different class")
    void shouldNotBeEqualToDifferentClass() {
        // Given
        AttributeLabel label = AttributeLabel.of(AttributeEnum.INT);
        String differentClass = "INT";

        // When & Then
        assertThat(label).isNotEqualTo(differentClass);
    }
}