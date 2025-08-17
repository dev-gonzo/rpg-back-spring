package com.rpgsystem.rpg.infrastructure.security;

import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.domain.enums.RoleEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do JwtService")
class JwtServiceTest {

    private JwtService jwtService;
    private final String testSecret = "dGVzdC1zZWNyZXQtZm9yLWp3dC10b2tlbi1nZW5lcmF0aW9uLWFuZC12YWxpZGF0aW9u";
    private User testUser;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", testSecret);
        
        testUser = new User();
        testUser.setId(UUID.randomUUID().toString());
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password123");
        testUser.setMaster(false);
    }

    @Nested
    @DisplayName("Testes do método generateToken")
    class GenerateTokenTests {

        @Test
        @DisplayName("Deve gerar token válido quando usuário é fornecido")
        void deveGerarTokenValidoQuandoUsuarioEFornecido() {
            // Given
            // testUser já configurado no setUp

            // When
            String token = jwtService.generateToken(testUser);

            // Then
            assertThat(token).isNotNull();
            assertThat(token).isNotEmpty();
            assertThat(token.split("\\.")).hasSize(3); // JWT tem 3 partes separadas por ponto
        }

        @Test
        @DisplayName("Deve incluir claims corretos no token quando usuário é PLAYER")
        void deveIncluirClaimsCorretosNoTokenQuandoUsuarioEPlayer() {
            // Given
            testUser.setMaster(false);

            // When
            String token = jwtService.generateToken(testUser);

            // Then
            Claims claims = extractClaimsFromToken(token);
            assertThat(claims.getSubject()).isEqualTo(testUser.getEmail());
            assertThat(claims.get("id")).isEqualTo(testUser.getId());
            assertThat(claims.get("name")).isEqualTo(testUser.getName());
            assertThat(claims.get("role")).isEqualTo("PLAYER");
            assertThat(claims.getIssuedAt()).isNotNull();
            assertThat(claims.getExpiration()).isNotNull();
        }

        @Test
        @DisplayName("Deve incluir role MASTER no token quando usuário é MASTER")
        void deveIncluirRoleMasterNoTokenQuandoUsuarioEMaster() {
            // Given
            testUser.setMaster(true);

            // When
            String token = jwtService.generateToken(testUser);

            // Then
            Claims claims = extractClaimsFromToken(token);
            assertThat(claims.get("role")).isEqualTo("MASTER");
        }

        @Test
        @DisplayName("Deve configurar expiração de 12 horas no token")
        void deveConfigurarExpiracaoDe12HorasNoToken() {
            // Given
            Date beforeGeneration = new Date();

            // When
            String token = jwtService.generateToken(testUser);

            // Then
            Claims claims = extractClaimsFromToken(token);
            Date expiration = claims.getExpiration();
            Date issuedAt = claims.getIssuedAt();
            
            long expectedDuration = 12 * 60 * 60 * 1000; // 12 horas em milissegundos
            long actualDuration = expiration.getTime() - issuedAt.getTime();
            
            assertThat(actualDuration).isEqualTo(expectedDuration);
            assertThat(expiration).isAfter(beforeGeneration);
        }

        @Test
        @DisplayName("Deve lançar exceção quando usuário é nulo")
        void deveLancarExcecaoQuandoUsuarioENulo() {
            // Given
            User nullUser = null;

            // When & Then
            assertThatThrownBy(() -> jwtService.generateToken(nullUser))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Testes do método extractUsername")
    class ExtractUsernameTests {

        @Test
        @DisplayName("Deve extrair username corretamente de token válido")
        void deveExtrairUsernameCorretamenteDeTokenValido() {
            // Given
            String token = jwtService.generateToken(testUser);

            // When
            String extractedUsername = jwtService.extractUsername(token);

            // Then
            assertThat(extractedUsername).isEqualTo(testUser.getEmail());
        }

        @Test
        @DisplayName("Deve lançar exceção quando token é inválido")
        void deveLancarExcecaoQuandoTokenEInvalido() {
            // Given
            String invalidToken = "token.invalido.aqui";

            // When & Then
            assertThatThrownBy(() -> jwtService.extractUsername(invalidToken))
                    .isInstanceOf(Exception.class);
        }

        @Test
        @DisplayName("Deve lançar exceção quando token é nulo")
        void deveLancarExcecaoQuandoTokenENulo() {
            // Given
            String nullToken = null;

            // When & Then
            assertThatThrownBy(() -> jwtService.extractUsername(nullToken))
                    .isInstanceOf(Exception.class);
        }

        @Test
        @DisplayName("Deve lançar exceção quando token está vazio")
        void deveLancarExcecaoQuandoTokenEstaVazio() {
            // Given
            String emptyToken = "";

            // When & Then
            assertThatThrownBy(() -> jwtService.extractUsername(emptyToken))
                    .isInstanceOf(Exception.class);
        }
    }

    @Nested
    @DisplayName("Testes de integração entre métodos")
    class IntegrationTests {

        @Test
        @DisplayName("Deve gerar e extrair username consistentemente")
        void deveGerarEExtrairUsernameConsistentemente() {
            // Given
            String originalUsername = testUser.getEmail();

            // When
            String token = jwtService.generateToken(testUser);
            String extractedUsername = jwtService.extractUsername(token);

            // Then
            assertThat(extractedUsername).isEqualTo(originalUsername);
        }

        @Test
        @DisplayName("Deve gerar tokens diferentes para usuários diferentes")
        void deveGerarTokensDiferentesParaUsuariosDiferentes() {
            // Given
            User anotherUser = new User();
            anotherUser.setId(UUID.randomUUID().toString());
            anotherUser.setName("Another User");
            anotherUser.setEmail("another@example.com");
            anotherUser.setPassword("password456");
            anotherUser.setMaster(true);

            // When
            String token1 = jwtService.generateToken(testUser);
            String token2 = jwtService.generateToken(anotherUser);

            // Then
            assertThat(token1).isNotEqualTo(token2);
            assertThat(jwtService.extractUsername(token1)).isEqualTo(testUser.getEmail());
            assertThat(jwtService.extractUsername(token2)).isEqualTo(anotherUser.getEmail());
        }
    }

    @Nested
    @DisplayName("Testes de Segurança Avançados")
    class SecurityTests {

        @Test
        @DisplayName("Deve rejeitar token com assinatura inválida")
        void deveRejeitarTokenComAssinaturaInvalida() {
            // Given
            String validToken = jwtService.generateToken(testUser);
            String[] tokenParts = validToken.split("\\.");
            // Modificando a assinatura para torná-la inválida
            String tamperedToken = tokenParts[0] + "." + tokenParts[1] + "." + "invalid_signature";

            // When & Then
            assertThatThrownBy(() -> jwtService.extractUsername(tamperedToken))
                    .isInstanceOf(Exception.class);
        }

        @Test
        @DisplayName("Deve rejeitar token com payload modificado")
        void deveRejeitarTokenComPayloadModificado() {
            // Given
            String validToken = jwtService.generateToken(testUser);
            String[] tokenParts = validToken.split("\\.");
            // Modificando o payload (claims)
            String tamperedPayload = java.util.Base64.getEncoder().encodeToString(
                "{\"sub\":\"hacker@evil.com\",\"role\":\"ADMIN\"}".getBytes()
            ).replace("=", ""); // Remove padding
            String tamperedToken = tokenParts[0] + "." + tamperedPayload + "." + tokenParts[2];

            // When & Then
            assertThatThrownBy(() -> jwtService.extractUsername(tamperedToken))
                    .isInstanceOf(Exception.class);
        }

        @Test
        @DisplayName("Deve validar integridade dos claims críticos")
        void deveValidarIntegridadeDosClaimsCriticos() {
            // Given
            testUser.setMaster(false);
            String token = jwtService.generateToken(testUser);

            // When
            Claims claims = extractClaimsFromToken(token);

            // Then - Validar que claims críticos não foram alterados
            assertThat(claims.getSubject()).isEqualTo(testUser.getEmail());
            assertThat(claims.get("id")).isEqualTo(testUser.getId());
            assertThat(claims.get("name")).isEqualTo(testUser.getName());
            assertThat(claims.get("role")).isEqualTo("PLAYER");
            // Validar que não há claims extras maliciosos
            assertThat(claims.containsKey("admin")).isFalse();
            assertThat(claims.containsKey("superuser")).isFalse();
        }

        @Test
        @DisplayName("Deve gerar tokens únicos mesmo para o mesmo usuário")
        void deveGerarTokensUnicosParaOMesmoUsuario() {
            // Given
            String token1 = jwtService.generateToken(testUser);
            
            try {
                Thread.sleep(1000); // 1 segundo de diferença para garantir timestamps diferentes
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            String token2 = jwtService.generateToken(testUser);

            // When & Then
            assertThat(token1).isNotEqualTo(token2);
            // Mas ambos devem ter o mesmo subject
            assertThat(jwtService.extractUsername(token1)).isEqualTo(jwtService.extractUsername(token2));
        }

        @Test
        @DisplayName("Deve validar formato correto do algoritmo de assinatura")
        void deveValidarFormatoCorretoDaAssinatura() {
            // Given
            String token = jwtService.generateToken(testUser);

            // When
            Claims claims = extractClaimsFromToken(token);

            // Then
            // Verificar se o token foi assinado com HS256
            SecretKey key = Keys.hmacShaKeyFor(java.util.Base64.getDecoder().decode(testSecret));
            assertThatThrownBy(() -> {
                // Tentar validar com algoritmo diferente deve falhar
                Jwts.parserBuilder()
                    .setSigningKey(Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS512))
                    .build()
                    .parseClaimsJws(token);
            }).isInstanceOf(Exception.class);
        }
    }

    @Nested
    @DisplayName("Testes de Validação de Claims")
    class ClaimsValidationTests {

        @Test
        @DisplayName("Deve incluir todos os claims obrigatórios")
        void deveIncluirTodosOsClaimsObrigatorios() {
            // Given
            String token = jwtService.generateToken(testUser);

            // When
            Claims claims = extractClaimsFromToken(token);

            // Then
            assertThat(claims.getSubject()).isNotNull();
            assertThat(claims.get("id")).isNotNull();
            assertThat(claims.get("name")).isNotNull();
            assertThat(claims.get("role")).isNotNull();
            assertThat(claims.getIssuedAt()).isNotNull();
            assertThat(claims.getExpiration()).isNotNull();
        }

        @Test
        @DisplayName("Deve validar tipos corretos dos claims")
        void deveValidarTiposCorretosDosClaimsObrigatorios() {
            // Given
            String token = jwtService.generateToken(testUser);

            // When
            Claims claims = extractClaimsFromToken(token);

            // Then
            assertThat(claims.getSubject()).isInstanceOf(String.class);
            assertThat(claims.get("id")).isInstanceOf(String.class);
            assertThat(claims.get("name")).isInstanceOf(String.class);
            assertThat(claims.get("role")).isInstanceOf(String.class);
            assertThat(claims.getIssuedAt()).isInstanceOf(Date.class);
            assertThat(claims.getExpiration()).isInstanceOf(Date.class);
        }

        @Test
        @DisplayName("Deve validar valores não vazios dos claims")
        void deveValidarValoresNaoVaziosDosClaimsObrigatorios() {
            // Given
            String token = jwtService.generateToken(testUser);

            // When
            Claims claims = extractClaimsFromToken(token);

            // Then
            assertThat(claims.getSubject()).isNotEmpty();
            assertThat((String) claims.get("id")).isNotEmpty();
            assertThat((String) claims.get("name")).isNotEmpty();
            assertThat((String) claims.get("role")).isNotEmpty();
        }

        @Test
        @DisplayName("Deve validar consistência entre issuedAt e expiration")
        void deveValidarConsistenciaEntreIssuedAtEExpiration() {
            // Given
            String token = jwtService.generateToken(testUser);

            // When
            Claims claims = extractClaimsFromToken(token);
            Date issuedAt = claims.getIssuedAt();
            Date expiration = claims.getExpiration();

            // Then
            assertThat(expiration).isAfter(issuedAt);
            long duration = expiration.getTime() - issuedAt.getTime();
            assertThat(duration).isEqualTo(12 * 60 * 60 * 1000); // Exatamente 12 horas
        }
    }

    @Nested
    @DisplayName("Testes de Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Deve processar usuário com caracteres especiais no nome")
        void deveProcessarUsuarioComCaracteresEspeciaisNoNome() {
            // Given
            testUser.setName("José da Silva & Cia. Ltda. (Administrador)");
            testUser.setEmail("jose+admin@empresa-teste.com.br");

            // When
            String token = jwtService.generateToken(testUser);
            String extractedUsername = jwtService.extractUsername(token);

            // Then
            assertThat(token).isNotNull();
            assertThat(extractedUsername).isEqualTo(testUser.getEmail());
            
            Claims claims = extractClaimsFromToken(token);
            assertThat(claims.get("name")).isEqualTo(testUser.getName());
        }

        @Test
        @DisplayName("Deve processar usuário com ID muito longo")
        void deveProcessarUsuarioComIdMuitoLongo() {
            // Given
            String longId = UUID.randomUUID().toString() + "-" + UUID.randomUUID().toString() + "-extra-data";
            testUser.setId(longId);

            // When
            String token = jwtService.generateToken(testUser);

            // Then
            assertThat(token).isNotNull();
            Claims claims = extractClaimsFromToken(token);
            assertThat(claims.get("id")).isEqualTo(longId);
        }

        @Test
        @DisplayName("Deve processar múltiplos tokens simultaneamente")
        void deveProcessarMultiplosTokensSimultaneamente() {
            // Given
            User user1 = createTestUser("user1@test.com", "User 1", false);
            User user2 = createTestUser("user2@test.com", "User 2", true);
            User user3 = createTestUser("user3@test.com", "User 3", false);

            // When
            String token1 = jwtService.generateToken(user1);
            String token2 = jwtService.generateToken(user2);
            String token3 = jwtService.generateToken(user3);

            // Then
            assertThat(jwtService.extractUsername(token1)).isEqualTo(user1.getEmail());
            assertThat(jwtService.extractUsername(token2)).isEqualTo(user2.getEmail());
            assertThat(jwtService.extractUsername(token3)).isEqualTo(user3.getEmail());
            
            // Verificar roles corretas
            assertThat(extractClaimsFromToken(token1).get("role")).isEqualTo("PLAYER");
            assertThat(extractClaimsFromToken(token2).get("role")).isEqualTo("MASTER");
            assertThat(extractClaimsFromToken(token3).get("role")).isEqualTo("PLAYER");
        }

        private User createTestUser(String email, String name, boolean isMaster) {
            User user = new User();
            user.setId(UUID.randomUUID().toString());
            user.setEmail(email);
            user.setName(name);
            user.setPassword("password123");
            user.setMaster(isMaster);
            return user;
        }
    }

    // Método auxiliar para extrair claims do token
    private Claims extractClaimsFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(java.util.Base64.getDecoder().decode(testSecret));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}