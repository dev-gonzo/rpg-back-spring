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

@DisplayName("RegisterRequest Tests")
class RegisterRequestTest {

    private Validator validator;
    private RegisterRequest validRequest;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        validRequest = new RegisterRequest();
        validRequest.setName("John Doe");
        validRequest.setEmail("john.doe@example.com");
        validRequest.setPassword("password123");
        validRequest.setCountry("Brasil");
        validRequest.setCity("São Paulo");
    }

    @Nested
    @DisplayName("Testes de Construção")
    class ConstructionTests {

        @Test
        @DisplayName("Deve criar RegisterRequest vazio")
        void deveCriarRegisterRequestVazio() {
            // When
            RegisterRequest request = new RegisterRequest();

            // Then
            assertThat(request).isNotNull();
            assertThat(request.getName()).isNull();
            assertThat(request.getEmail()).isNull();
            assertThat(request.getPassword()).isNull();
            assertThat(request.getCountry()).isNull();
            assertThat(request.getCity()).isNull();
        }

        @Test
        @DisplayName("Deve definir e obter todos os campos")
        void deveDefinirEObterTodosCampos() {
            // Given
            RegisterRequest request = new RegisterRequest();
            String name = "Test User";
            String email = "test@example.com";
            String password = "password123";
            String country = "Brasil";
            String city = "São Paulo";

            // When
            request.setName(name);
            request.setEmail(email);
            request.setPassword(password);
            request.setCountry(country);
            request.setCity(city);

            // Then
            assertThat(request.getName()).isEqualTo(name);
            assertThat(request.getEmail()).isEqualTo(email);
            assertThat(request.getPassword()).isEqualTo(password);
            assertThat(request.getCountry()).isEqualTo(country);
            assertThat(request.getCity()).isEqualTo(city);
        }
    }

    @Nested
    @DisplayName("Testes de Validação - Campos Obrigatórios")
    class RequiredFieldsValidationTests {

        @Test
        @DisplayName("Deve validar com sucesso quando todos os campos obrigatórios são válidos")
        void deveValidarComSucessoQuandoTodosCamposObrigatoriosSaoValidos() {
            // When
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(validRequest);

            // Then
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Deve falhar validação quando name é nulo")
        void deveFalharValidacaoQuandoNameENulo() {
            // Given
            validRequest.setName(null);

            // When
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(validRequest);

            // Then
            assertThat(violations).hasSize(1);
            ConstraintViolation<RegisterRequest> violation = violations.iterator().next();
            assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
            assertThat(violation.getMessage()).isEqualTo("Name is required");
        }

        @Test
        @DisplayName("Deve falhar validação quando name é vazio")
        void deveFalharValidacaoQuandoNameEVazio() {
            // Given
            validRequest.setName("");

            // When
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(validRequest);

            // Then
            assertThat(violations).hasSize(2);
            assertThat(violations).extracting(ConstraintViolation::getMessage)
                .containsExactlyInAnyOrder("Name is required", "Name must be between 2 and 100 characters");
        }

        @Test
        @DisplayName("Deve falhar validação quando name contém apenas espaços")
        void deveFalharValidacaoQuandoNameContemApenasEspacos() {
            // Given
            validRequest.setName("   ");

            // When
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(validRequest);

            // Then
            assertThat(violations).hasSize(1);
            ConstraintViolation<RegisterRequest> violation = violations.iterator().next();
            assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
            assertThat(violation.getMessage()).isEqualTo("Name is required");
        }

        @Test
        @DisplayName("Deve falhar validação quando email é nulo")
        void deveFalharValidacaoQuandoEmailENulo() {
            // Given
            validRequest.setEmail(null);

            // When
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(validRequest);

            // Then
            assertThat(violations).hasSize(1);
            ConstraintViolation<RegisterRequest> violation = violations.iterator().next();
            assertThat(violation.getPropertyPath().toString()).isEqualTo("email");
            assertThat(violation.getMessage()).isEqualTo("Email is required");
        }

        @Test
        @DisplayName("Deve falhar validação quando password é nulo")
        void deveFalharValidacaoQuandoPasswordENulo() {
            // Given
            validRequest.setPassword(null);

            // When
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(validRequest);

            // Then
            assertThat(violations).hasSize(1);
            ConstraintViolation<RegisterRequest> violation = violations.iterator().next();
            assertThat(violation.getPropertyPath().toString()).isEqualTo("password");
            assertThat(violation.getMessage()).isEqualTo("Password is required");
        }

        @Test
        @DisplayName("Deve falhar validação quando country é nulo")
        void deveFalharValidacaoQuandoCountryENulo() {
            // Given
            validRequest.setCountry(null);

            // When
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(validRequest);

            // Then
            assertThat(violations).hasSize(1);
            ConstraintViolation<RegisterRequest> violation = violations.iterator().next();
            assertThat(violation.getPropertyPath().toString()).isEqualTo("country");
            assertThat(violation.getMessage()).isEqualTo("Country is required");
        }
    }

    @Nested
    @DisplayName("Testes de Validação - Tamanhos")
    class SizeValidationTests {

        @Test
        @DisplayName("Deve falhar validação quando name tem menos de 2 caracteres")
        void deveFalharValidacaoQuandoNameTemMenosDe2Caracteres() {
            // Given
            validRequest.setName("A");

            // When
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(validRequest);

            // Then
            assertThat(violations).hasSize(1);
            ConstraintViolation<RegisterRequest> violation = violations.iterator().next();
            assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
            assertThat(violation.getMessage()).isEqualTo("Name must be between 2 and 100 characters");
        }

        @Test
        @DisplayName("Deve falhar validação quando name tem mais de 100 caracteres")
        void deveFalharValidacaoQuandoNameTemMaisDe100Caracteres() {
            // Given
            validRequest.setName("A".repeat(101));

            // When
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(validRequest);

            // Then
            assertThat(violations).hasSize(1);
            ConstraintViolation<RegisterRequest> violation = violations.iterator().next();
            assertThat(violation.getPropertyPath().toString()).isEqualTo("name");
            assertThat(violation.getMessage()).isEqualTo("Name must be between 2 and 100 characters");
        }

        @Test
        @DisplayName("Deve aceitar name com exatamente 2 caracteres")
        void deveAceitarNameComExatamente2Caracteres() {
            // Given
            validRequest.setName("AB");

            // When
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(validRequest);

            // Then
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Deve aceitar name com exatamente 100 caracteres")
        void deveAceitarNameComExatamente100Caracteres() {
            // Given
            validRequest.setName("A".repeat(100));

            // When
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(validRequest);

            // Then
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Deve falhar validação quando password tem menos de 6 caracteres")
        void deveFalharValidacaoQuandoPasswordTemMenosDe6Caracteres() {
            // Given
            validRequest.setPassword("12345");

            // When
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(validRequest);

            // Then
            assertThat(violations).hasSize(1);
            ConstraintViolation<RegisterRequest> violation = violations.iterator().next();
            assertThat(violation.getPropertyPath().toString()).isEqualTo("password");
            assertThat(violation.getMessage()).isEqualTo("Password must be at least 6 characters");
        }

        @Test
        @DisplayName("Deve aceitar password com exatamente 6 caracteres")
        void deveAceitarPasswordComExatamente6Caracteres() {
            // Given
            validRequest.setPassword("123456");

            // When
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(validRequest);

            // Then
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Deve falhar validação quando country tem menos de 2 caracteres")
        void deveFalharValidacaoQuandoCountryTemMenosDe2Caracteres() {
            // Given
            validRequest.setCountry("B");

            // When
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(validRequest);

            // Then
            assertThat(violations).hasSize(1);
            ConstraintViolation<RegisterRequest> violation = violations.iterator().next();
            assertThat(violation.getPropertyPath().toString()).isEqualTo("country");
            assertThat(violation.getMessage()).isEqualTo("Country must be between 2 and 100 characters");
        }

        @Test
        @DisplayName("Deve falhar validação quando country tem mais de 100 caracteres")
        void deveFalharValidacaoQuandoCountryTemMaisDe100Caracteres() {
            // Given
            validRequest.setCountry("B".repeat(101));

            // When
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(validRequest);

            // Then
            assertThat(violations).hasSize(1);
            ConstraintViolation<RegisterRequest> violation = violations.iterator().next();
            assertThat(violation.getPropertyPath().toString()).isEqualTo("country");
            assertThat(violation.getMessage()).isEqualTo("Country must be between 2 and 100 characters");
        }
    }

    @Nested
    @DisplayName("Testes de Validação - Email")
    class EmailValidationTests {

        @Test
        @DisplayName("Deve falhar validação quando email tem formato inválido")
        void deveFalharValidacaoQuandoEmailTemFormatoInvalido() {
            // Given
            validRequest.setEmail("invalid-email");

            // When
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(validRequest);

            // Then
            assertThat(violations).hasSize(1);
            ConstraintViolation<RegisterRequest> violation = violations.iterator().next();
            assertThat(violation.getPropertyPath().toString()).isEqualTo("email");
            assertThat(violation.getMessage()).isEqualTo("Email must be valid");
        }

        @Test
        @DisplayName("Deve falhar validação quando email tem formato inválido curto")
        void deveFalharValidacaoQuandoEmailTemFormatoInvalidoCurto() {
            // Given
            validRequest.setEmail("ab");

            // When
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(validRequest);

            // Then
            assertThat(violations).hasSize(1); // Apenas @Email
            ConstraintViolation<RegisterRequest> violation = violations.iterator().next();
            assertThat(violation.getPropertyPath().toString()).isEqualTo("email");
            assertThat(violation.getMessage()).isEqualTo("Email must be valid");
        }

        @Test
        @DisplayName("Deve aceitar email válido")
        void deveAceitarEmailValido() {
            // Given
            validRequest.setEmail("test@example.com");

            // When
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(validRequest);

            // Then
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Deve aceitar email com subdomínio")
        void deveAceitarEmailComSubdominio() {
            // Given
            validRequest.setEmail("test@mail.example.com");

            // When
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(validRequest);

            // Then
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Deve aceitar email com caracteres especiais")
        void deveAceitarEmailComCaracteresEspeciais() {
            // Given
            validRequest.setEmail("test+tag@example-domain.co.uk");

            // When
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(validRequest);

            // Then
            assertThat(violations).isEmpty();
        }
    }

    @Nested
    @DisplayName("Testes de Validação - City (Campo Opcional)")
    class CityValidationTests {

        @Test
        @DisplayName("Deve aceitar city nulo")
        void deveAceitarCityNulo() {
            // Given
            validRequest.setCity(null);

            // When
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(validRequest);

            // Then
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Deve falhar validação quando city é vazio")
        void deveFalharValidacaoQuandoCityEVazio() {
            // Given
            validRequest.setCity("");

            // When
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(validRequest);

            // Then
            assertThat(violations).hasSize(1);
            ConstraintViolation<RegisterRequest> violation = violations.iterator().next();
            assertThat(violation.getPropertyPath().toString()).isEqualTo("city");
            assertThat(violation.getMessage()).isEqualTo("City must be between 2 and 100 characters");
        }

        @Test
        @DisplayName("Deve falhar validação quando city tem 1 caractere")
        void deveFalharValidacaoQuandoCityTem1Caractere() {
            // Given
            validRequest.setCity("S");

            // When
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(validRequest);

            // Then
            assertThat(violations).hasSize(1);
            ConstraintViolation<RegisterRequest> violation = violations.iterator().next();
            assertThat(violation.getPropertyPath().toString()).isEqualTo("city");
            assertThat(violation.getMessage()).isEqualTo("City must be between 2 and 100 characters");
        }

        @Test
        @DisplayName("Deve falhar validação quando city tem mais de 100 caracteres")
        void deveFalharValidacaoQuandoCityTemMaisDe100Caracteres() {
            // Given
            validRequest.setCity("S".repeat(101));

            // When
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(validRequest);

            // Then
            assertThat(violations).hasSize(1);
            ConstraintViolation<RegisterRequest> violation = violations.iterator().next();
            assertThat(violation.getPropertyPath().toString()).isEqualTo("city");
            assertThat(violation.getMessage()).isEqualTo("City must be between 2 and 100 characters");
        }

        @Test
        @DisplayName("Deve aceitar city com exatamente 2 caracteres")
        void deveAceitarCityComExatamente2Caracteres() {
            // Given
            validRequest.setCity("SP");

            // When
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(validRequest);

            // Then
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Deve aceitar city com exatamente 100 caracteres")
        void deveAceitarCityComExatamente100Caracteres() {
            // Given
            validRequest.setCity("S".repeat(100));

            // When
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(validRequest);

            // Then
            assertThat(violations).isEmpty();
        }
    }

    @Nested
    @DisplayName("Testes de Múltiplas Violações")
    class MultipleViolationsTests {

        @Test
        @DisplayName("Deve falhar validação com múltiplas violações")
        void deveFalharValidacaoComMultiplasViolacoes() {
            // Given
            RegisterRequest request = new RegisterRequest();
            request.setName(""); // @NotBlank + @Size = 2 violações
            request.setEmail("invalid"); // @Email = 1 violação
            request.setPassword("123"); // @Size min 6 = 1 violação
            request.setCountry(""); // @NotBlank + @Size = 2 violações
            request.setCity(""); // @Size min 2 = 1 violação (city vazio)

            // When
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

            // Then
            assertThat(violations).hasSize(7); // Total: 7 violações
            assertThat(violations)
                    .extracting(ConstraintViolation::getPropertyPath)
                    .extracting(Object::toString)
                    .containsExactlyInAnyOrder("name", "name", "email", "password", "country", "country", "city");
        }

        @Test
        @DisplayName("Deve falhar validação quando todos os campos obrigatórios são nulos")
        void deveFalharValidacaoQuandoTodosCamposObrigatoriosSaoNulos() {
            // Given
            RegisterRequest request = new RegisterRequest();

            // When
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

            // Then
            assertThat(violations).hasSize(4); // name, email, password, country
            assertThat(violations)
                    .extracting(ConstraintViolation::getPropertyPath)
                    .extracting(Object::toString)
                    .containsExactlyInAnyOrder("name", "email", "password", "country");
        }
    }

    @Nested
    @DisplayName("Testes de Casos Extremos")
    class EdgeCaseTests {

        @Test
        @DisplayName("Deve aceitar caracteres Unicode em name")
        void deveAceitarCaracteresUnicodeEmName() {
            // Given
            validRequest.setName("José María Ñoño");

            // When
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(validRequest);

            // Then
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Deve aceitar caracteres especiais em password")
        void deveAceitarCaracteresEspeciaisEmPassword() {
            // Given
            validRequest.setPassword("p@ssw0rd!@#$%^&*()");

            // When
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(validRequest);

            // Then
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Deve aceitar country com caracteres especiais")
        void deveAceitarCountryComCaracteresEspeciais() {
            // Given
            validRequest.setCountry("São Tomé e Príncipe");

            // When
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(validRequest);

            // Then
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Deve aceitar city com caracteres especiais")
        void deveAceitarCityComCaracteresEspeciais() {
            // Given
            validRequest.setCity("Düsseldorf");

            // When
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(validRequest);

            // Then
            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("Deve aceitar email com domínio internacional")
        void deveAceitarEmailComDominioInternacional() {
            // Given
            validRequest.setEmail("test@münchen.de");

            // When
            Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(validRequest);

            // Then
            assertThat(violations).isEmpty();
        }
    }

    @Nested
    @DisplayName("Testes de Performance")
    class PerformanceTests {

        @Test
        @DisplayName("Deve validar múltiplas instâncias rapidamente")
        void deveValidarMultiplasInstanciasRapidamente() {
            // Given
            int iterations = 1000;

            // When
            long startTime = System.nanoTime();
            for (int i = 0; i < iterations; i++) {
                RegisterRequest request = new RegisterRequest();
                request.setName("User " + i);
                request.setEmail("user" + i + "@example.com");
                request.setPassword("password" + i);
                request.setCountry("Country " + i);
                request.setCity("City " + i);
                
                Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);
                assertThat(violations).isEmpty();
            }
            long endTime = System.nanoTime();

            // Then
            long duration = endTime - startTime;
            assertThat(duration).isLessThan(1_000_000_000L); // Menos de 1 segundo
        }
    }
}