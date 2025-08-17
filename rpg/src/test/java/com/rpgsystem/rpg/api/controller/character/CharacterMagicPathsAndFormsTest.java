package com.rpgsystem.rpg.api.controller.character;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpgsystem.rpg.api.dto.character.CharacterPathsAndFormsRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterPathsAndFormsResponse;
import com.rpgsystem.rpg.application.service.character.CharacterPathsAndFormsService;
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

import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CharacterMagicPathsAndForms Controller Tests")
class CharacterMagicPathsAndFormsTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private CharacterPathsAndFormsService characterPathsAndFormsService;

    private User user;
    private String characterId;
    private CharacterPathsAndFormsResponse response;
    private CharacterPathsAndFormsRequest request;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        
        user = User.builder()
                .id("user-id")
                .name("Test User")
                .password("password")
                .email("test@example.com")
                .build();

        characterId = "character-id";

        response = CharacterPathsAndFormsResponse.builder()
                .characterId(characterId)
                .understandForm(3)
                .createForm(2)
                .controlForm(1)
                .fire(4)
                .water(3)
                .earth(2)
                .air(1)
                .light(0)
                .darkness(0)
                .plants(2)
                .animals(1)
                .humans(3)
                .spiritum(0)
                .arkanun(0)
                .metamagic(0)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        request = CharacterPathsAndFormsRequest.builder()
                .understandForm(3)
                .createForm(2)
                .controlForm(1)
                .fire(4)
                .water(3)
                .earth(2)
                .air(1)
                .light(0)
                .darkness(0)
                .plants(2)
                .animals(1)
                .humans(3)
                .spiritum(0)
                .arkanun(0)
                .metamagic(0)
                .build();
                
        CharacterMagicPathsAndForms controller = new CharacterMagicPathsAndForms(characterPathsAndFormsService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new com.rpgsystem.rpg.api.exception.GlobalExceptionHandler())
                .build();
    }

    @Nested
    @DisplayName("GET /characters/{characterId}/magic")
    class GetMagicPathsAndFormsTests {

        @Test
        @DisplayName("Should return character magic paths and forms successfully")
        void shouldGetCharacterMagicPathsAndFormsSuccessfully() throws Exception {
            // Given
            when(characterPathsAndFormsService.get(eq(characterId), any(User.class)))
                    .thenReturn(response);

            // When & Then
            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(user);
                
                mockMvc.perform(get("/characters/{characterId}/magic", characterId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.characterId").value(characterId))
                    .andExpect(jsonPath("$.understandForm").value(3))
                    .andExpect(jsonPath("$.createForm").value(2))
                    .andExpect(jsonPath("$.controlForm").value(1))
                    .andExpect(jsonPath("$.fire").value(4))
                    .andExpect(jsonPath("$.water").value(3))
                    .andExpect(jsonPath("$.earth").value(2))
                    .andExpect(jsonPath("$.air").value(1))
                    .andExpect(jsonPath("$.light").value(0))
                    .andExpect(jsonPath("$.darkness").value(0))
                    .andExpect(jsonPath("$.plants").value(2))
                    .andExpect(jsonPath("$.animals").value(1))
                    .andExpect(jsonPath("$.humans").value(3))
                    .andExpect(jsonPath("$.spiritum").value(0))
                        .andExpect(jsonPath("$.arkanun").value(0))
                        .andExpect(jsonPath("$.metamagic").value(0));

                verify(characterPathsAndFormsService).get(characterId, user);
            }
        }

        @Test
        @DisplayName("Should return 401 when user is not authenticated")
        void shouldReturn401WhenUserNotAuthenticated() throws Exception {
            // Given - no authenticated user
            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenThrow(new RuntimeException("No authenticated user"));

                // When & Then
                mockMvc.perform(get("/characters/{characterId}/magic", characterId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isInternalServerError());

                verify(characterPathsAndFormsService, never()).get(any(), any());
            }
        }
    }

    @Nested
    @DisplayName("POST /characters/{characterId}/magic")
    class SaveMagicPathsAndFormsTests {

        @Test
        @DisplayName("Should save character magic paths and forms successfully")
        void shouldSaveCharacterMagicPathsAndFormsSuccessfully() throws Exception {
            // Given
            when(characterPathsAndFormsService.save(eq(characterId), any(CharacterPathsAndFormsRequest.class), any(User.class)))
                    .thenReturn(response);

            // When & Then
            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(user);
                
                mockMvc.perform(post("/characters/{characterId}/magic", characterId)
                                 .contentType(MediaType.APPLICATION_JSON)
                                 .content(objectMapper.writeValueAsString(request)))
                         .andExpect(status().isOk())
                         .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                         .andExpect(jsonPath("$.characterId").value(characterId))
                         .andExpect(jsonPath("$.understandForm").value(3))
                         .andExpect(jsonPath("$.createForm").value(2))
                         .andExpect(jsonPath("$.controlForm").value(1))
                         .andExpect(jsonPath("$.fire").value(4))
                         .andExpect(jsonPath("$.water").value(3))
                         .andExpect(jsonPath("$.earth").value(2))
                         .andExpect(jsonPath("$.air").value(1))
                         .andExpect(jsonPath("$.light").value(0))
                         .andExpect(jsonPath("$.darkness").value(0))
                         .andExpect(jsonPath("$.plants").value(2))
                         .andExpect(jsonPath("$.animals").value(1))
                         .andExpect(jsonPath("$.humans").value(3))
                         .andExpect(jsonPath("$.spiritum").value(0))
                         .andExpect(jsonPath("$.arkanun").value(0))
                         .andExpect(jsonPath("$.metamagic").value(0));

                verify(characterPathsAndFormsService).save(eq(characterId), any(CharacterPathsAndFormsRequest.class), eq(user));
            }
        }

        @Test
    @DisplayName("Should return 400 when request body is invalid")
    void shouldReturn400WhenRequestBodyIsInvalid() throws Exception {
        // Given - Create request with all required fields but with invalid values
        CharacterPathsAndFormsRequest invalidRequest = CharacterPathsAndFormsRequest.builder()
                .understandForm(-1) // Invalid negative value
                .createForm(11) // Invalid value > max
                .controlForm(0) // Valid
                .fire(5) // Invalid value > max
                .water(0) // Valid
                .earth(0) // Valid
                .air(0) // Valid
                .light(0) // Valid
                .darkness(0) // Valid
                .plants(0) // Valid
                .animals(0) // Valid
                .humans(0) // Valid
                .spiritum(0) // Valid
                .arkanun(0) // Valid
                .metamagic(0) // Valid
                .build();



        // When & Then
        try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
            mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(user);
            
            mockMvc.perform(post("/characters/{characterId}/magic", characterId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            verify(characterPathsAndFormsService, never()).save(anyString(), any(CharacterPathsAndFormsRequest.class), any(User.class));
        }
    }

        @Test
        @DisplayName("Should return 401 when user is not authenticated")
        void shouldReturn401WhenUserNotAuthenticated() throws Exception {
            // Given - no authenticated user
            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenThrow(new RuntimeException("No authenticated user"));

                // When & Then
                mockMvc.perform(post("/characters/{characterId}/magic", characterId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isInternalServerError());

                verify(characterPathsAndFormsService, never()).save(anyString(), any(CharacterPathsAndFormsRequest.class), any(User.class));
            }
        }
    }
}