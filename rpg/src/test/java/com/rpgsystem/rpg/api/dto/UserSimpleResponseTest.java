package com.rpgsystem.rpg.api.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserSimpleResponse Tests")
class UserSimpleResponseTest {

    @Nested
    @DisplayName("Testes de Construção")
    class ConstructionTests {

        @Test
        @DisplayName("Deve criar UserSimpleResponse vazio")
        void deveCriarUserSimpleResponseVazio() {
            // When
            UserSimpleResponse response = new UserSimpleResponse();

            // Then
            assertThat(response).isNotNull();
            assertThat(response.getId()).isNull();
            assertThat(response.getName()).isNull();
            assertThat(response.getEmail()).isNull();
        }

        @Test
        @DisplayName("Deve definir e obter todos os campos")
        void deveDefinirEObterTodosCampos() {
            // Given
            UserSimpleResponse response = new UserSimpleResponse();
            String id = "1";
            String name = "John Doe";
            String email = "john.doe@example.com";

            // When
            response.setId(id);
            response.setName(name);
            response.setEmail(email);

            // Then
            assertThat(response.getId()).isEqualTo(id);
            assertThat(response.getName()).isEqualTo(name);
            assertThat(response.getEmail()).isEqualTo(email);
        }

        @Test
        @DisplayName("Deve aceitar valores nulos")
        void deveAceitarValoresNulos() {
            // Given
            UserSimpleResponse response = new UserSimpleResponse();

            // When
            response.setId(null);
            response.setName(null);
            response.setEmail(null);

            // Then
            assertThat(response.getId()).isNull();
            assertThat(response.getName()).isNull();
            assertThat(response.getEmail()).isNull();
        }

        @Test
        @DisplayName("Deve aceitar strings vazias")
        void deveAceitarStringsVazias() {
            // Given
            UserSimpleResponse response = new UserSimpleResponse();

            // When
            response.setName("");
            response.setEmail("");

            // Then
            assertThat(response.getName()).isEmpty();
            assertThat(response.getEmail()).isEmpty();
        }

        @Test
        @DisplayName("Deve aceitar strings com espaços")
        void deveAceitarStringsComEspacos() {
            // Given
            UserSimpleResponse response = new UserSimpleResponse();
            String nameWithSpaces = "  John Doe  ";
            String emailWithSpaces = "  john.doe@example.com  ";

            // When
            response.setName(nameWithSpaces);
            response.setEmail(emailWithSpaces);

            // Then
            assertThat(response.getName()).isEqualTo(nameWithSpaces);
            assertThat(response.getEmail()).isEqualTo(emailWithSpaces);
        }
    }

    @Nested
    @DisplayName("Testes de Igualdade e HashCode")
    class EqualityAndHashCodeTests {

        @Test
        @DisplayName("Deve ser igual a si mesmo")
        void deveSerIgualASiMesmo() {
            // Given
            UserSimpleResponse response = createValidResponse();

            // Then
            assertThat(response).isEqualTo(response);
            assertThat(response.hashCode()).isEqualTo(response.hashCode());
        }

        @Test
        @DisplayName("Deve ser igual a outro objeto com os mesmos valores")
        void deveSerIgualAOutroObjetoComMesmosValores() {
            // Given
            UserSimpleResponse response1 = createValidResponse();
            UserSimpleResponse response2 = createValidResponse();

            // Then
            assertThat(response1).isEqualTo(response2);
            assertThat(response1.hashCode()).isEqualTo(response2.hashCode());
        }

        @Test
        @DisplayName("Não deve ser igual a null")
        void naoDeveSerIgualANull() {
            // Given
            UserSimpleResponse response = createValidResponse();

            // Then
            assertThat(response).isNotEqualTo(null);
        }

        @Test
        @DisplayName("Não deve ser igual a objeto de classe diferente")
        void naoDeveSerIgualAObjetoDeClasseDiferente() {
            // Given
            UserSimpleResponse response = createValidResponse();
            String differentObject = "different";

            // Then
            assertThat(response).isNotEqualTo(differentObject);
        }

        @Test
        @DisplayName("Não deve ser igual quando id é diferente")
        void naoDeveSerIgualQuandoIdEDiferente() {
            // Given
            UserSimpleResponse response1 = createValidResponse();
            UserSimpleResponse response2 = createValidResponse();
            response2.setId("999");

            // Then
            assertThat(response1).isNotEqualTo(response2);
        }

        @Test
        @DisplayName("Não deve ser igual quando name é diferente")
        void naoDeveSerIgualQuandoNameEDiferente() {
            // Given
            UserSimpleResponse response1 = createValidResponse();
            UserSimpleResponse response2 = createValidResponse();
            response2.setName("Different Name");

            // Then
            assertThat(response1).isNotEqualTo(response2);
        }

