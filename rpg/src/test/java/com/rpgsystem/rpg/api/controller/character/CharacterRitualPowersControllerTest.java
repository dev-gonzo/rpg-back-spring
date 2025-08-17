package com.rpgsystem.rpg.api.controller.character;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpgsystem.rpg.api.dto.character.CharacterRitualPowerRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterRitualPowerResponse;
import com.rpgsystem.rpg.application.service.character.CharacterRitualPowerService;
import com.rpgsystem.rpg.domain.entity.User;
import jakarta.persistence.EntityNotFoundException;
import com.rpgsystem.rpg.domain.exception.UnauthorizedActionException;
import com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@DisplayName("CharacterRitualPowers Controller Tests")
class CharacterRitualPowersControllerTest {

    private MockMvc mockMvc;
    private CharacterRitualPowerService service;
    private ObjectMapper objectMapper = new ObjectMapper();

    private User testUser;
    private final String characterId = "test-character-id";
    private final String ritualPowerId = "test-ritual-power-id";

    @BeforeEach
    void setUp() {
        service = mock(CharacterRitualPowerService.class);
        
        testUser = User.builder()
                .id("test-user-id")
                .email("testuser@example.com")
                .name("Test User")
                .build();
                
        CharacterRitualPowers controller = new CharacterRitualPowers(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new com.rpgsystem.rpg.api.exception.GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("Should get all ritual powers successfully")
    void shouldGetAllRitualPowersSuccessfully() throws Exception {
        // Given
        CharacterRitualPowerResponse response1 = CharacterRitualPowerResponse.builder()
                .id("ritual-1")
                .name("Fireball")
                .pathsForms("Fire 3, Create 2")
                .description("A powerful fire spell")
                .bookPage("Page 89")
                .build();

        CharacterRitualPowerResponse response2 = CharacterRitualPowerResponse.builder()
                .id("ritual-2")
                .name("Healing Light")
                .pathsForms("Light 2, Create 1")
                .description("Restores health")
                .bookPage("Page 45")
                .build();

        List<CharacterRitualPowerResponse> ritualPowers = Arrays.asList(response1, response2);

        when(service.getAll(eq(characterId), eq(testUser)))
                .thenReturn(ritualPowers);

        // When & Then
        try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
            mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

            mockMvc.perform(get("/characters/{characterId}/ritual-powers", characterId))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].id").value("ritual-1"))
                    .andExpect(jsonPath("$[0].name").value("Fireball"))
                    .andExpect(jsonPath("$[1].id").value("ritual-2"))
                    .andExpect(jsonPath("$[1].name").value("Healing Light"));

            verify(service).getAll(characterId, testUser);
        }
    }

    @Test
    @DisplayName("Should return empty list when no ritual powers found")
    void shouldReturnEmptyListWhenNoRitualPowersFound() throws Exception {
        // Given
        when(service.getAll(eq(characterId), eq(testUser)))
                .thenReturn(Collections.emptyList());

        // When & Then
        try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
            mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

            mockMvc.perform(get("/characters/{characterId}/ritual-powers", characterId))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(0));

