package com.rpgsystem.rpg.api.controller.user;

import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.infrastructure.security.AuthenticatedUserProvider;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do UserController")
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthenticatedUserProvider userProvider;

    private ObjectMapper objectMapper = new ObjectMapper();

    private User testUser;

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
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
        
        UserController userController = new UserController(userProvider);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Nested
    @DisplayName("Testes do endpoint GET /user/me")
    class GetLoggedUserInfoTests {

        @Test
        @DisplayName("Deve retornar informações do usuário logado quando autenticado")
        void deveRetornarInformacoesDoUsuarioLogadoQuandoAutenticado() throws Exception {
            // Given
            when(userProvider.getAuthenticatedUser()).thenReturn(testUser);

            // When & Then
            mockMvc.perform(get("/user/me")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(testUser.getId()))
                    .andExpect(jsonPath("$.name").value(testUser.getName()))
                    .andExpect(jsonPath("$.email").value(testUser.getEmail()))
                    .andExpect(jsonPath("$.isMaster").value(testUser.isMaster()));
        }

        @Test
        @DisplayName("Deve retornar isMaster true quando usuário é MASTER")
        void deveRetornarIsMasterTrueQuandoUsuarioEMaster() throws Exception {
            // Given
            testUser.setMaster(true);
            when(userProvider.getAuthenticatedUser()).thenReturn(testUser);

            // When & Then
            mockMvc.perform(get("/user/me")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.isMaster").value(true));
        }

        @Test
        @DisplayName("Deve retornar isMaster false quando usuário é PLAYER")
        void deveRetornarIsMasterFalseQuandoUsuarioEPlayer() throws Exception {
            // Given
            testUser.setMaster(false);
            when(userProvider.getAuthenticatedUser()).thenReturn(testUser);

            // When & Then
            mockMvc.perform(get("/user/me")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.isMaster").value(false));
        }

        @Test
        @DisplayName("Deve retornar estrutura JSON correta com todos os campos")
        void deveRetornarEstruturaJsonCorretaComTodosOsCampos() throws Exception {
            // Given
            when(userProvider.getAuthenticatedUser()).thenReturn(testUser);

            // When & Then
            mockMvc.perform(get("/user/me")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.name").exists())
                    .andExpect(jsonPath("$.email").exists())
                    .andExpect(jsonPath("$.isMaster").exists())
                    .andExpect(jsonPath("$.id").isString())
                    .andExpect(jsonPath("$.name").isString())
                    .andExpect(jsonPath("$.email").isString())
                    .andExpect(jsonPath("$.isMaster").isBoolean());
        }

        @Test
        @DisplayName("Deve não incluir campos sensíveis na resposta")
        void deveNaoIncluirCamposSensiveisNaResposta() throws Exception {
            // Given
            when(userProvider.getAuthenticatedUser()).thenReturn(testUser);

            // When & Then
            mockMvc.perform(get("/user/me")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.password").doesNotExist())
                    .andExpect(jsonPath("$.createdAt").doesNotExist())
                    .andExpect(jsonPath("$.updatedAt").doesNotExist())
                    .andExpect(jsonPath("$.country").doesNotExist())
                    .andExpect(jsonPath("$.city").doesNotExist());
        }

        @Test
        @DisplayName("Deve retornar 401 quando usuário não está autenticado")
        void deveRetornar401QuandoUsuarioNaoEstaAutenticado() throws Exception {
            // Given
            when(userProvider.getAuthenticatedUser()).thenReturn(null);

            // When & Then
            mockMvc.perform(get("/user/me")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Deve usar método GET correto")
        void deveUsarMetodoGetCorreto() throws Exception {
            // Given
            when(userProvider.getAuthenticatedUser()).thenReturn(testUser);

            // When & Then
            mockMvc.perform(get("/user/me"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Deve aceitar requisições com diferentes Content-Types")
        void deveAceitarRequisicoesComDiferentesContentTypes() throws Exception {
            // Given
            when(userProvider.getAuthenticatedUser()).thenReturn(testUser);

            // When & Then - Sem Content-Type
            mockMvc.perform(get("/user/me"))
                    .andExpect(status().isOk());

            // When & Then - Com Content-Type JSON
            mockMvc.perform(get("/user/me")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

            // When & Then - Com Content-Type Text
            mockMvc.perform(get("/user/me")
                    .contentType(MediaType.TEXT_PLAIN))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("Testes de integração do endpoint")
    class IntegrationTests {

        @Test
        @DisplayName("Deve retornar dados consistentes em múltiplas chamadas")
        void deveRetornarDadosConsistentesEmMultiplasChamadas() throws Exception {
            // Given
            when(userProvider.getAuthenticatedUser()).thenReturn(testUser);

            // When & Then - Primeira chamada
            String response1 = mockMvc.perform(get("/user/me"))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            // When & Then - Segunda chamada
            String response2 = mockMvc.perform(get("/user/me"))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

            // Then
            assert response1.equals(response2);
        }

        @Test
        @DisplayName("Deve funcionar com diferentes tipos de usuários")
        void deveFuncionarComDiferentesTiposDeUsuarios() throws Exception {
            // Given - Usuário PLAYER
            User playerUser = User.builder()
                    .id("player-id")
                    .name("Player User")
                    .email("player@example.com")
                    .password("password")
                    .isMaster(false)
                    .build();

            when(userProvider.getAuthenticatedUser()).thenReturn(playerUser);

            // When & Then - Player
            mockMvc.perform(get("/user/me"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.isMaster").value(false));

            // Given - Usuário MASTER
            User masterUser = User.builder()
                    .id("master-id")
                    .name("Master User")
                    .email("master@example.com")
                    .password("password")
                    .isMaster(true)
                    .build();

            when(userProvider.getAuthenticatedUser()).thenReturn(masterUser);

            // When & Then - Master
            mockMvc.perform(get("/user/me"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.isMaster").value(true));
        }
    }
}