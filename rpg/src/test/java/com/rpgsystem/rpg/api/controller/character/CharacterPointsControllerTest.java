package com.rpgsystem.rpg.api.controller.character;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpgsystem.rpg.api.dto.character.CharacterPointsRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterPointsResponse;
import com.rpgsystem.rpg.api.dto.character.CharacterCurrentPointsRequest;
import com.rpgsystem.rpg.application.service.character.CharacterPointService;
import com.rpgsystem.rpg.domain.entity.User;

import com.rpgsystem.rpg.infrastructure.security.AuthenticatedUserProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CharacterPointsController Tests")
class CharacterPointsControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private CharacterPointService characterPointService;

    @Mock
    private AuthenticatedUserProvider authenticatedUserProvider;

    private User user;
    private String characterId;
    private CharacterPointsResponse response;
    private CharacterPointsRequest request;
    private CharacterCurrentPointsRequest currentPointsRequest;

    @BeforeEach
    void setUp() {
        CharacterPointsController controller = new CharacterPointsController(
            characterPointService, authenticatedUserProvider
        );
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        user = User.builder()
                .id("user-id")
                .name("Test User")
                .email("test@example.com")
                .build();

        characterId = "character-id";

        response = CharacterPointsResponse.builder()
                .characterId(characterId)
                .hitPoints(100)
                .currentHitPoints(85)
                .magicPoints(50)
                .currentMagicPoints(40)
                .faithPoints(75)
                .currentFaithPoints(70)
                .heroPoints(30)
                .currentHeroPoints(25)
                .initiative(15)
                .currentInitiative(12)
                .protectionIndex(5)
                .currentProtectionIndex(4)
                .build();

        request = CharacterPointsRequest.builder()
                .hitPoints(100)
                .magicPoints(50)
                .faithPoints(75)
                .heroPoints(30)
                .initiative(15)
                .protectionIndex(5)
                .build();

        currentPointsRequest = CharacterCurrentPointsRequest.builder()
                .currentHitPoints(85)
                .currentMagicPoints(40)
                .currentFaithPoints(70)
                .currentHeroPoints(25)
                .currentInitiative(12)
                .currentProtectionIndex(4)
                .build();
    }

    @Test
    @DisplayName("Should return character points successfully")
    void shouldGetCharacterPointsSuccessfully() throws Exception {
        // Given
        when(characterPointService.getInfo(eq(characterId)))
                .thenReturn(response);

        // When & Then
        mockMvc.perform(get("/characters/{characterId}/points", characterId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.characterId").value(characterId))
                .andExpect(jsonPath("$.hitPoints").value(100))
                .andExpect(jsonPath("$.currentHitPoints").value(85))
                .andExpect(jsonPath("$.magicPoints").value(50))
                .andExpect(jsonPath("$.currentMagicPoints").value(40))
                .andExpect(jsonPath("$.faithPoints").value(75))
                .andExpect(jsonPath("$.currentFaithPoints").value(70))
                .andExpect(jsonPath("$.heroPoints").value(30))
                .andExpect(jsonPath("$.currentHeroPoints").value(25));

        verify(characterPointService).getInfo(characterId);
    }



    @Test
    @DisplayName("Should save character points successfully")
    void shouldSaveCharacterPointsSuccessfully() throws Exception {
        // Given
        when(authenticatedUserProvider.getAuthenticatedUser()).thenReturn(user);
        when(characterPointService.save(any(CharacterPointsRequest.class), eq(characterId), eq(user)))
                .thenReturn(response);

        // When & Then
        mockMvc.perform(post("/characters/{characterId}/points", characterId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.characterId").value(characterId))
                .andExpect(jsonPath("$.hitPoints").value(100))
                .andExpect(jsonPath("$.currentHitPoints").value(85))
                .andExpect(jsonPath("$.magicPoints").value(50))
                .andExpect(jsonPath("$.currentMagicPoints").value(40))
                .andExpect(jsonPath("$.faithPoints").value(75))
                .andExpect(jsonPath("$.currentFaithPoints").value(70))
                .andExpect(jsonPath("$.heroPoints").value(30))
                .andExpect(jsonPath("$.currentHeroPoints").value(25));

        verify(characterPointService).save(any(CharacterPointsRequest.class), eq(characterId), eq(user));
    }

    @Test
    @DisplayName("Should save current character points successfully")
    void shouldSaveCurrentCharacterPointsSuccessfully() throws Exception {
        // Given
        when(authenticatedUserProvider.getAuthenticatedUser()).thenReturn(user);
        when(characterPointService.saveCurrent(any(CharacterCurrentPointsRequest.class), eq(characterId), eq(user)))
                .thenReturn(response);

        // When & Then
        mockMvc.perform(post("/characters/{characterId}/points/current", characterId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(currentPointsRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.characterId").value(characterId))
                .andExpect(jsonPath("$.currentHitPoints").value(85))
                .andExpect(jsonPath("$.currentMagicPoints").value(40))
                .andExpect(jsonPath("$.currentFaithPoints").value(70))
                .andExpect(jsonPath("$.currentHeroPoints").value(25));

        verify(characterPointService).saveCurrent(any(CharacterCurrentPointsRequest.class), eq(characterId), eq(user));
    }

    @Test
    @DisplayName("Should save current adjust character points successfully")
    void shouldSaveCurrentAdjustCharacterPointsSuccessfully() throws Exception {
        // Given
        when(authenticatedUserProvider.getAuthenticatedUser()).thenReturn(user);
        when(characterPointService.saveCurrentAdjust(any(CharacterCurrentPointsRequest.class), eq(characterId), eq(user)))
                .thenReturn(response);

        // When & Then
        mockMvc.perform(post("/characters/{characterId}/points/current/adjust", characterId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(currentPointsRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.characterId").value(characterId))
                .andExpect(jsonPath("$.currentHitPoints").value(85))
                .andExpect(jsonPath("$.currentMagicPoints").value(40))
                .andExpect(jsonPath("$.currentFaithPoints").value(70))
                .andExpect(jsonPath("$.currentHeroPoints").value(25));

        verify(characterPointService).saveCurrentAdjust(any(CharacterCurrentPointsRequest.class), eq(characterId), eq(user));
    }
}