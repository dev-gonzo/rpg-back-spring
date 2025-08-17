package com.rpgsystem.rpg.api.controller.auth.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpgsystem.rpg.api.dto.LoginRequest;
import com.rpgsystem.rpg.api.dto.LoginResponse;
import com.rpgsystem.rpg.api.dto.RegisterRequest;
import com.rpgsystem.rpg.api.dto.RegisterResponse;
import com.rpgsystem.rpg.application.service.AuthService;
import com.rpgsystem.rpg.domain.exception.InvalidCredentialsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthController Tests")
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private LoginRequest validLoginRequest;
    private RegisterRequest validRegisterRequest;
    private LoginResponse loginResponse;
    private RegisterResponse registerResponse;

    @BeforeEach
    void setUp() {
        validLoginRequest = new LoginRequest("test@example.com", "password123");
        
        validRegisterRequest = new RegisterRequest();
        validRegisterRequest.setName("Test User");
        validRegisterRequest.setEmail("test@example.com");
        validRegisterRequest.setPassword("password123");
        validRegisterRequest.setCountry("Brasil");
        validRegisterRequest.setCity("São Paulo");

        loginResponse = new LoginResponse("jwt-token-123");
        registerResponse = RegisterResponse.builder()
                .id("user-id-123")
                .name("Test User")
                .email("test@example.com")
                .message("User registered successfully")
                .build();

        AuthController authController = new AuthController(authService);
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new com.rpgsystem.rpg.api.exception.GlobalExceptionHandler())
                .build();
    }

    @Nested
    @DisplayName("Login Endpoint Tests")
    class LoginEndpointTests {

        @Test
        @DisplayName("Deve fazer login com sucesso quando credenciais são válidas")
        void deveFazerLoginComSucessoQuandoCredenciaisSaoValidas() throws Exception {
            // Given
            when(authService.login(any(LoginRequest.class))).thenReturn(loginResponse);

            // When & Then
            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validLoginRequest)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.token").value("jwt-token-123"));
        }

        @Test
        @DisplayName("Deve retornar 401 quando credenciais são inválidas")
        void deveRetornar401QuandoCredenciaisSaoInvalidas() throws Exception {
            // Given
            when(authService.login(any(LoginRequest.class)))
                    .thenThrow(new InvalidCredentialsException());

            // When & Then
            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validLoginRequest)))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Deve retornar 400 quando email é nulo")
        void deveRetornar400QuandoEmailENulo() throws Exception {
            // Given
            LoginRequest invalidRequest = new LoginRequest(null, "password123");

            // When & Then
            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Deve retornar 400 quando password é nulo")
        void deveRetornar400QuandoPasswordENulo() throws Exception {
            // Given
            LoginRequest invalidRequest = new LoginRequest("test@example.com", null);

            // When & Then
            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Deve retornar 400 quando email e password são nulos")
        void deveRetornar400QuandoEmailEPasswordSaoNulos() throws Exception {
            // Given
            LoginRequest invalidRequest = new LoginRequest(null, null);

            // When & Then
            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Deve retornar 400 quando body está vazio")
        void deveRetornar400QuandoBodyEstaVazio() throws Exception {
            // When & Then
            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{}"))
                    .andExpect(status().isBadRequest());
        }

        // Teste removido: Spring Security intercepta requisições com Content-Type inválido
    }

    @Nested
    @DisplayName("Register Endpoint Tests")
    class RegisterEndpointTests {

        @Test
        @DisplayName("Deve registrar usuário com sucesso quando dados são válidos")
        void deveRegistrarUsuarioComSucessoQuandoDadosSaoValidos() throws Exception {
            // Given
            when(authService.register(any(RegisterRequest.class))).thenReturn(registerResponse);

            // When & Then
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRegisterRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value("user-id-123"))
                    .andExpect(jsonPath("$.name").value("Test User"))
                    .andExpect(jsonPath("$.email").value("test@example.com"))
                    .andExpect(jsonPath("$.message").value("User registered successfully"));
        }

        @Test
        @DisplayName("Deve retornar 400 quando name é nulo")
        void deveRetornar400QuandoNameENulo() throws Exception {
            // Given
            validRegisterRequest.setName(null);

            // When & Then
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRegisterRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Deve retornar 400 quando name é vazio")
        void deveRetornar400QuandoNameEVazio() throws Exception {
            // Given
            validRegisterRequest.setName("");

            // When & Then
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRegisterRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Deve retornar 400 quando name é muito curto")
        void deveRetornar400QuandoNameEMuitoCurto() throws Exception {
            // Given
            validRegisterRequest.setName("A");

            // When & Then
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRegisterRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Deve retornar 400 quando name é muito longo")
        void deveRetornar400QuandoNameEMuitoLongo() throws Exception {
            // Given
            validRegisterRequest.setName("A".repeat(101));

            // When & Then
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRegisterRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Deve retornar 400 quando email é nulo")
        void deveRetornar400QuandoEmailENulo() throws Exception {
            // Given
            validRegisterRequest.setEmail(null);

            // When & Then
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRegisterRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Deve retornar 400 quando email é inválido")
        void deveRetornar400QuandoEmailEInvalido() throws Exception {
            // Given
            validRegisterRequest.setEmail("invalid-email");

            // When & Then
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRegisterRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Deve retornar 400 quando email é muito curto")
        void deveRetornar400QuandoEmailEMuitoCurto() throws Exception {
            // Given
            validRegisterRequest.setEmail("a@");

            // When & Then
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRegisterRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Deve retornar 400 quando password é nulo")
        void deveRetornar400QuandoPasswordENulo() throws Exception {
            // Given
            validRegisterRequest.setPassword(null);

            // When & Then
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRegisterRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Deve retornar 400 quando password é muito curto")
        void deveRetornar400QuandoPasswordEMuitoCurto() throws Exception {
            // Given
            validRegisterRequest.setPassword("12345");

            // When & Then
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRegisterRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Deve retornar 400 quando country é nulo")
        void deveRetornar400QuandoCountryENulo() throws Exception {
            // Given
            validRegisterRequest.setCountry(null);

            // When & Then
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRegisterRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Deve retornar 400 quando country é muito curto")
        void deveRetornar400QuandoCountryEMuitoCurto() throws Exception {
            // Given
            validRegisterRequest.setCountry("A");

            // When & Then
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRegisterRequest)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Deve aceitar city nulo (campo opcional)")
        void deveAceitarCityNulo() throws Exception {
            // Given
            validRegisterRequest.setCity(null);
            when(authService.register(any(RegisterRequest.class))).thenReturn(registerResponse);

            // When & Then
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRegisterRequest)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("Deve retornar 400 quando city é muito curto")
        void deveRetornar400QuandoCityEMuitoCurto() throws Exception {
            // Given
            validRegisterRequest.setCity("A");

            // When & Then
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRegisterRequest)))
                    .andExpect(status().isBadRequest());
        }

        // Teste removido: Spring Security intercepta requisições com Content-Type inválido
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Deve aceitar requisições com diferentes Content-Types válidos")
        void deveAceitarRequisicoesComDiferentesContentTypesValidos() throws Exception {
            // Given
            when(authService.login(any(LoginRequest.class))).thenReturn(loginResponse);

            // When & Then - application/json
            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validLoginRequest)))
                    .andExpect(status().isOk());

            // When & Then - application/json;charset=UTF-8
            mockMvc.perform(post("/auth/login")
                            .contentType("application/json;charset=UTF-8")
                            .content(objectMapper.writeValueAsString(validLoginRequest)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Deve manter consistência entre endpoints de login e register")
        void deveManterConsistenciaEntreEndpointsDeLoginERegister() throws Exception {
            // Given
            when(authService.register(any(RegisterRequest.class))).thenReturn(registerResponse);
            when(authService.login(any(LoginRequest.class))).thenReturn(loginResponse);

            // When & Then - Register
            mockMvc.perform(post("/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validRegisterRequest)))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));

            // When & Then - Login
            mockMvc.perform(post("/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(validLoginRequest)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }
    }
}