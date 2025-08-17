package com.rpgsystem.rpg.domain.character.updater;

import com.rpgsystem.rpg.api.dto.character.CharacterImprovementRequest;
import com.rpgsystem.rpg.domain.entity.ImprovementEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("CharacterImprovementUpdater Tests")
class CharacterImprovementUpdaterTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Deve criar updater com request válido")
        void deveCriarUpdaterComRequestValido() {
            // Given
            CharacterImprovementRequest request = CharacterImprovementRequest.builder()
                    .name("Improvement Test")
                    .cost(100)
                    .kitValue(50)
                    .build();

            // When
            CharacterImprovementUpdater updater = new CharacterImprovementUpdater(request);

            // Then
            assertNotNull(updater);
        }

        @Test
        @DisplayName("Deve criar updater com request null")
        void deveCriarUpdaterComRequestNull() {
            // When
            CharacterImprovementUpdater updater = new CharacterImprovementUpdater(null);

            // Then
            assertNotNull(updater);
        }
    }

    @Nested
    @DisplayName("Apply Method Tests")
    class ApplyMethodTests {

        @Test
        @DisplayName("Deve aplicar melhorias com todos os valores válidos")
        void deveAplicarMelhoriasComTodosValoresValidos() {
            // Given
            CharacterImprovementRequest request = CharacterImprovementRequest.builder()
                    .name("Improvement Test")
                    .cost(100)
                    .kitValue(50)
                    .build();

            ImprovementEntity entity = mock(ImprovementEntity.class);
            CharacterImprovementUpdater updater = new CharacterImprovementUpdater(request);

            // When
            updater.apply(entity);

            // Then
            verify(entity).setName("Improvement Test");
            verify(entity).setCost(100);
            verify(entity).setKitValue(50);
        }

        @Test
        @DisplayName("Deve aplicar melhorias com cost null (usar valor padrão 0)")
        void deveAplicarMelhoriasComCostNull() {
            // Given
            CharacterImprovementRequest request = CharacterImprovementRequest.builder()
                    .name("Improvement Test")
                    .cost(null)
                    .kitValue(50)
                    .build();

            ImprovementEntity entity = mock(ImprovementEntity.class);
            CharacterImprovementUpdater updater = new CharacterImprovementUpdater(request);

            // When
            updater.apply(entity);

            // Then
            verify(entity).setName("Improvement Test");
            verify(entity).setCost(0);
            verify(entity).setKitValue(50);
        }

        @Test
        @DisplayName("Deve aplicar melhorias com kitValue null (usar valor padrão 0)")
        void deveAplicarMelhoriasComKitValueNull() {
            // Given
            CharacterImprovementRequest request = CharacterImprovementRequest.builder()
                    .name("Improvement Test")
                    .cost(100)
                    .kitValue(null)
                    .build();

            ImprovementEntity entity = mock(ImprovementEntity.class);
            CharacterImprovementUpdater updater = new CharacterImprovementUpdater(request);

            // When
            updater.apply(entity);

            // Then
            verify(entity).setName("Improvement Test");
            verify(entity).setCost(100);
            verify(entity).setKitValue(0);
        }

        @Test
        @DisplayName("Deve aplicar melhorias com cost e kitValue null")
        void deveAplicarMelhoriasComCostEKitValueNull() {
            // Given
            CharacterImprovementRequest request = CharacterImprovementRequest.builder()
                    .name("Improvement Test")
                    .cost(null)
                    .kitValue(null)
                    .build();

            ImprovementEntity entity = mock(ImprovementEntity.class);
            CharacterImprovementUpdater updater = new CharacterImprovementUpdater(request);

            // When
            updater.apply(entity);

            // Then
            verify(entity).setName("Improvement Test");
            verify(entity).setCost(0);
            verify(entity).setKitValue(0);
        }

        @Test
        @DisplayName("Não deve fazer nada quando request é null")
        void naoDeveFazerNadaQuandoRequestEhNull() {
            // Given
            ImprovementEntity entity = mock(ImprovementEntity.class);
            CharacterImprovementUpdater updater = new CharacterImprovementUpdater(null);

            // When
            updater.apply(entity);

            // Then
            verifyNoInteractions(entity);
        }

        @Test
        @DisplayName("Não deve fazer nada quando entity é null")
        void naoDeveFazerNadaQuandoEntityEhNull() {
            // Given
            CharacterImprovementRequest request = CharacterImprovementRequest.builder()
                    .name("Improvement Test")
                    .cost(100)
                    .kitValue(50)
                    .build();

            CharacterImprovementUpdater updater = new CharacterImprovementUpdater(request);

            // When & Then (não deve lançar exceção)
            assertDoesNotThrow(() -> updater.apply(null));
        }

        @Test
        @DisplayName("Não deve fazer nada quando request e entity são null")
        void naoDeveFazerNadaQuandoRequestEEntitySaoNull() {
            // Given
            CharacterImprovementUpdater updater = new CharacterImprovementUpdater(null);

            // When & Then (não deve lançar exceção)
            assertDoesNotThrow(() -> updater.apply(null));
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Deve lidar com nome vazio")
        void deveLidarComNomeVazio() {
            // Given
            CharacterImprovementRequest request = CharacterImprovementRequest.builder()
                    .name("")
                    .cost(100)
                    .kitValue(50)
                    .build();

            ImprovementEntity entity = mock(ImprovementEntity.class);
            CharacterImprovementUpdater updater = new CharacterImprovementUpdater(request);

            // When & Then
            assertThrows(Exception.class, () -> updater.apply(entity));
        }

        @Test
        @DisplayName("Deve lidar com nome null")
        void deveLidarComNomeNull() {
            // Given
            CharacterImprovementRequest request = CharacterImprovementRequest.builder()
                    .name(null)
                    .cost(100)
                    .kitValue(50)
                    .build();

            ImprovementEntity entity = mock(ImprovementEntity.class);
            CharacterImprovementUpdater updater = new CharacterImprovementUpdater(request);

            // When & Then
            assertThrows(Exception.class, () -> updater.apply(entity));
        }

        @Test
        @DisplayName("Deve aplicar melhorias com valores negativos")
        void deveAplicarMelhoriasComValoresNegativos() {
            // Given
            CharacterImprovementRequest request = CharacterImprovementRequest.builder()
                    .name("Negative Test")
                    .cost(-100)
                    .kitValue(-50)
                    .build();

            ImprovementEntity entity = mock(ImprovementEntity.class);
            CharacterImprovementUpdater updater = new CharacterImprovementUpdater(request);

            // When
            updater.apply(entity);

            // Then
            verify(entity).setName("Negative Test");
            verify(entity).setCost(-100);
            verify(entity).setKitValue(-50);
        }

        @Test
        @DisplayName("Deve aplicar melhorias com valores zero")
        void deveAplicarMelhoriasComValoresZero() {
            // Given
            CharacterImprovementRequest request = CharacterImprovementRequest.builder()
                    .name("Zero Test")
                    .cost(0)
                    .kitValue(0)
                    .build();

            ImprovementEntity entity = mock(ImprovementEntity.class);
            CharacterImprovementUpdater updater = new CharacterImprovementUpdater(request);

            // When
            updater.apply(entity);

            // Then
            verify(entity).setName("Zero Test");
            verify(entity).setCost(0);
            verify(entity).setKitValue(0);
        }

        @Test
        @DisplayName("Deve aplicar melhorias com valores máximos")
        void deveAplicarMelhoriasComValoresMaximos() {
            // Given
            CharacterImprovementRequest request = CharacterImprovementRequest.builder()
                    .name("Max Values Test")
                    .cost(Integer.MAX_VALUE)
                    .kitValue(Integer.MAX_VALUE)
                    .build();

            ImprovementEntity entity = mock(ImprovementEntity.class);
            CharacterImprovementUpdater updater = new CharacterImprovementUpdater(request);

            // When
            updater.apply(entity);

            // Then
            verify(entity).setName("Max Values Test");
            verify(entity).setCost(Integer.MAX_VALUE);
            verify(entity).setKitValue(Integer.MAX_VALUE);
        }
    }
}