        @Test
        @DisplayName("Não deve ser igual quando email é diferente")
        void naoDeveSerIgualQuandoEmailEDiferente() {
            // Given
            UserSimpleResponse response1 = createValidResponse();
            UserSimpleResponse response2 = createValidResponse();
            response2.setEmail("different@example.com");

            // Then
            assertThat(response1).isNotEqualTo(response2);
        }

        @Test
        @DisplayName("Deve ser igual quando todos os campos são nulos")
        void deveSerIgualQuandoTodosCamposSaoNulos() {
            // Given
            UserSimpleResponse response1 = new UserSimpleResponse();
            UserSimpleResponse response2 = new UserSimpleResponse();

            // Then
            assertThat(response1).isEqualTo(response2);
            assertThat(response1.hashCode()).isEqualTo(response2.hashCode());
        }

        @Test
        @DisplayName("Deve ter hashCode consistente")
        void deveTerHashCodeConsistente() {
            // Given
            UserSimpleResponse response = createValidResponse();

            // When
            int hashCode1 = response.hashCode();
            int hashCode2 = response.hashCode();

            // Then
            assertThat(hashCode1).isEqualTo(hashCode2);
        }

        @Test
        @DisplayName("Objetos iguais devem ter mesmo hashCode")
        void objetosIguaisDevemTerMesmoHashCode() {
            // Given
            UserSimpleResponse response1 = createValidResponse();
            UserSimpleResponse response2 = createValidResponse();

            // When & Then
            assertThat(response1).isEqualTo(response2);
            assertThat(response1.hashCode()).isEqualTo(response2.hashCode());
        }
    }

    @Nested
    @DisplayName("Testes de ToString")
    class ToStringTests {

        @Test
        @DisplayName("Deve gerar toString com todos os campos")
        void deveGerarToStringComTodosCampos() {
            // Given
            UserSimpleResponse response = createValidResponse();

            // When
            String toString = response.toString();

            // Then
            assertThat(toString)
                    .contains("UserSimpleResponse")
                    .contains("id=1")
                    .contains("name=John Doe")
                    .contains("email=john.doe@example.com");
        }

        @Test
        @DisplayName("Deve gerar toString com campos nulos")
        void deveGerarToStringComCamposNulos() {
            // Given
            UserSimpleResponse response = new UserSimpleResponse();

            // When
            String toString = response.toString();

            // Then
            assertThat(toString)
                    .contains("UserSimpleResponse")
                    .contains("id=null")
                    .contains("name=null")
                    .contains("email=null");
        }

        @Test
        @DisplayName("Deve gerar toString com strings vazias")
        void deveGerarToStringComStringsVazias() {
            // Given
            UserSimpleResponse response = new UserSimpleResponse();
            response.setId("1");
            response.setName("");
            response.setEmail("");

            // When
            String toString = response.toString();

            // Then
            assertThat(toString)
                    .contains("UserSimpleResponse")
                    .contains("id=1")
                    .contains("name=")
                    .contains("email=");
        }

        @Test
        @DisplayName("ToString deve ser consistente")
        void toStringDeveSerConsistente() {
            // Given
            UserSimpleResponse response = createValidResponse();

            // When
            String toString1 = response.toString();
            String toString2 = response.toString();

            // Then
            assertThat(toString1).isEqualTo(toString2);
        }

        @Test
        @DisplayName("ToString deve incluir informações relevantes")
        void toStringDeveIncluirInformacoesRelevantes() {
            // Given
            UserSimpleResponse response = new UserSimpleResponse();
            response.setId("42");
            response.setName("Test User");
            response.setEmail("test@domain.com");

            // When
            String toString = response.toString();

            // Then
            assertThat(toString)
                    .contains("42")
                    .contains("Test User")
                    .contains("test@domain.com");
        }
    }

    @Nested
    @DisplayName("Testes de Casos Extremos")
    class EdgeCaseTests {

        @Test
        @DisplayName("Deve aceitar id com valor máximo de Long")
        void deveAceitarIdComValorMaximoDeLong() {
            // Given
            UserSimpleResponse response = new UserSimpleResponse();
            String maxId = String.valueOf(Long.MAX_VALUE);

            // When
            response.setId(maxId);

            // Then
            assertThat(response.getId()).isEqualTo(maxId);
        }

        @Test
        @DisplayName("Deve aceitar id com valor mínimo de Long")
        void deveAceitarIdComValorMinimoDeLong() {
            // Given
            UserSimpleResponse response = new UserSimpleResponse();
            String minId = String.valueOf(Long.MIN_VALUE);

            // When
            response.setId(minId);

            // Then
            assertThat(response.getId()).isEqualTo(minId);
        }

