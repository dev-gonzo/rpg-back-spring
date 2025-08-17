package com.rpgsystem.rpg.domain.character.updater;

import com.rpgsystem.rpg.api.dto.character.CharacterAttributeModRequest;
import com.rpgsystem.rpg.domain.entity.AttributeEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterAttributesModUpdaterTest {

    @Test
    void deveRetornarQuandoRequestForNulo() {
        // Given
        CharacterAttributesModUpdater updater = new CharacterAttributesModUpdater(null);
        AttributeEntity entity = new AttributeEntity();
        entity.setConMod(5);
        
        // When
        updater.apply(entity);
        
        // Then
        assertEquals(5, entity.getConMod()); // Valor deve permanecer inalterado
    }

    @Test
    void deveRetornarQuandoEntityForNulo() {
        // Given
        CharacterAttributeModRequest request = CharacterAttributeModRequest.builder()
                .characterId("test-id")
                .build();
        CharacterAttributesModUpdater updater = new CharacterAttributesModUpdater(request);
        
        // When & Then
        assertDoesNotThrow(() -> updater.apply(null));
    }

    @Test
    void deveAplicarModificadoresValidos() {
        // Given
        CharacterAttributeModRequest request = CharacterAttributeModRequest.builder()
                .characterId("test-id")
                .conMod(3)
                .frMod(-2)
                .dexMod(1)
                .agiMod(0)
                .intMod(4)
                .willMod(-1)
                .perMod(2)
                .carMod(-3)
                .build();
        
        CharacterAttributesModUpdater updater = new CharacterAttributesModUpdater(request);
        AttributeEntity entity = new AttributeEntity();
        
        // When
        updater.apply(entity);
        
        // Then
        assertEquals(3, entity.getConMod());
        assertEquals(-2, entity.getFrMod());
        assertEquals(1, entity.getDexMod());
        assertEquals(0, entity.getAgiMod());
        assertEquals(4, entity.getIntMod());
        assertEquals(-1, entity.getWillMod());
        assertEquals(2, entity.getPerMod());
        assertEquals(-3, entity.getCarMod());
    }

    @Test
    void deveAplicarNullQuandoModificadoresForemNulos() {
        // Given
        CharacterAttributeModRequest request = CharacterAttributeModRequest.builder()
                .characterId("test-id")
                .build();
        // Todos os modificadores são null por padrão
        
        CharacterAttributesModUpdater updater = new CharacterAttributesModUpdater(request);
        AttributeEntity entity = new AttributeEntity();
        entity.setConMod(5); // Valor inicial
        
        // When
        updater.apply(entity);
        
        // Then
        assertNull(entity.getConMod());
        assertNull(entity.getFrMod());
        assertNull(entity.getDexMod());
        assertNull(entity.getAgiMod());
        assertNull(entity.getIntMod());
        assertNull(entity.getWillMod());
        assertNull(entity.getPerMod());
        assertNull(entity.getCarMod());
    }

    @Test
    void deveAplicarMisturaDeMoficadoresValidosENulos() {
        // Given
        CharacterAttributeModRequest request = CharacterAttributeModRequest.builder()
                .characterId("test-id")
                .conMod(2)
                .frMod(null)
                .dexMod(-1)
                .agiMod(null)
                .intMod(3)
                .willMod(null)
                .perMod(1)
                .carMod(null)
                .build();
        
        CharacterAttributesModUpdater updater = new CharacterAttributesModUpdater(request);
        AttributeEntity entity = new AttributeEntity();
        
        // When
        updater.apply(entity);
        
        // Then
        assertEquals(2, entity.getConMod());
        assertNull(entity.getFrMod());
        assertEquals(-1, entity.getDexMod());
        assertNull(entity.getAgiMod());
        assertEquals(3, entity.getIntMod());
        assertNull(entity.getWillMod());
        assertEquals(1, entity.getPerMod());
        assertNull(entity.getCarMod());
    }

    @Test
    void deveAplicarMultiplasAtualizacoesConsecutivas() {
        // Given
        CharacterAttributeModRequest request1 = CharacterAttributeModRequest.builder()
                .characterId("test-id")
                .conMod(1)
                .frMod(2)
                .build();
        
        CharacterAttributeModRequest request2 = CharacterAttributeModRequest.builder()
                .characterId("test-id")
                .conMod(3)
                .dexMod(4)
                .build();
        
        AttributeEntity entity = new AttributeEntity();
        
        // When
        new CharacterAttributesModUpdater(request1).apply(entity);
        new CharacterAttributesModUpdater(request2).apply(entity);
        
        // Then
        assertEquals(3, entity.getConMod()); // Sobrescrito pela segunda atualização
        assertNull(entity.getFrMod()); // Sobrescrito com null pela segunda atualização
        assertEquals(4, entity.getDexMod()); // Definido pela segunda atualização
    }
}