package com.rpgsystem.rpg.api.controller.character;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpgsystem.rpg.api.dto.character.CharacterEquipmentRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterEquipmentResponse;
import com.rpgsystem.rpg.application.service.character.CharacterEquipmentService;
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
@DisplayName("CharacterEquipments Controller Tests")
class CharacterEquipmentsTest {

    @Mock
    private CharacterEquipmentService characterEquipmentService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private User testUser;
    private final String characterId = "test-character-id";

    @BeforeEach
    void setUp() {
        CharacterEquipments controller = new CharacterEquipments(characterEquipmentService);
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
    @DisplayName("Get Equipments Tests")
    class GetEquipmentsTests {

        @Test
        @DisplayName("Should return list of equipments successfully")
        void shouldReturnListOfEquipmentsSuccessfully() throws Exception {
            // Given
            List<CharacterEquipmentResponse> equipments = Arrays.asList(
                    CharacterEquipmentResponse.builder()
                            .id("equipment-1")
                            .name("Sword")
                            .classification("Weapon")
                            .quantity(1)
                             .description("A sharp sword")
                            
                            .build(),
                    CharacterEquipmentResponse.builder()
                            .id("equipment-2")
                            .name("Shield")
                            .classification("Armor")
                            .quantity(1)
                             .description("A protective shield")
                            
                            .build()
            );

            when(characterEquipmentService.getAll(eq(characterId), eq(testUser)))
                    .thenReturn(equipments);

            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(get("/characters/{characterId}/equipments", characterId))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$").isArray())
                        .andExpect(jsonPath("$.length()").value(2))
                        .andExpect(jsonPath("$[0].id").value("equipment-1"))
                        .andExpect(jsonPath("$[0].name").value("Sword"))
                        .andExpect(jsonPath("$[0].classification").value("Weapon"))
                        .andExpect(jsonPath("$[0].quantity").value(1))
                         .andExpect(jsonPath("$[0].description").value("A sharp sword"))
                        
                        .andExpect(jsonPath("$[1].id").value("equipment-2"))
                        .andExpect(jsonPath("$[1].name").value("Shield"))
                        .andExpect(jsonPath("$[1].classification").value("Armor"))
                        .andExpect(jsonPath("$[1].quantity").value(1))
                         .andExpect(jsonPath("$[1].description").value("A protective shield"))
                        ;

                verify(characterEquipmentService).getAll(characterId, testUser);
            }
        }

        @Test
        @DisplayName("Should return empty list when no equipments found")
        void shouldReturnEmptyListWhenNoEquipmentsFound() throws Exception {
            // Given
            when(characterEquipmentService.getAll(eq(characterId), eq(testUser)))
                    .thenReturn(Arrays.asList());

            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(get("/characters/{characterId}/equipments", characterId))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$").isArray())
                        .andExpect(jsonPath("$.length()").value(0));

                verify(characterEquipmentService).getAll(characterId, testUser);
            }
        }

