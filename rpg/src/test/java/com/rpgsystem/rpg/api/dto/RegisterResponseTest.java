package com.rpgsystem.rpg.api.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("RegisterResponse Tests")
class RegisterResponseTest {

    @Nested
    @DisplayName("Testes de Construção")
    class ConstructionTests {

        @Test
        @DisplayName("Deve criar RegisterResponse com builder")
        void deveCriarRegisterResponseComBuilder() {
            // Given
            String id = "1";
            String name = "John Doe";
            String email = "john.doe@example.com";
            String message = "User registered successfully";

            // When
            RegisterResponse response = RegisterResponse.builder()
                    .id(id)
                    .name(name)
                    .email(email)
                    .message(message)
                    .build();

            // Then
            assertThat(response).isNotNull();
            assertThat(response.getId()).isEqualTo(id);
            assertThat(response.getName()).isEqualTo(name);
            assertThat(response.getEmail()).isEqualTo(email);
            assertThat(response.getMessage()).isEqualTo(message);
        }

        @Test
        @DisplayName("Deve criar RegisterResponse com campos nulos")
        void deveCriarRegisterResponseComCamposNulos() {
            // When
            RegisterResponse response = RegisterResponse.builder()
                    .id(null)
                    .name(null)
                    .email(null)
                    .message(null)
                    .build();

            // Then
            assertThat(response).isNotNull();
            assertThat(response.getId()).isNull();
            assertThat(response.getName()).isNull();
            assertThat(response.getEmail()).isNull();
            assertThat(response.getMessage()).isNull();
        }

        @Test
        @DisplayName("Deve criar RegisterResponse com strings vazias")
        void deveCriarRegisterResponseComStringsVazias() {
            // When
            RegisterResponse response = RegisterResponse.builder()
                    .id("")
                    .name("")
                    .email("")
                    .message("")
                    .build();

            // Then
            assertThat(response.getId()).isEmpty();
            assertThat(response.getName()).isEmpty();
            assertThat(response.getEmail()).isEmpty();
            assertThat(response.getMessage()).isEmpty();
        }

        @Test
        @DisplayName("Deve criar RegisterResponse com strings com espaços")
        void deveCriarRegisterResponseComStringsComEspacos() {
            // Given
            String idWithSpaces = "  1  ";
            String nameWithSpaces = "  John Doe  ";
            String emailWithSpaces = "  john.doe@example.com  ";
            String messageWithSpaces = "  User registered successfully  ";

            // When
            RegisterResponse response = RegisterResponse.builder()
                    .id(idWithSpaces)
                    .name(nameWithSpaces)
                    .email(emailWithSpaces)
                    .message(messageWithSpaces)
                    .build();

            // Then
            assertThat(response.getId()).isEqualTo(idWithSpaces);
            assertThat(response.getName()).isEqualTo(nameWithSpaces);
            assertThat(response.getEmail()).isEqualTo(emailWithSpaces);
            assertThat(response.getMessage()).isEqualTo(messageWithSpaces);
        }
    }

    @Nested
    @DisplayName("Testes de Igualdade e HashCode")
    class EqualityAndHashCodeTests {

        @Test
        @DisplayName("Deve ser igual a si mesmo")
        void deveSerIgualASiMesmo() {
            // Given
            RegisterResponse response = createValidResponse();

            // Then
            assertThat(response).isEqualTo(response);
            assertThat(response.hashCode()).isEqualTo(response.hashCode());
        }

        @Test
        @DisplayName("Deve ser igual a outro objeto com os mesmos valores")
        void deveSerIgualAOutroObjetoComMesmosValores() {
            // Given
            RegisterResponse response1 = createValidResponse();
            RegisterResponse response2 = createValidResponse();

            // Then
            assertThat(response1).isEqualTo(response2);
            assertThat(response1.hashCode()).isEqualTo(response2.hashCode());
        }

