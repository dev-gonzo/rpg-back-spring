package com.rpgsystem.rpg.domain.common;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("CodigoId - Testes Unitários")
class CodigoIdTest {

    @Nested
    @DisplayName("Testes de Construção")
    class ConstructorTests {

        @Test
        @DisplayName("Deve criar CodigoId com valor válido")
        void deveCriarCodigoIdComValorValido() {
            // Given
            String valor = "123e4567-e89b-12d3-a456-426614174000";

            // When
            CodigoId codigoId = new CodigoId(valor);

            // Then
            assertThat(codigoId.getValue()).isEqualTo(valor);
        }

        @Test
        @DisplayName("Deve lançar exceção quando valor é nulo")
        void deveLancarExcecaoQuandoValorEhNulo() {
            // When & Then
            assertThatThrownBy(() -> new CodigoId(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Código não pode ser nulo ou vazio");
        }

        @Test
        @DisplayName("Deve lançar exceção quando valor é vazio")
        void deveLancarExcecaoQuandoValorEhVazio() {
            // When & Then
            assertThatThrownBy(() -> new CodigoId(""))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Código não pode ser nulo ou vazio");
        }

        @Test
        @DisplayName("Deve lançar exceção quando valor é apenas espaços")
        void deveLancarExcecaoQuandoValorEhApenasEspacos() {
            // When & Then
            assertThatThrownBy(() -> new CodigoId("   "))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Código não pode ser nulo ou vazio");
        }
    }

    @Nested
    @DisplayName("Testes de Método Estático")
    class StaticMethodTests {

        @Test
        @DisplayName("Deve gerar novo CodigoId com UUID válido")
        void deveGerarNovoCodigoIdComUuidValido() {
            // When
            CodigoId codigoId = CodigoId.novo();

            // Then
            assertThat(codigoId.getValue())
                    .isNotNull()
                    .hasSize(36)
                    .matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
        }

        @Test
        @DisplayName("Deve gerar CodigoIds únicos")
        void deveGerarCodigoIdsUnicos() {
            // When
            CodigoId codigo1 = CodigoId.novo();
            CodigoId codigo2 = CodigoId.novo();

            // Then
            assertThat(codigo1.getValue()).isNotEqualTo(codigo2.getValue());
        }
    }

    @Nested
    @DisplayName("Testes de Equals e HashCode")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("Deve ser igual a si mesmo")
        void deveSerIgualASiMesmo() {
            // Given
            CodigoId codigoId = new CodigoId("test-id");

            // When & Then
            assertThat(codigoId).isEqualTo(codigoId);
        }

        @Test
        @DisplayName("Deve ser igual a outro CodigoId com mesmo valor")
        void deveSerIgualAOutroCodigoIdComMesmoValor() {
            // Given
            String valor = "test-id";
            CodigoId codigoId1 = new CodigoId(valor);
            CodigoId codigoId2 = new CodigoId(valor);

            // When & Then
            assertThat(codigoId1).isEqualTo(codigoId2);
            assertThat(codigoId1.hashCode()).isEqualTo(codigoId2.hashCode());
        }

        @Test
        @DisplayName("Não deve ser igual a CodigoId com valor diferente")
        void naoDeveSerIgualACodigoIdComValorDiferente() {
            // Given
            CodigoId codigoId1 = new CodigoId("test-id-1");
            CodigoId codigoId2 = new CodigoId("test-id-2");

            // When & Then
            assertThat(codigoId1).isNotEqualTo(codigoId2);
        }

        @Test
        @DisplayName("Não deve ser igual a null")
        void naoDeveSerIgualANull() {
            // Given
            CodigoId codigoId = new CodigoId("test-id");

            // When & Then
            assertThat(codigoId).isNotEqualTo(null);
        }

        @Test
        @DisplayName("Não deve ser igual a objeto de classe diferente")
        void naoDeveSerIgualAObjetoDeClasseDiferente() {
            // Given
            CodigoId codigoId = new CodigoId("test-id");
            String string = "test-id";

            // When & Then
            assertThat(codigoId).isNotEqualTo(string);
        }

        @Test
        @DisplayName("Deve ter hashCode consistente")
        void deveTerHashCodeConsistente() {
            // Given
            CodigoId codigoId = new CodigoId("test-id");

            // When
            int hashCode1 = codigoId.hashCode();
            int hashCode2 = codigoId.hashCode();

            // Then
            assertThat(hashCode1).isEqualTo(hashCode2);
        }
    }

    @Nested
    @DisplayName("Testes de ToString")
    class ToStringTests {

        @Test
        @DisplayName("Deve retornar o valor como string")
        void deveRetornarOValorComoString() {
            // Given
            String valor = "test-id-123";
            CodigoId codigoId = new CodigoId(valor);

            // When
            String toString = codigoId.toString();

            // Then
            assertThat(toString).isEqualTo(valor);
        }

        @Test
        @DisplayName("Deve retornar string com caracteres especiais")
        void deveRetornarStringComCaracteresEspeciais() {
            // Given
            String valor = "test-id-@#$%";
            CodigoId codigoId = new CodigoId(valor);

            // When
            String toString = codigoId.toString();

            // Then
            assertThat(toString).isEqualTo(valor);
        }
    }

    @Nested
    @DisplayName("Testes de Casos Extremos")
    class EdgeCaseTests {

        @Test
        @DisplayName("Deve aceitar valor muito longo")
        void deveAceitarValorMuitoLongo() {
            // Given
            String valorLongo = "a".repeat(1000);

            // When
            CodigoId codigoId = new CodigoId(valorLongo);

            // Then
            assertThat(codigoId.getValue()).isEqualTo(valorLongo);
        }

        @Test
        @DisplayName("Deve aceitar caracteres Unicode")
        void deveAceitarCaracteresUnicode() {
            // Given
            String valorUnicode = "测试-🎯-Ñoël";

            // When
            CodigoId codigoId = new CodigoId(valorUnicode);

            // Then
            assertThat(codigoId.getValue()).isEqualTo(valorUnicode);
        }

        @Test
        @DisplayName("Deve aceitar valor com um único caractere")
        void deveAceitarValorComUmUnicoCaractere() {
            // Given
            String valorMinimo = "a";

            // When
            CodigoId codigoId = new CodigoId(valorMinimo);

            // Then
            assertThat(codigoId.getValue()).isEqualTo(valorMinimo);
        }
    }
}