            verify(service).getAll(characterId, testUser);
        }
    }

    @Test
    @DisplayName("Should handle unauthorized access when getting all ritual powers")
    void shouldHandleUnauthorizedAccessWhenGettingAllRitualPowers() throws Exception {
        // Given
        when(service.getAll(eq(characterId), eq(testUser)))
                .thenThrow(new UnauthorizedActionException("Access denied"));

        // When & Then
        try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
            mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

            mockMvc.perform(get("/characters/{characterId}/ritual-powers", characterId))
                    .andExpect(status().isForbidden());

            verify(service).getAll(characterId, testUser);
        }
    }

    @Test
    @DisplayName("Should get specific ritual power successfully")
    void shouldGetSpecificRitualPowerSuccessfully() throws Exception {
        // Given
        CharacterRitualPowerResponse response = CharacterRitualPowerResponse.builder()
                .id(ritualPowerId)
                .name("Lightning Bolt")
                .pathsForms("Air 4, Create 2")
                .description("Electric damage spell")
                .bookPage("Page 67")
                .build();

        when(service.get(eq(characterId), eq(ritualPowerId), eq(testUser)))
                .thenReturn(response);

        // When & Then
        try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
            mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

            mockMvc.perform(get("/characters/{characterId}/ritual-powers/{id}", characterId, ritualPowerId))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(ritualPowerId))
                    .andExpect(jsonPath("$.name").value("Lightning Bolt"))
                    .andExpect(jsonPath("$.description").value("Electric damage spell"));

            verify(service).get(characterId, ritualPowerId, testUser);
        }
    }

    @Test
    @DisplayName("Should handle not found when getting specific ritual power")
    void shouldHandleNotFoundWhenGettingSpecificRitualPower() throws Exception {
        // Given
        when(service.get(eq(characterId), eq(ritualPowerId), eq(testUser)))
                .thenThrow(new EntityNotFoundException("Ritual power not found"));

        // When & Then
        try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
            mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

            mockMvc.perform(get("/characters/{characterId}/ritual-powers/{id}", characterId, ritualPowerId))
                    .andExpect(status().isNotFound());

            verify(service).get(characterId, ritualPowerId, testUser);
        }
    }

    @Test
    @DisplayName("Should save ritual power successfully")
    void shouldSaveRitualPowerSuccessfully() throws Exception {
        // Given
        CharacterRitualPowerRequest request = CharacterRitualPowerRequest.builder()
                .name("Ice Shard")
                .pathsForms("Water 2, Create 1")
                .description("Freezing projectile")
                .bookPage("Page 45")
                .build();

        CharacterRitualPowerResponse response = CharacterRitualPowerResponse.builder()
                .id(ritualPowerId)
                .name("Ice Shard")
                .pathsForms("Water 2, Create 1")
                .description("Freezing projectile")
                .bookPage("Page 45")
                .build();

        when(service.save(eq(characterId), eq(ritualPowerId), any(CharacterRitualPowerRequest.class), eq(testUser)))
                .thenReturn(response);

        // When & Then
        try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
            mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

            mockMvc.perform(post("/characters/{characterId}/ritual-powers/{id}", characterId, ritualPowerId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(ritualPowerId))
                    .andExpect(jsonPath("$.name").value("Ice Shard"))
                    .andExpect(jsonPath("$.description").value("Freezing projectile"));

            verify(service).save(eq(characterId), eq(ritualPowerId), any(CharacterRitualPowerRequest.class), eq(testUser));
        }
    }

    @Test
    @DisplayName("Should handle validation errors when saving ritual power")
    void shouldHandleValidationErrorsWhenSavingRitualPower() throws Exception {
        // Given
        CharacterRitualPowerRequest invalidRequest = CharacterRitualPowerRequest.builder()
                .name("") // Invalid empty name
                .pathsForms("")
                .description("")
                .bookPage("")
                .build();

        // When & Then
        try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
            mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

            mockMvc.perform(post("/characters/{characterId}/ritual-powers/{id}", characterId, ritualPowerId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            verify(service, never()).save(any(), any(), any(), any());
        }
    }

    @Test
    @DisplayName("Should handle unauthorized access when saving ritual power")
    void shouldHandleUnauthorizedAccessWhenSavingRitualPower() throws Exception {
        // Given
        CharacterRitualPowerRequest request = CharacterRitualPowerRequest.builder()
                .name("Test Ritual")
                .pathsForms("Fire 2, Create 1")
                .description("Test description")
                .bookPage("Page 123")
                .build();

        when(service.save(eq(characterId), eq(ritualPowerId), any(CharacterRitualPowerRequest.class), eq(testUser)))
                .thenThrow(new UnauthorizedActionException("Access denied"));

        // When & Then
        try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
            mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

            mockMvc.perform(post("/characters/{characterId}/ritual-powers/{id}", characterId, ritualPowerId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isForbidden());

            verify(service).save(eq(characterId), eq(ritualPowerId), any(CharacterRitualPowerRequest.class), eq(testUser));
        }
    }
}