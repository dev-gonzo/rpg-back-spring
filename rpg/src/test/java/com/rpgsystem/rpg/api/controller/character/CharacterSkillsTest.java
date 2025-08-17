package com.rpgsystem.rpg.api.controller.character;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpgsystem.rpg.api.dto.character.CharacterSkillRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterSkillResponse;
import com.rpgsystem.rpg.application.service.character.CharacterSkillService;
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

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.mockito.MockedStatic;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import com.rpgsystem.rpg.domain.exception.UnauthorizedActionException;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CharacterSkillsTest {

    @Mock
    private CharacterSkillService service;

    @Mock
    private AuthenticatedUserProvider authenticatedUserProvider;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private User user;
    private String characterId;
    private String skillId;
    private CharacterSkillResponse skillResponse;
    private CharacterSkillRequest skillRequest;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        user = new User();
        user.setId("user-id");
        user.setName("testuser");
        user.setEmail("test@example.com");
        user.setMaster(false);

        characterId = "character-123";
        skillId = "skill-456";

        skillResponse = CharacterSkillResponse.builder()
                .id(skillId)
                .characterId(characterId)
                .skill("Firearms")
                .kitValue(15)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        skillRequest = CharacterSkillRequest.builder()
                .skill("Firearms")
                .cost(10)
                .kitValue(15)
                .build();

        CharacterSkills characterSkills = new CharacterSkills(service);
        mockMvc = MockMvcBuilders.standaloneSetup(characterSkills)
                .setControllerAdvice(new com.rpgsystem.rpg.api.exception.GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("Should get all skills successfully")
    void shouldGetAllSkillsSuccessfully() throws Exception {
        // Given
        List<CharacterSkillResponse> skills = Arrays.asList(
                skillResponse,
                CharacterSkillResponse.builder()
                        .id("skill-789")
                        .characterId(characterId)
                        .skill("Melee")
                        .kitValue(12)
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build()
        );

        try (MockedStatic<com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper> mockedHelper = 
                mockStatic(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper.class)) {
            
            mockedHelper.when(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper::get)
                       .thenReturn(user);
            when(service.getSkills(eq(characterId), eq(user))).thenReturn(skills);

            // When & Then
            mockMvc.perform(get("/characters/{characterId}/skills", characterId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].id").value(skillId))
                    .andExpect(jsonPath("$[0].skill").value("Firearms"))
                    .andExpect(jsonPath("$[0].kitValue").value(15))
                    .andExpect(jsonPath("$[1].skill").value("Melee"))
                    .andExpect(jsonPath("$[1].kitValue").value(12));
        }
    }

    @Test
    @DisplayName("Should get single skill successfully")
    void shouldGetSingleSkillSuccessfully() throws Exception {
        // Given
        try (MockedStatic<com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper> mockedHelper = 
                mockStatic(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper.class)) {
            
            mockedHelper.when(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper::get)
                       .thenReturn(user);
            when(service.getSkill(eq(skillId), eq(characterId), eq(user))).thenReturn(skillResponse);

            // When & Then
            mockMvc.perform(get("/characters/{characterId}/skills/{id}", characterId, skillId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(skillId))
                    .andExpect(jsonPath("$.characterId").value(characterId))
                    .andExpect(jsonPath("$.skill").value("Firearms"))
                    .andExpect(jsonPath("$.kitValue").value(15));
        }
    }

    @Test
    @DisplayName("Should save skill successfully")
    void shouldSaveSkillSuccessfully() throws Exception {
        // Given
        try (MockedStatic<com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper> mockedHelper = 
                mockStatic(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper.class)) {
            
            mockedHelper.when(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper::get)
                       .thenReturn(user);
            when(service.save(any(CharacterSkillRequest.class), eq(skillId), eq(characterId), eq(user)))
                    .thenReturn(skillResponse);

            // When & Then
            mockMvc.perform(post("/characters/{characterId}/skills/{id}", characterId, skillId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(skillRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(skillId))
                    .andExpect(jsonPath("$.characterId").value(characterId))
                    .andExpect(jsonPath("$.skill").value("Firearms"))
                    .andExpect(jsonPath("$.kitValue").value(15));
        }
    }

    @Test
    @DisplayName("Should return 400 when request body is invalid")
    void shouldReturn400WhenRequestBodyIsInvalid() throws Exception {
        // Given
        CharacterSkillRequest invalidRequest = CharacterSkillRequest.builder()
                .skill("") // Invalid empty skill
                .kitValue(-1) // Invalid negative kitValue
                .build();

        try (MockedStatic<com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper> mockedHelper = 
                mockStatic(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper.class)) {
            
            mockedHelper.when(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper::get)
                       .thenReturn(user);

            // When & Then
             mockMvc.perform(post("/characters/{characterId}/skills/{id}", characterId, skillId)
                             .contentType(MediaType.APPLICATION_JSON)
                             .content(objectMapper.writeValueAsString(invalidRequest)))
                     .andExpect(status().isBadRequest());
        }
    }

    @Test
    @DisplayName("Should return 404 when character not found")
    void shouldReturn404WhenCharacterNotFound() throws Exception {
        // Given
        try (MockedStatic<com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper> mockedHelper = 
                mockStatic(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper.class)) {
            
            mockedHelper.when(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper::get)
                       .thenReturn(user);
            when(service.getSkills(eq(characterId), eq(user)))
                    .thenThrow(new jakarta.persistence.EntityNotFoundException("Character not found"));

            // When & Then
            mockMvc.perform(get("/characters/{characterId}/skills", characterId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    @Test
    @DisplayName("Should return 404 when skill not found")
    void shouldReturn404WhenSkillNotFound() throws Exception {
        // Given
        try (MockedStatic<com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper> mockedHelper = 
                mockStatic(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper.class)) {
            
            mockedHelper.when(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper::get)
                       .thenReturn(user);
            when(service.getSkill(eq(skillId), eq(characterId), eq(user)))
                    .thenThrow(new jakarta.persistence.EntityNotFoundException("Skill not found"));

            // When & Then
            mockMvc.perform(get("/characters/{characterId}/skills/{id}", characterId, skillId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    @Test
    @DisplayName("Should return 403 when user has no permission")
    void shouldReturn403WhenUserHasNoPermission() throws Exception {
        // Given
        try (MockedStatic<com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper> mockedHelper = 
                mockStatic(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper.class)) {
            
            mockedHelper.when(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper::get)
                       .thenReturn(user);
            when(service.getSkills(eq(characterId), eq(user)))
                    .thenThrow(new UnauthorizedActionException("User does not have permission"));

            // When & Then
            mockMvc.perform(get("/characters/{characterId}/skills", characterId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden());
        }
    }
}