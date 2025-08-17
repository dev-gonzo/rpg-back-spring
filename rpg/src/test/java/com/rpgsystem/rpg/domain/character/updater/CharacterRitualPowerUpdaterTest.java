package com.rpgsystem.rpg.domain.character.updater;

import com.rpgsystem.rpg.api.dto.character.CharacterRitualPowerRequest;
import com.rpgsystem.rpg.domain.entity.RitualPowerEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterRitualPowerUpdaterTest {

    @Test
    void deveRetornarQuandoRequestForNulo() {
        // Given
        CharacterRitualPowerUpdater updater = new CharacterRitualPowerUpdater(null);
        RitualPowerEntity entity = new RitualPowerEntity();
        entity.setName("Nome Original");
        
        // When
        updater.apply(entity);
        
        // Then
        assertEquals("Nome Original", entity.getName());
    }
    
    @Test
    void deveRetornarQuandoEntityForNulo() {
        // Given
        CharacterRitualPowerRequest request = CharacterRitualPowerRequest.builder()
                .name("Ritual Teste")
                .pathsForms("Fogo 2")
                .build();
        CharacterRitualPowerUpdater updater = new CharacterRitualPowerUpdater(request);
        
        // When & Then
        assertDoesNotThrow(() -> updater.apply(null));
    }
    
    @Test
    void deveAplicarTodasAtualizacoesQuandoRequestEEntitySaoValidos() {
        // Given
        CharacterRitualPowerRequest request = CharacterRitualPowerRequest.builder()
                .name("Bola de Fogo")
                .pathsForms("Fogo 3, Criar 2")
                .description("Um ritual que cria uma esfera de fogo")
                .bookPage("Página 180")
                .build();
        
        CharacterRitualPowerUpdater updater = new CharacterRitualPowerUpdater(request);
        RitualPowerEntity entity = new RitualPowerEntity();
        
        // When
        updater.apply(entity);
        
        // Then
        assertEquals("Bola de Fogo", entity.getName());
        assertEquals("Fogo 3, Criar 2", entity.getPathsForms());
        assertEquals("Um ritual que cria uma esfera de fogo", entity.getDescription());
        assertEquals("Página 180", entity.getBookPage());
    }
    
    @Test
    void deveAplicarAtualizacoesComValoresVazios() {
        // Given
        CharacterRitualPowerRequest request = CharacterRitualPowerRequest.builder()
                .name("Ritual Básico")
                .pathsForms("")
                .description("")
                .bookPage("")
                .build();
        
        CharacterRitualPowerUpdater updater = new CharacterRitualPowerUpdater(request);
        RitualPowerEntity entity = new RitualPowerEntity();
        
        // When
        updater.apply(entity);
        
        // Then
        assertEquals("Ritual Básico", entity.getName());
        assertEquals("", entity.getPathsForms());
        assertEquals("", entity.getDescription());
        assertEquals("", entity.getBookPage());
    }
    
    @Test
    void deveAplicarAtualizacoesComValoresComplexos() {
        // Given
        CharacterRitualPowerRequest request = CharacterRitualPowerRequest.builder()
                .name("Ritual Supremo")
                .pathsForms("Fogo 4, Água 3, Terra 2, Ar 1, Luz 4, Trevas 3, Plantas 2, Animais 1, Humanos 4, Spiritum 3, Arkanun 2, Metamagia 1")
                .description("Um ritual extremamente complexo que combina múltiplos caminhos e formas para criar efeitos devastadores")
                .bookPage("Página 999")
                .build();
        
        CharacterRitualPowerUpdater updater = new CharacterRitualPowerUpdater(request);
        RitualPowerEntity entity = new RitualPowerEntity();
        
        // When
        updater.apply(entity);
        
        // Then
        assertEquals("Ritual Supremo", entity.getName());
        assertEquals("Fogo 4, Água 3, Terra 2, Ar 1, Luz 4, Trevas 3, Plantas 2, Animais 1, Humanos 4, Spiritum 3, Arkanun 2, Metamagia 1", entity.getPathsForms());
        assertEquals("Um ritual extremamente complexo que combina múltiplos caminhos e formas para criar efeitos devastadores", entity.getDescription());
        assertEquals("Página 999", entity.getBookPage());
    }
    
    @Test
    void deveAplicarMultiplasAtualizacoesConsecutivas() {
        // Given
        CharacterRitualPowerRequest request1 = CharacterRitualPowerRequest.builder()
                .name("Primeiro Ritual")
                .pathsForms("Fogo 1")
                .build();
        
        CharacterRitualPowerRequest request2 = CharacterRitualPowerRequest.builder()
                .name("Segundo Ritual")
                .pathsForms("Água 2")
                .build();
        
        CharacterRitualPowerUpdater updater1 = new CharacterRitualPowerUpdater(request1);
        CharacterRitualPowerUpdater updater2 = new CharacterRitualPowerUpdater(request2);
        RitualPowerEntity entity = new RitualPowerEntity();
        
        // When
        updater1.apply(entity);
        updater2.apply(entity);
        
        // Then
        assertEquals("Segundo Ritual", entity.getName());
        assertEquals("Água 2", entity.getPathsForms());
    }
}