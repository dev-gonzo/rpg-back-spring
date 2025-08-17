package com.rpgsystem.rpg.application.builder;

import com.rpgsystem.rpg.api.dto.character.CharacterNoteResponse;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.NoteEntity;
import com.rpgsystem.rpg.domain.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CharacterNoteDtoBuilder Tests")
class CharacterNoteDtoBuilderTest {

    @Test
    @DisplayName("Should build CharacterNoteResponse from NoteEntity with all fields")
    void shouldBuildCharacterNoteResponseFromNoteEntityWithAllFields() {
        // Given
        User user = User.builder()
                .id("user-id")
                .name("Test User")
                .email("test@example.com")
                .build();

        CharacterEntity character = CharacterEntity.builder()
                .id("character-id")
                .name("Test Character")
                .controlUser(user)
                .build();

        Instant createdAt = Instant.parse("2023-01-01T10:00:00Z");
        Instant updatedAt = Instant.parse("2023-01-02T15:30:00Z");

        NoteEntity noteEntity = NoteEntity.builder()
                .id("note-id")
                .character(character)
                .note("This is a detailed character note with important information.")
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        // When
        CharacterNoteResponse result = CharacterNoteDtoBuilder.from(noteEntity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("note-id");
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getNote()).isEqualTo("This is a detailed character note with important information.");
        assertThat(result.getCreatedAt()).isEqualTo(createdAt);
        assertThat(result.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("Should build CharacterNoteResponse from NoteEntity with null note")
    void shouldBuildCharacterNoteResponseFromNoteEntityWithNullNote() {
        // Given
        User user = User.builder()
                .id("user-id")
                .name("Test User")
                .email("test@example.com")
                .build();

        CharacterEntity character = CharacterEntity.builder()
                .id("character-id")
                .name("Test Character")
                .controlUser(user)
                .build();

        Instant timestamp = Instant.now();

        NoteEntity noteEntity = NoteEntity.builder()
                .id("note-null")
                .character(character)
                .note(null)
                .createdAt(timestamp)
                .updatedAt(timestamp)
                .build();

        // When
        CharacterNoteResponse result = CharacterNoteDtoBuilder.from(noteEntity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("note-null");
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getNote()).isNull();
        assertThat(result.getCreatedAt()).isEqualTo(timestamp);
        assertThat(result.getUpdatedAt()).isEqualTo(timestamp);
    }

    @Test
    @DisplayName("Should build CharacterNoteResponse from NoteEntity with empty note")
    void shouldBuildCharacterNoteResponseFromNoteEntityWithEmptyNote() {
        // Given
        User user = User.builder()
                .id("user-id")
                .name("Test User")
                .email("test@example.com")
                .build();

        CharacterEntity character = CharacterEntity.builder()
                .id("character-id")
                .name("Test Character")
                .controlUser(user)
                .build();

        Instant timestamp = Instant.now();

        NoteEntity noteEntity = NoteEntity.builder()
                .id("note-empty")
                .character(character)
                .note("")
                .createdAt(timestamp)
                .updatedAt(timestamp)
                .build();

        // When
        CharacterNoteResponse result = CharacterNoteDtoBuilder.from(noteEntity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("note-empty");
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getNote()).isEmpty();
        assertThat(result.getCreatedAt()).isEqualTo(timestamp);
        assertThat(result.getUpdatedAt()).isEqualTo(timestamp);
    }

    @Test
    @DisplayName("Should build CharacterNoteResponse from NoteEntity with null timestamps")
    void shouldBuildCharacterNoteResponseFromNoteEntityWithNullTimestamps() {
        // Given
        User user = User.builder()
                .id("user-id")
                .name("Test User")
                .email("test@example.com")
                .build();

        CharacterEntity character = CharacterEntity.builder()
                .id("character-id")
                .name("Test Character")
                .controlUser(user)
                .build();

        NoteEntity noteEntity = NoteEntity.builder()
                .id("note-no-timestamps")
                .character(character)
                .note("Note without timestamps")
                .createdAt(null)
                .updatedAt(null)
                .build();

        // When
        CharacterNoteResponse result = CharacterNoteDtoBuilder.from(noteEntity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("note-no-timestamps");
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getNote()).isEqualTo("Note without timestamps");
        assertThat(result.getCreatedAt()).isNull();
        assertThat(result.getUpdatedAt()).isNull();
    }

    @Test
    @DisplayName("Should build CharacterNoteResponse from NoteEntity with long note")
    void shouldBuildCharacterNoteResponseFromNoteEntityWithLongNote() {
        // Given
        User user = User.builder()
                .id("user-id")
                .name("Test User")
                .email("test@example.com")
                .build();

        CharacterEntity character = CharacterEntity.builder()
                .id("character-id")
                .name("Test Character")
                .controlUser(user)
                .build();

        String longNote = "This is a very long note that contains multiple paragraphs and detailed information about the character. "
                + "It includes background story, personality traits, important relationships, and significant events in the character's life. "
                + "The note can be used by the game master and players to better understand the character's motivations and history. "
                + "It may also contain tactical information, equipment details, and future plot hooks for the campaign.";

        Instant timestamp = Instant.now();

        NoteEntity noteEntity = NoteEntity.builder()
                .id("note-long")
                .character(character)
                .note(longNote)
                .createdAt(timestamp)
                .updatedAt(timestamp)
                .build();

        // When
        CharacterNoteResponse result = CharacterNoteDtoBuilder.from(noteEntity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("note-long");
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getNote()).isEqualTo(longNote);
        assertThat(result.getNote()).hasSize(longNote.length());
        assertThat(result.getCreatedAt()).isEqualTo(timestamp);
        assertThat(result.getUpdatedAt()).isEqualTo(timestamp);
    }

    @Test
    @DisplayName("Should build CharacterNoteResponse from NoteEntity with Unicode characters")
    void shouldBuildCharacterNoteResponseFromNoteEntityWithUnicodeCharacters() {
        // Given
        User user = User.builder()
                .id("user-id")
                .name("Test User")
                .email("test@example.com")
                .build();

        CharacterEntity character = CharacterEntity.builder()
                .id("character-id")
                .name("Test Character")
                .controlUser(user)
                .build();

        String unicodeNote = "Nota com caracteres especiais: áéíóú, àèìòù, âêîôû, ãõ, ç, ñ, ü. "
                + "Símbolos: ★☆♠♣♥♦, emojis: 😀😎🎲🗡️🛡️, e outros: €£¥₹₽";

        Instant timestamp = Instant.now();

        NoteEntity noteEntity = NoteEntity.builder()
                .id("note-unicode")
                .character(character)
                .note(unicodeNote)
                .createdAt(timestamp)
                .updatedAt(timestamp)
                .build();

        // When
        CharacterNoteResponse result = CharacterNoteDtoBuilder.from(noteEntity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("note-unicode");
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getNote()).isEqualTo(unicodeNote);
        assertThat(result.getCreatedAt()).isEqualTo(timestamp);
        assertThat(result.getUpdatedAt()).isEqualTo(timestamp);
    }

    @Test
    @DisplayName("Should throw NullPointerException when NoteEntity is null")
    void shouldThrowNullPointerExceptionWhenNoteEntityIsNull() {
        // Given
        NoteEntity nullEntity = null;

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            CharacterNoteDtoBuilder.from(nullEntity);
        });
    }

    @Test
    @DisplayName("Should throw NullPointerException when character is null")
    void shouldThrowNullPointerExceptionWhenCharacterIsNull() {
        // Given
        NoteEntity noteEntity = NoteEntity.builder()
                .id("note-no-char")
                .character(null)
                .note("Note without character")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            CharacterNoteDtoBuilder.from(noteEntity);
        });
    }

    @Test
    @DisplayName("Should not allow instantiation of utility class")
    void shouldNotAllowInstantiationOfUtilityClass() throws NoSuchMethodException {
        // Given
        Constructor<CharacterNoteDtoBuilder> constructor = CharacterNoteDtoBuilder.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // When & Then
        assertThrows(InvocationTargetException.class, constructor::newInstance);
    }
}