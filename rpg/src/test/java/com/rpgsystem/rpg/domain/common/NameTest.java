package com.rpgsystem.rpg.domain.common;

import com.rpgsystem.rpg.domain.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Name - Testes Unitários")
class NameTest {

    @Nested
    @DisplayName("Testes de Construção")
    class ConstructorTests {

        @Test
        @DisplayName("Deve criar Name com valor válido")
        void deveCriarNameComValorValido() {
            // Given
            String valor = "João Silva";

            // When
            Name name = new Name(valor);

            // Then
            assertThat(name.getValue()).isEqualTo(valor);
        }

        @Test
        @DisplayName("Deve criar Name removendo espaços extras")
        void deveCriarNameRemovendoEspacosExtras() {
            // Given
            String valorComEspacos = "  João Silva  ";
            String valorEsperado = "João Silva";

            // When
            Name name = new Name(valorComEspacos);

            // Then
            assertThat(name.getValue()).isEqualTo(valorEsperado);
        }

        @Test
        @DisplayName("Deve lançar exceção quando valor é nulo")
        void deveLancarExcecaoQuandoValorEhNulo() {
            // When & Then
            assertThatThrownBy(() -> new Name(null))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Name must not be null or blank");
        }

        @Test
        @DisplayName("Deve lançar exceção quando valor é vazio")
        void deveLancarExcecaoQuandoValorEhVazio() {
            // When & Then
            assertThatThrownBy(() -> new Name(""))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Name must not be null or blank");
        }

        @Test
        @DisplayName("Deve lançar exceção quando valor é apenas espaços")
        void deveLancarExcecaoQuandoValorEhApenasEspacos() {
            // When & Then
            assertThatThrownBy(() -> new Name("   "))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Name must not be null or blank");
        }

        @Test
        @DisplayName("Deve lançar exceção quando valor após trim é vazio")
        void deveLancarExcecaoQuandoValorAposTrimEhVazio() {
            // When & Then
            assertThatThrownBy(() -> new Name("\t\n\r "))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Name must not be null or blank");
        }

        @Test
        @DisplayName("Deve lançar exceção quando valor é string com apenas espaços e tabs")
        void deveLancarExcecaoQuandoValorEhStringComApenasEspacosETabs() {
            // When & Then
            assertThatThrownBy(() -> new Name(" \t "))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Name must not be null or blank");
        }

        @Test
        @DisplayName("Deve lançar exceção quando valor contém apenas caracteres de controle")
        void deveLancarExcecaoQuandoValorContemApenasCaracteresDeControle() {
            // Given - string com caracteres de controle que não são detectados por isBlank()
            String valorComControle = "\u0000\u0001\u0002"; // caracteres de controle
            
            // When & Then
            assertThatThrownBy(() -> new Name(valorComControle))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Name must not be null or blank");
        }

        @Test
        @DisplayName("Deve lançar exceção quando valor excede 100 caracteres")
        void deveLancarExcecaoQuandoValorExcede100Caracteres() {
            // Given
            String valorLongo = "a".repeat(101);

            // When & Then
            assertThatThrownBy(() -> new Name(valorLongo))
                    .isInstanceOf(DomainException.class)
                    .hasMessage("Name must be at most 100 characters");
        }

        @Test
        @DisplayName("Deve aceitar valor com exatamente 100 caracteres")
        void deveAceitarValorComExatamente100Caracteres() {
            // Given
            String valor100Chars = "a".repeat(100);

            // When
            Name name = new Name(valor100Chars);

            // Then
            assertThat(name.getValue()).isEqualTo(valor100Chars);
            assertThat(name.getValue()).hasSize(100);
        }
    }

    @Nested
    @DisplayName("Testes de Método Estático of()")
    class StaticOfMethodTests {

        @Test
        @DisplayName("Deve retornar Name quando valor é válido")
        void deveRetornarNameQuandoValorEhValido() {
            // Given
            String valor = "Maria Santos";

            // When
            Name name = Name.of(valor);

            // Then
            assertThat(name).isNotNull();
            assertThat(name.getValue()).isEqualTo(valor);
        }

        @Test
        @DisplayName("Deve retornar null quando valor é nulo")
        void deveRetornarNullQuandoValorEhNulo() {
            // When
            Name name = Name.of(null);

            // Then
            assertThat(name).isNull();
        }

        @Test
        @DisplayName("Deve retornar null quando valor é vazio")
        void deveRetornarNullQuandoValorEhVazio() {
            // When
            Name name = Name.of("");

            // Then
            assertThat(name).isNull();
        }

