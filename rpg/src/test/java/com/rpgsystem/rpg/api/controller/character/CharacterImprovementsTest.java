package com.rpgsystem.rpg.api.controller.character;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpgsystem.rpg.api.dto.character.CharacterImprovementRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterImprovementResponse;
import com.rpgsystem.rpg.application.service.character.CharacterImprovementService;
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

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CharacterImprovements Controller Tests")
class CharacterImprovementsTest {

    @Mock
    private CharacterImprovementService characterImprovementService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private User testUser;
    private final String characterId = "test-character-id";

    @BeforeEach
    void setUp() {
        CharacterImprovements controller = new CharacterImprovements(characterImprovementService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new com.rpgsystem.rpg.api.exception.GlobalExceptionHandler())
                .build();
        objectMapper = new ObjectMapper();
        testUser = createTestUser();
    }

    private User createTestUser() {
        return User.builder()
                .id(UUID.randomUUID().toString())
                .name("testuser")
                .email("test@example.com")
                .password("password")
                .isMaster(false)
                .build();
    }

    @Nested
    @DisplayName("Get Improvements Tests")
    class GetImprovementsTests {

        @Test
        @DisplayName("Should return list of improvements successfully")
        void shouldReturnListOfImprovementsSuccessfully() throws Exception {
            // Given
            List<CharacterImprovementResponse> improvements = Arrays.asList(
                    CharacterImprovementResponse.builder()
                            .characterId(characterId)
                            .name("Enhanced Strength")
                            .cost(10)
                            .kitCost(5)
                            .build(),
                    CharacterImprovementResponse.builder()
                            .characterId(characterId)
                            .name("Mental Fortitude")
                            .cost(15)
                            .kitCost(8)
                            .build()
            );

            when(characterImprovementService.getImprovements(eq(characterId), eq(testUser)))
                    .thenReturn(improvements);

            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(get("/characters/{characterId}/improvements", characterId))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$").isArray())
                        .andExpect(jsonPath("$.length()").value(2))
                        .andExpect(jsonPath("$[0].characterId").value(characterId))
                        .andExpect(jsonPath("$[0].name").value("Enhanced Strength"))
                        .andExpect(jsonPath("$[0].cost").value(10))
                        .andExpect(jsonPath("$[0].kitCost").value(5))
                        .andExpect(jsonPath("$[1].characterId").value(characterId))
                        .andExpect(jsonPath("$[1].name").value("Mental Fortitude"))
                        .andExpect(jsonPath("$[1].cost").value(15))
                        .andExpect(jsonPath("$[1].kitCost").value(8));

                verify(characterImprovementService).getImprovements(characterId, testUser);
            }
        }

