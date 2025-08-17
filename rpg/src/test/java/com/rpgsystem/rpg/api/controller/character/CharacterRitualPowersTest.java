package com.rpgsystem.rpg.api.controller.character;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpgsystem.rpg.api.dto.character.CharacterRitualPowerRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterRitualPowerResponse;
import com.rpgsystem.rpg.application.service.character.CharacterRitualPowerService;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.rpgsystem.rpg.api.exception.GlobalExceptionHandler;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CharacterRitualPowers Controller Tests")
class CharacterRitualPowersTest {

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Mock
    private CharacterRitualPowerService characterRitualPowerService;

    // AuthenticatedUserHelper will be mocked statically in individual tests

    private User user;
    private String characterId;
    private String ritualPowerId;
    private CharacterRitualPowerResponse response;
    private CharacterRitualPowerRequest request;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new CharacterRitualPowers(characterRitualPowerService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        
        objectMapper = new ObjectMapper();
        
        user = User.builder()
                .id("user-id")
                .name("Test User")
                .email("test@example.com")
                .password("password")
                .build();

        characterId = "character-id";
        ritualPowerId = "ritual-power-id";

        response = CharacterRitualPowerResponse.builder()
                .id(ritualPowerId)
                .characterId(characterId)
                .name("Healing Ritual")
                .pathsForms("Fire, Water")
                .description("A powerful ritual")
                .bookPage("Page 123")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        request = CharacterRitualPowerRequest.builder()
                .name("Healing Ritual")
                .pathsForms("Fire, Water")
                .description("A powerful ritual")
                .bookPage("Page 123")
                .build();
    }

    @Nested
    @DisplayName("GET /characters/{characterId}/ritual-powers")
    class GetAllRitualPowersTests {

        @Test
        @DisplayName("Should return all ritual powers successfully")
        void shouldGetAllRitualPowersSuccessfully() throws Exception {
            // Given
            List<CharacterRitualPowerResponse> ritualPowers = Arrays.asList(response);
            when(characterRitualPowerService.getAll(eq(characterId), any(User.class)))
                    .thenReturn(ritualPowers);

            // When & Then
            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(user);

                mockMvc.perform(get("/characters/{characterId}/ritual-powers", characterId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$").isArray())
                        .andExpect(jsonPath("$[0].id").value(ritualPowerId))
                        .andExpect(jsonPath("$[0].characterId").value(characterId))
                        .andExpect(jsonPath("$[0].name").value("Healing Ritual"))
                        .andExpect(jsonPath("$[0].pathsForms").value("Fire, Water"))
                        .andExpect(jsonPath("$[0].description").value("A powerful ritual"))
                        .andExpect(jsonPath("$[0].bookPage").value("Page 123"));

                verify(characterRitualPowerService).getAll(characterId, user);
            }
        }

        @Test
        @DisplayName("Should return empty list when no ritual powers found")
        void shouldReturnEmptyListWhenNoRitualPowersFound() throws Exception {
            // Given
            when(characterRitualPowerService.getAll(eq(characterId), any(User.class)))
                    .thenReturn(Arrays.asList());

            // When & Then
            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(user);

                mockMvc.perform(get("/characters/{characterId}/ritual-powers", characterId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$").isArray())
                        .andExpect(jsonPath("$").isEmpty());

                verify(characterRitualPowerService).getAll(characterId, user);
            }
        }

        // Authentication tests are not applicable with standalone setup
        // as security aspects are not triggered
    }

    @Nested
    @DisplayName("GET /characters/{characterId}/ritual-powers/{id}")
    class GetRitualPowerTests {

        @Test
        @DisplayName("Should return ritual power successfully")
        void shouldGetRitualPowerSuccessfully() throws Exception {
            // Given
            when(characterRitualPowerService.get(eq(characterId), eq(ritualPowerId), any(User.class)))
                    .thenReturn(response);

            // When & Then
            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(user);

                mockMvc.perform(get("/characters/{characterId}/ritual-powers/{id}", characterId, ritualPowerId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.id").value(ritualPowerId))
                        .andExpect(jsonPath("$.characterId").value(characterId))
                        .andExpect(jsonPath("$.name").value("Healing Ritual"))
                        .andExpect(jsonPath("$.pathsForms").value("Fire, Water"))
                        .andExpect(jsonPath("$.description").value("A powerful ritual"))
                        .andExpect(jsonPath("$.bookPage").value("Page 123"));

                verify(characterRitualPowerService).get(characterId, ritualPowerId, user);
            }
        }

        // Authentication tests are not applicable with standalone setup
    }

    @Nested
    @DisplayName("POST /characters/{characterId}/ritual-powers/{id}")
    class SaveRitualPowerTests {

        @Test
        @DisplayName("Should save ritual power successfully")
        void shouldSaveRitualPowerSuccessfully() throws Exception {
            // Given
            when(characterRitualPowerService.save(eq(characterId), eq(ritualPowerId), any(CharacterRitualPowerRequest.class), any(User.class)))
                    .thenReturn(response);

            // When & Then
            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(user);

                mockMvc.perform(post("/characters/{characterId}/ritual-powers/{id}", characterId, ritualPowerId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.id").value(ritualPowerId))
                        .andExpect(jsonPath("$.characterId").value(characterId))
                        .andExpect(jsonPath("$.name").value("Healing Ritual"))
                        .andExpect(jsonPath("$.pathsForms").value("Fire, Water"))
                        .andExpect(jsonPath("$.description").value("A powerful ritual"))
                        .andExpect(jsonPath("$.bookPage").value("Page 123"));

                verify(characterRitualPowerService).save(eq(characterId), eq(ritualPowerId), any(CharacterRitualPowerRequest.class), eq(user));
            }
        }

        @Test
        @DisplayName("Should return 400 when request body is invalid")
        void shouldReturn400WhenRequestBodyIsInvalid() throws Exception {
            // Given
            CharacterRitualPowerRequest invalidRequest = CharacterRitualPowerRequest.builder()
                    .name("") // Empty name
                    .pathsForms("Fire, Water")
                    .description("A powerful ritual")
                    .bookPage("Page 123")
                    .build();

            // When & Then
            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(user);

                mockMvc.perform(post("/characters/{characterId}/ritual-powers/{id}", characterId, ritualPowerId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest)))
                        .andExpect(status().isBadRequest());

                verify(characterRitualPowerService, never()).save(anyString(), anyString(), any(CharacterRitualPowerRequest.class), any(User.class));
            }
        }

        // Authentication tests are not applicable with standalone setup
    }
}