        @Test
        @DisplayName("Deve aceitar id zero")
        void deveAceitarIdZero() {
            // Given
            UserSimpleResponse response = new UserSimpleResponse();
            String zeroId = "0";

            // When
            response.setId(zeroId);

            // Then
            assertThat(response.getId()).isEqualTo(zeroId);
        }

        @Test
        @DisplayName("Deve aceitar name muito longo")
        void deveAceitarNameMuitoLongo() {
            // Given
            UserSimpleResponse response = new UserSimpleResponse();
            String longName = "A".repeat(10000);

            // When
            response.setName(longName);

            // Then
            assertThat(response.getName()).isEqualTo(longName);
            assertThat(response.getName()).hasSize(10000);
        }

        @Test
        @DisplayName("Deve aceitar email muito longo")
        void deveAceitarEmailMuitoLongo() {
            // Given
            UserSimpleResponse response = new UserSimpleResponse();
            String longEmail = "a".repeat(100) + "@" + "b".repeat(100) + ".com";

            // When
            response.setEmail(longEmail);

            // Then
            assertThat(response.getEmail()).isEqualTo(longEmail);
        }

        @Test
        @DisplayName("Deve aceitar caracteres especiais em name")
        void deveAceitarCaracteresEspeciaisEmName() {
            // Given
            UserSimpleResponse response = new UserSimpleResponse();
            String specialName = "José María Ñoño @#$%^&*()";

            // When
            response.setName(specialName);

            // Then
            assertThat(response.getName()).isEqualTo(specialName);
        }

        @Test
        @DisplayName("Deve aceitar caracteres especiais em email")
        void deveAceitarCaracteresEspeciaisEmEmail() {
            // Given
            UserSimpleResponse response = new UserSimpleResponse();
            String specialEmail = "test+tag@example-domain.co.uk";

            // When
            response.setEmail(specialEmail);

            // Then
            assertThat(response.getEmail()).isEqualTo(specialEmail);
        }

        @Test
        @DisplayName("Deve aceitar caracteres Unicode")
        void deveAceitarCaracteresUnicode() {
            // Given
            UserSimpleResponse response = new UserSimpleResponse();
            String unicodeName = "用户名 🌟 Пользователь";
            String unicodeEmail = "用户@example.com";

            // When
            response.setName(unicodeName);
            response.setEmail(unicodeEmail);

            // Then
            assertThat(response.getName()).isEqualTo(unicodeName);
            assertThat(response.getEmail()).isEqualTo(unicodeEmail);
        }

        @Test
        @DisplayName("Deve aceitar quebras de linha em name")
        void deveAceitarQuebrasDeLinhaEmName() {
            // Given
            UserSimpleResponse response = new UserSimpleResponse();
            String nameWithNewlines = "First Line\nSecond Line\r\nThird Line";

            // When
            response.setName(nameWithNewlines);

            // Then
            assertThat(response.getName()).isEqualTo(nameWithNewlines);
        }

        @Test
        @DisplayName("Deve aceitar apenas espaços em strings")
        void deveAceitarApenasEspacosEmStrings() {
            // Given
            UserSimpleResponse response = new UserSimpleResponse();
            String onlySpaces = "     ";

            // When
            response.setName(onlySpaces);
            response.setEmail(onlySpaces);

            // Then
            assertThat(response.getName()).isEqualTo(onlySpaces);
            assertThat(response.getEmail()).isEqualTo(onlySpaces);
        }

        @Test
        @DisplayName("Deve aceitar strings com tabs e caracteres de controle")
        void deveAceitarStringsComTabsECaracteresDeControle() {
            // Given
            UserSimpleResponse response = new UserSimpleResponse();
            String nameWithTabs = "Name\tWith\tTabs";
            String emailWithTabs = "email\t@\texample.com";

            // When
            response.setName(nameWithTabs);
            response.setEmail(emailWithTabs);

            // Then
            assertThat(response.getName()).isEqualTo(nameWithTabs);
            assertThat(response.getEmail()).isEqualTo(emailWithTabs);
        }
    }

    @Nested
    @DisplayName("Testes de Imutabilidade e Performance")
    class ImmutabilityAndPerformanceTests {

        @Test
        @DisplayName("Deve permitir modificação após criação")
        void devePermitirModificacaoAposCriacao() {
            // Given
            UserSimpleResponse response = createValidResponse();
            String newId = "999";
            String newName = "New Name";
            String newEmail = "new@example.com";

            // When
            response.setId(newId);
            response.setName(newName);
            response.setEmail(newEmail);

            // Then
            assertThat(response.getId()).isEqualTo(newId);
            assertThat(response.getName()).isEqualTo(newName);
            assertThat(response.getEmail()).isEqualTo(newEmail);
        }

