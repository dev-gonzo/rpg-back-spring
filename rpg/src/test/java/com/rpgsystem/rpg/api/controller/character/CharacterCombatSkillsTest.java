package com.rpgsystem.rpg.api.controller.character;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpgsystem.rpg.api.dto.character.CharacterCombatSkillRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterCombatSkillResponse;
import com.rpgsystem.rpg.application.service.character.CharacterCombatSkillService;
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
@DisplayName("CharacterCombatSkills Controller Tests")
class CharacterCombatSkillsTest {

    @Mock
    private CharacterCombatSkillService characterCombatSkillService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private User testUser;
    private final String characterId = "test-character-id";
    private final String skillId = "test-skill-id";

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        testUser = createTestUser();
        
        CharacterCombatSkills characterCombatSkills = new CharacterCombatSkills(characterCombatSkillService);
        mockMvc = MockMvcBuilders.standaloneSetup(characterCombatSkills)
                .setControllerAdvice(new com.rpgsystem.rpg.api.exception.GlobalExceptionHandler())
                .build();
    }

    private User createTestUser() {
        return User.builder()
                .name("Test User")
                .email("testuser@example.com")
                .password("password")
                .isMaster(false)
                .build();
    }

    @Nested
    @DisplayName("Get Skills Tests")
    class GetSkillsTests {

        @Test
        @DisplayName("Should return list of combat skills successfully")
        void shouldReturnListOfCombatSkillsSuccessfully() throws Exception {
            // Given
            List<CharacterCombatSkillResponse> skills = Arrays.asList(
                    CharacterCombatSkillResponse.builder()
                            .id("skill-1")
                            .characterId(characterId)
                            .skill("Sword Fighting")
                             .group("Combat")
                             .attribute("Strength")
                             .attackCost(10)
                             .defenseCost(8)
                             .attackKitValue(5)
                             .defenseKitValue(3)
                             .build(),
                    CharacterCombatSkillResponse.builder()
                            .id("skill-2")
                            .characterId(characterId)
                            .skill("Archery")
                             .group("Ranged")
                             .attribute("Dexterity")
                             .attackCost(12)
                             .defenseCost(6)
                             .attackKitValue(4)
                             .defenseKitValue(2)
                             .build()
            );

            when(characterCombatSkillService.getSkills(eq(characterId), eq(testUser)))
                    .thenReturn(skills);

            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(get("/characters/{characterId}/combat-skills", characterId))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$").isArray())
                        .andExpect(jsonPath("$.length()").value(2))
                        .andExpect(jsonPath("$[0].id").value("skill-1"))
                        .andExpect(jsonPath("$[0].skill").value("Sword Fighting"))
                         .andExpect(jsonPath("$[0].group").value("Combat"))
                         .andExpect(jsonPath("$[0].attribute").value("Strength"))
                         .andExpect(jsonPath("$[0].attackCost").value(10))
                         .andExpect(jsonPath("$[0].defenseCost").value(8))
                         .andExpect(jsonPath("$[0].attackKitValue").value(5))
                         .andExpect(jsonPath("$[0].defenseKitValue").value(3))
                        .andExpect(jsonPath("$[1].id").value("skill-2"))
                        .andExpect(jsonPath("$[1].skill").value("Archery"))
                         .andExpect(jsonPath("$[1].group").value("Ranged"))
                         .andExpect(jsonPath("$[1].attribute").value("Dexterity"))
                         .andExpect(jsonPath("$[1].attackCost").value(12))
                         .andExpect(jsonPath("$[1].defenseCost").value(6))
                         .andExpect(jsonPath("$[1].attackKitValue").value(4))
                         .andExpect(jsonPath("$[1].defenseKitValue").value(2));

                verify(characterCombatSkillService).getSkills(characterId, testUser);
            }
        }

        @Test
        @DisplayName("Should return empty list when no skills found")
        void shouldReturnEmptyListWhenNoSkillsFound() throws Exception {
            // Given
            when(characterCombatSkillService.getSkills(eq(characterId), eq(testUser)))
                    .thenReturn(Arrays.asList());

            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(get("/characters/{characterId}/combat-skills", characterId))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$").isArray())
                        .andExpect(jsonPath("$.length()").value(0));

                verify(characterCombatSkillService).getSkills(characterId, testUser);
            }
        }
    }

    @Nested
    @DisplayName("Get Single Skill Tests")
    class GetSingleSkillTests {

        @Test
        @DisplayName("Should return single combat skill successfully")
        void shouldReturnSingleCombatSkillSuccessfully() throws Exception {
            // Given
            CharacterCombatSkillResponse skill = CharacterCombatSkillResponse.builder()
                    .id(skillId)
                    .characterId(characterId)
                    .skill("Sword Fighting")
                    .group("Combat")
                    .attribute("Strength")
                    .attackCost(10)
                    .defenseCost(8)
                    .attackKitValue(5)
                    .defenseKitValue(3)
                     .build();

            when(characterCombatSkillService.getSkill(eq(skillId), eq(characterId), eq(testUser)))
                    .thenReturn(skill);

            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(get("/characters/{characterId}/combat-skills/{id}", characterId, skillId))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.id").value(skillId))
                        .andExpect(jsonPath("$.characterId").value(characterId))
                        .andExpect(jsonPath("$.skill").value("Sword Fighting"))
                        .andExpect(jsonPath("$.group").value("Combat"))
                        .andExpect(jsonPath("$.attribute").value("Strength"))
                        .andExpect(jsonPath("$.attackCost").value(10))
                        .andExpect(jsonPath("$.defenseCost").value(8))
                        .andExpect(jsonPath("$.attackKitValue").value(5))
                        .andExpect(jsonPath("$.defenseKitValue").value(3));

                verify(characterCombatSkillService).getSkill(skillId, characterId, testUser);
            }
        }

        @Test
        @DisplayName("Should handle service exception when getting single skill")
        void shouldHandleServiceExceptionWhenGettingSingleSkill() throws Exception {
            // Given
            when(characterCombatSkillService.getSkill(eq(skillId), eq(characterId), eq(testUser)))
                    .thenThrow(new RuntimeException("Skill not found"));

            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(get("/characters/{characterId}/combat-skills/{id}", characterId, skillId))
                        .andExpect(status().isInternalServerError());

                verify(characterCombatSkillService).getSkill(skillId, characterId, testUser);
            }
        }
    }

    @Nested
    @DisplayName("Save Skill Tests")
    class SaveSkillTests {

        @Test
        @DisplayName("Should save combat skill successfully")
        void shouldSaveCombatSkillSuccessfully() throws Exception {
            // Given
            CharacterCombatSkillRequest request = CharacterCombatSkillRequest.builder()
                    .skill("Sword Fighting")
                    .group("Combat")
                    .attribute("Strength")
                    .attackCost(10)
                    .defenseCost(8)
                    .attackKitValue(5)
                    .defenseKitValue(3)
                     .build();

            CharacterCombatSkillResponse response = CharacterCombatSkillResponse.builder()
                    .id(skillId)
                    .characterId(characterId)
                    .skill("Sword Fighting")
                    .group("Combat")
                    .attribute("Strength")
                    .attackCost(10)
                    .defenseCost(8)
                    .attackKitValue(5)
                    .defenseKitValue(3)
                     .build();

            when(characterCombatSkillService.save(any(CharacterCombatSkillRequest.class), eq(skillId), eq(characterId), eq(testUser)))
                    .thenReturn(response);

            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(post("/characters/{characterId}/combat-skills/{id}", characterId, skillId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.id").value(skillId))
                        .andExpect(jsonPath("$.characterId").value(characterId))
                        .andExpect(jsonPath("$.skill").value("Sword Fighting"))
                        .andExpect(jsonPath("$.group").value("Combat"))
                        .andExpect(jsonPath("$.attribute").value("Strength"))
                        .andExpect(jsonPath("$.attackCost").value(10))
                        .andExpect(jsonPath("$.defenseCost").value(8))
                        .andExpect(jsonPath("$.attackKitValue").value(5))
                        .andExpect(jsonPath("$.defenseKitValue").value(3));

                verify(characterCombatSkillService).save(any(CharacterCombatSkillRequest.class), eq(skillId), eq(characterId), eq(testUser));
            }
        }

        @Test
        @DisplayName("Should return 400 when request body is invalid")
        void shouldReturn400WhenRequestBodyIsInvalid() throws Exception {
            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(post("/characters/{characterId}/combat-skills/{id}", characterId, skillId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"invalid\": \"json\"}"))
                        .andExpect(status().isBadRequest());

                verify(characterCombatSkillService, never()).save(any(), any(), any(), any());
            }
        }

        @Test
        @DisplayName("Should handle service exception when saving skill")
        void shouldHandleServiceExceptionWhenSavingSkill() throws Exception {
            // Given
            CharacterCombatSkillRequest request = CharacterCombatSkillRequest.builder()
                    .skill("Test Skill")
                    .group("Combat")
                    .attribute("Strength")
                    .attackCost(5)
                    .defenseCost(5)
                    .attackKitValue(1)
                    .defenseKitValue(1)
                    .build();

            when(characterCombatSkillService.save(any(CharacterCombatSkillRequest.class), eq(skillId), eq(characterId), eq(testUser)))
                    .thenThrow(new RuntimeException("Service error"));

            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(post("/characters/{characterId}/combat-skills/{id}", characterId, skillId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isInternalServerError());

                verify(characterCombatSkillService).save(any(CharacterCombatSkillRequest.class), eq(skillId), eq(characterId), eq(testUser));
            }
        }
    }
}