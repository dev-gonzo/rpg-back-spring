package com.rpgsystem.rpg.api.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LoginResponse Tests")
class LoginResponseTest {

    @Nested
    @DisplayName("Testes de Construção")
    class ConstructionTests {

        @Test
        @DisplayName("Deve criar LoginResponse com token válido")
        void deveCriarLoginResponseComTokenValido() {
            // Given
            String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

            // When
            LoginResponse response = new LoginResponse(token);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.token()).isEqualTo(token);
        }

        @Test
        @DisplayName("Deve criar LoginResponse com token nulo")
        void deveCriarLoginResponseComTokenNulo() {
            // When
            LoginResponse response = new LoginResponse(null);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.token()).isNull();
        }

        @Test
        @DisplayName("Deve criar LoginResponse com token vazio")
        void deveCriarLoginResponseComTokenVazio() {
            // Given
            String token = "";

            // When
            LoginResponse response = new LoginResponse(token);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.token()).isEmpty();
        }

        @Test
        @DisplayName("Deve criar LoginResponse com token contendo espaços")
        void deveCriarLoginResponseComTokenContendoEspacos() {
            // Given
            String token = "   token with spaces   ";

            // When
            LoginResponse response = new LoginResponse(token);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.token()).isEqualTo(token);
        }
    }

    @Nested
    @DisplayName("Testes de Igualdade e HashCode")
    class EqualityAndHashCodeTests {

        @Test
        @DisplayName("Deve ser igual quando tokens são iguais")
        void deveSerIgualQuandoTokensSaoIguais() {
            // Given
            String token = "jwt-token-123";
            LoginResponse response1 = new LoginResponse(token);
            LoginResponse response2 = new LoginResponse(token);

            // Then
            assertThat(response1).isEqualTo(response2);
            assertThat(response1.hashCode()).isEqualTo(response2.hashCode());
        }

        @Test
        @DisplayName("Deve ser diferente quando tokens são diferentes")
        void deveSerDiferenteQuandoTokensSaoDiferentes() {
            // Given
            LoginResponse response1 = new LoginResponse("token1");
            LoginResponse response2 = new LoginResponse("token2");

            // Then
            assertThat(response1).isNotEqualTo(response2);
        }

        @Test
        @DisplayName("Deve ser igual a si mesmo")
        void deveSerIgualASiMesmo() {
            // Given
            LoginResponse response = new LoginResponse("jwt-token-123");

            // Then
            assertThat(response).isEqualTo(response);
        }

        @Test
        @DisplayName("Não deve ser igual a null")
        void naoDeveSerIgualANull() {
            // Given
            LoginResponse response = new LoginResponse("jwt-token-123");

            // Then
            assertThat(response).isNotEqualTo(null);
        }

        @Test
        @DisplayName("Não deve ser igual a objeto de classe diferente")
        void naoDeveSerIgualAObjetoDeClasseDiferente() {
            // Given
            LoginResponse response = new LoginResponse("jwt-token-123");
            String otherObject = "jwt-token-123";

            // Then
            assertThat(response).isNotEqualTo(otherObject);
        }

        @Test
        @DisplayName("Deve ser igual quando ambos os tokens são nulos")
        void deveSerIgualQuandoAmbosTokensSaoNulos() {
            // Given
            LoginResponse response1 = new LoginResponse(null);
            LoginResponse response2 = new LoginResponse(null);

            // Then
            assertThat(response1).isEqualTo(response2);
            assertThat(response1.hashCode()).isEqualTo(response2.hashCode());
        }

        @Test
        @DisplayName("Deve ser diferente quando um token é nulo e outro não")
        void deveSerDiferenteQuandoUmTokenENuloEOutroNao() {
            // Given
            LoginResponse response1 = new LoginResponse(null);
            LoginResponse response2 = new LoginResponse("jwt-token-123");

            // Then
            assertThat(response1).isNotEqualTo(response2);
            assertThat(response2).isNotEqualTo(response1);
        }
    }

    @Nested
    @DisplayName("Testes de ToString")
    class ToStringTests {

        @Test
        @DisplayName("Deve gerar toString com token")
        void deveGerarToStringComToken() {
            // Given
            String token = "jwt-token-123";
            LoginResponse response = new LoginResponse(token);

            // When
            String toString = response.toString();

            // Then
            assertThat(toString).contains("LoginResponse");
            assertThat(toString).contains(token);
        }

        @Test
        @DisplayName("Deve gerar toString com token nulo")
        void deveGerarToStringComTokenNulo() {
            // Given
            LoginResponse response = new LoginResponse(null);

            // When
            String toString = response.toString();

            // Then
            assertThat(toString).contains("LoginResponse");
            assertThat(toString).contains("null");
        }

        @Test
        @DisplayName("Deve gerar toString com token vazio")
        void deveGerarToStringComTokenVazio() {
            // Given
            LoginResponse response = new LoginResponse("");

            // When
            String toString = response.toString();

            // Then
            assertThat(toString).contains("LoginResponse");
        }
    }

    @Nested
    @DisplayName("Testes de Casos Extremos")
    class EdgeCaseTests {

        @Test
        @DisplayName("Deve aceitar token muito longo")
        void deveAceitarTokenMuitoLongo() {
            // Given
            String longToken = "a".repeat(10000);

            // When
            LoginResponse response = new LoginResponse(longToken);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.token()).isEqualTo(longToken);
            assertThat(response.token()).hasSize(10000);
        }

        @Test
        @DisplayName("Deve aceitar token com caracteres especiais")
        void deveAceitarTokenComCaracteresEspeciais() {
            // Given
            String specialToken = "jwt.token-with_special@chars#123!";

            // When
            LoginResponse response = new LoginResponse(specialToken);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.token()).isEqualTo(specialToken);
        }

        @Test
        @DisplayName("Deve aceitar token com caracteres Unicode")
        void deveAceitarTokenComCaracteresUnicode() {
            // Given
            String unicodeToken = "jwt-tökën-with-ünïcödë-123";

            // When
            LoginResponse response = new LoginResponse(unicodeToken);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.token()).isEqualTo(unicodeToken);
        }

        @Test
        @DisplayName("Deve aceitar token com quebras de linha")
        void deveAceitarTokenComQuebrasDeLinhas() {
            // Given
            String tokenWithNewlines = "jwt\ntoken\rwith\r\nnewlines";

            // When
            LoginResponse response = new LoginResponse(tokenWithNewlines);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.token()).isEqualTo(tokenWithNewlines);
        }

        @Test
        @DisplayName("Deve aceitar token com apenas espaços")
        void deveAceitarTokenComApenasEspacos() {
            // Given
            String spacesToken = "     ";

            // When
            LoginResponse response = new LoginResponse(spacesToken);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.token()).isEqualTo(spacesToken);
        }
    }

    @Nested
    @DisplayName("Testes de Imutabilidade")
    class ImmutabilityTests {

        @Test
        @DisplayName("Deve manter imutabilidade do token")
        void deveManterImutabilidadeDoToken() {
            // Given
            String originalToken = "jwt-token-123";
            LoginResponse response = new LoginResponse(originalToken);

            // When
            String retrievedToken = response.token();

            // Then
            assertThat(retrievedToken).isEqualTo(originalToken);
            assertThat(retrievedToken).isSameAs(originalToken); // String interning
        }

        @Test
        @DisplayName("Deve criar nova instância para cada construção")
        void deveCriarNovaInstanciaParaCadaConstrucao() {
            // Given
            String token = "jwt-token-123";

            // When
            LoginResponse response1 = new LoginResponse(token);
            LoginResponse response2 = new LoginResponse(token);

            // Then
            assertThat(response1).isNotSameAs(response2);
            assertThat(response1).isEqualTo(response2);
        }
    }

    @Nested
    @DisplayName("Testes de Performance")
    class PerformanceTests {

        @Test
        @DisplayName("Deve criar múltiplas instâncias rapidamente")
        void deveCriarMultiplasInstanciasRapidamente() {
            // Given
            String token = "jwt-token-123";
            int iterations = 10000;

            // When
            long startTime = System.nanoTime();
            for (int i = 0; i < iterations; i++) {
                LoginResponse response = new LoginResponse(token + i);
                assertThat(response.token()).isNotNull();
            }
            long endTime = System.nanoTime();

            // Then
            long duration = endTime - startTime;
            assertThat(duration).isLessThan(1_000_000_000L); // Menos de 1 segundo
        }

        @Test
        @DisplayName("Deve ter hashCode consistente")
        void deveTerHashCodeConsistente() {
            // Given
            String token = "jwt-token-123";
            LoginResponse response = new LoginResponse(token);

            // When & Then
            int hashCode1 = response.hashCode();
            int hashCode2 = response.hashCode();
            int hashCode3 = response.hashCode();

            assertThat(hashCode1).isEqualTo(hashCode2);
            assertThat(hashCode2).isEqualTo(hashCode3);
        }
    }
}