        @Test
        @DisplayName("Should handle service exception when getting equipments")
        void shouldHandleServiceExceptionWhenGettingEquipments() throws Exception {
            // Given
            when(characterEquipmentService.getAll(eq(characterId), eq(testUser)))
                    .thenThrow(new RuntimeException("Service error"));

            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(get("/characters/{characterId}/equipments", characterId))
                        .andExpect(status().isInternalServerError());

                verify(characterEquipmentService).getAll(characterId, testUser);
            }
        }
    }

    @Nested
    @DisplayName("Get Single Equipment Tests")
    class GetSingleEquipmentTests {

        @Test
        @DisplayName("Should return single equipment successfully")
        void shouldReturnSingleEquipmentSuccessfully() throws Exception {
            // Given
            String equipmentId = "equipment-1";
            CharacterEquipmentResponse equipment = CharacterEquipmentResponse.builder()
                    .id(equipmentId)
                    .name("Magic Sword")
                    .classification("Weapon")
                    .quantity(1)
                     .description("A magical sword")
                    
                    .build();

            when(characterEquipmentService.get(eq(characterId), eq(equipmentId), eq(testUser)))
                    .thenReturn(equipment);

            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(get("/characters/{characterId}/equipments/{id}", characterId, equipmentId))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.id").value(equipmentId))
                        .andExpect(jsonPath("$.name").value("Magic Sword"))
                        .andExpect(jsonPath("$.classification").value("Weapon"))
                        .andExpect(jsonPath("$.quantity").value(1))
                         .andExpect(jsonPath("$.description").value("A magical sword"))
                        ;

                verify(characterEquipmentService).get(characterId, equipmentId, testUser);
            }
        }

        @Test
        @DisplayName("Should handle service exception when getting single equipment")
        void shouldHandleServiceExceptionWhenGettingSingleEquipment() throws Exception {
            // Given
            String equipmentId = "equipment-1";
            when(characterEquipmentService.get(eq(characterId), eq(equipmentId), eq(testUser)))
                    .thenThrow(new RuntimeException("Service error"));

            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(get("/characters/{characterId}/equipments/{id}", characterId, equipmentId))
                        .andExpect(status().isInternalServerError());

                verify(characterEquipmentService).get(characterId, equipmentId, testUser);
            }
        }
    }

    @Nested
    @DisplayName("Save Equipment Tests")
    class SaveEquipmentTests {

        @Test
        @DisplayName("Should save equipment successfully")
        void shouldSaveEquipmentSuccessfully() throws Exception {
            // Given
            String equipmentId = "equipment-1";
            CharacterEquipmentRequest request = CharacterEquipmentRequest.builder()
                    .name("Test Equipment")
                    .classification("Test Category")
                    .quantity(2)
                     .description("Test equipment description")
                    
                    .build();

            CharacterEquipmentResponse response = CharacterEquipmentResponse.builder()
                    .id(equipmentId)
                    .name("Test Equipment")
                    .classification("Test Category")
                    .quantity(2)
                     .description("Test equipment description")
                    
                    .build();

            when(characterEquipmentService.save(eq(characterId), eq(equipmentId), any(CharacterEquipmentRequest.class), eq(testUser)))
                    .thenReturn(response);

            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(post("/characters/{characterId}/equipments/{id}", characterId, equipmentId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.id").value(equipmentId))
                        .andExpect(jsonPath("$.name").value("Test Equipment"))
                        .andExpect(jsonPath("$.classification").value("Test Category"))
                        .andExpect(jsonPath("$.quantity").value(2))
                         .andExpect(jsonPath("$.description").value("Test equipment description"))
                        ;

                verify(characterEquipmentService).save(eq(characterId), eq(equipmentId), any(CharacterEquipmentRequest.class), eq(testUser));
            }
        }

        @Test
        @DisplayName("Should handle invalid request body when saving equipment")
        void shouldHandleInvalidRequestBodyWhenSavingEquipment() throws Exception {
            // Given
            String equipmentId = "equipment-1";

            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(post("/characters/{characterId}/equipments/{id}", characterId, equipmentId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"invalid\": \"json\"}"))
                        .andExpect(status().isBadRequest());

                verify(characterEquipmentService, never()).save(any(), any(), any(), any());
            }
        }

        @Test
        @DisplayName("Should handle service exception when saving equipment")
        void shouldHandleServiceExceptionWhenSavingEquipment() throws Exception {
            // Given
            String equipmentId = "equipment-1";
            CharacterEquipmentRequest request = CharacterEquipmentRequest.builder()
                    .name("Test Equipment")
                    .classification("Test Category")
                    .quantity(1)
                     .description("Invalid equipment")
                    .build();

            when(characterEquipmentService.save(eq(characterId), eq(equipmentId), any(CharacterEquipmentRequest.class), eq(testUser)))
                    .thenThrow(new RuntimeException("Service error"));

            try (MockedStatic<AuthenticatedUserHelper> mockedHelper = mockStatic(AuthenticatedUserHelper.class)) {
                mockedHelper.when(AuthenticatedUserHelper::get).thenReturn(testUser);

                // When & Then
                mockMvc.perform(post("/characters/{characterId}/equipments/{id}", characterId, equipmentId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isInternalServerError());

                verify(characterEquipmentService).save(eq(characterId), eq(equipmentId), any(CharacterEquipmentRequest.class), eq(testUser));
            }
        }
    }
}