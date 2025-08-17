package com.rpgsystem.rpg.domain.character.updater;

import com.rpgsystem.rpg.api.dto.character.CharacterCombatSkillRequest;
import com.rpgsystem.rpg.domain.entity.CombatSkillEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCombatSkillUpdaterTest {

    @Test
    void deveRetornarQuandoRequestForNulo() {
        // Given
        CharacterCombatSkillUpdater updater = new CharacterCombatSkillUpdater(null);
        CombatSkillEntity entity = new CombatSkillEntity();
        entity.setName("Nome Original");
        
        // When
        updater.apply(entity);
        
        // Then
        assertEquals("Nome Original", entity.getName());
    }
    
    @Test
    void deveRetornarQuandoEntityForNulo() {
        // Given
        CharacterCombatSkillRequest request = CharacterCombatSkillRequest.builder()
                .skill("Armas Brancas")
                .build();
        CharacterCombatSkillUpdater updater = new CharacterCombatSkillUpdater(request);
        
        // When & Then
        assertDoesNotThrow(() -> updater.apply(null));
    }
    
    @Test
    void deveAplicarTodasAtualizacoesQuandoRequestEEntitySaoValidos() {
        // Given
        CharacterCombatSkillRequest request = CharacterCombatSkillRequest.builder()
                .skill("Armas de Fogo")
                .build();
        
        CharacterCombatSkillUpdater updater = new CharacterCombatSkillUpdater(request);
        CombatSkillEntity entity = new CombatSkillEntity();
        
        // When
        updater.apply(entity);
        
        // Then
        assertEquals("Armas de Fogo", entity.getName());
        assertEquals(0, entity.getSkillValue());
        assertNull(entity.getBookPage());
    }
    
    @Test
    void deveAplicarAtualizacoesComSkillComEspacos() {
        // Given
        CharacterCombatSkillRequest request = CharacterCombatSkillRequest.builder()
                .skill("  Skill com Espaços  ")
                .build();
        
        CharacterCombatSkillUpdater updater = new CharacterCombatSkillUpdater(request);
        CombatSkillEntity entity = new CombatSkillEntity();
        
        // When
        updater.apply(entity);
        
        // Then - Name.of() deve fazer trim da string
        assertEquals("Skill com Espaços", entity.getName());
        assertEquals(0, entity.getSkillValue());
        assertNull(entity.getBookPage());
    }
    
    @Test
    void deveAplicarAtualizacoesComDiferentesSkills() {
        // Given
        CharacterCombatSkillRequest request = CharacterCombatSkillRequest.builder()
                .skill("Esquiva")
                .build();
        
        CharacterCombatSkillUpdater updater = new CharacterCombatSkillUpdater(request);
        CombatSkillEntity entity = new CombatSkillEntity();
        
        // When
        updater.apply(entity);
        
        // Then
        assertEquals("Esquiva", entity.getName());
        assertEquals(0, entity.getSkillValue());
        assertNull(entity.getBookPage());
    }
    
    @Test
    void deveAplicarMultiplasAtualizacoesConsecutivas() {
        // Given
        CharacterCombatSkillRequest request1 = CharacterCombatSkillRequest.builder()
                .skill("Primeira Skill")
                .build();
        
        CharacterCombatSkillRequest request2 = CharacterCombatSkillRequest.builder()
                .skill("Segunda Skill")
                .build();
        
        CharacterCombatSkillUpdater updater1 = new CharacterCombatSkillUpdater(request1);
        CharacterCombatSkillUpdater updater2 = new CharacterCombatSkillUpdater(request2);
        CombatSkillEntity entity = new CombatSkillEntity();
        
        // When
        updater1.apply(entity);
        updater2.apply(entity);
        
        // Then
        assertEquals("Segunda Skill", entity.getName());
        assertEquals(0, entity.getSkillValue());
        assertNull(entity.getBookPage());
    }
}