        @Test
        @DisplayName("Não deve ser igual a null")
        void naoDeveSerIgualANull() {
            // Given
            RegisterResponse response = createValidResponse();

            // Then
            assertThat(response).isNotEqualTo(null);
        }

        @Test
        @DisplayName("Não deve ser igual a objeto de classe diferente")
        void naoDeveSerIgualAObjetoDeClasseDiferente() {
            // Given
            RegisterResponse response = createValidResponse();
            String differentObject = "different";

            // Then
            assertThat(response).isNotEqualTo(differentObject);
        }

        @Test
        @DisplayName("Não deve ser igual quando id é diferente")
        void naoDeveSerIgualQuandoIdEDiferente() {
            // Given
            RegisterResponse response1 = createValidResponse();
            RegisterResponse response2 = RegisterResponse.builder()
                    .id("999")
                    .name("John Doe")
                    .email("john.doe@example.com")
                    .message("User registered successfully")
                    .build();

            // Then
            assertThat(response1).isNotEqualTo(response2);
        }

        @Test
        @DisplayName("Não deve ser igual quando name é diferente")
        void naoDeveSerIgualQuandoNameEDiferente() {
            // Given
            RegisterResponse response1 = createValidResponse();
            RegisterResponse response2 = RegisterResponse.builder()
                    .id("1")
                    .name("Different Name")
                    .email("john.doe@example.com")
                    .message("User registered successfully")
                    .build();

            // Then
            assertThat(response1).isNotEqualTo(response2);
        }

        @Test
        @DisplayName("Não deve ser igual quando email é diferente")
        void naoDeveSerIgualQuandoEmailEDiferente() {
            // Given
            RegisterResponse response1 = createValidResponse();
            RegisterResponse response2 = RegisterResponse.builder()
                    .id("1")
                    .name("John Doe")
                    .email("different@example.com")
                    .message("User registered successfully")
                    .build();

            // Then
            assertThat(response1).isNotEqualTo(response2);
        }

        @Test
        @DisplayName("Não deve ser igual quando message é diferente")
        void naoDeveSerIgualQuandoMessageEDiferente() {
            // Given
            RegisterResponse response1 = createValidResponse();
            RegisterResponse response2 = RegisterResponse.builder()
                    .id("1")
                    .name("John Doe")
                    .email("john.doe@example.com")
                    .message("Different message")
                    .build();

            // Then
            assertThat(response1).isNotEqualTo(response2);
        }

        @Test
        @DisplayName("Deve ser igual quando todos os campos são nulos")
        void deveSerIgualQuandoTodosCamposSaoNulos() {
            // Given
            RegisterResponse response1 = RegisterResponse.builder()
                    .id(null)
                    .name(null)
                    .email(null)
                    .message(null)
                    .build();
            RegisterResponse response2 = RegisterResponse.builder()
                    .id(null)
                    .name(null)
                    .email(null)
                    .message(null)
                    .build();

            // Then
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
            RegisterResponse response = createValidResponse();

            // When
            String toString = response.toString();

            // Then
            assertThat(toString)
                    .contains("RegisterResponse")
                    .contains("id=1")
                    .contains("name=John Doe")
                    .contains("email=john.doe@example.com")
                    .contains("message=User registered successfully");
        }

        @Test
        @DisplayName("Deve gerar toString com campos nulos")
        void deveGerarToStringComCamposNulos() {
            // Given
            RegisterResponse response = RegisterResponse.builder()
                    .id(null)
                    .name(null)
                    .email(null)
                    .message(null)
                    .build();

            // When
            String toString = response.toString();

            // Then
            assertThat(toString)
                    .contains("RegisterResponse")
                    .contains("id=null")
                    .contains("name=null")
                    .contains("email=null")
                    .contains("message=null");
        }