        @Test
        @DisplayName("Deve retornar null quando valor é apenas espaços")
        void deveRetornarNullQuandoValorEhApenasEspacos() {
            // When
            Name name = Name.of("   ");

            // Then
            assertThat(name).isNull();
        }

        @Test
        @DisplayName("Deve retornar null quando valor é apenas caracteres de espaçamento")
        void deveRetornarNullQuandoValorEhApenasCaracteresDeEspacamento() {
            // When
            Name name = Name.of("\t\n\r ");

            // Then
            assertThat(name).isNull();
        }

        @Test
        @DisplayName("Deve criar Name com valor válido removendo espaços extras")
        void deveCriarNameComValorValidoRemovendoEspacosExtras() {
            // Given
            String valorComEspacos = "  Pedro Oliveira  ";
            String valorEsperado = "Pedro Oliveira";

            // When
            Name name = Name.of(valorComEspacos);

            // Then
            assertThat(name).isNotNull();
            assertThat(name.getValue()).isEqualTo(valorEsperado);
        }
    }

    @Nested
    @DisplayName("Testes de Equals e HashCode")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("Deve ser igual a outro Name com mesmo valor")
        void deveSerIgualAOutroNameComMesmoValor() {
            // Given
            String valor = "Ana Costa";
            Name name1 = new Name(valor);
            Name name2 = new Name(valor);

            // When & Then
            assertThat(name1).isEqualTo(name2);
            assertThat(name1.hashCode()).isEqualTo(name2.hashCode());
        }

        @Test
        @DisplayName("Não deve ser igual a Name com valor diferente")
        void naoDeveSerIgualANameComValorDiferente() {
            // Given
            Name name1 = new Name("Carlos Silva");
            Name name2 = new Name("Maria Silva");

            // When & Then
            assertThat(name1).isNotEqualTo(name2);
        }

        @Test
        @DisplayName("Deve ser igual considerando trim")
        void deveSerIgualConsiderandoTrim() {
            // Given
            Name name1 = new Name("José Santos");
            Name name2 = new Name("  José Santos  ");

            // When & Then
            assertThat(name1).isEqualTo(name2);
            assertThat(name1.hashCode()).isEqualTo(name2.hashCode());
        }
    }

    @Nested
    @DisplayName("Testes de ToString")
    class ToStringTests {

        @Test
        @DisplayName("Deve retornar representação string correta")
        void deveRetornarRepresentacaoStringCorreta() {
            // Given
            String valor = "Roberto Lima";
            Name name = new Name(valor);

            // When
            String toString = name.toString();

            // Then
            assertThat(toString).contains("Name");
            assertThat(toString).contains(valor);
        }
    }

    @Nested
    @DisplayName("Testes de Casos Extremos")
    class EdgeCaseTests {

        @Test
        @DisplayName("Deve aceitar caracteres especiais")
        void deveAceitarCaracteresEspeciais() {
            // Given
            String valorEspecial = "José María Ñoño";

            // When
            Name name = new Name(valorEspecial);

            // Then
            assertThat(name.getValue()).isEqualTo(valorEspecial);
        }

        @Test
        @DisplayName("Deve aceitar caracteres Unicode")
        void deveAceitarCaracteresUnicode() {
            // Given
            String valorUnicode = "李小明 🎯 Владимир";

            // When
            Name name = new Name(valorUnicode);

            // Then
            assertThat(name.getValue()).isEqualTo(valorUnicode);
        }

        @Test
        @DisplayName("Deve aceitar nome com um único caractere")
        void deveAceitarNomeComUmUnicoCaractere() {
            // Given
            String nomeMinimo = "A";

            // When
            Name name = new Name(nomeMinimo);

            // Then
            assertThat(name.getValue()).isEqualTo(nomeMinimo);
        }

        @Test
        @DisplayName("Deve aceitar nome com números")
        void deveAceitarNomeComNumeros() {
            // Given
            String nomeComNumeros = "João Silva 123";

            // When
            Name name = new Name(nomeComNumeros);

            // Then
            assertThat(name.getValue()).isEqualTo(nomeComNumeros);
        }

        @Test
        @DisplayName("Deve aceitar nome com símbolos")
        void deveAceitarNomeComSimbolos() {
            // Given
            String nomeComSimbolos = "O'Connor & Associates";

            // When
            Name name = new Name(nomeComSimbolos);

            // Then
            assertThat(name.getValue()).isEqualTo(nomeComSimbolos);
        }
    }
}