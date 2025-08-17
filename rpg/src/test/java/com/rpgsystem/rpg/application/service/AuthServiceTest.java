package com.rpgsystem.rpg.application.service;

import com.rpgsystem.rpg.api.dto.LoginRequest;
import com.rpgsystem.rpg.api.dto.LoginResponse;
import com.rpgsystem.rpg.api.dto.RegisterRequest;
import com.rpgsystem.rpg.api.dto.RegisterResponse;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.domain.enums.RoleEnum;
import com.rpgsystem.rpg.domain.exception.InvalidCredentialsException;
import com.rpgsystem.rpg.domain.repository.UserRepository;
import com.rpgsystem.rpg.infrastructure.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do AuthService")
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private LoginRequest loginRequest;
    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(UUID.randomUUID().toString())
                .name("Test User")
                .email("test@example.com")
                .password("encodedPassword")
                .country("Brasil")
                .city("São Paulo")
                .isMaster(false)
                .build();

        loginRequest = new LoginRequest("test@example.com", "password123");
        
        registerRequest = new RegisterRequest();
        registerRequest.setName("New User");
        registerRequest.setEmail("newuser@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setCountry("Brasil");
        registerRequest.setCity("Rio de Janeiro");
    }

    @Nested
    @DisplayName("Testes do método login")
    class LoginTests {

        @Test
        @DisplayName("Deve realizar login com sucesso quando credenciais são válidas")
        void deveRealizarLoginComSucessoQuandoCredenciaisSaoValidas() {
            // Given
            String expectedToken = "jwt.token.here";
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(testUser);
            when(jwtService.generateToken(testUser)).thenReturn(expectedToken);

            // When
            LoginResponse response = authService.login(loginRequest);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.token()).isEqualTo(expectedToken);
            
            verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
            verify(jwtService).generateToken(testUser);
        }

        @Test
        @DisplayName("Deve lançar InvalidCredentialsException quando credenciais são inválidas")
        void deveLancarInvalidCredentialsExceptionQuandoCredenciaisSaoInvalidas() {
            // Given
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new BadCredentialsException("Invalid credentials"));

            // When & Then
            assertThatThrownBy(() -> authService.login(loginRequest))
                    .isInstanceOf(InvalidCredentialsException.class);
            
            verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
            verify(jwtService, never()).generateToken(any(User.class));
        }

        @Test
        @DisplayName("Deve usar email e senha corretos na autenticação")
        void deveUsarEmailESenhaCorretosNaAutenticacao() {
            // Given
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(testUser);
            when(jwtService.generateToken(testUser)).thenReturn("token");

            // When
            authService.login(loginRequest);

            // Then
            verify(authenticationManager).authenticate(
                    argThat(auth -> {
                        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) auth;
                        return loginRequest.email().equals(token.getName()) &&
                               loginRequest.password().equals(token.getCredentials());
                    })
            );
        }

        @Test
        @DisplayName("Deve lançar exceção quando loginRequest é nulo")
        void deveLancarExcecaoQuandoLoginRequestENulo() {
            // Given
            LoginRequest nullRequest = null;

            // When & Then
            assertThatThrownBy(() -> authService.login(nullRequest))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Testes do método register")
    class RegisterTests {

        @Test
        @DisplayName("Deve registrar usuário com sucesso quando email não existe")
        void deveRegistrarUsuarioComSucessoQuandoEmailNaoExiste() {
            // Given
            String encodedPassword = "encodedPassword123";
            User savedUser = User.builder()
                    .id("generated-id")
                    .name(registerRequest.getName())
                    .email(registerRequest.getEmail())
                    .password(encodedPassword)
                    .country(registerRequest.getCountry())
                    .city(registerRequest.getCity())
                    .isMaster(false)
                    .build();

            when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
            when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn(encodedPassword);
            when(userRepository.save(any(User.class))).thenReturn(savedUser);

            // When
            RegisterResponse response = authService.register(registerRequest);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.getId()).isEqualTo(savedUser.getId());
            assertThat(response.getName()).isEqualTo(savedUser.getName());
            assertThat(response.getEmail()).isEqualTo(savedUser.getEmail());
            assertThat(response.getMessage()).isEqualTo("User registered successfully");
            
            verify(userRepository).findByEmail(registerRequest.getEmail());
            verify(passwordEncoder).encode(registerRequest.getPassword());
            verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("Deve lançar IllegalArgumentException quando email já existe")
        void deveLancarIllegalArgumentExceptionQuandoEmailJaExiste() {
            // Given
            when(userRepository.findByEmail(registerRequest.getEmail()))
                    .thenReturn(Optional.of(testUser));

            // When & Then
            assertThatThrownBy(() -> authService.register(registerRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Email already exists");
            
            verify(userRepository).findByEmail(registerRequest.getEmail());
            verify(passwordEncoder, never()).encode(anyString());
            verify(userRepository, never()).save(any(User.class));
        }

        @Test
        @DisplayName("Deve criar usuário com dados corretos")
        void deveCriarUsuarioComDadosCorretos() {
            // Given
            String encodedPassword = "encodedPassword123";
            when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
            when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn(encodedPassword);
            when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
                User user = invocation.getArgument(0);
                return User.builder()
                        .id("generated-id")
                        .name(user.getName())
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .country(user.getCountry())
                        .city(user.getCity())
                        .isMaster(user.isMaster())
                        .build();
            });

            // When
            authService.register(registerRequest);

            // Then
            verify(userRepository).save(argThat(user -> {
                return registerRequest.getName().equals(user.getName()) &&
                       registerRequest.getEmail().equals(user.getEmail()) &&
                       encodedPassword.equals(user.getPassword()) &&
                       registerRequest.getCountry().equals(user.getCountry()) &&
                       registerRequest.getCity().equals(user.getCity()) &&
                       !user.isMaster() &&
                       user.getId() != null;
            }));
        }

        @Test
        @DisplayName("Deve codificar senha antes de salvar usuário")
        void deveCodificarSenhaAntesDeSalvarUsuario() {
            // Given
            String rawPassword = registerRequest.getPassword();
            String encodedPassword = "encodedPassword123";
            
            when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
            when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);
            when(userRepository.save(any(User.class))).thenReturn(testUser);

            // When
            authService.register(registerRequest);

            // Then
            verify(passwordEncoder).encode(rawPassword);
            verify(userRepository).save(argThat(user -> 
                encodedPassword.equals(user.getPassword())
            ));
        }

        @Test
        @DisplayName("Deve definir isMaster como false por padrão")
        void deveDefinirIsMasterComoFalsePorPadrao() {
            // Given
            when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
            when(passwordEncoder.encode(anyString())).thenReturn("encoded");
            when(userRepository.save(any(User.class))).thenReturn(testUser);

            // When
            authService.register(registerRequest);

            // Then
            verify(userRepository).save(argThat(user -> !user.isMaster()));
        }

        @Test
        @DisplayName("Deve gerar ID único para novo usuário")
        void deveGerarIdUnicoParaNovoUsuario() {
            // Given
            when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
            when(passwordEncoder.encode(anyString())).thenReturn("encoded");
            when(userRepository.save(any(User.class))).thenReturn(testUser);

            // When
            authService.register(registerRequest);

            // Then
            verify(userRepository).save(argThat(user -> {
                String id = user.getId();
                return id != null && !id.isEmpty() && id.length() == 36; // UUID length
            }));
        }

        @Test
        @DisplayName("Deve lançar exceção quando registerRequest é nulo")
        void deveLancarExcecaoQuandoRegisterRequestENulo() {
            // Given
            RegisterRequest nullRequest = null;

            // When & Then
            assertThatThrownBy(() -> authService.register(nullRequest))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Testes de Segurança Avançados")
    class SecurityTests {

        @Test
        @DisplayName("Deve propagar exceção de conta bloqueada")
        void devePropagrarExcecaoDeContaBloqueada() {
            // Given
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new LockedException("Account is locked"));

            // When & Then
            assertThatThrownBy(() -> authService.login(loginRequest))
                    .isInstanceOf(LockedException.class)
                    .hasMessage("Account is locked");
            
            verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
            verify(jwtService, never()).generateToken(any(User.class));
        }

        @Test
        @DisplayName("Deve propagar exceção de conta desabilitada")
        void devePropagrarExcecaoDeContaDesabilitada() {
            // Given
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new DisabledException("Account is disabled"));

            // When & Then
            assertThatThrownBy(() -> authService.login(loginRequest))
                    .isInstanceOf(DisabledException.class)
                    .hasMessage("Account is disabled");
            
            verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
            verify(jwtService, never()).generateToken(any(User.class));
        }

        @Test
        @DisplayName("Deve validar entrada contra SQL Injection no email")
        void deveValidarEntradaContraSqlInjectionNoEmail() {
            // Given
            LoginRequest maliciousRequest = new LoginRequest("test@example.com'; DROP TABLE users; --", "password");
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(testUser);
            when(jwtService.generateToken(testUser)).thenReturn("token");

            // When
            LoginResponse response = authService.login(maliciousRequest);

            // Then
            assertThat(response).isNotNull();
            verify(authenticationManager).authenticate(
                    argThat(auth -> maliciousRequest.email().equals(auth.getName()))
            );
        }

        @Test
        @DisplayName("Deve validar entrada contra XSS no registro")
        void deveValidarEntradaContraXssNoRegistro() {
            // Given
            RegisterRequest xssRequest = new RegisterRequest();
            xssRequest.setName("<script>alert('XSS')</script>");
            xssRequest.setEmail("xss@example.com");
            xssRequest.setPassword("password123");
            xssRequest.setCountry("<img src=x onerror=alert('XSS')>");
            xssRequest.setCity("Normal City");

            when(userRepository.findByEmail(xssRequest.getEmail())).thenReturn(Optional.empty());
            when(passwordEncoder.encode(anyString())).thenReturn("encoded");
            when(userRepository.save(any(User.class))).thenReturn(testUser);

            // When
            RegisterResponse response = authService.register(xssRequest);

            // Then
            assertThat(response).isNotNull();
            verify(userRepository).save(argThat(user -> 
                xssRequest.getName().equals(user.getName()) &&
                xssRequest.getCountry().equals(user.getCountry())
            ));
        }

        @Test
        @DisplayName("Deve processar múltiplas tentativas de login simultâneas")
        void deveProcessarMultiplasTentativasDeLoginSimultaneas() throws Exception {
            // Given
            ExecutorService executor = Executors.newFixedThreadPool(10);
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(testUser);
            when(jwtService.generateToken(testUser)).thenReturn("token");

            // When
            CompletableFuture<?>[] futures = IntStream.range(0, 10)
                    .mapToObj(i -> CompletableFuture.runAsync(() -> {
                        LoginResponse response = authService.login(loginRequest);
                        assertThat(response).isNotNull();
                    }, executor))
                    .toArray(CompletableFuture[]::new);

            CompletableFuture.allOf(futures).get(5, TimeUnit.SECONDS);
            executor.shutdown();

            // Then
            verify(authenticationManager, times(10)).authenticate(any(UsernamePasswordAuthenticationToken.class));
            verify(jwtService, times(10)).generateToken(testUser);
        }
    }

    @Nested
    @DisplayName("Testes de Validação de Entrada")
    class InputValidationTests {

        @Test
        @DisplayName("Deve processar email com caracteres especiais válidos")
        void deveProcessarEmailComCaracteresEspeciaisValidos() {
            // Given
            LoginRequest specialEmailRequest = new LoginRequest("test+tag@sub-domain.example.com", "password");
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(testUser);
            when(jwtService.generateToken(testUser)).thenReturn("token");

            // When
            LoginResponse response = authService.login(specialEmailRequest);

            // Then
            assertThat(response).isNotNull();
            verify(authenticationManager).authenticate(
                    argThat(auth -> specialEmailRequest.email().equals(auth.getName()))
            );
        }

        @Test
        @DisplayName("Deve processar senha com caracteres especiais")
        void deveProcessarSenhaComCaracteresEspeciais() {
            // Given
            String complexPassword = "P@ssw0rd!#$%^&*()_+-=[]{}|;':,.<>?";
            LoginRequest complexPasswordRequest = new LoginRequest("test@example.com", complexPassword);
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(testUser);
            when(jwtService.generateToken(testUser)).thenReturn("token");

            // When
            LoginResponse response = authService.login(complexPasswordRequest);

            // Then
            assertThat(response).isNotNull();
            verify(authenticationManager).authenticate(
                    argThat(auth -> complexPassword.equals(auth.getCredentials()))
            );
        }

        @Test
        @DisplayName("Deve processar nome com caracteres unicode no registro")
        void deveProcessarNomeComCaracteresUnicodeNoRegistro() {
            // Given
            RegisterRequest unicodeRequest = new RegisterRequest();
            unicodeRequest.setName("José María Ñoño 中文 العربية");
            unicodeRequest.setEmail("unicode@example.com");
            unicodeRequest.setPassword("password123");
            unicodeRequest.setCountry("España");
            unicodeRequest.setCity("São Paulo");

            when(userRepository.findByEmail(unicodeRequest.getEmail())).thenReturn(Optional.empty());
            when(passwordEncoder.encode(anyString())).thenReturn("encoded");
            when(userRepository.save(any(User.class))).thenReturn(testUser);

            // When
            RegisterResponse response = authService.register(unicodeRequest);

            // Then
            assertThat(response).isNotNull();
            verify(userRepository).save(argThat(user -> 
                unicodeRequest.getName().equals(user.getName())
            ));
        }

        @Test
        @DisplayName("Deve processar campos com tamanhos máximos")
        void deveProcessarCamposComTamanhosMaximos() {
            // Given
            String longName = "A".repeat(255); // Nome muito longo
            String longCity = "B".repeat(100); // Cidade longa
            String longCountry = "C".repeat(100); // País longo
            
            RegisterRequest longFieldsRequest = new RegisterRequest();
            longFieldsRequest.setName(longName);
            longFieldsRequest.setEmail("longfields@example.com");
            longFieldsRequest.setPassword("password123");
            longFieldsRequest.setCountry(longCountry);
            longFieldsRequest.setCity(longCity);

            when(userRepository.findByEmail(longFieldsRequest.getEmail())).thenReturn(Optional.empty());
            when(passwordEncoder.encode(anyString())).thenReturn("encoded");
            when(userRepository.save(any(User.class))).thenReturn(testUser);

            // When
            RegisterResponse response = authService.register(longFieldsRequest);

            // Then
            assertThat(response).isNotNull();
            verify(userRepository).save(argThat(user -> 
                longName.equals(user.getName()) &&
                longCity.equals(user.getCity()) &&
                longCountry.equals(user.getCountry())
            ));
        }
    }

    @Nested
    @DisplayName("Testes de Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Deve processar email em maiúsculas")
        void deveProcessarEmailEmMaiusculas() {
            // Given
            LoginRequest uppercaseEmailRequest = new LoginRequest("TEST@EXAMPLE.COM", "password");
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(testUser);
            when(jwtService.generateToken(testUser)).thenReturn("token");

            // When
            LoginResponse response = authService.login(uppercaseEmailRequest);

            // Then
            assertThat(response).isNotNull();
            verify(authenticationManager).authenticate(
                    argThat(auth -> "TEST@EXAMPLE.COM".equals(auth.getName()))
            );
        }

        @Test
        @DisplayName("Deve processar senha vazia (delegando validação ao AuthenticationManager)")
        void deveProcessarSenhaVazia() {
            // Given
            LoginRequest emptyPasswordRequest = new LoginRequest("test@example.com", "");
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new BadCredentialsException("Empty password"));

            // When & Then
            assertThatThrownBy(() -> authService.login(emptyPasswordRequest))
                    .isInstanceOf(InvalidCredentialsException.class);
            
            verify(authenticationManager).authenticate(
                    argThat(auth -> "".equals(auth.getCredentials()))
            );
        }

        @Test
        @DisplayName("Deve processar registro com campos opcionais vazios")
        void deveProcessarRegistroComCamposOpcionaisVazios() {
            // Given
            RegisterRequest minimalRequest = new RegisterRequest();
            minimalRequest.setName("Minimal User");
            minimalRequest.setEmail("minimal@example.com");
            minimalRequest.setPassword("password123");
            minimalRequest.setCountry(""); // Campo vazio
            minimalRequest.setCity(""); // Campo vazio

            when(userRepository.findByEmail(minimalRequest.getEmail())).thenReturn(Optional.empty());
            when(passwordEncoder.encode(anyString())).thenReturn("encoded");
            when(userRepository.save(any(User.class))).thenReturn(testUser);

            // When
            RegisterResponse response = authService.register(minimalRequest);

            // Then
            assertThat(response).isNotNull();
            verify(userRepository).save(argThat(user -> 
                "".equals(user.getCountry()) &&
                "".equals(user.getCity())
            ));
        }

        @Test
        @DisplayName("Deve gerar IDs únicos para registros simultâneos")
        void deveGerarIdsUnicosParaRegistrosSimultaneos() throws Exception {
            // Given
            ExecutorService executor = Executors.newFixedThreadPool(5);
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
            when(passwordEncoder.encode(anyString())).thenReturn("encoded");
            when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
                User user = invocation.getArgument(0);
                return User.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .password(user.getPassword())
                        .country(user.getCountry())
                        .city(user.getCity())
                        .isMaster(user.isMaster())
                        .build();
            });

            // When
            CompletableFuture<RegisterResponse>[] futures = IntStream.range(0, 5)
                    .mapToObj(i -> {
                        RegisterRequest request = new RegisterRequest();
                        request.setName("User " + i);
                        request.setEmail("user" + i + "@example.com");
                        request.setPassword("password123");
                        request.setCountry("Brasil");
                        request.setCity("São Paulo");
                        
                        return CompletableFuture.supplyAsync(() -> authService.register(request), executor);
                    })
                    .toArray(CompletableFuture[]::new);

            CompletableFuture.allOf(futures).get(5, TimeUnit.SECONDS);
            executor.shutdown();

            // Then
            verify(userRepository, times(5)).save(any(User.class));
        }
    }

    @Nested
    @DisplayName("Testes de integração entre métodos")
    class IntegrationTests {

        @Test
        @DisplayName("Deve permitir login após registro bem-sucedido")
        void devePermitirLoginAposRegistroBemSucedido() {
            // Given - Primeiro registrar
            when(userRepository.findByEmail(registerRequest.getEmail())).thenReturn(Optional.empty());
            when(passwordEncoder.encode(anyString())).thenReturn("encoded");
            when(userRepository.save(any(User.class))).thenReturn(testUser);

            RegisterResponse registerResponse = authService.register(registerRequest);
            assertThat(registerResponse).isNotNull();

            // Given - Depois fazer login
            LoginRequest loginAfterRegister = new LoginRequest(registerRequest.getEmail(), registerRequest.getPassword());
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(testUser);
            when(jwtService.generateToken(testUser)).thenReturn("token");

            // When
            LoginResponse loginResponse = authService.login(loginAfterRegister);

            // Then
            assertThat(loginResponse).isNotNull();
            assertThat(loginResponse.token()).isNotNull();
        }

        @Test
        @DisplayName("Deve manter consistência entre múltiplas operações")
        void deveManterConsistenciaEntreMultiplasOperacoes() {
            // Given
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
            when(passwordEncoder.encode(anyString())).thenReturn("encoded");
            when(userRepository.save(any(User.class))).thenReturn(testUser);
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(testUser);
            when(jwtService.generateToken(testUser)).thenReturn("token");

            // When - Registrar múltiplos usuários e fazer login
            for (int i = 0; i < 3; i++) {
                RegisterRequest request = new RegisterRequest();
                request.setName("User " + i);
                request.setEmail("user" + i + "@example.com");
                request.setPassword("password" + i);
                request.setCountry("Brasil");
                request.setCity("São Paulo");
                
                RegisterResponse registerResponse = authService.register(request);
                assertThat(registerResponse).isNotNull();
                
                LoginRequest loginRequest = new LoginRequest(request.getEmail(), request.getPassword());
                LoginResponse loginResponse = authService.login(loginRequest);
                assertThat(loginResponse).isNotNull();
            }

            // Then
            verify(userRepository, times(3)).save(any(User.class));
            verify(authenticationManager, times(3)).authenticate(any(UsernamePasswordAuthenticationToken.class));
            verify(jwtService, times(3)).generateToken(testUser);
        }
    }
}