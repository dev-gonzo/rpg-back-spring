package com.rpgsystem.rpg.api.controller.character;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpgsystem.rpg.api.dto.character.CharacterAttributeModRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterAttributeRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterAttributeResponse;
import com.rpgsystem.rpg.application.service.character.CharacterAttributesService;
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
@DisplayName("CharacterAttributes Controller Tests")
class CharacterAttributesTest {

    @Mock
    private CharacterAttributesService characterAttributesService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private User testUser;
    private final String characterId = "test-character-id";

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        testUser = createTestUser();
        
        CharacterAttributes characterAttributes = new CharacterAttributes(characterAttributesService);
        mockMvc = MockMvcBuilders.standaloneSetup(characterAttributes)
                .setControllerAdvice(new com.rpgsystem.rpg.api.exception.GlobalExceptionHandler())
                .build();
    }

    private User createTestUser() {
        return User.builder()
                .name("Test User")
                .email("testuser@example.com")
                .password("password")
                .isMaster(false)
                .build();
    }

    @Nested
    @DisplayName("Get Attributes Tests")
    class GetAttributesTests {

        @Test
        @DisplayName("Should return character attributes successfully")
        void shouldReturnCharacterAttributesSuccessfully() throws Exception {
            // Given
            CharacterAttributeResponse response = CharacterAttributeResponse.builder()
                    .con(15)
                    .fr(14)
                    .dex(13)
                    .agi(12)
                    .intel(16)
                    .will(14)
                    .per(13)
                    .car(11)
                    .conMod(2)
                    .frMod(2)
                    .dexMod(1)
                    .agiMod(1)
                    .intMod(3)
                    .willMod(2)
                    .perMod(1)
                    .carMod(0)
                    .build();

            when(characterAttributesService.getCharacterAttributes(eq(characterId), eq(testUser)))
                    .thenReturn(response);

            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(get("/characters/{characterId}/attributes", characterId))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.con").value(15))
                        .andExpect(jsonPath("$.fr").value(14))
                        .andExpect(jsonPath("$.dex").value(13))
                        .andExpect(jsonPath("$.agi").value(12))
                        .andExpect(jsonPath("$.int").value(16))
                        .andExpect(jsonPath("$.will").value(14))
                        .andExpect(jsonPath("$.per").value(13))
                        .andExpect(jsonPath("$.car").value(11))
                        .andExpect(jsonPath("$.conMod").value(2))
                        .andExpect(jsonPath("$.frMod").value(2))
                        .andExpect(jsonPath("$.dexMod").value(1))
                        .andExpect(jsonPath("$.agiMod").value(1))
                        .andExpect(jsonPath("$.intMod").value(3))
                        .andExpect(jsonPath("$.willMod").value(2))
                        .andExpect(jsonPath("$.perMod").value(1))
                        .andExpect(jsonPath("$.carMod").value(0));

                verify(characterAttributesService).getCharacterAttributes(characterId, testUser);
            }
        }

        @Test
        @DisplayName("Should handle service exception when getting attributes")
        void shouldHandleServiceExceptionWhenGettingAttributes() throws Exception {
            // Given
            when(characterAttributesService.getCharacterAttributes(eq(characterId), eq(testUser)))
                    .thenThrow(new RuntimeException("Service error"));

            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(get("/characters/{characterId}/attributes", characterId))
                        .andExpect(status().isInternalServerError());

                verify(characterAttributesService).getCharacterAttributes(characterId, testUser);
            }
        }
    }

    @Nested
    @DisplayName("Save Attributes Tests")
    class SaveAttributesTests {

        @Test
        @DisplayName("Should save character attributes successfully")
        void shouldSaveCharacterAttributesSuccessfully() throws Exception {
            // Given
            CharacterAttributeRequest request = CharacterAttributeRequest.builder()
                    .con(15)
                    .fr(14)
                    .dex(13)
                    .agi(12)
                    .intel(16)
                    .will(14)
                    .per(13)
                    .car(11)
                    .build();

            CharacterAttributeResponse response = CharacterAttributeResponse.builder()
                    .con(15)
                    .fr(14)
                    .dex(13)
                    .agi(12)
                    .intel(16)
                    .will(14)
                    .per(13)
                    .car(11)
                    .conMod(2)
                    .frMod(2)
                    .dexMod(1)
                    .agiMod(1)
                    .intMod(3)
                    .willMod(2)
                    .perMod(1)
                    .carMod(0)
                    .build();

            when(characterAttributesService.save(any(CharacterAttributeRequest.class), eq(characterId), eq(testUser)))
                    .thenReturn(response);

            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(post("/characters/{characterId}/attributes", characterId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.con").value(15))
                        .andExpect(jsonPath("$.fr").value(14))
                        .andExpect(jsonPath("$.dex").value(13))
                        .andExpect(jsonPath("$.agi").value(12))
                        .andExpect(jsonPath("$.int").value(16))
                        .andExpect(jsonPath("$.will").value(14))
                        .andExpect(jsonPath("$.per").value(13))
                        .andExpect(jsonPath("$.car").value(11));

                verify(characterAttributesService).save(any(CharacterAttributeRequest.class), eq(characterId), eq(testUser));
            }
        }

        @Test
        @DisplayName("Should return 400 when request body is invalid")
        void shouldReturn400WhenRequestBodyIsInvalid() throws Exception {
            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(post("/characters/{characterId}/attributes", characterId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"invalid\": \"json\"}"))
                        .andExpect(status().isBadRequest());

                verify(characterAttributesService, never()).save(any(), any(), any());
            }
        }
    }

    @Nested
    @DisplayName("Save Attribute Modifiers Tests")
    class SaveAttributeModifiersTests {

        @Test
        @DisplayName("Should save character attribute modifiers successfully")
        void shouldSaveCharacterAttributeModifiersSuccessfully() throws Exception {
            // Given
            CharacterAttributeModRequest request = CharacterAttributeModRequest.builder()
                    .characterId(characterId)
                    .conMod(1)
                    .frMod(2)
                    .dexMod(0)
                    .agiMod(-1)
                    .intMod(3)
                    .willMod(1)
                    .perMod(2)
                    .carMod(0)
                    .build();

            CharacterAttributeResponse response = CharacterAttributeResponse.builder()
                    .con(15)
                    .fr(14)
                    .dex(13)
                    .agi(12)
                    .intel(16)
                    .will(14)
                    .per(13)
                    .car(11)
                    .conMod(3) // base + mod
                    .frMod(4)
                    .dexMod(1)
                    .agiMod(0)
                    .intMod(6)
                    .willMod(3)
                    .perMod(3)
                    .carMod(0)
                    .build();

            when(characterAttributesService.saveMod(any(CharacterAttributeModRequest.class), eq(characterId), eq(testUser)))
                    .thenReturn(response);

            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(post("/characters/{characterId}/attributes/mod", characterId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.conMod").value(3))
                        .andExpect(jsonPath("$.frMod").value(4))
                        .andExpect(jsonPath("$.dexMod").value(1))
                        .andExpect(jsonPath("$.agiMod").value(0))
                        .andExpect(jsonPath("$.intMod").value(6))
                        .andExpect(jsonPath("$.willMod").value(3))
                        .andExpect(jsonPath("$.perMod").value(3))
                        .andExpect(jsonPath("$.carMod").value(0));

                verify(characterAttributesService).saveMod(any(CharacterAttributeModRequest.class), eq(characterId), eq(testUser));
            }
        }

        @Test
        @DisplayName("Should return 400 when modifier request is invalid")
        void shouldReturn400WhenModifierRequestIsInvalid() throws Exception {
            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(post("/characters/{characterId}/attributes/mod", characterId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"invalid\": \"json\"}"))
                        .andExpect(status().isBadRequest());

                verify(characterAttributesService, never()).saveMod(any(), any(), any());
            }
        }
    }
}