        @Test
        @DisplayName("Deve gerar toString com strings vazias")
        void deveGerarToStringComStringsVazias() {
            // Given
            RegisterResponse response = RegisterResponse.builder()
                    .id("1")
                    .name("")
                    .email("")
                    .message("")
                    .build();

            // When
            String toString = response.toString();

            // Then
            assertThat(toString)
                    .contains("RegisterResponse")
                    .contains("id=1")
                    .contains("name=")
                    .contains("email=")
                    .contains("message=");
        }

        @Test
        @DisplayName("ToString deve ser consistente")
        void toStringDeveSerConsistente() {
            // Given
            RegisterResponse response = createValidResponse();

            // When
            String toString1 = response.toString();
            String toString2 = response.toString();

            // Then
            assertThat(toString1).isEqualTo(toString2);
        }
    }

    @Nested
    @DisplayName("Testes de Casos Extremos")
    class EdgeCaseTests {

        @Test
        @DisplayName("Deve aceitar id com valor máximo de Long")
        void deveAceitarIdComValorMaximoDeLong() {
            // Given
            String maxId = String.valueOf(Long.MAX_VALUE);

            // When
            RegisterResponse response = RegisterResponse.builder()
                    .id(maxId)
                    .name("Test Name")
                    .email("test@example.com")
                    .message("Test message")
                    .build();

            // Then
            assertThat(response.getId()).isEqualTo(maxId);
        }

        @Test
        @DisplayName("Deve aceitar id com valor mínimo de Long")
        void deveAceitarIdComValorMinimoDeLong() {
            // Given
            String minId = String.valueOf(Long.MIN_VALUE);

            // When
            RegisterResponse response = RegisterResponse.builder()
                    .id(minId)
                    .name("Test Name")
                    .email("test@example.com")
                    .message("Test message")
                    .build();

            // Then
            assertThat(response.getId()).isEqualTo(minId);
        }

        @Test
        @DisplayName("Deve aceitar name muito longo")
        void deveAceitarNameMuitoLongo() {
            // Given
            String longName = "A".repeat(10000);

            // When
            RegisterResponse response = RegisterResponse.builder()
                    .id("1")
                    .name(longName)
                    .email("test@example.com")
                    .message("Test message")
                    .build();

            // Then
            assertThat(response.getName()).isEqualTo(longName);
            assertThat(response.getName()).hasSize(10000);
        }

        @Test
        @DisplayName("Deve aceitar email muito longo")
        void deveAceitarEmailMuitoLongo() {
            // Given
            String longEmail = "a".repeat(100) + "@" + "b".repeat(100) + ".com";

            // When
            RegisterResponse response = RegisterResponse.builder()
                    .id("1")
                    .name("Test")
                    .email(longEmail)
                    .message("Test message")
                    .build();

            // Then
            assertThat(response.getEmail()).isEqualTo(longEmail);
        }

        @Test
        @DisplayName("Deve aceitar message muito longa")
        void deveAceitarMessageMuitoLonga() {
            // Given
            String longMessage = "Message ".repeat(1000);

            // When
            RegisterResponse response = RegisterResponse.builder()
                    .id("1")
                    .name("Test")
                    .email("test@example.com")
                    .message(longMessage)
                    .build();

            // Then
            assertThat(response.getMessage()).isEqualTo(longMessage);
        }

        @Test
        @DisplayName("Deve aceitar caracteres especiais em todos os campos")
        void deveAceitarCaracteresEspeciaisEmTodosCampos() {
            // Given
            String specialName = "José María Ñoño @#$%^&*()";
            String specialEmail = "test+tag@example-domain.co.uk";
            String specialMessage = "Usuário registrado com sucesso! 🎉 @#$%^&*()";

            // When
            RegisterResponse response = RegisterResponse.builder()
                    .id("1")
                    .name(specialName)
                    .email(specialEmail)
                    .message(specialMessage)
                    .build();

            // Then
            assertThat(response.getName()).isEqualTo(specialName);
            assertThat(response.getEmail()).isEqualTo(specialEmail);
            assertThat(response.getMessage()).isEqualTo(specialMessage);
        }

