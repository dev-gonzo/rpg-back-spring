package com.rpgsystem.rpg.domain.character.updater;

import com.rpgsystem.rpg.api.dto.character.CharacterPathsAndFormsRequest;
import com.rpgsystem.rpg.domain.entity.PathsAndFormsEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("CharacterPathsAndFormsUpdater Tests")
class CharacterPathsAndFormsUpdaterTest {

    @Test
    @DisplayName("Deve retornar sem fazer nada quando request é nulo")
    void deveRetornarSemFazerNadaQuandoRequestEhNulo() {
        // Given
        CharacterPathsAndFormsUpdater updater = new CharacterPathsAndFormsUpdater(null);
        PathsAndFormsEntity entity = mock(PathsAndFormsEntity.class);

        // When
        updater.apply(entity);

        // Then
        verifyNoInteractions(entity);
    }

    @Test
    @DisplayName("Deve retornar sem fazer nada quando entity é nulo")
    void deveRetornarSemFazerNadaQuandoEntityEhNulo() {
        // Given
        CharacterPathsAndFormsRequest request = CharacterPathsAndFormsRequest.builder()
                .understandForm(1)
                .createForm(2)
                .controlForm(3)
                .fire(1)
                .water(2)
                .earth(3)
                .air(4)
                .light(5)
                .darkness(1)
                .plants(2)
                .animals(3)
                .humans(4)
                .spiritum(5)
                .arkanun(1)
                .metamagic(2)
                .build();
        CharacterPathsAndFormsUpdater updater = new CharacterPathsAndFormsUpdater(request);

        // When
        updater.apply(null);

        // Then
        // Não deve lançar exceção e deve retornar normalmente
        assertDoesNotThrow(() -> updater.apply(null));
    }

    @Test
    @DisplayName("Deve aplicar todas as atualizações quando request e entity são válidos")
    void deveAplicarTodasAtualizacoesQuandoRequestEEntitySaoValidos() {
        // Given
        CharacterPathsAndFormsRequest request = CharacterPathsAndFormsRequest.builder()
                .understandForm(1)
                .createForm(2)
                .controlForm(3)
                .fire(1)
                .water(2)
                .earth(3)
                .air(4)
                .light(4)
                .darkness(1)
                .plants(2)
                .animals(3)
                .humans(4)
                .spiritum(4)
                .arkanun(1)
                .metamagic(2)
                .build();
        CharacterPathsAndFormsUpdater updater = new CharacterPathsAndFormsUpdater(request);
        PathsAndFormsEntity entity = mock(PathsAndFormsEntity.class);

        // When
        updater.apply(entity);

        // Then
        verify(entity).setUnderstandForm(1);
        verify(entity).setCreateForm(2);
        verify(entity).setControlForm(3);
        verify(entity).setFire(1);
        verify(entity).setWater(2);
        verify(entity).setEarth(3);
        verify(entity).setAir(4);
        verify(entity).setLight(4);
        verify(entity).setDarkness(1);
        verify(entity).setPlants(2);
        verify(entity).setAnimals(3);
        verify(entity).setHumans(4);
        verify(entity).setSpiritum(4);
        verify(entity).setArkanun(1);
        verify(entity).setMetamagic(2);
    }

    @Test
    @DisplayName("Deve aplicar atualizações com valores zero")
    void deveAplicarAtualizacoesComValoresZero() {
        // Given
        CharacterPathsAndFormsRequest request = CharacterPathsAndFormsRequest.builder()
                .understandForm(0)
                .createForm(0)
                .controlForm(0)
                .fire(0)
                .water(0)
                .earth(0)
                .air(0)
                .light(0)
                .darkness(0)
                .plants(0)
                .animals(0)
                .humans(0)
                .spiritum(0)
                .arkanun(0)
                .metamagic(0)
                .build();
        CharacterPathsAndFormsUpdater updater = new CharacterPathsAndFormsUpdater(request);
        PathsAndFormsEntity entity = mock(PathsAndFormsEntity.class);

        // When
        updater.apply(entity);

        // Then
        verify(entity).setUnderstandForm(0);
        verify(entity).setCreateForm(0);
        verify(entity).setControlForm(0);
        verify(entity).setFire(0);
        verify(entity).setWater(0);
        verify(entity).setEarth(0);
        verify(entity).setAir(0);
        verify(entity).setLight(0);
        verify(entity).setDarkness(0);
        verify(entity).setPlants(0);
        verify(entity).setAnimals(0);
        verify(entity).setHumans(0);
        verify(entity).setSpiritum(0);
        verify(entity).setArkanun(0);
        verify(entity).setMetamagic(0);
    }

    @Test
    @DisplayName("Deve aplicar atualizações com valores máximos")
    void deveAplicarAtualizacoesComValoresMaximos() {
        // Given
        CharacterPathsAndFormsRequest request = CharacterPathsAndFormsRequest.builder()
                .understandForm(10)
                .createForm(10)
                .controlForm(10)
                .fire(4)
                .water(4)
                .earth(4)
                .air(4)
                .light(4)
                .darkness(4)
                .plants(4)
                .animals(4)
                .humans(4)
                .spiritum(4)
                .arkanun(4)
                .metamagic(4)
                .build();
        CharacterPathsAndFormsUpdater updater = new CharacterPathsAndFormsUpdater(request);
        PathsAndFormsEntity entity = mock(PathsAndFormsEntity.class);

        // When
        updater.apply(entity);

        // Then
        verify(entity).setUnderstandForm(10);
        verify(entity).setCreateForm(10);
        verify(entity).setControlForm(10);
        verify(entity).setFire(4);
        verify(entity).setWater(4);
        verify(entity).setEarth(4);
        verify(entity).setAir(4);
        verify(entity).setLight(4);
        verify(entity).setDarkness(4);
        verify(entity).setPlants(4);
        verify(entity).setAnimals(4);
        verify(entity).setHumans(4);
        verify(entity).setSpiritum(4);
        verify(entity).setArkanun(4);
        verify(entity).setMetamagic(4);
    }

    @Test
    @DisplayName("Deve aplicar múltiplas chamadas consecutivas")
    void deveAplicarMultiplasChmadasConsecutivas() {
        // Given
        CharacterPathsAndFormsRequest request = CharacterPathsAndFormsRequest.builder()
                .understandForm(3)
                .createForm(3)
                .controlForm(3)
                .fire(3)
                .water(3)
                .earth(3)
                .air(3)
                .light(3)
                .darkness(3)
                .plants(3)
                .animals(3)
                .humans(3)
                .spiritum(3)
                .arkanun(3)
                .metamagic(3)
                .build();
        CharacterPathsAndFormsUpdater updater = new CharacterPathsAndFormsUpdater(request);
        PathsAndFormsEntity entity1 = mock(PathsAndFormsEntity.class);
        PathsAndFormsEntity entity2 = mock(PathsAndFormsEntity.class);

        // When
        updater.apply(entity1);
        updater.apply(entity2);

        // Then
        verify(entity1, times(1)).setUnderstandForm(3);
        verify(entity2, times(1)).setUnderstandForm(3);
        verify(entity1, times(1)).setFire(3);
        verify(entity2, times(1)).setFire(3);
    }
}