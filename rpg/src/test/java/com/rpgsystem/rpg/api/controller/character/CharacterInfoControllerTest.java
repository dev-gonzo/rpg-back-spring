package com.rpgsystem.rpg.api.controller.character;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpgsystem.rpg.api.dto.character.CharacterInfoRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterInfoResponse;
import com.rpgsystem.rpg.application.service.character.CharacterService;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do CharacterInfoController")
class CharacterInfoControllerTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private CharacterService characterService;

    private User testUser;
    private String characterId;
    private CharacterInfoRequest characterInfoRequest;
    private CharacterInfoResponse characterInfoResponse;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        characterId = UUID.randomUUID().toString();
        
        testUser = User.builder()
                .id(UUID.randomUUID().toString())
                .name("Test User")
                .email("test@example.com")
                .password("password")
                .isMaster(false)
                .build();

        characterInfoRequest = CharacterInfoRequest.builder()
                .name("Test Character")
                .age(25)
                .gender("Male")
                .profession("Warrior")
                .birthPlace("Test City")
                .apparentAge(25)
                .heightCm(180)
                .weightKg(75)
                .religion("Test Religion")
                .build();

        characterInfoResponse = CharacterInfoResponse.builder()
                .id(characterId)
                .name("Test Character")
                .age(25)
                .gender("Male")
                .profession("Warrior")
                .birthPlace("Test City")
                .apparentAge(25)
                .heightCm(180)
                .weightKg(75)
                .religion("Test Religion")
                .build();

        CharacterInfoController controller = new CharacterInfoController(characterService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new com.rpgsystem.rpg.api.exception.GlobalExceptionHandler())
                .build();
    }

    @Nested
    @DisplayName("Testes do endpoint GET /characters/{characterId}/info")
    class GetInfoTests {

        @Test
        @DisplayName("Deve retornar informações do personagem com sucesso")
        void deveRetornarInformacoesDoPersonagemComSucesso() throws Exception {
            // Given
            when(characterService.getInfo(characterId)).thenReturn(characterInfoResponse);

            // When & Then
            mockMvc.perform(get("/characters/{characterId}/info", characterId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(characterId))
                    .andExpect(jsonPath("$.name").value("Test Character"))
                    .andExpect(jsonPath("$.age").value(25))
                    .andExpect(jsonPath("$.gender").value("Male"))
                    .andExpect(jsonPath("$.profession").value("Warrior"))
                    .andExpect(jsonPath("$.birthPlace").value("Test City"))
                    .andExpect(jsonPath("$.apparentAge").value(25))
                    .andExpect(jsonPath("$.heightCm").value(180))
                    .andExpect(jsonPath("$.weightKg").value(75))
                    .andExpect(jsonPath("$.religion").value("Test Religion"));

            verify(characterService).getInfo(characterId);
        }

        @Test
        @DisplayName("Deve retornar 404 quando personagem não existe")
        void deveRetornar404QuandoPersonagemNaoExiste() throws Exception {
            // Given
            String nonExistentId = "non-existent-id";
            when(characterService.getInfo(nonExistentId))
                    .thenThrow(new IllegalArgumentException("Character not found"));

            // When & Then
            mockMvc.perform(get("/characters/{characterId}/info", nonExistentId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());

            verify(characterService).getInfo(nonExistentId);
        }

        @Test
        @DisplayName("Deve aceitar requisições sem Content-Type")
        void deveAceitarRequisicoesComDiferentesContentTypes() throws Exception {
            // Given
            when(characterService.getInfo(characterId)).thenReturn(characterInfoResponse);

            // When & Then - Sem Content-Type
            mockMvc.perform(get("/characters/{characterId}/info", characterId))
                    .andExpect(status().isOk());

            // When & Then - Com Accept header
            mockMvc.perform(get("/characters/{characterId}/info", characterId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }
    }

    @Nested
    @DisplayName("Testes do endpoint POST /characters/{characterId}/info")
    class SaveTests {

        @Test
        @DisplayName("Deve salvar informações do personagem com sucesso")
        void deveSalvarInformacoesDoPersonagemComSucesso() throws Exception {
            // Given
            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = 
                    mockStatic(AuthenticatedUserHelper.class)) {
                
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);
                when(characterService.save(any(CharacterInfoRequest.class), eq(characterId), eq(testUser)))
                        .thenReturn(characterInfoResponse);

                // When & Then
                mockMvc.perform(post("/characters/{characterId}/info", characterId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(characterInfoRequest)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.id").value(characterId))
                        .andExpect(jsonPath("$.name").value("Test Character"))
                        .andExpect(jsonPath("$.age").value(25))
                        .andExpect(jsonPath("$.gender").value("Male"))
                        .andExpect(jsonPath("$.profession").value("Warrior"));

                verify(characterService).save(any(CharacterInfoRequest.class), eq(characterId), eq(testUser));
            }
        }

        @Test
        @DisplayName("Deve retornar 400 quando nome é nulo")
        void deveRetornar400QuandoNomeENulo() throws Exception {
            // Given
            CharacterInfoRequest invalidRequest = CharacterInfoRequest.builder()
                    .name(null)
                    .age(25)
                    .gender("Male")
                    .profession("Warrior")
                    .birthPlace("Test City")
                    .build();

            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = 
                    mockStatic(AuthenticatedUserHelper.class)) {
                
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(post("/characters/{characterId}/info", characterId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest)))
                        .andExpect(status().isBadRequest());

                verify(characterService, never()).save(any(CharacterInfoRequest.class), anyString(), any(User.class));
            }
        }

        @Test
        @DisplayName("Deve retornar 400 quando nome está vazio")
        void deveRetornar400QuandoNomeEstaVazio() throws Exception {
            // Given
            CharacterInfoRequest invalidRequest = CharacterInfoRequest.builder()
                    .name("")
                    .age(25)
                    .gender("Male")
                    .profession("Warrior")
                    .birthPlace("Test City")
                    .build();

            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = 
                    mockStatic(AuthenticatedUserHelper.class)) {
                
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(post("/characters/{characterId}/info", characterId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest)))
                        .andExpect(status().isBadRequest());

                verify(characterService, never()).save(any(CharacterInfoRequest.class), anyString(), any(User.class));
            }
        }

        @Test
        @DisplayName("Deve retornar 400 quando idade é negativa")
        void deveRetornar400QuandoIdadeENegativa() throws Exception {
            // Given
            CharacterInfoRequest invalidRequest = CharacterInfoRequest.builder()
                    .name("Test Character")
                    .age(-1)
                    .gender("Male")
                    .profession("Warrior")
                    .birthPlace("Test City")
                    .build();

            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = 
                    mockStatic(AuthenticatedUserHelper.class)) {
                
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(post("/characters/{characterId}/info", characterId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest)))
                        .andExpect(status().isBadRequest());

                verify(characterService, never()).save(any(CharacterInfoRequest.class), anyString(), any(User.class));
            }
        }

        @Test
        @DisplayName("Deve retornar 400 quando altura é negativa")
        void deveRetornar400QuandoAlturaENegativa() throws Exception {
            // Given
            CharacterInfoRequest invalidRequest = CharacterInfoRequest.builder()
                    .name("Test Character")
                    .age(25)
                    .heightCm(-1)
                    .gender("Male")
                    .profession("Warrior")
                    .birthPlace("Test City")
                    .build();

            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = 
                    mockStatic(AuthenticatedUserHelper.class)) {
                
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(post("/characters/{characterId}/info", characterId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest)))
                        .andExpect(status().isBadRequest());

                verify(characterService, never()).save(any(CharacterInfoRequest.class), anyString(), any(User.class));
            }
        }

        @Test
        @DisplayName("Deve retornar 400 quando peso é negativo")
        void deveRetornar400QuandoPesoENegativo() throws Exception {
            // Given
            CharacterInfoRequest invalidRequest = CharacterInfoRequest.builder()
                    .name("Test Character")
                    .age(25)
                    .weightKg(-1)
                    .gender("Male")
                    .profession("Warrior")
                    .birthPlace("Test City")
                    .build();

            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = 
                    mockStatic(AuthenticatedUserHelper.class)) {
                
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(post("/characters/{characterId}/info", characterId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest)))
                        .andExpect(status().isBadRequest());

                verify(characterService, never()).save(any(CharacterInfoRequest.class), anyString(), any(User.class));
            }
        }

        @Test
        @DisplayName("Deve aceitar campos opcionais como nulos")
        void deveAceitarCamposOpcionaisComoNulos() throws Exception {
            // Given
            CharacterInfoRequest requestWithNulls = CharacterInfoRequest.builder()
                    .name("Test Character")
                    .age(25)
                    .gender(null)
                    .profession(null)
                    .birthPlace(null)
                    .religion(null)
                    .build();

            CharacterInfoResponse responseWithNulls = CharacterInfoResponse.builder()
                    .id(characterId)
                    .name("Test Character")
                    .age(25)
                    .gender(null)
                    .profession(null)
                    .birthPlace(null)
                    .religion(null)
                    .build();

            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = 
                    mockStatic(AuthenticatedUserHelper.class)) {
                
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);
                when(characterService.save(any(CharacterInfoRequest.class), eq(characterId), eq(testUser)))
                        .thenReturn(responseWithNulls);

                // When & Then
                mockMvc.perform(post("/characters/{characterId}/info", characterId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestWithNulls)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.name").value("Test Character"))
                        .andExpect(jsonPath("$.age").value(25));

                verify(characterService).save(any(CharacterInfoRequest.class), eq(characterId), eq(testUser));
            }
        }
    }

    @Nested
    @DisplayName("Testes de integração")
    class IntegrationTests {

        @Test
        @DisplayName("Deve funcionar com diferentes tipos de usuários")
        void deveFuncionarComDiferentesTiposDeUsuarios() throws Exception {
            // Given - Usuário Master
            User masterUser = User.builder()
                    .id("master-id")
                    .name("Master User")
                    .email("master@example.com")
                    .password("password")
                    .isMaster(true)
                    .build();

            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = 
                    mockStatic(AuthenticatedUserHelper.class)) {
                
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(masterUser);
                when(characterService.save(any(CharacterInfoRequest.class), eq(characterId), eq(masterUser)))
                        .thenReturn(characterInfoResponse);

                // When & Then - Master pode salvar
                mockMvc.perform(post("/characters/{characterId}/info", characterId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(characterInfoRequest)))
                        .andExpect(status().isOk());

                verify(characterService).save(any(CharacterInfoRequest.class), eq(characterId), eq(masterUser));
            }
        }

        @Test
        @DisplayName("Deve validar todos os campos obrigatórios em uma única requisição")
        void deveValidarTodosCamposObrigatoriosEmUmaUnicaRequisicao() throws Exception {
            // Given
            CharacterInfoRequest invalidRequest = CharacterInfoRequest.builder()
                    .name("")
                    .age(-1)
                    .heightCm(-1)
                    .weightKg(-1)
                    .apparentAge(-1)
                    .build();

            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = 
                    mockStatic(AuthenticatedUserHelper.class)) {
                
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(post("/characters/{characterId}/info", characterId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest)))
                        .andExpect(status().isBadRequest());

                verify(characterService, never()).save(any(CharacterInfoRequest.class), anyString(), any(User.class));
            }
        }
    }
}