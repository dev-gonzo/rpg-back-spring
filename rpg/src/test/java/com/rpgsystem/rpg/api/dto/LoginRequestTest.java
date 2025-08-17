package com.rpgsystem.rpg.api.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LoginRequest Tests")
class LoginRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Nested
    @DisplayName("Testes de Construção")
    class ConstructionTests {

        @Test
        @DisplayName("Deve criar LoginRequest com dados válidos")
        void deveCriarLoginRequestComDadosValidos() {
            // Given
            String email = "test@example.com";
            String password = "password123";

            // When
            LoginRequest request = new LoginRequest(email, password);

            // Then
            assertThat(request).isNotNull();
            assertThat(request.email()).isEqualTo(email);
            assertThat(request.password()).isEqualTo(password);
        }

        @Test
        @DisplayName("Deve criar LoginRequest com email e senha nulos")
        void deveCriarLoginRequestComEmailESenhaNulos() {
            // When
            LoginRequest request = new LoginRequest(null, null);

            // Then
            assertThat(request).isNotNull();
            assertThat(request.email()).isNull();
            assertThat(request.password()).isNull();
        }

        @Test
        @DisplayName("Deve criar LoginRequest com strings vazias")
        void deveCriarLoginRequestComStringsVazias() {
            // Given
            String email = "";
            String password = "";

            // When
            LoginRequest request = new LoginRequest(email, password);

            // Then
            assertThat(request).isNotNull();
            assertThat(request.email()).isEmpty();
            assertThat(request.password()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Testes de Validação")
    class ValidationTests {

        @Test
        @DisplayName("Deve validar com sucesso quando todos os campos são válidos")
        void deveValidarComSucessoQuandoTodosCamposSaoValidos() {
            // Given
            LoginRequest request = new LoginRequest("test@example.com", "password123");

            // When
            Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

            // Then
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Deve falhar validação quando email é nulo")
        void deveFalharValidacaoQuandoEmailENulo() {
            // Given
            LoginRequest request = new LoginRequest(null, "password123");

            // When
            Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

            // Then
            assertThat(violations).hasSize(1);
            ConstraintViolation<LoginRequest> violation = violations.iterator().next();
            assertThat(violation.getPropertyPath().toString()).isEqualTo("email");
            assertThat(violation.getMessage()).isEqualTo("E-mail is required");
        }

        @Test
        @DisplayName("Deve falhar validação quando password é nulo")
        void deveFalharValidacaoQuandoPasswordENulo() {
            // Given
            LoginRequest request = new LoginRequest("test@example.com", null);

            // When
            Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

            // Then
            assertThat(violations).hasSize(1);
            ConstraintViolation<LoginRequest> violation = violations.iterator().next();
            assertThat(violation.getPropertyPath().toString()).isEqualTo("password");
            assertThat(violation.getMessage()).isEqualTo("Password is required");
        }

        @Test
        @DisplayName("Deve falhar validação quando ambos os campos são nulos")
        void deveFalharValidacaoQuandoAmbosCamposSaoNulos() {
            // Given
            LoginRequest request = new LoginRequest(null, null);

            // When
            Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

            // Then
            assertThat(violations).hasSize(2);
            assertThat(violations)
                    .extracting(ConstraintViolation::getPropertyPath)
                    .extracting(Object::toString)
                    .containsExactlyInAnyOrder("email", "password");
        }

        @Test
        @DisplayName("Deve aceitar strings vazias na validação")
        void deveAceitarStringsVaziasNaValidacao() {
            // Given
            LoginRequest request = new LoginRequest("", "");

            // When
            Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

            // Then
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Deve aceitar strings com espaços em branco")
        void deveAceitarStringsComEspacosEmBranco() {
            // Given
            LoginRequest request = new LoginRequest("   ", "   ");

            // When
            Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

            // Then
            assertThat(violations).isEmpty();
        }
    }

    @Nested
    @DisplayName("Testes de Igualdade e HashCode")
    class EqualityAndHashCodeTests {

        @Test
        @DisplayName("Deve ser igual quando todos os campos são iguais")
        void deveSerIgualQuandoTodosCamposSaoIguais() {
            // Given
            LoginRequest request1 = new LoginRequest("test@example.com", "password123");
            LoginRequest request2 = new LoginRequest("test@example.com", "password123");

            // Then
            assertThat(request1).isEqualTo(request2);
            assertThat(request1.hashCode()).isEqualTo(request2.hashCode());
        }

        @Test
        @DisplayName("Deve ser diferente quando email é diferente")
        void deveSerDiferenteQuandoEmailEDiferente() {
            // Given
            LoginRequest request1 = new LoginRequest("test1@example.com", "password123");
            LoginRequest request2 = new LoginRequest("test2@example.com", "password123");

            // Then
            assertThat(request1).isNotEqualTo(request2);
        }

        @Test
        @DisplayName("Deve ser diferente quando password é diferente")
        void deveSerDiferenteQuandoPasswordEDiferente() {
            // Given
            LoginRequest request1 = new LoginRequest("test@example.com", "password123");
            LoginRequest request2 = new LoginRequest("test@example.com", "password456");

            // Then
            assertThat(request1).isNotEqualTo(request2);
        }

        @Test
        @DisplayName("Deve ser igual a si mesmo")
        void deveSerIgualASiMesmo() {
            // Given
            LoginRequest request = new LoginRequest("test@example.com", "password123");

            // Then
            assertThat(request).isEqualTo(request);
        }

        @Test
        @DisplayName("Não deve ser igual a null")
        void naoDeveSerIgualANull() {
            // Given
            LoginRequest request = new LoginRequest("test@example.com", "password123");

            // Then
            assertThat(request).isNotEqualTo(null);
        }

        @Test
        @DisplayName("Não deve ser igual a objeto de classe diferente")
        void naoDeveSerIgualAObjetoDeClasseDiferente() {
            // Given
            LoginRequest request = new LoginRequest("test@example.com", "password123");
            String otherObject = "test@example.com";

            // Then
            assertThat(request).isNotEqualTo(otherObject);
        }
    }

    @Nested
    @DisplayName("Testes de ToString")
    class ToStringTests {

        @Test
        @DisplayName("Deve gerar toString com todos os campos")
        void deveGerarToStringComTodosCampos() {
            // Given
            LoginRequest request = new LoginRequest("test@example.com", "password123");

            // When
            String toString = request.toString();

            // Then
            assertThat(toString).contains("LoginRequest");
            assertThat(toString).contains("test@example.com");
            assertThat(toString).contains("password123");
        }

        @Test
        @DisplayName("Deve gerar toString com campos nulos")
        void deveGerarToStringComCamposNulos() {
            // Given
            LoginRequest request = new LoginRequest(null, null);

            // When
            String toString = request.toString();

            // Then
            assertThat(toString).contains("LoginRequest");
            assertThat(toString).contains("null");
        }
    }

    @Nested
    @DisplayName("Testes de Casos Extremos")
    class EdgeCaseTests {

        @Test
        @DisplayName("Deve aceitar email muito longo")
        void deveAceitarEmailMuitoLongo() {
            // Given
            String longEmail = "a".repeat(1000) + "@example.com";
            LoginRequest request = new LoginRequest(longEmail, "password123");

            // When
            Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

            // Then
            assertThat(violations).isEmpty();
            assertThat(request.email()).isEqualTo(longEmail);
        }

        @Test
        @DisplayName("Deve aceitar password muito longo")
        void deveAceitarPasswordMuitoLongo() {
            // Given
            String longPassword = "a".repeat(1000);
            LoginRequest request = new LoginRequest("test@example.com", longPassword);

            // When
            Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

            // Then
            assertThat(violations).isEmpty();
            assertThat(request.password()).isEqualTo(longPassword);
        }

        @Test
        @DisplayName("Deve aceitar caracteres especiais no email")
        void deveAceitarCaracteresEspeciaisNoEmail() {
            // Given
            String specialEmail = "test+special@example-domain.co.uk";
            LoginRequest request = new LoginRequest(specialEmail, "password123");

            // When
            Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

            // Then
            assertThat(violations).isEmpty();
            assertThat(request.email()).isEqualTo(specialEmail);
        }

        @Test
        @DisplayName("Deve aceitar caracteres especiais na password")
        void deveAceitarCaracteresEspeciaisNaPassword() {
            // Given
            String specialPassword = "p@ssw0rd!@#$%^&*()";
            LoginRequest request = new LoginRequest("test@example.com", specialPassword);

            // When
            Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

            // Then
            assertThat(violations).isEmpty();
            assertThat(request.password()).isEqualTo(specialPassword);
        }

        @Test
        @DisplayName("Deve aceitar caracteres Unicode")
        void deveAceitarCaracteresUnicode() {
            // Given
            String unicodeEmail = "tëst@éxämplé.com";
            String unicodePassword = "pässwörd123";
            LoginRequest request = new LoginRequest(unicodeEmail, unicodePassword);

            // When
            Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

            // Then
            assertThat(violations).isEmpty();
            assertThat(request.email()).isEqualTo(unicodeEmail);
            assertThat(request.password()).isEqualTo(unicodePassword);
        }
    }
}