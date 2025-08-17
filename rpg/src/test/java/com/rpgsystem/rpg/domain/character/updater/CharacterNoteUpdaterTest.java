package com.rpgsystem.rpg.domain.character.updater;

import com.rpgsystem.rpg.api.dto.character.CharacterNoteRequest;
import com.rpgsystem.rpg.domain.entity.NoteEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("CharacterNoteUpdater Tests")
class CharacterNoteUpdaterTest {

    private NoteEntity entity;
    private CharacterNoteRequest request;

    @BeforeEach
    void setUp() {
        entity = NoteEntity.builder()
                .note("Original Note")
                .build();
    }

    @Test
    @DisplayName("Deve retornar quando request for nulo")
    void deveRetornarQuandoRequestForNulo() {
        // Given
        CharacterNoteUpdater updater = new CharacterNoteUpdater(null);

        // When & Then
        assertDoesNotThrow(() -> updater.apply(entity));
        
        // Verify entity remains unchanged
        assertThat(entity.getNote()).isEqualTo("Original Note");
    }

    @Test
    @DisplayName("Deve retornar quando entity for nulo")
    void deveRetornarQuandoEntityForNulo() {
        // Given
        request = CharacterNoteRequest.builder()
                .note("New Note")
                .build();
        
        CharacterNoteUpdater updater = new CharacterNoteUpdater(request);

        // When & Then
        assertDoesNotThrow(() -> updater.apply(null));
    }

    @Test
    @DisplayName("Deve aplicar nota válida do request")
    void deveAplicarNotaValidaDoRequest() {
        // Given
        request = CharacterNoteRequest.builder()
                .note("New Note Content")
                .build();
        
        CharacterNoteUpdater updater = new CharacterNoteUpdater(request);

        // When
        updater.apply(entity);

        // Then
        assertThat(entity.getNote()).isEqualTo("New Note Content");
    }

    @Test
    @DisplayName("Deve aplicar nota com espaços e caracteres especiais")
    void deveAplicarNotaComEspacosECaracteresEspeciais() {
        // Given
        request = CharacterNoteRequest.builder()
                .note("  Note with spaces and\nnew lines\tand special chars: @#$%  ")
                .build();
        
        CharacterNoteUpdater updater = new CharacterNoteUpdater(request);

        // When
        updater.apply(entity);

        // Then
        assertThat(entity.getNote()).isEqualTo("  Note with spaces and\nnew lines\tand special chars: @#$%  ");
    }

    @Test
    @DisplayName("Deve aplicar nota longa")
    void deveAplicarNotaLonga() {
        // Given
        String longNote = "This is a very long note that contains multiple sentences. "
                + "It should be able to handle large amounts of text without any issues. "
                + "The note can contain various types of content including descriptions, "
                + "character backstory, important events, and other relevant information "
                + "that the player wants to keep track of during their RPG sessions.";
        
        request = CharacterNoteRequest.builder()
                .note(longNote)
                .build();
        
        CharacterNoteUpdater updater = new CharacterNoteUpdater(request);

        // When
        updater.apply(entity);

        // Then
        assertThat(entity.getNote()).isEqualTo(longNote);
    }

    @Test
    @DisplayName("Deve aplicar múltiplas atualizações consecutivas")
    void deveAplicarMultiplasAtualizacoesConsecutivas() {
        // Given
        CharacterNoteRequest firstRequest = CharacterNoteRequest.builder()
                .note("First Note")
                .build();
        
        CharacterNoteRequest secondRequest = CharacterNoteRequest.builder()
                .note("Second Note")
                .build();
        
        CharacterNoteUpdater firstUpdater = new CharacterNoteUpdater(firstRequest);
        CharacterNoteUpdater secondUpdater = new CharacterNoteUpdater(secondRequest);

        // When
        firstUpdater.apply(entity);
        secondUpdater.apply(entity);

        // Then
        assertThat(entity.getNote()).isEqualTo("Second Note");
    }

    @Test
    @DisplayName("Deve aplicar nota vazia")
    void deveAplicarNotaVazia() {
        // Given
        request = CharacterNoteRequest.builder()
                .note("")
                .build();
        
        CharacterNoteUpdater updater = new CharacterNoteUpdater(request);

        // When
        updater.apply(entity);

        // Then
        assertThat(entity.getNote()).isEqualTo("");
    }

    @Test
    @DisplayName("Deve aplicar nota com apenas espaços")
    void deveAplicarNotaComApenasEspacos() {
        // Given
        request = CharacterNoteRequest.builder()
                .note("   ")
                .build();
        
        CharacterNoteUpdater updater = new CharacterNoteUpdater(request);

        // When
        updater.apply(entity);

        // Then
        assertThat(entity.getNote()).isEqualTo("   ");
    }
}