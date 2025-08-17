package com.rpgsystem.rpg.domain.character.updater;

import com.rpgsystem.rpg.api.dto.character.CharacterEquipmentRequest;
import com.rpgsystem.rpg.domain.entity.EquipmentEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterEquipmentUpdaterTest {

    @Test
    void deveRetornarQuandoRequestForNulo() {
        // Given
        CharacterEquipmentUpdater updater = new CharacterEquipmentUpdater(null);
        EquipmentEntity entity = new EquipmentEntity();
        entity.setName("Nome Original");
        
        // When
        updater.apply(entity);
        
        // Then
        assertEquals("Nome Original", entity.getName());
    }
    
    @Test
    void deveRetornarQuandoEntityForNulo() {
        // Given
        CharacterEquipmentRequest request = CharacterEquipmentRequest.builder()
                .name("Equipamento Teste")
                .quantity(1)
                .build();
        CharacterEquipmentUpdater updater = new CharacterEquipmentUpdater(request);
        
        // When & Then
        assertDoesNotThrow(() -> updater.apply(null));
    }
    
    @Test
    void deveAplicarTodasAtualizacoesQuandoRequestEEntitySaoValidos() {
        // Given
        CharacterEquipmentRequest request = CharacterEquipmentRequest.builder()
                .name("Armadura de Couro")
                .quantity(1)
                .classification("Armadura Leve")
                .description("Uma armadura básica de couro")
                .kineticProtection(2)
                .ballisticProtection(1)
                .dexterityPenalty(0)
                .agilityPenalty(0)
                .initiative(0)
                .bookPage("Página 150")
                .build();
        
        CharacterEquipmentUpdater updater = new CharacterEquipmentUpdater(request);
        EquipmentEntity entity = new EquipmentEntity();
        
        // When
        updater.apply(entity);
        
        // Then
        assertEquals("Armadura de Couro", entity.getName());
        assertEquals(1, entity.getQuantity());
        assertEquals("Armadura Leve", entity.getClassification());
        assertEquals("Uma armadura básica de couro", entity.getDescription());
        assertEquals(2, entity.getKineticProtection());
        assertEquals(1, entity.getBallisticProtection());
        assertEquals(0, entity.getDexterityPenalty());
        assertEquals(0, entity.getAgilityPenalty());
        assertEquals(0, entity.getInitiative());
        assertEquals("Página 150", entity.getBookPage());
    }
    
    @Test
    void deveAplicarAtualizacoesComValoresZero() {
        // Given
        CharacterEquipmentRequest request = CharacterEquipmentRequest.builder()
                .name("Item Básico")
                .quantity(0)
                .classification("")
                .description("")
                .kineticProtection(0)
                .ballisticProtection(0)
                .dexterityPenalty(0)
                .agilityPenalty(0)
                .initiative(0)
                .bookPage("")
                .build();
        
        CharacterEquipmentUpdater updater = new CharacterEquipmentUpdater(request);
        EquipmentEntity entity = new EquipmentEntity();
        
        // When
        updater.apply(entity);
        
        // Then
        assertEquals("Item Básico", entity.getName());
        assertEquals(0, entity.getQuantity());
        assertEquals("", entity.getClassification());
        assertEquals("", entity.getDescription());
        assertEquals(0, entity.getKineticProtection());
        assertEquals(0, entity.getBallisticProtection());
        assertEquals(0, entity.getDexterityPenalty());
        assertEquals(0, entity.getAgilityPenalty());
        assertEquals(0, entity.getInitiative());
        assertEquals("", entity.getBookPage());
    }
    
    @Test
    void deveAplicarAtualizacoesComValoresMaximos() {
        // Given
        CharacterEquipmentRequest request = CharacterEquipmentRequest.builder()
                .name("Armadura Completa")
                .quantity(999)
                .classification("Armadura Pesada")
                .description("Armadura de proteção máxima")
                .kineticProtection(10)
                .ballisticProtection(8)
                .dexterityPenalty(-5)
                .agilityPenalty(-3)
                .initiative(-2)
                .bookPage("Página 200")
                .build();
        
        CharacterEquipmentUpdater updater = new CharacterEquipmentUpdater(request);
        EquipmentEntity entity = new EquipmentEntity();
        
        // When
        updater.apply(entity);
        
        // Then
        assertEquals("Armadura Completa", entity.getName());
        assertEquals(999, entity.getQuantity());
        assertEquals("Armadura Pesada", entity.getClassification());
        assertEquals("Armadura de proteção máxima", entity.getDescription());
        assertEquals(10, entity.getKineticProtection());
        assertEquals(8, entity.getBallisticProtection());
        assertEquals(-5, entity.getDexterityPenalty());
        assertEquals(-3, entity.getAgilityPenalty());
        assertEquals(-2, entity.getInitiative());
        assertEquals("Página 200", entity.getBookPage());
    }
    
    @Test
    void deveAplicarMultiplasAtualizacoesConsecutivas() {
        // Given
        CharacterEquipmentRequest request1 = CharacterEquipmentRequest.builder()
                .name("Primeira Armadura")
                .quantity(1)
                .build();
        
        CharacterEquipmentRequest request2 = CharacterEquipmentRequest.builder()
                .name("Segunda Armadura")
                .quantity(2)
                .build();
        
        CharacterEquipmentUpdater updater1 = new CharacterEquipmentUpdater(request1);
        CharacterEquipmentUpdater updater2 = new CharacterEquipmentUpdater(request2);
        EquipmentEntity entity = new EquipmentEntity();
        
        // When
        updater1.apply(entity);
        updater2.apply(entity);
        
        // Then
        assertEquals("Segunda Armadura", entity.getName());
        assertEquals(2, entity.getQuantity());
    }
}