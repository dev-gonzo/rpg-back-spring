package com.rpgsystem.rpg.api.controller.character;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpgsystem.rpg.api.dto.character.CharacterPathsAndFormsRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterPathsAndFormsResponse;
import com.rpgsystem.rpg.application.service.character.CharacterPathsAndFormsService;
import com.rpgsystem.rpg.domain.entity.User;
import jakarta.persistence.EntityNotFoundException;
import com.rpgsystem.rpg.domain.exception.UnauthorizedActionException;
import com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.rpgsystem.rpg.config.TestSecurityConfig;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class CharacterMagicPathsAndFormsControllerTest {

    private MockMvc mockMvc;
    private CharacterPathsAndFormsService service;
    private ObjectMapper objectMapper = new ObjectMapper();

    private User mockUser;
    private CharacterPathsAndFormsResponse mockResponse;
    private CharacterPathsAndFormsRequest mockRequest;

    @BeforeEach
    void setUp() {
        service = mock(CharacterPathsAndFormsService.class);
        
        mockUser = User.builder()
                .id("user-123")
                .email("test@example.com")
                .name("testuser")
                .build();

        mockResponse = CharacterPathsAndFormsResponse.builder()
                .characterId("char-123")
                .understandForm(2)
                .createForm(3)
                .controlForm(1)
                .fire(2)
                .water(1)
                .earth(0)
                .air(0)
                .light(0)
                .darkness(0)
                .plants(0)
                .animals(0)
                .humans(0)
                .spiritum(0)
                .arkanun(0)
                .metamagic(0)
                .build();

        mockRequest = CharacterPathsAndFormsRequest.builder()
                .understandForm(2)
                .createForm(3)
                .controlForm(1)
                .fire(2)
                .water(1)
                .earth(0)
                .air(0)
                .light(0)
                .darkness(0)
                .plants(0)
                .animals(0)
                .humans(0)
                .spiritum(0)
                .arkanun(0)
                .metamagic(0)
                .build();
                
        CharacterMagicPathsAndForms controller = new CharacterMagicPathsAndForms(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new com.rpgsystem.rpg.api.exception.GlobalExceptionHandler())
                .build();
    }

    @Test
    void get_ShouldReturnPathsAndForms_WhenCharacterExists() throws Exception {
        // Given
        String characterId = "char-123";
        
        try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
            mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(mockUser);
            when(service.get(characterId, mockUser)).thenReturn(mockResponse);

            // When & Then
            mockMvc.perform(get("/characters/{characterId}/magic", characterId))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.characterId").value("char-123"))
                    .andExpect(jsonPath("$.understandForm").value(2))
                    .andExpect(jsonPath("$.createForm").value(3))
                    .andExpect(jsonPath("$.controlForm").value(1))
                    .andExpect(jsonPath("$.fire").value(2))
                    .andExpect(jsonPath("$.water").value(1));

            verify(service).get(characterId, mockUser);
        }
    }

    @Test
    void get_ShouldReturnEmptyPathsAndForms_WhenCharacterHasNoMagic() throws Exception {
        // Given
        String characterId = "char-123";
        CharacterPathsAndFormsResponse emptyResponse = CharacterPathsAndFormsResponse.builder()
                .characterId(characterId)
                .understandForm(0)
                .createForm(0)
                .controlForm(0)
                .fire(0)
                .water(0)
                .earth(0)
                .air(0)
                .light(0)
                .darkness(0)
                .plants(0)
                .animals(0)
                .humans(0)
                .spiritum(0)
                .arkanun(0)
                .metamagic(0)
                .build();
        
        try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
            mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(mockUser);
            when(service.get(characterId, mockUser)).thenReturn(emptyResponse);

            // When & Then
            mockMvc.perform(get("/characters/{characterId}/magic", characterId))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.characterId").value("char-123"))
                    .andExpect(jsonPath("$.understandForm").value(0))
                    .andExpect(jsonPath("$.createForm").value(0))
                    .andExpect(jsonPath("$.controlForm").value(0))
                    .andExpect(jsonPath("$.fire").value(0))
                    .andExpect(jsonPath("$.water").value(0));

            verify(service).get(characterId, mockUser);
        }
    }

    @Test
    void get_ShouldReturnNotFound_WhenCharacterDoesNotExist() throws Exception {
        // Given
        String characterId = "char-123";
        
        try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
            mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(mockUser);
            when(service.get(characterId, mockUser))
                    .thenThrow(new EntityNotFoundException("Character not found"));

            // When & Then
            mockMvc.perform(get("/characters/{characterId}/magic", characterId))
                    .andExpect(status().isNotFound());

            verify(service).get(characterId, mockUser);
        }
    }

    @Test
    void get_ShouldReturnUnauthorized_WhenUserNotAuthorized() throws Exception {
        // Given
        String characterId = "char-123";
        
        try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
            mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(mockUser);
            when(service.get(characterId, mockUser))
                    .thenThrow(new UnauthorizedActionException("User not authorized to access this character"));

            // When & Then
            mockMvc.perform(get("/characters/{characterId}/magic", characterId))
                    .andExpect(status().isForbidden());

            verify(service).get(characterId, mockUser);
        }
    }

    @Test
    void save_ShouldReturnSavedPathsAndForms_WhenValidRequest() throws Exception {
        // Given
        String characterId = "char-123";
        
        try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
            mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(mockUser);
            when(service.save(eq(characterId), any(CharacterPathsAndFormsRequest.class), eq(mockUser)))
                    .thenReturn(mockResponse);

            // When & Then
            mockMvc.perform(post("/characters/{characterId}/magic", characterId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(mockRequest)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.characterId").value("char-123"))
                    .andExpect(jsonPath("$.fire").value(2))
                    .andExpect(jsonPath("$.water").value(1))
                    .andExpect(jsonPath("$.understandForm").value(2))
                    .andExpect(jsonPath("$.createForm").value(3))
                    .andExpect(jsonPath("$.controlForm").value(1));

            verify(service).save(eq(characterId), any(CharacterPathsAndFormsRequest.class), eq(mockUser));
        }
    }

    @Test
    void save_ShouldReturnBadRequest_WhenInvalidRequest() throws Exception {
        // Given
        String characterId = "char-123";
        CharacterPathsAndFormsRequest invalidRequest = CharacterPathsAndFormsRequest.builder()
                .understandForm(null) // null values should trigger validation errors
                .createForm(null)
                .controlForm(null)
                .fire(null)
                .water(null)
                .earth(null)
                .air(null)
                .light(null)
                .darkness(null)
                .plants(null)
                .animals(null)
                .humans(null)
                .spiritum(null)
                .arkanun(null)
                .metamagic(null)
                .build();
        
        // When & Then
        mockMvc.perform(post("/characters/{characterId}/magic", characterId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(service, never()).save(any(), any(), any());
    }

    @Test
    void save_ShouldReturnUnauthorized_WhenUserNotAuthorized() throws Exception {
        // Given
        String characterId = "char-123";
        
        try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
            mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(mockUser);
            when(service.save(eq(characterId), any(CharacterPathsAndFormsRequest.class), eq(mockUser)))
                    .thenThrow(new UnauthorizedActionException("User not authorized to access this character"));

            // When & Then
            mockMvc.perform(post("/characters/{characterId}/magic", characterId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(mockRequest)))
                    .andExpect(status().isForbidden());

            verify(service).save(eq(characterId), any(CharacterPathsAndFormsRequest.class), eq(mockUser));
        }
    }

    @Test
    void save_ShouldReturnNotFound_WhenCharacterDoesNotExist() throws Exception {
        // Given
        String characterId = "char-123";
        
        try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
            mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(mockUser);
            when(service.save(eq(characterId), any(CharacterPathsAndFormsRequest.class), eq(mockUser)))
                    .thenThrow(new EntityNotFoundException("Character not found"));

            // When & Then
            mockMvc.perform(post("/characters/{characterId}/magic", characterId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(mockRequest)))
                    .andExpect(status().isNotFound());

            verify(service).save(eq(characterId), any(CharacterPathsAndFormsRequest.class), eq(mockUser));
        }
    }
}