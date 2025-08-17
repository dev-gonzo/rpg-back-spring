package com.rpgsystem.rpg.api.controller.character;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpgsystem.rpg.api.dto.character.CharacterBackgroundRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterBackgroundResponse;
import com.rpgsystem.rpg.application.service.character.CharacterBackgroundService;
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
@DisplayName("CharacterBackgroundController Tests")
class CharacterBackgroundControllerTest {

    @Mock
    private CharacterBackgroundService characterBackgroundService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private User testUser;
    private final String characterId = "test-character-id";
    private final String backgroundId = "test-background-id";

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        testUser = createTestUser();
        
        CharacterBackgroundController characterBackgroundController = new CharacterBackgroundController(characterBackgroundService);
        mockMvc = MockMvcBuilders.standaloneSetup(characterBackgroundController)
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
    @DisplayName("Get Background Tests")
    class GetBackgroundTests {

        @Test
        @DisplayName("Should return character background successfully")
        void shouldReturnCharacterBackgroundSuccessfully() throws Exception {
            // Given
            CharacterBackgroundResponse response = CharacterBackgroundResponse.builder()
                    .id(backgroundId)
                    .characterId(characterId)
                    .title("Character Origin")
                    .text("Born in a small village, the character learned the ways of combat from an early age.")
                    .isPublic(true)
                    .build();

            when(characterBackgroundService.get(eq(characterId), eq(testUser)))
                    .thenReturn(response);

            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(get("/characters/{characterId}/background", characterId))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.id").value(backgroundId))
                        .andExpect(jsonPath("$.characterId").value(characterId))
                        .andExpect(jsonPath("$.title").value("Character Origin"))
                        .andExpect(jsonPath("$.text").value("Born in a small village, the character learned the ways of combat from an early age."))
                        .andExpect(jsonPath("$.isPublic").value(true));

                verify(characterBackgroundService).get(characterId, testUser);
            }
        }

        @Test
        @DisplayName("Should handle service exception when getting background")
        void shouldHandleServiceExceptionWhenGettingBackground() throws Exception {
            // Given
            when(characterBackgroundService.get(eq(characterId), eq(testUser)))
                    .thenThrow(new RuntimeException("Service error"));

            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(get("/characters/{characterId}/background", characterId))
                        .andExpect(status().isInternalServerError());

                verify(characterBackgroundService).get(characterId, testUser);
            }
        }
    }

    @Nested
    @DisplayName("Save Background Tests")
    class SaveBackgroundTests {

        @Test
        @DisplayName("Should save character background successfully")
        void shouldSaveCharacterBackgroundSuccessfully() throws Exception {
            // Given
            CharacterBackgroundRequest request = CharacterBackgroundRequest.builder()
                    .title("Character Origin")
                    .text("Born in a small village, the character learned the ways of combat from an early age.")
                    .isPublic(true)
                    .build();

            CharacterBackgroundResponse response = CharacterBackgroundResponse.builder()
                    .id(backgroundId)
                    .characterId(characterId)
                    .title("Character Origin")
                     .text("Born in a small village, the character learned the ways of combat from an early age.")
                     .isPublic(true)
                    .build();

            when(characterBackgroundService.save(eq(characterId), eq(backgroundId), any(CharacterBackgroundRequest.class), eq(testUser)))
                    .thenReturn(response);

            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(post("/characters/{characterId}/background/{id}", characterId, backgroundId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.id").value(backgroundId))
                        .andExpect(jsonPath("$.characterId").value(characterId))
                        .andExpect(jsonPath("$.title").value("Character Origin"))
                         .andExpect(jsonPath("$.text").value("Born in a small village, the character learned the ways of combat from an early age."))
                         .andExpect(jsonPath("$.isPublic").value(true));

                verify(characterBackgroundService).save(eq(characterId), eq(backgroundId), any(CharacterBackgroundRequest.class), eq(testUser));
            }
        }

        @Test
        @DisplayName("Should return 400 when request body is invalid")
        void shouldReturn400WhenRequestBodyIsInvalid() throws Exception {
            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(post("/characters/{characterId}/background/{id}", characterId, backgroundId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"invalid\": \"json\"}"))
                        .andExpect(status().isBadRequest());

                verify(characterBackgroundService, never()).save(any(), any(), any(), any());
            }
        }

        @Test
        @DisplayName("Should handle service exception when saving background")
        void shouldHandleServiceExceptionWhenSavingBackground() throws Exception {
            // Given
            CharacterBackgroundRequest request = CharacterBackgroundRequest.builder()
                    .title("Test Background")
                    .text("Test background story")
                    .isPublic(false)
                    .build();

            when(characterBackgroundService.save(eq(characterId), eq(backgroundId), any(CharacterBackgroundRequest.class), eq(testUser)))
                    .thenThrow(new RuntimeException("Service error"));

            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(post("/characters/{characterId}/background/{id}", characterId, backgroundId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isInternalServerError());

                verify(characterBackgroundService).save(eq(characterId), eq(backgroundId), any(CharacterBackgroundRequest.class), eq(testUser));
            }
        }
    }
}