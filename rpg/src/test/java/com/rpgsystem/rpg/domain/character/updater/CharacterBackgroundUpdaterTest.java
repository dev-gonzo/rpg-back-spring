package com.rpgsystem.rpg.domain.character.updater;

import com.rpgsystem.rpg.api.dto.character.CharacterBackgroundRequest;
import com.rpgsystem.rpg.domain.entity.BackgroundEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("CharacterBackgroundUpdater Tests")
class CharacterBackgroundUpdaterTest {

    private BackgroundEntity entity;
    private CharacterBackgroundRequest request;

    @BeforeEach
    void setUp() {
        entity = BackgroundEntity.builder()
                .title("Original Title")
                .text("Original Text")
                .isPublic(false)
                .build();
    }

    @Test
    @DisplayName("Deve retornar quando request for nulo")
    void deveRetornarQuandoRequestForNulo() {
        // Given
        CharacterBackgroundUpdater updater = new CharacterBackgroundUpdater(null);

        // When & Then
        assertDoesNotThrow(() -> updater.apply(entity));
        
        // Verify entity remains unchanged
        assertThat(entity.getTitle()).isEqualTo("Original Title");
        assertThat(entity.getText()).isEqualTo("Original Text");
        assertThat(entity.isPublic()).isFalse();
    }

    @Test
    @DisplayName("Deve retornar quando entity for nulo")
    void deveRetornarQuandoEntityForNulo() {
        // Given
        request = CharacterBackgroundRequest.builder()
                .title("New Title")
                .text("New Text")
                .isPublic(true)
                .build();
        
        CharacterBackgroundUpdater updater = new CharacterBackgroundUpdater(request);

        // When & Then
        assertDoesNotThrow(() -> updater.apply(null));
    }

    @Test
    @DisplayName("Deve aplicar valores válidos do request")
    void deveAplicarValoresValidosDoRequest() {
        // Given
        request = CharacterBackgroundRequest.builder()
                .title("New Title")
                .text("New Text")
                .isPublic(true)
                .build();
        
        CharacterBackgroundUpdater updater = new CharacterBackgroundUpdater(request);

        // When
        updater.apply(entity);

        // Then
        assertThat(entity.getTitle()).isEqualTo("New Title");
        assertThat(entity.getText()).isEqualTo("New Text");
        assertThat(entity.isPublic()).isTrue();
    }

    @Test
    @DisplayName("Deve aplicar valores com espaços e caracteres especiais")
    void deveAplicarValoresComEspacosECaracteresEspeciais() {
        // Given
        request = CharacterBackgroundRequest.builder()
                .title("  Title with spaces  ")
                .text("Text with\nnew lines and special chars: @#$%")
                .isPublic(false)
                .build();
        
        CharacterBackgroundUpdater updater = new CharacterBackgroundUpdater(request);

        // When
        updater.apply(entity);

        // Then
        assertThat(entity.getTitle()).isEqualTo("  Title with spaces  ");
        assertThat(entity.getText()).isEqualTo("Text with\nnew lines and special chars: @#$%");
        assertThat(entity.isPublic()).isFalse();
    }

    @Test
    @DisplayName("Deve aplicar múltiplas atualizações consecutivas")
    void deveAplicarMultiplasAtualizacoesConsecutivas() {
        // Given
        CharacterBackgroundRequest firstRequest = CharacterBackgroundRequest.builder()
                .title("First Title")
                .text("First Text")
                .isPublic(true)
                .build();
        
        CharacterBackgroundRequest secondRequest = CharacterBackgroundRequest.builder()
                .title("Second Title")
                .text("Second Text")
                .isPublic(false)
                .build();
        
        CharacterBackgroundUpdater firstUpdater = new CharacterBackgroundUpdater(firstRequest);
        CharacterBackgroundUpdater secondUpdater = new CharacterBackgroundUpdater(secondRequest);

        // When
        firstUpdater.apply(entity);
        secondUpdater.apply(entity);

        // Then
        assertThat(entity.getTitle()).isEqualTo("Second Title");
        assertThat(entity.getText()).isEqualTo("Second Text");
        assertThat(entity.isPublic()).isFalse();
    }

    @Test
    @DisplayName("Deve aplicar valores quando isPublic for true")
    void deveAplicarValoresQuandoIsPublicForTrue() {
        // Given
        request = CharacterBackgroundRequest.builder()
                .title("Public Title")
                .text("Public Text")
                .isPublic(true)
                .build();
        
        CharacterBackgroundUpdater updater = new CharacterBackgroundUpdater(request);

        // When
        updater.apply(entity);

        // Then
        assertThat(entity.getTitle()).isEqualTo("Public Title");
        assertThat(entity.getText()).isEqualTo("Public Text");
        assertThat(entity.isPublic()).isTrue();
    }

    @Test
    @DisplayName("Deve aplicar valores quando isPublic for false")
    void deveAplicarValoresQuandoIsPublicForFalse() {
        // Given
        request = CharacterBackgroundRequest.builder()
                .title("Private Title")
                .text("Private Text")
                .isPublic(false)
                .build();
        
        CharacterBackgroundUpdater updater = new CharacterBackgroundUpdater(request);

        // When
        updater.apply(entity);

        // Then
        assertThat(entity.getTitle()).isEqualTo("Private Title");
        assertThat(entity.getText()).isEqualTo("Private Text");
        assertThat(entity.isPublic()).isFalse();
    }
}