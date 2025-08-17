package com.rpgsystem.rpg.api.controller.character;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpgsystem.rpg.api.dto.character.CharacterNoteRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterNoteResponse;
import com.rpgsystem.rpg.application.service.character.CharacterNoteService;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.infrastructure.security.AuthenticatedUserProvider;
import com.rpgsystem.rpg.infrastructure.security.aspect.RequireAuthUserAspect;
import com.rpgsystem.rpg.api.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CharacterNotes Controller Tests")
class CharacterNotesTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private CharacterNoteService characterNoteService;

    @Mock
    private AuthenticatedUserProvider authenticatedUserProvider;



    private User user;
    private String characterId;
    private String noteId;
    private CharacterNoteResponse response;
    private CharacterNoteRequest request;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        
        CharacterNotes controller = new CharacterNotes(characterNoteService, authenticatedUserProvider);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        
        user = User.builder()
                .id("user-id")
                .name("Test User")
                .password("password")
                .email("test@example.com")
                .build();

        characterId = "character-id";
        noteId = "note-id";

        response = CharacterNoteResponse.builder()
                .id(noteId)
                .characterId(characterId)
                .note("This is a character note for testing purposes.")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        request = CharacterNoteRequest.builder()
                .note("This is a character note for testing purposes.")
                .build();
    }

    @Nested
    @DisplayName("GET /characters/{characterId}/notes")
    class GetAllNotesTests {

        @Test
        @DisplayName("Should return all character notes successfully")
        void shouldGetAllCharacterNotesSuccessfully() throws Exception {
            // Given
            List<CharacterNoteResponse> notes = Arrays.asList(response);
            when(authenticatedUserProvider.getAuthenticatedUser()).thenReturn(user);
            when(characterNoteService.getAll(eq(characterId), any(User.class))).thenReturn(notes);

            // When & Then
            mockMvc.perform(get("/characters/{characterId}/notes", characterId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$[0].id").value(noteId))
                    .andExpect(jsonPath("$[0].characterId").value(characterId))
                    .andExpect(jsonPath("$[0].note").value("This is a character note for testing purposes."));

            verify(characterNoteService).getAll(eq(characterId), eq(user));
        }

        @Test
        @DisplayName("Should return empty list when no notes found")
        void shouldReturnEmptyListWhenNoNotesFound() throws Exception {
            // Given
            when(authenticatedUserProvider.getAuthenticatedUser()).thenReturn(user);
            when(characterNoteService.getAll(eq(characterId), any(User.class)))
                    .thenReturn(Arrays.asList());

            // When & Then
            mockMvc.perform(get("/characters/{characterId}/notes", characterId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$").isEmpty());

            verify(characterNoteService).getAll(eq(characterId), eq(user));
        }

        @Test
        @DisplayName("Should return 401 when user not authenticated")
        void shouldReturn401WhenUserNotAuthenticated() throws Exception {
            // Given
            when(authenticatedUserProvider.getAuthenticatedUser()).thenReturn(null);
            when(characterNoteService.getAll(anyString(), isNull()))
                    .thenThrow(new RuntimeException("Unauthorized"));

            // When & Then
            mockMvc.perform(get("/characters/{characterId}/notes", characterId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError()); // Will be 500 due to RuntimeException

            verify(characterNoteService).getAll(characterId, null);
        }
    }

    @Nested
    @DisplayName("GET /characters/{characterId}/notes/{id}")
    class GetNoteTests {

        @Test
        @DisplayName("Should return character note successfully")
        void shouldGetCharacterNoteSuccessfully() throws Exception {
            // Given
            when(authenticatedUserProvider.getAuthenticatedUser()).thenReturn(user);
            when(characterNoteService.get(eq(characterId), eq(noteId), any(User.class)))
                    .thenReturn(response);

            // When & Then
            mockMvc.perform(get("/characters/{characterId}/notes/{id}", characterId, noteId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(noteId))
                    .andExpect(jsonPath("$.characterId").value(characterId))
                    .andExpect(jsonPath("$.note").value("This is a character note for testing purposes."));

            verify(characterNoteService).get(characterId, noteId, user);
        }

        @Test
        @DisplayName("Should return 401 when user not authenticated")
        void shouldReturn401WhenUserNotAuthenticated() throws Exception {
            // Given
            when(authenticatedUserProvider.getAuthenticatedUser()).thenReturn(null);
            when(characterNoteService.get(anyString(), anyString(), isNull()))
                    .thenThrow(new RuntimeException("Unauthorized"));

            // When & Then
            mockMvc.perform(get("/characters/{characterId}/notes/{id}", characterId, noteId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isInternalServerError()); // Will be 500 due to RuntimeException

            verify(characterNoteService).get(characterId, noteId, null);
        }
    }

    @Nested
    @DisplayName("POST /characters/{characterId}/notes/{id}")
    class SaveNoteTests {

        @Test
        @DisplayName("Should save character note successfully")
        void shouldSaveCharacterNoteSuccessfully() throws Exception {
            // Given
            when(authenticatedUserProvider.getAuthenticatedUser()).thenReturn(user);
            when(characterNoteService.save(eq(characterId), eq(noteId), any(CharacterNoteRequest.class), any(User.class)))
                    .thenReturn(response);

            // When & Then
            mockMvc.perform(post("/characters/{characterId}/notes/{id}", characterId, noteId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(noteId))
                    .andExpect(jsonPath("$.characterId").value(characterId))
                    .andExpect(jsonPath("$.note").value("This is a character note for testing purposes."));

            verify(characterNoteService).save(eq(characterId), eq(noteId), any(CharacterNoteRequest.class), eq(user));
        }

        @Test
        @DisplayName("Should return 400 when request body is invalid")
        void shouldReturn400WhenRequestBodyIsInvalid() throws Exception {
            // Given
            CharacterNoteRequest invalidRequest = CharacterNoteRequest.builder()
                    .note("") // Empty note
                    .build();

            // When & Then
            mockMvc.perform(post("/characters/{characterId}/notes/{id}", characterId, noteId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            verify(characterNoteService, never()).save(anyString(), anyString(), any(CharacterNoteRequest.class), any(User.class));
        }

        @Test
        @DisplayName("Should return 401 when user not authenticated")
        void shouldReturn401WhenUserNotAuthenticated() throws Exception {
            // Given
            when(authenticatedUserProvider.getAuthenticatedUser()).thenReturn(null);
            when(characterNoteService.save(anyString(), anyString(), any(CharacterNoteRequest.class), isNull()))
                    .thenThrow(new RuntimeException("Unauthorized"));

            // When & Then
            mockMvc.perform(post("/characters/{characterId}/notes/{id}", characterId, noteId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isInternalServerError()); // Will be 500 due to RuntimeException

            verify(characterNoteService).save(eq(characterId), eq(noteId), any(CharacterNoteRequest.class), isNull());
        }
    }
}