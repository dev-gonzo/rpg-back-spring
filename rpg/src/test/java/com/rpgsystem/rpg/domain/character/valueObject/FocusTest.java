package com.rpgsystem.rpg.domain.character.valueObject;

import com.rpgsystem.rpg.domain.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Focus Value Object Tests")
class FocusTest {

    @Nested
    @DisplayName("Path Focus Tests")
    class PathFocusTests {

        @Test
        @DisplayName("Should create Path Focus with valid value")
        void shouldCreatePathFocusWithValidValue() {
            // Given
            int value = 2;

            // When
            Focus focus = Focus.ofPath(value);

            // Then
            assertThat(focus).isNotNull();
            assertThat(focus.getValue()).isEqualTo(value);
        }

        @Test
        @DisplayName("Should create Path Focus with minimum value")
        void shouldCreatePathFocusWithMinimumValue() {
            // Given
            int value = 0;

            // When
            Focus focus = Focus.ofPath(value);

            // Then
            assertThat(focus).isNotNull();
            assertThat(focus.getValue()).isEqualTo(value);
        }

        @Test
        @DisplayName("Should create Path Focus with maximum value")
        void shouldCreatePathFocusWithMaximumValue() {
            // Given
            int value = 4;

            // When
            Focus focus = Focus.ofPath(value);

            // Then
            assertThat(focus).isNotNull();
            assertThat(focus.getValue()).isEqualTo(value);
        }

        @Test
        @DisplayName("Should return null when Path value is null")
        void shouldReturnNullWhenPathValueIsNull() {
            // Given
            Integer nullValue = null;

            // When
            Focus focus = Focus.ofPath(nullValue);

            // Then
            assertThat(focus).isNull();
        }

        @Test
        @DisplayName("Should throw exception when Path value is negative")
        void shouldThrowExceptionWhenPathValueIsNegative() {
            // Given
            int negativeValue = -1;

            // When & Then
            assertThatThrownBy(() -> Focus.ofPath(negativeValue))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Path focus must not be negative.");
        }

        @Test
        @DisplayName("Should throw exception when Path value exceeds maximum")
        void shouldThrowExceptionWhenPathValueExceedsMaximum() {
            // Given
            int exceedsMaxValue = 5;

            // When & Then
            assertThatThrownBy(() -> Focus.ofPath(exceedsMaxValue))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Path focus cannot be greater than 4.");
        }

        @Test
        @DisplayName("Should throw exception when Path value is much greater than maximum")
        void shouldThrowExceptionWhenPathValueIsMuchGreaterThanMaximum() {
            // Given
            int muchGreaterValue = 100;

            // When & Then
            assertThatThrownBy(() -> Focus.ofPath(muchGreaterValue))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Path focus cannot be greater than 4.");
        }
    }

    @Nested
    @DisplayName("Form Focus Tests")
    class FormFocusTests {

        @Test
        @DisplayName("Should create Form Focus with valid value")
        void shouldCreateFormFocusWithValidValue() {
            // Given
            int value = 5;

            // When
            Focus focus = Focus.ofForm(value);

            // Then
            assertThat(focus).isNotNull();
            assertThat(focus.getValue()).isEqualTo(value);
        }

        @Test
        @DisplayName("Should create Form Focus with minimum value")
        void shouldCreateFormFocusWithMinimumValue() {
            // Given
            int value = 0;

            // When
            Focus focus = Focus.ofForm(value);

            // Then
            assertThat(focus).isNotNull();
            assertThat(focus.getValue()).isEqualTo(value);
        }

        @Test
        @DisplayName("Should create Form Focus with maximum value")
        void shouldCreateFormFocusWithMaximumValue() {
            // Given
            int value = 10;

            // When
            Focus focus = Focus.ofForm(value);

            // Then
            assertThat(focus).isNotNull();
            assertThat(focus.getValue()).isEqualTo(value);
        }

        @Test
        @DisplayName("Should return null when Form value is null")
        void shouldReturnNullWhenFormValueIsNull() {
            // Given
            Integer nullValue = null;

            // When
            Focus focus = Focus.ofForm(nullValue);

            // Then
            assertThat(focus).isNull();
        }

        @Test
        @DisplayName("Should throw exception when Form value is negative")
        void shouldThrowExceptionWhenFormValueIsNegative() {
            // Given
            int negativeValue = -1;

            // When & Then
            assertThatThrownBy(() -> Focus.ofForm(negativeValue))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Form focus must not be negative.");
        }

