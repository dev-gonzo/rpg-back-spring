package com.rpgsystem.rpg.domain.character.updater;

import com.rpgsystem.rpg.api.dto.character.CharacterWeaponRequest;
import com.rpgsystem.rpg.domain.entity.WeaponEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterWeaponUpdaterTest {

    @Test
    void deveRetornarQuandoRequestForNulo() {
        // Given
        CharacterWeaponUpdater updater = new CharacterWeaponUpdater(null);
        WeaponEntity entity = new WeaponEntity();
        entity.setName("Nome Original");
        
        // When
        updater.apply(entity);
        
        // Then
        assertEquals("Nome Original", entity.getName());
    }
    
    @Test
    void deveRetornarQuandoEntityForNulo() {
        // Given
        CharacterWeaponRequest request = CharacterWeaponRequest.builder()
                .name("Arma Teste")
                .damage("1d6")
                .build();
        CharacterWeaponUpdater updater = new CharacterWeaponUpdater(request);
        
        // When & Then
        assertDoesNotThrow(() -> updater.apply(null));
    }
    
    @Test
    void deveAplicarTodasAtualizacoesQuandoRequestEEntitySaoValidos() {
        // Given
        CharacterWeaponRequest request = CharacterWeaponRequest.builder()
                .name("Espada Longa")
                .description("Uma espada de lâmina longa")
                .damage("1d8+2")
                .initiative(2)
                .range("Corpo a corpo")
                .rof("1")
                .ammunition("N/A")
                .bookPage("Página 120")
                .build();
        
        CharacterWeaponUpdater updater = new CharacterWeaponUpdater(request);
        WeaponEntity entity = new WeaponEntity();
        
        // When
        updater.apply(entity);
        
        // Then
        assertEquals("Espada Longa", entity.getName());
        assertEquals("Uma espada de lâmina longa", entity.getDescription());
        assertEquals("1d8+2", entity.getDamage());
        assertEquals(2, entity.getInitiative());
        assertEquals("Corpo a corpo", entity.getRange());
        assertEquals("1", entity.getRof());
        assertEquals("N/A", entity.getAmmunition());
        assertEquals("Página 120", entity.getBookPage());
    }
    
    @Test
    void deveAplicarAtualizacoesComValoresZero() {
        // Given
        CharacterWeaponRequest request = CharacterWeaponRequest.builder()
                .name("Arma Básica")
                .description("")
                .damage("0")
                .initiative(0)
                .range("")
                .rof("0")
                .ammunition("")
                .bookPage("")
                .build();
        
        CharacterWeaponUpdater updater = new CharacterWeaponUpdater(request);
        WeaponEntity entity = new WeaponEntity();
        
        // When
        updater.apply(entity);
        
        // Then
        assertEquals("Arma Básica", entity.getName());
        assertEquals("", entity.getDescription());
        assertEquals("0", entity.getDamage());
        assertEquals(0, entity.getInitiative());
        assertEquals("", entity.getRange());
        assertEquals("0", entity.getRof());
        assertEquals("", entity.getAmmunition());
        assertEquals("", entity.getBookPage());
    }
    
    @Test
    void deveAplicarAtualizacoesComValoresMaximos() {
        // Given
        CharacterWeaponRequest request = CharacterWeaponRequest.builder()
                .name("Arma Poderosa")
                .description("Arma de destruição máxima")
                .damage("10d10+50")
                .initiative(10)
                .range("1000m")
                .rof("Auto")
                .ammunition("999")
                .bookPage("Página 999")
                .build();
        
        CharacterWeaponUpdater updater = new CharacterWeaponUpdater(request);
        WeaponEntity entity = new WeaponEntity();
        
        // When
        updater.apply(entity);
        
        // Then
        assertEquals("Arma Poderosa", entity.getName());
        assertEquals("Arma de destruição máxima", entity.getDescription());
        assertEquals("10d10+50", entity.getDamage());
        assertEquals(10, entity.getInitiative());
        assertEquals("1000m", entity.getRange());
        assertEquals("Auto", entity.getRof());
        assertEquals("999", entity.getAmmunition());
        assertEquals("Página 999", entity.getBookPage());
    }
    
    @Test
    void deveAplicarMultiplasAtualizacoesConsecutivas() {
        // Given
        CharacterWeaponRequest request1 = CharacterWeaponRequest.builder()
                .name("Primeira Arma")
                .damage("1d6")
                .build();
        
        CharacterWeaponRequest request2 = CharacterWeaponRequest.builder()
                .name("Segunda Arma")
                .damage("2d6")
                .build();
        
        CharacterWeaponUpdater updater1 = new CharacterWeaponUpdater(request1);
        CharacterWeaponUpdater updater2 = new CharacterWeaponUpdater(request2);
        WeaponEntity entity = new WeaponEntity();
        
        // When
        updater1.apply(entity);
        updater2.apply(entity);
        
        // Then
        assertEquals("Segunda Arma", entity.getName());
        assertEquals("2d6", entity.getDamage());
    }
}