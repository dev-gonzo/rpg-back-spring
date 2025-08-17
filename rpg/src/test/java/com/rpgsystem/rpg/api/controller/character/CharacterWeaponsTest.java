package com.rpgsystem.rpg.api.controller.character;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpgsystem.rpg.api.dto.character.CharacterWeaponRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterWeaponResponse;
import com.rpgsystem.rpg.application.service.character.CharacterWeaponService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mockStatic;
import org.mockito.MockedStatic;
import com.rpgsystem.rpg.domain.exception.UnauthorizedActionException;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CharacterWeaponsTest {

    @Mock
    private CharacterWeaponService service;

    @Mock
    private AuthenticatedUserProvider authenticatedUserProvider;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private User user;
    private String characterId;
    private String weaponId;
    private CharacterWeaponResponse weaponResponse;
    private CharacterWeaponRequest weaponRequest;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        user = new User();
        user.setId("user-id");
        user.setName("testuser");
        user.setEmail("test@example.com");
        user.setMaster(false);

        characterId = "character-123";
        weaponId = "weapon-456";

        weaponResponse = CharacterWeaponResponse.builder()
                .id(weaponId)
                .characterId(characterId)
                .name("Glock 17")
                .description("Semi-automatic pistol")
                .damage("2d6+1")
                .initiative(3)
                .range("50m")
                .rof("3")
                .ammunition("9mm")
                .bookPage("p.142")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        weaponRequest = CharacterWeaponRequest.builder()
                .name("Glock 17")
                .description("Semi-automatic pistol")
                .damage("2d6+1")
                .initiative(3)
                .range("50m")
                .rof("3")
                .ammunition("9mm")
                .bookPage("p.142")
                .build();

        CharacterWeapons characterWeapons = new CharacterWeapons(service);
        mockMvc = MockMvcBuilders.standaloneSetup(characterWeapons)
                .setControllerAdvice(new com.rpgsystem.rpg.api.exception.GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("Should get all weapons successfully")
    void shouldGetAllWeaponsSuccessfully() throws Exception {
        // Given
        List<CharacterWeaponResponse> weapons = Arrays.asList(
                weaponResponse,
                CharacterWeaponResponse.builder()
                        .id("weapon-789")
                        .characterId(characterId)
                        .name("Katana")
                        .description("Japanese sword")
                        .damage("3d6")
                        .initiative(2)
                        .range("Melee")
                        .rof("1")
                        .ammunition("-")
                        .bookPage("p.145")
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build()
        );

        when(service.getAll(eq(characterId), eq(user))).thenReturn(weapons);

        try (MockedStatic<com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper> mockedHelper = 
                mockStatic(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper.class)) {
            
            mockedHelper.when(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper::get)
                       .thenReturn(user);

            // When & Then
            mockMvc.perform(get("/characters/{characterId}/weapons", characterId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].id").value(weaponId))
                    .andExpect(jsonPath("$[0].name").value("Glock 17"))
                    .andExpect(jsonPath("$[0].damage").value("2d6+1"))
                    .andExpect(jsonPath("$[0].initiative").value(3))
                    .andExpect(jsonPath("$[1].name").value("Katana"))
                    .andExpect(jsonPath("$[1].damage").value("3d6"));
        }
    }

    @Test
    @DisplayName("Should get single weapon successfully")
    void shouldGetSingleWeaponSuccessfully() throws Exception {
        // Given
        when(service.get(eq(characterId), eq(weaponId), eq(user))).thenReturn(weaponResponse);

        try (MockedStatic<com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper> mockedHelper = 
                mockStatic(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper.class)) {
            
            mockedHelper.when(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper::get)
                       .thenReturn(user);

            // When & Then
            mockMvc.perform(get("/characters/{characterId}/weapons/{id}", characterId, weaponId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(weaponId))
                    .andExpect(jsonPath("$.characterId").value(characterId))
                    .andExpect(jsonPath("$.name").value("Glock 17"))
                    .andExpect(jsonPath("$.description").value("Semi-automatic pistol"))
                    .andExpect(jsonPath("$.damage").value("2d6+1"))
                    .andExpect(jsonPath("$.initiative").value(3))
                    .andExpect(jsonPath("$.range").value("50m"))
                    .andExpect(jsonPath("$.rof").value("3"))
                    .andExpect(jsonPath("$.ammunition").value("9mm"))
                    .andExpect(jsonPath("$.bookPage").value("p.142"));
        }
    }

    @Test
    @DisplayName("Should save weapon successfully")
    void shouldSaveWeaponSuccessfully() throws Exception {
        // Given
        CharacterWeaponResponse response = CharacterWeaponResponse.builder()
                .id(weaponId)
                .characterId(characterId)
                .name("Espada Longa")
                .description("Semi-automatic pistol")
                .damage("1d8+2")
                .initiative(2)
                .range("Corpo a corpo")
                .rof("3")
                .ammunition("9mm")
                .bookPage("p.142")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        when(service.save(eq(characterId), eq(weaponId), any(CharacterWeaponRequest.class), eq(user)))
                .thenReturn(response);

        try (MockedStatic<com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper> mockedHelper = 
                mockStatic(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper.class)) {
            
            mockedHelper.when(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper::get)
                       .thenReturn(user);

            // When & Then
            mockMvc.perform(post("/characters/{characterId}/weapons/{id}", characterId, weaponId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(weaponRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(weaponId))
                    .andExpect(jsonPath("$.characterId").value(characterId))
                    .andExpect(jsonPath("$.name").value("Espada Longa"))
                    .andExpect(jsonPath("$.damage").value("1d8+2"))
                    .andExpect(jsonPath("$.range").value("Corpo a corpo"))
                    .andExpect(jsonPath("$.initiative").value(2));
        }
    }

    @Test
    @DisplayName("Should return 400 when request body is invalid")
    void shouldReturn400WhenRequestBodyIsInvalid() throws Exception {
        // Given
        CharacterWeaponRequest invalidRequest = CharacterWeaponRequest.builder()
                .name("") // Invalid empty name
                .description("Valid description")
                .damage("") // Invalid empty damage
                .initiative(null) // Invalid null initiative
                .range("")
                .rof("")
                .ammunition("")
                .bookPage("")
                .build();

        try (MockedStatic<com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper> mockedHelper = 
                mockStatic(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper.class)) {
            
            mockedHelper.when(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper::get)
                       .thenReturn(user);

            // When & Then
            mockMvc.perform(post("/characters/{characterId}/weapons/{id}", characterId, weaponId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Test
    @DisplayName("Should return 404 when character not found")
    void shouldReturn404WhenCharacterNotFound() throws Exception {
        // Given
        when(service.getAll(eq(characterId), eq(user)))
                .thenThrow(new jakarta.persistence.EntityNotFoundException("Character not found"));

        try (MockedStatic<com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper> mockedHelper = 
                mockStatic(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper.class)) {
            
            mockedHelper.when(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper::get)
                       .thenReturn(user);

            // When & Then
            mockMvc.perform(get("/characters/{characterId}/weapons", characterId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    @Test
    @DisplayName("Should return 404 when weapon not found")
    void shouldReturn404WhenWeaponNotFound() throws Exception {
        // Given
        when(service.get(eq(characterId), eq(weaponId), eq(user)))
                .thenThrow(new jakarta.persistence.EntityNotFoundException("Weapon not found"));

        try (MockedStatic<com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper> mockedHelper = 
                mockStatic(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper.class)) {
            
            mockedHelper.when(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper::get)
                       .thenReturn(user);

            // When & Then
            mockMvc.perform(get("/characters/{characterId}/weapons/{id}", characterId, weaponId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }

    @Test
    @DisplayName("Should return 403 when user has no permission")
    void shouldReturn403WhenUserHasNoPermission() throws Exception {
        // Given
        when(service.getAll(eq(characterId), eq(user)))
                .thenThrow(new UnauthorizedActionException("User does not have permission"));

        try (MockedStatic<com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper> mockedHelper = 
                mockStatic(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper.class)) {
            
            mockedHelper.when(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper::get)
                       .thenReturn(user);

            // When & Then
            mockMvc.perform(get("/characters/{characterId}/weapons", characterId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isForbidden());
        }
    }

    @Test
    @DisplayName("Should handle malformed JSON request")
    void shouldHandleMalformedJsonRequest() throws Exception {
        // Given
        String malformedJson = "{\"name\": \"Glock\", \"damage\": }";

        try (MockedStatic<com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper> mockedHelper = 
                mockStatic(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper.class)) {
            
            mockedHelper.when(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper::get)
                       .thenReturn(user);

            // When & Then
            mockMvc.perform(post("/characters/{characterId}/weapons/{id}", characterId, weaponId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(malformedJson))
                    .andExpect(status().isBadRequest());
        }
    }
}