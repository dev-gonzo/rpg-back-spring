package com.rpgsystem.rpg.infrastructure.security;

import com.rpgsystem.rpg.domain.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do AuthenticatedUserProvider")
class AuthenticatedUserProviderTest {

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthenticatedUserProvider authenticatedUserProvider;

    private MockedStatic<SecurityContextHolder> securityContextHolderMock;
    private User testUser;

    @BeforeEach
    void setUp() {
        securityContextHolderMock = mockStatic(SecurityContextHolder.class);
        securityContextHolderMock.when(SecurityContextHolder::getContext).thenReturn(securityContext);
        
        testUser = User.builder()
                .id("test-user-id")
                .name("Test User")
                .email("test@example.com")
                .password("password")
                .country("Brasil")
                .city("São Paulo")
                .isMaster(false)
                .build();
    }

    @AfterEach
    void tearDown() {
        securityContextHolderMock.close();
    }

    @Nested
    @DisplayName("Testes do método getAuthenticatedUser")
    class GetAuthenticatedUserTests {

        @Test
        @DisplayName("Deve retornar usuário quando autenticação é válida e principal é User")
        void deveRetornarUsuarioQuandoAutenticacaoEhValidaEPrincipalEhUser() {
            // Given
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(testUser);

            // When
            User result = authenticatedUserProvider.getAuthenticatedUser();

            // Then
            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(testUser);
            assertThat(result.getId()).isEqualTo("test-user-id");
            assertThat(result.getName()).isEqualTo("Test User");
            assertThat(result.getEmail()).isEqualTo("test@example.com");
            
            verify(securityContext).getAuthentication();
            verify(authentication).getPrincipal();
        }

        @Test
        @DisplayName("Deve retornar null quando autenticação é null")
        void deveRetornarNullQuandoAutenticacaoEhNull() {
            // Given
            when(securityContext.getAuthentication()).thenReturn(null);

            // When
            User result = authenticatedUserProvider.getAuthenticatedUser();

            // Then
            assertThat(result).isNull();
            
            verify(securityContext).getAuthentication();
            verify(authentication, never()).getPrincipal();
        }

        @Test
        @DisplayName("Deve retornar null quando principal não é instância de User")
        void deveRetornarNullQuandoPrincipalNaoEhInstanciaDeUser() {
            // Given
            String nonUserPrincipal = "string-principal";
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(nonUserPrincipal);

            // When
            User result = authenticatedUserProvider.getAuthenticatedUser();

            // Then
            assertThat(result).isNull();
            
            verify(securityContext).getAuthentication();
            verify(authentication).getPrincipal();
        }

        @Test
        @DisplayName("Deve retornar null quando principal é UserDetails mas não User")
        void deveRetornarNullQuandoPrincipalEhUserDetailsmasNaoUser() {
            // Given
            CustomUserDetails userDetails = new CustomUserDetails(testUser);
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(userDetails);

            // When
            User result = authenticatedUserProvider.getAuthenticatedUser();

            // Then
            assertThat(result).isNull();
            
            verify(securityContext).getAuthentication();
            verify(authentication).getPrincipal();
        }

        @Test
        @DisplayName("Deve retornar usuário master quando principal é User master")
        void deveRetornarUsuarioMasterQuandoPrincipalEhUserMaster() {
            // Given
            User masterUser = User.builder()
                    .id("master-user-id")
                    .name("Master User")
                    .email("master@example.com")
                    .password("password")
                    .country("Brasil")
                    .city("São Paulo")
                    .isMaster(true)
                    .build();
            
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(masterUser);

            // When
            User result = authenticatedUserProvider.getAuthenticatedUser();

            // Then
            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(masterUser);
            assertThat(result.isMaster()).isTrue();
            
            verify(securityContext).getAuthentication();
            verify(authentication).getPrincipal();
        }
    }

    @Nested
    @DisplayName("Testes de Cenários Extremos")
    class EdgeCaseTests {

        @Test
        @DisplayName("Deve lidar com múltiplas chamadas consecutivas")
        void deveLidarComMultiplasChamadasConsecutivas() {
            // Given
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(testUser);

            // When
            User result1 = authenticatedUserProvider.getAuthenticatedUser();
            User result2 = authenticatedUserProvider.getAuthenticatedUser();
            User result3 = authenticatedUserProvider.getAuthenticatedUser();

            // Then
            assertThat(result1).isEqualTo(testUser);
            assertThat(result2).isEqualTo(testUser);
            assertThat(result3).isEqualTo(testUser);
            
            verify(securityContext, times(3)).getAuthentication();
            verify(authentication, times(3)).getPrincipal();
        }

        @Test
        @DisplayName("Deve lidar com mudança de contexto de segurança")
        void deveLidarComMudancaDeContextoDeSeguranca() {
            // Given - Primeira chamada com usuário
            when(securityContext.getAuthentication()).thenReturn(authentication);
            when(authentication.getPrincipal()).thenReturn(testUser);

            // When - Primeira chamada
            User result1 = authenticatedUserProvider.getAuthenticatedUser();

            // Given - Segunda chamada sem autenticação
            when(securityContext.getAuthentication()).thenReturn(null);

            // When - Segunda chamada
            User result2 = authenticatedUserProvider.getAuthenticatedUser();

            // Then
            assertThat(result1).isEqualTo(testUser);
            assertThat(result2).isNull();
            
            verify(securityContext, times(2)).getAuthentication();
        }
    }
}