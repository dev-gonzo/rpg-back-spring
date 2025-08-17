package com.rpgsystem.rpg.api.controller.character;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpgsystem.rpg.api.dto.UserSimpleResponse;
import com.rpgsystem.rpg.api.dto.character.CharacterHomeDto;
import com.rpgsystem.rpg.application.service.character.CharacterHomeService;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.infrastructure.security.AuthenticatedUserProvider;
import com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do CharacterHomeController")
class CharacterHomeControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private CharacterHomeService characterHomeService;

    @Mock
    private AuthenticatedUserProvider userProvider;

    private User testUser;
    private User masterUser;
    private CharacterHomeDto playerCharacter;
    private CharacterHomeDto npcCharacter;
    private List<CharacterHomeDto> characterList;

    @BeforeEach
    void setUp() {
        CharacterHomeController controller = new CharacterHomeController(characterHomeService, userProvider);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new com.rpgsystem.rpg.api.exception.GlobalExceptionHandler())
                .build();
        testUser = User.builder()
                .id(UUID.randomUUID().toString())
                .name("Test User")
                .email("test@example.com")
                .password("password")
                .isMaster(false)
                .build();

        masterUser = User.builder()
                .id(UUID.randomUUID().toString())
                .name("Master User")
                .email("master@example.com")
                .password("password")
                .isMaster(true)
                .build();

        UserSimpleResponse controlUser = UserSimpleResponse.builder()
                .id(testUser.getId())
                .name(testUser.getName())
                .email(testUser.getEmail())
                .build();

        playerCharacter = CharacterHomeDto.builder()
                .id("player-char-id")
                .name("Player Character")
                .age(25)
                .apparentAge(23)
                .profession("Warrior")
                .hitPoints(100)
                .currentHitPoints(85)
                .initiative(15)
                .currentInitiative(15)
                .heroPoints(3)
                .currentHeroPoints(2)
                .magicPoints(0)
                .currentMagicPoints(0)
                .faithPoints(0)
                .currentFaithPoints(0)
                .protectionIndex(5)
                .currentProtectionIndex(5)
                .image("/images/player-char.jpg")
                .controlUser(controlUser)
                .edit(true)
                .build();

        npcCharacter = CharacterHomeDto.builder()
                .id("npc-char-id")
                .name("NPC Character")
                .age(40)
                .apparentAge(40)
                .profession("Merchant")
                .hitPoints(50)
                .currentHitPoints(50)
                .initiative(10)
                .currentInitiative(10)
                .heroPoints(0)
                .currentHeroPoints(0)
                .magicPoints(20)
                .currentMagicPoints(15)
                .faithPoints(10)
                .currentFaithPoints(8)
                .protectionIndex(2)
                .currentProtectionIndex(2)
                .image("/images/npc-char.jpg")
                .controlUser(null)
                .edit(false)
                .build();

        characterList = Arrays.asList(playerCharacter, npcCharacter);
    }

    @Nested
    @DisplayName("Testes do endpoint GET /characters")
    class ListAllTests {

        @Test
        @DisplayName("Deve retornar lista de personagens com sucesso para usuário autenticado")
        void deveRetornarListaDePersonagensComSucessoParaUsuarioAutenticado() throws Exception {
            // Given
            when(userProvider.getAuthenticatedUser()).thenReturn(testUser);
            when(characterHomeService.listByUser(testUser)).thenReturn(characterList);

                // When & Then
                mockMvc.perform(get("/characters")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$", hasSize(2)))
                        .andExpect(jsonPath("$[0].id").value(playerCharacter.getId()))
                        .andExpect(jsonPath("$[0].name").value(playerCharacter.getName()))
                        .andExpect(jsonPath("$[0].profession").value(playerCharacter.getProfession()))
                        .andExpect(jsonPath("$[0].age").value(playerCharacter.getAge()))
                        .andExpect(jsonPath("$[0].apparentAge").value(playerCharacter.getApparentAge()))
                        .andExpect(jsonPath("$[0].hitPoints").value(playerCharacter.getHitPoints()))
                        .andExpect(jsonPath("$[0].currentHitPoints").value(playerCharacter.getCurrentHitPoints()))
                        .andExpect(jsonPath("$[0].controlUser.id").value(testUser.getId()))
                        .andExpect(jsonPath("$[0].edit").value(true))
                        .andExpect(jsonPath("$[1].id").value(npcCharacter.getId()))
                        .andExpect(jsonPath("$[1].name").value(npcCharacter.getName()))
                        .andExpect(jsonPath("$[1].controlUser").isEmpty())
                        .andExpect(jsonPath("$[1].edit").value(false));

                verify(characterHomeService).listByUser(testUser);
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando usuário não possui personagens")
        void deveRetornarListaVaziaQuandoUsuarioNaoPossuiPersonagens() throws Exception {
            // Given
            when(userProvider.getAuthenticatedUser()).thenReturn(testUser);
            when(characterHomeService.listByUser(testUser)).thenReturn(Collections.emptyList());

            // When & Then
            mockMvc.perform(get("/characters")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(0)));

            verify(characterHomeService).listByUser(testUser);
        }

        @Test
        @DisplayName("Deve retornar personagens específicos para usuário MASTER")
        void deveRetornarPersonagensEspecificosParaUsuarioMaster() throws Exception {
            // Given
            CharacterHomeDto masterCharacter = CharacterHomeDto.builder()
                    .id("master-char-id")
                    .name("Master Character")
                    .age(50)
                    .apparentAge(45)
                    .profession("Archmage")
                    .hitPoints(200)
                    .currentHitPoints(200)
                    .initiative(20)
                    .currentInitiative(20)
                    .heroPoints(5)
                    .currentHeroPoints(5)
                    .magicPoints(100)
                    .currentMagicPoints(80)
                    .faithPoints(50)
                    .currentFaithPoints(40)
                    .protectionIndex(10)
                    .currentProtectionIndex(10)
                    .image("/images/master-char.jpg")
                    .controlUser(null)
                    .edit(true)
                    .build();

            List<CharacterHomeDto> masterCharacterList = Arrays.asList(masterCharacter, npcCharacter);

            when(userProvider.getAuthenticatedUser()).thenReturn(masterUser);
            when(characterHomeService.listByUser(masterUser)).thenReturn(masterCharacterList);

            // When & Then
            mockMvc.perform(get("/characters")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].id").value(masterCharacter.getId()))
                    .andExpect(jsonPath("$[0].name").value(masterCharacter.getName()))
                    .andExpect(jsonPath("$[0].profession").value(masterCharacter.getProfession()))
                    .andExpect(jsonPath("$[0].controlUser").isEmpty())
                    .andExpect(jsonPath("$[0].edit").value(true));

            verify(characterHomeService).listByUser(masterUser);
        }

        @Test
        @DisplayName("Deve validar estrutura completa do JSON de resposta")
        void deveValidarEstruturaCompletaDoJsonDeResposta() throws Exception {
            // Given
            when(userProvider.getAuthenticatedUser()).thenReturn(testUser);
            when(characterHomeService.listByUser(testUser)).thenReturn(Arrays.asList(playerCharacter));

            // When & Then
            mockMvc.perform(get("/characters")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").exists())
                    .andExpect(jsonPath("$[0].name").exists())
                    .andExpect(jsonPath("$[0].age").exists())
                    .andExpect(jsonPath("$[0].apparentAge").exists())
                    .andExpect(jsonPath("$[0].profession").exists())
                    .andExpect(jsonPath("$[0].hitPoints").exists())
                    .andExpect(jsonPath("$[0].currentHitPoints").exists())
                    .andExpect(jsonPath("$[0].initiative").exists())
                    .andExpect(jsonPath("$[0].currentInitiative").exists())
                    .andExpect(jsonPath("$[0].heroPoints").exists())
                    .andExpect(jsonPath("$[0].currentHeroPoints").exists())
                    .andExpect(jsonPath("$[0].magicPoints").exists())
                    .andExpect(jsonPath("$[0].currentMagicPoints").exists())
                    .andExpect(jsonPath("$[0].faithPoints").exists())
                    .andExpect(jsonPath("$[0].currentFaithPoints").exists())
                    .andExpect(jsonPath("$[0].protectionIndex").exists())
                    .andExpect(jsonPath("$[0].currentProtectionIndex").exists())
                    .andExpect(jsonPath("$[0].image").exists())
                    .andExpect(jsonPath("$[0].controlUser").exists())
                    .andExpect(jsonPath("$[0].edit").exists());
        }

        @Test
        @DisplayName("Deve retornar 401 quando usuário não está autenticado")
        void deveRetornar401QuandoUsuarioNaoEstaAutenticado() throws Exception {
            // When & Then
            mockMvc.perform(get("/characters")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());

            verify(characterHomeService, never()).listByUser(any());
        }

        @Test
        @DisplayName("Deve aceitar diferentes Content-Types")
        void deveAceitarDiferentesContentTypes() throws Exception {
            // Given
            when(userProvider.getAuthenticatedUser()).thenReturn(testUser);
            when(characterHomeService.listByUser(testUser)).thenReturn(characterList);

            // When & Then - Sem Content-Type
            mockMvc.perform(get("/characters"))
                    .andExpect(status().isOk());

            // When & Then - Com Accept header
            mockMvc.perform(get("/characters")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }

        @Test
        @DisplayName("Deve usar método HTTP GET correto")
        void deveUsarMetodoHttpGetCorreto() throws Exception {
            // Given
            when(userProvider.getAuthenticatedUser()).thenReturn(testUser);
            when(characterHomeService.listByUser(testUser)).thenReturn(characterList);

            // When & Then
            mockMvc.perform(get("/characters"))
                    .andExpect(status().isOk());

            verify(characterHomeService).listByUser(testUser);
        }
    }

    @Nested
    @DisplayName("Testes de integração com CharacterHomeService")
    class IntegrationTests {

        @Test
        @DisplayName("Deve chamar service com usuário correto obtido do userProvider")
        void deveChamarServiceComUsuarioCorretoObtidoDoUserProvider() throws Exception {
            // Given
            when(userProvider.getAuthenticatedUser()).thenReturn(testUser);
            when(characterHomeService.listByUser(testUser)).thenReturn(characterList);

            // When
            mockMvc.perform(get("/characters"))
                    .andExpect(status().isOk());

            // Then
            verify(userProvider, times(1)).getAuthenticatedUser();
            verify(characterHomeService).listByUser(testUser);
        }

        @Test
        @DisplayName("Deve propagar exceções do service corretamente")
        void devePropagrarExcecoesDoServiceCorretamente() throws Exception {
            // Given
            when(userProvider.getAuthenticatedUser()).thenReturn(testUser);
            when(characterHomeService.listByUser(testUser))
                    .thenThrow(new RuntimeException("Service error"));

            // When & Then
            mockMvc.perform(get("/characters"))
                    .andExpect(status().is5xxServerError());

            verify(characterHomeService).listByUser(testUser);
        }

        @Test
        @DisplayName("Deve retornar dados consistentes entre múltiplas chamadas")
        void deveRetornarDadosConsistentesEntreMultiplasChamadas() throws Exception {
            // Given
            when(userProvider.getAuthenticatedUser()).thenReturn(testUser);
            when(characterHomeService.listByUser(testUser)).thenReturn(characterList);

            // When & Then - Primeira chamada
            mockMvc.perform(get("/characters"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)));

            // When & Then - Segunda chamada
            mockMvc.perform(get("/characters"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)));

            verify(characterHomeService, times(2)).listByUser(testUser);
        }
    }

    @Nested
    @DisplayName("Testes de validação de tipos de dados")
    class DataTypeValidationTests {

        @Test
        @DisplayName("Deve retornar tipos de dados corretos para campos numéricos")
        void deveRetornarTiposDeDadosCorretosParaCamposNumericos() throws Exception {
            // Given
            when(userProvider.getAuthenticatedUser()).thenReturn(testUser);
            when(characterHomeService.listByUser(testUser)).thenReturn(Arrays.asList(playerCharacter));

            // When & Then
            mockMvc.perform(get("/characters"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].age").isNumber())
                    .andExpect(jsonPath("$[0].apparentAge").isNumber())
                    .andExpect(jsonPath("$[0].hitPoints").isNumber())
                    .andExpect(jsonPath("$[0].currentHitPoints").isNumber())
                    .andExpect(jsonPath("$[0].initiative").isNumber())
                    .andExpect(jsonPath("$[0].currentInitiative").isNumber())
                    .andExpect(jsonPath("$[0].heroPoints").isNumber())
                    .andExpect(jsonPath("$[0].currentHeroPoints").isNumber())
                    .andExpect(jsonPath("$[0].magicPoints").isNumber())
                    .andExpect(jsonPath("$[0].currentMagicPoints").isNumber())
                    .andExpect(jsonPath("$[0].faithPoints").isNumber())
                    .andExpect(jsonPath("$[0].currentFaithPoints").isNumber())
                    .andExpect(jsonPath("$[0].protectionIndex").isNumber())
                    .andExpect(jsonPath("$[0].currentProtectionIndex").isNumber());
        }

        @Test
        @DisplayName("Deve retornar tipos de dados corretos para campos de texto")
        void deveRetornarTiposDeDadosCorretosParaCamposDeTexto() throws Exception {
            // Given
            when(userProvider.getAuthenticatedUser()).thenReturn(testUser);
            when(characterHomeService.listByUser(testUser)).thenReturn(Arrays.asList(playerCharacter));

            // When & Then
            mockMvc.perform(get("/characters"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").isString())
                    .andExpect(jsonPath("$[0].name").isString())
                    .andExpect(jsonPath("$[0].profession").isString())
                    .andExpect(jsonPath("$[0].image").isString());
        }

        @Test
        @DisplayName("Deve retornar tipo boolean para campo edit")
        void deveRetornarTipoBooleanParaCampoEdit() throws Exception {
            // Given
            when(userProvider.getAuthenticatedUser()).thenReturn(testUser);
            when(characterHomeService.listByUser(testUser)).thenReturn(Arrays.asList(playerCharacter));

            // When & Then
            mockMvc.perform(get("/characters"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].edit").isBoolean())
                    .andExpect(jsonPath("$[0].edit").value(true));
        }
    }
}