        @Test
        @DisplayName("Should return empty list when no improvements found")
        void shouldReturnEmptyListWhenNoImprovementsFound() throws Exception {
            // Given
            when(characterImprovementService.getImprovements(eq(characterId), eq(testUser)))
                    .thenReturn(Arrays.asList());

            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(get("/characters/{characterId}/improvements", characterId))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$").isArray())
                        .andExpect(jsonPath("$.length()").value(0));

                verify(characterImprovementService).getImprovements(characterId, testUser);
            }
        }

        @Test
        @DisplayName("Should handle service exception when getting improvements")
        void shouldHandleServiceExceptionWhenGettingImprovements() throws Exception {
            // Given
            when(characterImprovementService.getImprovements(eq(characterId), eq(testUser)))
                    .thenThrow(new RuntimeException("Service error"));

            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(get("/characters/{characterId}/improvements", characterId))
                        .andExpect(status().isInternalServerError());

                verify(characterImprovementService).getImprovements(characterId, testUser);
            }
        }
    }

    @Nested
    @DisplayName("Get Single Improvement Tests")
    class GetSingleImprovementTests {

        @Test
        @DisplayName("Should return single improvement successfully")
        void shouldReturnSingleImprovementSuccessfully() throws Exception {
            // Given
            String improvementId = "improvement-1";
            CharacterImprovementResponse improvement = CharacterImprovementResponse.builder()
                    .characterId(characterId)
                    .name("Enhanced Strength")
                    .cost(10)
                    .kitCost(5)
                    .build();

            when(characterImprovementService.getImprovement(eq(improvementId), eq(characterId), eq(testUser)))
                    .thenReturn(improvement);

            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(get("/characters/{characterId}/improvements/{id}", characterId, improvementId))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.characterId").value(characterId))
                        .andExpect(jsonPath("$.name").value("Enhanced Strength"))
                        .andExpect(jsonPath("$.cost").value(10))
                        .andExpect(jsonPath("$.kitCost").value(5));

                verify(characterImprovementService).getImprovement(improvementId, characterId, testUser);
            }
        }

        @Test
        @DisplayName("Should handle service exception when getting single improvement")
        void shouldHandleServiceExceptionWhenGettingSingleImprovement() throws Exception {
            // Given
            String improvementId = "improvement-1";
            when(characterImprovementService.getImprovement(eq(improvementId), eq(characterId), eq(testUser)))
                    .thenThrow(new RuntimeException("Improvement not found"));

            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(get("/characters/{characterId}/improvements/{id}", characterId, improvementId))
                        .andExpect(status().isInternalServerError());

                verify(characterImprovementService).getImprovement(improvementId, characterId, testUser);
            }
        }
    }

    @Nested
    @DisplayName("Save Improvement Tests")
    class SaveImprovementTests {

        @Test
        @DisplayName("Should save improvement successfully")
        void shouldSaveImprovementSuccessfully() throws Exception {
            // Given
            String improvementId = "improvement-1";
            CharacterImprovementRequest request = CharacterImprovementRequest.builder()
                    .name("Enhanced Strength")
                    .cost(10)
                    .kitValue(5)
                    .build();

            CharacterImprovementResponse response = CharacterImprovementResponse.builder()
                    .characterId(characterId)
                    .name("Enhanced Strength")
                    .cost(10)
                    .kitCost(5)
                    .build();

            when(characterImprovementService.save(any(CharacterImprovementRequest.class), eq(improvementId), eq(characterId), eq(testUser)))
                    .thenReturn(response);

            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(post("/characters/{characterId}/improvements/{id}", characterId, improvementId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.characterId").value(characterId))
                        .andExpect(jsonPath("$.name").value("Enhanced Strength"))
                        .andExpect(jsonPath("$.cost").value(10))
                        .andExpect(jsonPath("$.kitCost").value(5));

                verify(characterImprovementService).save(any(CharacterImprovementRequest.class), eq(improvementId), eq(characterId), eq(testUser));
            }
        }

        @Test
        @DisplayName("Should return 400 when request body is invalid")
        void shouldReturn400WhenRequestBodyIsInvalid() throws Exception {
            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(post("/characters/{characterId}/improvements/{id}", characterId, "improvement-1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"invalid\": \"json\"}"))
                        .andExpect(status().isBadRequest());

                verify(characterImprovementService, never()).save(any(), any(), any(), any());
            }
        }

        @Test
        @DisplayName("Should handle service exception when saving improvement")
        void shouldHandleServiceExceptionWhenSavingImprovement() throws Exception {
            // Given
            String improvementId = "improvement-1";
            CharacterImprovementRequest request = CharacterImprovementRequest.builder()
                    .name("Test Improvement")
                    .cost(5)
                    .kitValue(3)
                    .build();

            when(characterImprovementService.save(any(CharacterImprovementRequest.class), eq(improvementId), eq(characterId), eq(testUser)))
                    .thenThrow(new RuntimeException("Service error"));

            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(post("/characters/{characterId}/improvements/{id}", characterId, improvementId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isInternalServerError());

                verify(characterImprovementService).save(any(CharacterImprovementRequest.class), eq(improvementId), eq(characterId), eq(testUser));
            }
        }
    }
}