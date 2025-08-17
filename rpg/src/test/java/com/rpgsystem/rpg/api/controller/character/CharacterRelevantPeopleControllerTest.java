package com.rpgsystem.rpg.api.controller.character;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpgsystem.rpg.api.dto.character.CharacterRelevantPeopleRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterRelevantPeopleResponse;
import com.rpgsystem.rpg.application.service.character.CharacterRelevantPeopleService;
import com.rpgsystem.rpg.domain.entity.User;
import jakarta.persistence.EntityNotFoundException;
import com.rpgsystem.rpg.domain.exception.UnauthorizedActionException;
import com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@DisplayName("CharacterRelevantPeople Controller Tests")
class CharacterRelevantPeopleControllerTest {

    private MockMvc mockMvc;
    private CharacterRelevantPeopleService service;
    private ObjectMapper objectMapper = new ObjectMapper();

    private User testUser;
    private final String characterId = "test-character-id";
    private final String personId = "test-person-id";

    @BeforeEach
    void setUp() {
        service = mock(CharacterRelevantPeopleService.class);
        
        testUser = User.builder()
                .id("test-user-id")
                .email("testuser@example.com")
                .name("Test User")
                .build();
                
        CharacterRelevantPeople controller = new CharacterRelevantPeople(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new com.rpgsystem.rpg.api.exception.GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("Should get all relevant people successfully")
    void shouldGetAllRelevantPeopleSuccessfully() throws Exception {
        // Given
        CharacterRelevantPeopleResponse response1 = CharacterRelevantPeopleResponse.builder()
                .id("person-1")
                .name("John Doe")
                .category("Friend")
                .briefDescription("A loyal companion")
                .build();

        CharacterRelevantPeopleResponse response2 = CharacterRelevantPeopleResponse.builder()
                .id("person-2")
                .name("Jane Smith")
                .category("Mentor")
                .briefDescription("Wise teacher")
                .build();

        List<CharacterRelevantPeopleResponse> relevantPeople = Arrays.asList(response1, response2);

        when(service.getAll(eq(characterId), eq(testUser)))
                .thenReturn(relevantPeople);

        // When & Then
        try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
            mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

            mockMvc.perform(get("/characters/{characterId}/relevant-people", characterId))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].id").value("person-1"))
                    .andExpect(jsonPath("$[0].name").value("John Doe"))
                    .andExpect(jsonPath("$[1].id").value("person-2"))
                    .andExpect(jsonPath("$[1].name").value("Jane Smith"));

            verify(service).getAll(characterId, testUser);
        }
    }

    @Test
    @DisplayName("Should return empty list when no relevant people found")
    void shouldReturnEmptyListWhenNoRelevantPeopleFound() throws Exception {
        // Given
        when(service.getAll(eq(characterId), eq(testUser)))
                .thenReturn(Collections.emptyList());

        // When & Then
        try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
            mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

            mockMvc.perform(get("/characters/{characterId}/relevant-people", characterId))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(0));

            verify(service).getAll(characterId, testUser);
        }
    }

    @Test
    @DisplayName("Should handle unauthorized access when getting all relevant people")
    void shouldHandleUnauthorizedAccessWhenGettingAllRelevantPeople() throws Exception {
        // Given
        when(service.getAll(eq(characterId), eq(testUser)))
                .thenThrow(new UnauthorizedActionException("Access denied"));

        // When & Then
        try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
            mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

            mockMvc.perform(get("/characters/{characterId}/relevant-people", characterId))
                    .andExpect(status().isForbidden());

            verify(service).getAll(characterId, testUser);
        }
    }

    @Test
    @DisplayName("Should get specific relevant person successfully")
    void shouldGetSpecificRelevantPersonSuccessfully() throws Exception {
        // Given
        CharacterRelevantPeopleResponse response = CharacterRelevantPeopleResponse.builder()
                .id(personId)
                .name("Bob Wilson")
                .category("Enemy")
                .briefDescription("A dangerous adversary")
                .build();

        when(service.get(eq(characterId), eq(personId), eq(testUser)))
                .thenReturn(response);

        // When & Then
        try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
            mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

            mockMvc.perform(get("/characters/{characterId}/relevant-people/{id}", characterId, personId))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(personId))
                    .andExpect(jsonPath("$.name").value("Bob Wilson"))
                    .andExpect(jsonPath("$.category").value("Enemy"));

            verify(service).get(characterId, personId, testUser);
        }
    }

    @Test
    @DisplayName("Should handle not found when getting specific relevant person")
    void shouldHandleNotFoundWhenGettingSpecificRelevantPerson() throws Exception {
        // Given
        when(service.get(eq(characterId), eq(personId), eq(testUser)))
                .thenThrow(new EntityNotFoundException("Relevant person not found"));

        // When & Then
        try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
            mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

            mockMvc.perform(get("/characters/{characterId}/relevant-people/{id}", characterId, personId))
                    .andExpect(status().isNotFound());

            verify(service).get(characterId, personId, testUser);
        }
    }

    @Test
    @DisplayName("Should save relevant person successfully")
    void shouldSaveRelevantPersonSuccessfully() throws Exception {
        // Given
        CharacterRelevantPeopleRequest request = CharacterRelevantPeopleRequest.builder()
                .name("Alice Brown")
                .category("Sister")
                .briefDescription("Beloved family member")
                .build();

        CharacterRelevantPeopleResponse response = CharacterRelevantPeopleResponse.builder()
                .id(personId)
                .name("Alice Brown")
                .category("Sister")
                .briefDescription("Beloved family member")
                .build();

        when(service.save(eq(characterId), eq(personId), any(CharacterRelevantPeopleRequest.class), eq(testUser)))
                .thenReturn(response);

        // When & Then
        try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
            mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

            mockMvc.perform(post("/characters/{characterId}/relevant-people/{id}", characterId, personId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.id").value(personId))
                    .andExpect(jsonPath("$.name").value("Alice Brown"))
                    .andExpect(jsonPath("$.category").value("Sister"));

            verify(service).save(eq(characterId), eq(personId), any(CharacterRelevantPeopleRequest.class), eq(testUser));
        }
    }

    @Test
    @DisplayName("Should handle validation errors when saving relevant person")
    void shouldHandleValidationErrorsWhenSavingRelevantPerson() throws Exception {
        // Given
        CharacterRelevantPeopleRequest invalidRequest = CharacterRelevantPeopleRequest.builder()
                .name("") // Invalid empty name
                .category("")
                .briefDescription("")
                .build();

        // When & Then
        try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
            mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

            mockMvc.perform(post("/characters/{characterId}/relevant-people/{id}", characterId, personId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest());

            verify(service, never()).save(any(), any(), any(), any());
        }
    }

    @Test
    @DisplayName("Should handle unauthorized access when saving relevant person")
    void shouldHandleUnauthorizedAccessWhenSavingRelevantPerson() throws Exception {
        // Given
        CharacterRelevantPeopleRequest request = CharacterRelevantPeopleRequest.builder()
                .name("Test Person")
                .category("Test Category")
                .briefDescription("Test description")
                .build();

        when(service.save(eq(characterId), eq(personId), any(CharacterRelevantPeopleRequest.class), eq(testUser)))
                .thenThrow(new UnauthorizedActionException("Access denied"));

        // When & Then
        try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
            mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

            mockMvc.perform(post("/characters/{characterId}/relevant-people/{id}", characterId, personId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isForbidden());

            verify(service).save(eq(characterId), eq(personId), any(CharacterRelevantPeopleRequest.class), eq(testUser));
        }
    }
}