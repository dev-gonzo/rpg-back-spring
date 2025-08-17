package com.rpgsystem.rpg.domain.character.valueObject;

import com.rpgsystem.rpg.domain.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("CharacterName Value Object Tests")
class CharacterNameTest {

    @Test
    @DisplayName("Should create CharacterName with valid name")
    void shouldCreateCharacterNameWithValidName() {
        // Given
        String name = "Aragorn";

        // When
        CharacterName characterName = new CharacterName(name);

        // Then
        assertThat(characterName).isNotNull();
        assertThat(characterName.getValue()).isEqualTo("Aragorn");
    }

    @Test
    @DisplayName("Should create CharacterName using static factory method")
    void shouldCreateCharacterNameUsingStaticFactoryMethod() {
        // Given
        String name = "Legolas";

        // When
        CharacterName characterName = CharacterName.of(name);

        // Then
        assertThat(characterName).isNotNull();
        assertThat(characterName.getValue()).isEqualTo("Legolas");
    }

    @Test
    @DisplayName("Should trim whitespace from name")
    void shouldTrimWhitespaceFromName() {
        // Given
        String nameWithSpaces = "  Gimli  ";

        // When
        CharacterName characterName = new CharacterName(nameWithSpaces);

        // Then
        assertThat(characterName.getValue()).isEqualTo("Gimli");
    }

    @Test
    @DisplayName("Should create CharacterName with maximum length")
    void shouldCreateCharacterNameWithMaximumLength() {
        // Given
        String maxLengthName = "A".repeat(100);

        // When
        CharacterName characterName = new CharacterName(maxLengthName);

        // Then
        assertThat(characterName).isNotNull();
        assertThat(characterName.getValue()).isEqualTo(maxLengthName);
        assertThat(characterName.getValue().length()).isEqualTo(100);
    }

    @Test
    @DisplayName("Should create CharacterName with Unicode characters")
    void shouldCreateCharacterNameWithUnicodeCharacters() {
        // Given
        String unicodeName = "Ñoël 龍王 José-María";

        // When
        CharacterName characterName = new CharacterName(unicodeName);

        // Then
        assertThat(characterName).isNotNull();
        assertThat(characterName.getValue()).isEqualTo(unicodeName);
    }

    @Test
    @DisplayName("Should create CharacterName with special characters")
    void shouldCreateCharacterNameWithSpecialCharacters() {
        // Given
        String specialName = "O'Connor-Smith Jr.";

        // When
        CharacterName characterName = new CharacterName(specialName);

        // Then
        assertThat(characterName).isNotNull();
        assertThat(characterName.getValue()).isEqualTo(specialName);
    }

    @Test
    @DisplayName("Should throw exception when name is null")
    void shouldThrowExceptionWhenNameIsNull() {
        // Given
        String nullName = null;

        // When & Then
        assertThatThrownBy(() -> new CharacterName(nullName))
                .isInstanceOf(DomainException.class)
                .hasMessage("Character name must not be null or blank");
    }

    @Test
    @DisplayName("Should throw exception when name is empty")
    void shouldThrowExceptionWhenNameIsEmpty() {
        // Given
        String emptyName = "";

        // When & Then
        assertThatThrownBy(() -> new CharacterName(emptyName))
                .isInstanceOf(DomainException.class)
                .hasMessage("Character name must not be null or blank");
    }

    @Test
    @DisplayName("Should throw exception when name is blank")
    void shouldThrowExceptionWhenNameIsBlank() {
        // Given
        String blankName = "   ";

        // When & Then
        assertThatThrownBy(() -> new CharacterName(blankName))
                .isInstanceOf(DomainException.class)
                .hasMessage("Character name must not be null or blank");
    }

    @Test
    @DisplayName("Should throw exception when name exceeds maximum length")
    void shouldThrowExceptionWhenNameExceedsMaximumLength() {
        // Given
        String tooLongName = "A".repeat(101);

        // When & Then
        assertThatThrownBy(() -> new CharacterName(tooLongName))
                .isInstanceOf(DomainException.class)
                .hasMessage("Character name must be at most 100 characters");
    }

    @Test
    @DisplayName("Should return null when static factory receives null")
    void shouldReturnNullWhenStaticFactoryReceivesNull() {
        // Given
        String nullName = null;

        // When
        CharacterName characterName = CharacterName.of(nullName);

        // Then
        assertThat(characterName).isNull();
    }

    @Test
    @DisplayName("Should return null when static factory receives blank")
    void shouldReturnNullWhenStaticFactoryReceivesBlank() {
        // Given
        String blankName = "   ";

        // When
        CharacterName characterName = CharacterName.of(blankName);

        // Then
        assertThat(characterName).isNull();
    }

    @Test
    @DisplayName("Should be equal when values are the same")
    void shouldBeEqualWhenValuesAreTheSame() {
        // Given
        CharacterName name1 = new CharacterName("Frodo");
        CharacterName name2 = new CharacterName("Frodo");

        // When & Then
        assertThat(name1).isEqualTo(name2);
        assertThat(name1.hashCode()).isEqualTo(name2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when values are different")
    void shouldNotBeEqualWhenValuesAreDifferent() {
        // Given
        CharacterName name1 = new CharacterName("Frodo");
        CharacterName name2 = new CharacterName("Sam");

        // When & Then
        assertThat(name1).isNotEqualTo(name2);
        assertThat(name1.hashCode()).isNotEqualTo(name2.hashCode());
    }

    @Test
    @DisplayName("Should have proper toString representation")
    void shouldHaveProperToStringRepresentation() {
        // Given
        CharacterName name = new CharacterName("Gandalf");

        // When
        String result = name.toString();

        // Then
        assertThat(result).contains("Gandalf");
        assertThat(result).contains("CharacterName");
    }

    @Test
    @DisplayName("Should not be equal to null")
    void shouldNotBeEqualToNull() {
        // Given
        CharacterName name = new CharacterName("Boromir");

        // When & Then
        assertThat(name).isNotEqualTo(null);
    }

    @Test
    @DisplayName("Should not be equal to different class")
    void shouldNotBeEqualToDifferentClass() {
        // Given
        CharacterName name = new CharacterName("Boromir");
        String differentClass = "Boromir";

        // When & Then
        assertThat(name).isNotEqualTo(differentClass);
    }

    @Test
    @DisplayName("Should be equal to itself")
    void shouldBeEqualToItself() {
        // Given
        CharacterName name = new CharacterName("Merry");

        // When & Then
        assertThat(name).isEqualTo(name);
    }
}