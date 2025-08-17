package com.rpgsystem.rpg.domain.character.updater;

import com.rpgsystem.rpg.api.dto.character.CharacterSkillRequest;
import com.rpgsystem.rpg.domain.entity.SkillEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterSkillUpdaterTest {

    @Test
    void deveRetornarQuandoRequestForNulo() {
        // Given
        CharacterSkillUpdater updater = new CharacterSkillUpdater(null);
        SkillEntity entity = new SkillEntity();
        entity.setName("Nome Original");
        
        // When
        updater.apply(entity);
        
        // Then
        assertEquals("Nome Original", entity.getName());
    }
    
    @Test
    void deveRetornarQuandoEntityForNulo() {
        // Given
        CharacterSkillRequest request = CharacterSkillRequest.builder()
                .skill("Computação")
                .cost(10)
                .kitValue(5)
                .build();
        CharacterSkillUpdater updater = new CharacterSkillUpdater(request);
        
        // When & Then
        assertDoesNotThrow(() -> updater.apply(null));
    }
    
    @Test
    void deveAplicarTodasAtualizacoesQuandoRequestEEntitySaoValidos() {
        // Given
        CharacterSkillRequest request = CharacterSkillRequest.builder()
                .skill("Medicina")
                .cost(15)
                .kitValue(8)
                .build();
        
        CharacterSkillUpdater updater = new CharacterSkillUpdater(request);
        SkillEntity entity = new SkillEntity();
        
        // When
        updater.apply(entity);
        
        // Then
        assertEquals("Medicina", entity.getName());
        assertEquals(0, entity.getSkillValue());
        assertNull(entity.getBookPage());
    }
    
    @Test
    void deveAplicarAtualizacoesComSkillComEspacos() {
        // Given
        CharacterSkillRequest request = CharacterSkillRequest.builder()
                .skill("  Investigação  ")
                .cost(12)
                .kitValue(6)
                .build();
        
        CharacterSkillUpdater updater = new CharacterSkillUpdater(request);
        SkillEntity entity = new SkillEntity();
        
        // When
        updater.apply(entity);
        
        // Then - Name.of() deve fazer trim da string
        assertEquals("Investigação", entity.getName());
        assertEquals(0, entity.getSkillValue());
        assertNull(entity.getBookPage());
    }
    
    @Test
    void deveAplicarAtualizacoesComDiferentesSkills() {
        // Given
        CharacterSkillRequest request = CharacterSkillRequest.builder()
                .skill("Ocultismo")
                .cost(20)
                .kitValue(10)
                .build();
        
        CharacterSkillUpdater updater = new CharacterSkillUpdater(request);
        SkillEntity entity = new SkillEntity();
        
        // When
        updater.apply(entity);
        
        // Then
        assertEquals("Ocultismo", entity.getName());
        assertEquals(0, entity.getSkillValue());
        assertNull(entity.getBookPage());
    }
    
    @Test
    void deveAplicarMultiplasAtualizacoesConsecutivas() {
        // Given
        CharacterSkillRequest request1 = CharacterSkillRequest.builder()
                .skill("Primeira Skill")
                .cost(5)
                .kitValue(2)
                .build();
        
        CharacterSkillRequest request2 = CharacterSkillRequest.builder()
                .skill("Segunda Skill")
                .cost(8)
                .kitValue(4)
                .build();
        
        CharacterSkillUpdater updater1 = new CharacterSkillUpdater(request1);
        CharacterSkillUpdater updater2 = new CharacterSkillUpdater(request2);
        SkillEntity entity = new SkillEntity();
        
        // When
        updater1.apply(entity);
        updater2.apply(entity);
        
        // Then
        assertEquals("Segunda Skill", entity.getName());
        assertEquals(0, entity.getSkillValue());
        assertNull(entity.getBookPage());
    }
}