        @Test
        @DisplayName("Deve aceitar caracteres Unicode")
        void deveAceitarCaracteresUnicode() {
            // Given
            String unicodeName = "用户名 🌟 Пользователь";
            String unicodeEmail = "用户@example.com";
            String unicodeMessage = "注册成功 ✅ Регистрация успешна";

            // When
            RegisterResponse response = RegisterResponse.builder()
                    .id("1")
                    .name(unicodeName)
                    .email(unicodeEmail)
                    .message(unicodeMessage)
                    .build();

            // Then
            assertThat(response.getName()).isEqualTo(unicodeName);
            assertThat(response.getEmail()).isEqualTo(unicodeEmail);
            assertThat(response.getMessage()).isEqualTo(unicodeMessage);
        }

        @Test
        @DisplayName("Deve aceitar quebras de linha em message")
        void deveAceitarQuebrasDeLinhaEmMessage() {
            // Given
            String messageWithNewlines = "Line 1\nLine 2\r\nLine 3\tTabbed";

            // When
            RegisterResponse response = RegisterResponse.builder()
                    .id("1")
                    .name("Test")
                    .email("test@example.com")
                    .message(messageWithNewlines)
                    .build();

            // Then
            assertThat(response.getMessage()).isEqualTo(messageWithNewlines);
        }

        @Test
        @DisplayName("Deve aceitar apenas espaços em strings")
        void deveAceitarApenasEspacosEmStrings() {
            // Given
            String onlySpaces = "     ";

            // When
            RegisterResponse response = RegisterResponse.builder()
                    .id("1")
                    .name(onlySpaces)
                    .email(onlySpaces)
                    .message(onlySpaces)
                    .build();

            // Then
            assertThat(response.getName()).isEqualTo(onlySpaces);
            assertThat(response.getEmail()).isEqualTo(onlySpaces);
            assertThat(response.getMessage()).isEqualTo(onlySpaces);
        }
    }

    @Nested
    @DisplayName("Testes de Imutabilidade e Performance")
    class ImmutabilityAndPerformanceTests {

        @Test
        @DisplayName("Deve criar nova instância com dados diferentes")
        void deveCriarNovaInstanciaComDadosDiferentes() {
            // Given
            RegisterResponse originalResponse = createValidResponse();
            String newId = "999";
            String newName = "New Name";
            String newEmail = "new@example.com";
            String newMessage = "New message";

            // When
            RegisterResponse newResponse = RegisterResponse.builder()
                    .id(newId)
                    .name(newName)
                    .email(newEmail)
                    .message(newMessage)
                    .build();

            // Then
            assertThat(newResponse.getId()).isEqualTo(newId);
            assertThat(newResponse.getName()).isEqualTo(newName);
            assertThat(newResponse.getEmail()).isEqualTo(newEmail);
            assertThat(newResponse.getMessage()).isEqualTo(newMessage);
            assertThat(newResponse).isNotEqualTo(originalResponse);
        }

        @Test
        @DisplayName("Deve criar múltiplas instâncias rapidamente")
        void deveCriarMultiplasInstanciasRapidamente() {
            // Given
            int iterations = 10000;

            // When
            long startTime = System.nanoTime();
            for (int i = 0; i < iterations; i++) {
                RegisterResponse response = RegisterResponse.builder()
                        .id(String.valueOf(i))
                        .name("User " + i)
                        .email("user" + i + "@example.com")
                        .message("Message " + i)
                        .build();
                
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
            RegisterResponse response1 = createValidResponse();
            RegisterResponse response2 = createValidResponse();
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
    }

    private RegisterResponse createValidResponse() {
        return RegisterResponse.builder()
                .id("1")
                .name("John Doe")
                .email("john.doe@example.com")
                .message("User registered successfully")
                .build();
    }
}