        @Test
        @DisplayName("Deve criar múltiplas instâncias rapidamente")
        void deveCriarMultiplasInstanciasRapidamente() {
            // Given
            int iterations = 10000;

            // When
            long startTime = System.nanoTime();
            for (int i = 0; i < iterations; i++) {
                UserSimpleResponse response = new UserSimpleResponse();
                response.setId(String.valueOf(i));
                response.setName("User " + i);
                response.setEmail("user" + i + "@example.com");
                
                // Verificação básica
                assertThat(response.getId()).isEqualTo(String.valueOf(i));
            }
            long endTime = System.nanoTime();

            // Then
            long duration = endTime - startTime;
            assertThat(duration).isLessThan(1_000_000_000L); // Menos de 1 segundo
        }

        @Test
        @DisplayName("Deve ter performance consistente em operações de igualdade")
        void deveTerPerformanceConsistenteEmOperacoesDeIgualdade() {
            // Given
            UserSimpleResponse response1 = createValidResponse();
            UserSimpleResponse response2 = createValidResponse();
            int iterations = 100000;

            // When
            long startTime = System.nanoTime();
            for (int i = 0; i < iterations; i++) {
                boolean equals = response1.equals(response2);
                int hashCode1 = response1.hashCode();
                int hashCode2 = response2.hashCode();
                
                assertThat(equals).isTrue();
                assertThat(hashCode1).isEqualTo(hashCode2);
            }
            long endTime = System.nanoTime();

            // Then
            long duration = endTime - startTime;
            assertThat(duration).isLessThan(1_000_000_000L); // Menos de 1 segundo
        }

        @Test
        @DisplayName("Deve ter performance consistente em operações toString")
        void deveTerPerformanceConsistenteEmOperacoesToString() {
            // Given
            UserSimpleResponse response = createValidResponse();
            int iterations = 10000;

            // When
            long startTime = System.nanoTime();
            for (int i = 0; i < iterations; i++) {
                String toString = response.toString();
                assertThat(toString).isNotNull().isNotEmpty();
            }
            long endTime = System.nanoTime();

            // Then
            long duration = endTime - startTime;
            assertThat(duration).isLessThan(1_000_000_000L); // Menos de 1 segundo
        }
    }

    @Nested
    @DisplayName("Testes de Cenários de Uso")
    class UsageScenarioTests {

        @Test
        @DisplayName("Deve representar usuário típico")
        void deveRepresentarUsuarioTipico() {
            // Given
            UserSimpleResponse response = new UserSimpleResponse();
            response.setId("123");
            response.setName("Maria Silva");
            response.setEmail("maria.silva@empresa.com.br");

            // Then
            assertThat(response.getId()).isNotNull();
            assertThat(response.getName()).contains(" "); // Nome completo
            assertThat(response.getEmail()).contains("@").contains(".");
        }

        @Test
        @DisplayName("Deve representar usuário com nome simples")
        void deveRepresentarUsuarioComNomeSimples() {
            // Given
            UserSimpleResponse response = new UserSimpleResponse();
            response.setId("456");
            response.setName("João");
            response.setEmail("joao@gmail.com");

            // Then
            assertThat(response.getId()).isNotNull();
            assertThat(response.getName()).doesNotContain(" "); // Nome simples
            assertThat(response.getEmail()).contains("@gmail.com");
        }

        @Test
        @DisplayName("Deve representar usuário internacional")
        void deveRepresentarUsuarioInternacional() {
            // Given
            UserSimpleResponse response = new UserSimpleResponse();
            response.setId("789");
            response.setName("François Müller");
            response.setEmail("francois.muller@example.de");

            // Then
            assertThat(response.getId()).isNotNull();
            assertThat(response.getName()).contains("ç").contains("ü"); // Caracteres especiais
            assertThat(response.getEmail()).endsWith(".de");
        }

        @Test
        @DisplayName("Deve ser adequado para serialização JSON")
        void deveSerAdequadoParaSerializacaoJSON() {
            // Given
            UserSimpleResponse response = createValidResponse();

            // Then - Verifica se todos os campos são acessíveis via getters
            assertThat(response.getId()).isNotNull();
            assertThat(response.getName()).isNotNull();
            assertThat(response.getEmail()).isNotNull();
            
            // Verifica se toString não quebra com caracteres especiais
            String toString = response.toString();
            assertThat(toString).isNotNull().isNotEmpty();
        }
    }

    private UserSimpleResponse createValidResponse() {
        UserSimpleResponse response = new UserSimpleResponse();
        response.setId("1");
        response.setName("John Doe");
        response.setEmail("john.doe@example.com");
        return response;
    }
}