        @Test
        @DisplayName("Should throw exception when Form value exceeds maximum")
        void shouldThrowExceptionWhenFormValueExceedsMaximum() {
            // Given
            int exceedsMaxValue = 11;

            // When & Then
            assertThatThrownBy(() -> Focus.ofForm(exceedsMaxValue))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Form focus cannot be greater than 10.");
        }

        @Test
        @DisplayName("Should throw exception when Form value is much greater than maximum")
        void shouldThrowExceptionWhenFormValueIsMuchGreaterThanMaximum() {
            // Given
            int muchGreaterValue = 100;

            // When & Then
            assertThatThrownBy(() -> Focus.ofForm(muchGreaterValue))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Form focus cannot be greater than 10.");
        }
    }

    @Nested
    @DisplayName("Focus Equality and Behavior Tests")
    class FocusEqualityAndBehaviorTests {

        @Test
        @DisplayName("Should be equal when values are the same regardless of creation method")
        void shouldBeEqualWhenValuesAreTheSameRegardlessOfCreationMethod() {
            // Given
            Focus pathFocus = Focus.ofPath(3);
            Focus formFocus = Focus.ofForm(3);

            // When & Then
            assertThat(pathFocus).isEqualTo(formFocus);
            assertThat(pathFocus.hashCode()).isEqualTo(formFocus.hashCode());
        }

        @Test
        @DisplayName("Should not be equal when values are different")
        void shouldNotBeEqualWhenValuesAreDifferent() {
            // Given
            Focus focus1 = Focus.ofPath(2);
            Focus focus2 = Focus.ofPath(3);

            // When & Then
            assertThat(focus1).isNotEqualTo(focus2);
            assertThat(focus1.hashCode()).isNotEqualTo(focus2.hashCode());
        }

        @Test
        @DisplayName("Should have proper toString representation")
        void shouldHaveProperToStringRepresentation() {
            // Given
            Focus focus = Focus.ofPath(2);

            // When
            String result = focus.toString();

            // Then
            assertThat(result).contains("2");
            assertThat(result).contains("Focus");
        }

        @Test
        @DisplayName("Should not be equal to null")
        void shouldNotBeEqualToNull() {
            // Given
            Focus focus = Focus.ofForm(5);

            // When & Then
            assertThat(focus).isNotEqualTo(null);
        }

        @Test
        @DisplayName("Should not be equal to different class")
        void shouldNotBeEqualToDifferentClass() {
            // Given
            Focus focus = Focus.ofForm(5);
            Integer differentClass = 5;

            // When & Then
            assertThat(focus).isNotEqualTo(differentClass);
        }

        @Test
        @DisplayName("Should be equal to itself")
        void shouldBeEqualToItself() {
            // Given
            Focus focus = Focus.ofPath(1);

            // When & Then
            assertThat(focus).isEqualTo(focus);
        }

        @Test
        @DisplayName("Should create different instances with same value")
        void shouldCreateDifferentInstancesWithSameValue() {
            // Given
            Focus focus1 = Focus.ofPath(2);
            Focus focus2 = Focus.ofPath(2);

            // When & Then
            assertThat(focus1).isNotSameAs(focus2);
            assertThat(focus1).isEqualTo(focus2);
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle boundary values correctly for Path")
        void shouldHandleBoundaryValuesCorrectlyForPath() {
            // Test all valid boundary values for Path (0-4)
            for (int i = 0; i <= 4; i++) {
                Focus focus = Focus.ofPath(i);
                assertThat(focus).isNotNull();
                assertThat(focus.getValue()).isEqualTo(i);
            }
        }

        @Test
        @DisplayName("Should handle boundary values correctly for Form")
        void shouldHandleBoundaryValuesCorrectlyForForm() {
            // Test all valid boundary values for Form (0-10)
            for (int i = 0; i <= 10; i++) {
                Focus focus = Focus.ofForm(i);
                assertThat(focus).isNotNull();
                assertThat(focus.getValue()).isEqualTo(i);
            }
        }

        @Test
        @DisplayName("Should throw exception for very negative Path values")
        void shouldThrowExceptionForVeryNegativePathValues() {
            // Given
            int veryNegativeValue = Integer.MIN_VALUE;

            // When & Then
            assertThatThrownBy(() -> Focus.ofPath(veryNegativeValue))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Path focus must not be negative.");
        }

        @Test
        @DisplayName("Should throw exception for very negative Form values")
        void shouldThrowExceptionForVeryNegativeFormValues() {
            // Given
            int veryNegativeValue = Integer.MIN_VALUE;

            // When & Then
            assertThatThrownBy(() -> Focus.ofForm(veryNegativeValue))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Form focus must not be negative.");
        }
    }
}