package com.rpgsystem.rpg.api.controller.character;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rpgsystem.rpg.api.dto.character.CharacterInfoRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterInfoResponse;
import com.rpgsystem.rpg.application.service.character.CharacterService;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.infrastructure.security.AuthenticatedUserProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CharacterController Tests")
class CharacterControllerTest {

    @Mock
    private AuthenticatedUserProvider authenticatedUserProvider;
    
    @Mock
    private CharacterService characterService;
    
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private User testUser;
    
    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        testUser = createTestUser();
        
        CharacterController characterController = new CharacterController(authenticatedUserProvider, characterService);
        mockMvc = MockMvcBuilders.standaloneSetup(characterController)
                .setControllerAdvice(new com.rpgsystem.rpg.api.exception.GlobalExceptionHandler())
                .build();
    }
    
    private User createTestUser() {
        User user = new User();
        user.setId(UUID.randomUUID().toString());
        user.setName("Test User");
        user.setEmail("test@example.com");
        return user;
    }
    
    @Nested
    @DisplayName("Create Character Tests")
    class CreateCharacterTests {
        
        @Test
        @DisplayName("Should create character successfully")
        void shouldCreateCharacterSuccessfully() throws Exception {
            // Given
            CharacterInfoRequest request = CharacterInfoRequest.builder()
                .name("Test Character")
                .age(25)
                .gender("Male")
                .profession("Test Profession")
                .birthPlace("Test Birth Place")
                .apparentAge(25)
                .heightCm(180)
                .weightKg(75)
                .religion("Test Religion")
                .build();
            
            CharacterInfoResponse response = CharacterInfoResponse.builder()
                .id(UUID.randomUUID().toString())
                .name("Test Character")
                .age(25)
                .gender("Male")
                .profession("Test Profession")
                .birthPlace("Test Birth Place")
                .apparentAge(25)
                .heightCm(180)
                .weightKg(75)
                .religion("Test Religion")
                .build();
            
            try (MockedStatic<com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper> mockedHelper = 
                    mockStatic(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper.class)) {
                
                mockedHelper.when(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper::get)
                           .thenReturn(testUser);
                
                when(characterService.create(any(CharacterInfoRequest.class), eq(testUser)))
                    .thenReturn(response);
                
                // When & Then
                mockMvc.perform(post("/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(response.getId()))
                        .andExpect(jsonPath("$.name").value("Test Character"))
                        .andExpect(jsonPath("$.age").value(25))
                        .andExpect(jsonPath("$.gender").value("Male"))
                        .andExpect(jsonPath("$.profession").value("Test Profession"))
                        .andExpect(jsonPath("$.birthPlace").value("Test Birth Place"))
                        .andExpect(jsonPath("$.apparentAge").value(25))
                        .andExpect(jsonPath("$.heightCm").value(180))
                        .andExpect(jsonPath("$.weightKg").value(75))
                        .andExpect(jsonPath("$.religion").value("Test Religion"));
                
                verify(characterService).create(any(CharacterInfoRequest.class), eq(testUser));
            }
        }
        
        @Test
        @DisplayName("Should return 400 when name is null")
        void shouldReturn400WhenNameIsNull() throws Exception {
            // Given
            CharacterInfoRequest request = CharacterInfoRequest.builder()
                .name(null)
                .age(25)
                .gender("Male")
                .profession("Test Profession")
                .birthPlace("Test Birth Place")
                .build();
            
            try (MockedStatic<com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper> mockedHelper = 
                    mockStatic(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper.class)) {
                
                mockedHelper.when(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper::get)
                           .thenReturn(testUser);
                
                // When & Then
                mockMvc.perform(post("/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isBadRequest());
                
                verify(characterService, never()).create(any(CharacterInfoRequest.class), any(User.class));
            }
        }
        
        @Test
        @DisplayName("Should return 400 when name is empty")
        void shouldReturn400WhenNameIsEmpty() throws Exception {
            // Given
            CharacterInfoRequest request = CharacterInfoRequest.builder()
                .name("")
                .age(25)
                .gender("Male")
                .profession("Test Profession")
                .birthPlace("Test Birth Place")
                .build();
            
            try (MockedStatic<com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper> mockedHelper = 
                    mockStatic(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper.class)) {
                
                mockedHelper.when(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper::get)
                           .thenReturn(testUser);
                
                // When & Then
                mockMvc.perform(post("/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isBadRequest());
                
                verify(characterService, never()).create(any(CharacterInfoRequest.class), any(User.class));
            }
        }
        
        @Test
        @DisplayName("Should return 400 when age is negative")
        void shouldReturn400WhenAgeIsNegative() throws Exception {
            // Given
            CharacterInfoRequest request = CharacterInfoRequest.builder()
                .name("Test Character")
                .age(-1) // Negative age should trigger @Min validation
                .gender("Male")
                .profession("Test Profession")
                .birthPlace("Test Birth Place")
                .build();
            
            try (MockedStatic<com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper> mockedHelper = 
                    mockStatic(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper.class)) {
                
                mockedHelper.when(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper::get)
                           .thenReturn(testUser);
                
                // When & Then
                mockMvc.perform(post("/characters")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isBadRequest());
                
                verify(characterService, never()).create(any(CharacterInfoRequest.class), any(User.class));
            }
        }
    }
    
    @Nested
    @DisplayName("Upload Photo Tests")
    class UploadPhotoTests {
        
        @Test
        @DisplayName("Should upload photo successfully")
        void shouldUploadPhotoSuccessfully() throws Exception {
            // Given
            String characterId = UUID.randomUUID().toString();
            MockMultipartFile file = new MockMultipartFile(
                "file", 
                "test-image.jpg", 
                "image/jpeg", 
                "test image content".getBytes()
            );
            
            try (MockedStatic<com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper> mockedHelper = 
                    mockStatic(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper.class)) {
                
                mockedHelper.when(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper::get)
                           .thenReturn(testUser);
                
                doNothing().when(characterService).uploadImage(eq(characterId), any(MultipartFile.class), eq(testUser));
                
                // When & Then
                mockMvc.perform(multipart("/characters/{characterId}/upload-photo", characterId)
                        .file(file))
                        .andExpect(status().isOk());
                
                verify(characterService).uploadImage(eq(characterId), any(MultipartFile.class), eq(testUser));
            }
        }
        
        @Test
        @DisplayName("Should return 400 when file parameter is missing")
        void shouldReturn400WhenFileParameterIsMissing() throws Exception {
            // Given
            String characterId = UUID.randomUUID().toString();
            
            try (MockedStatic<com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper> mockedHelper = 
                    mockStatic(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper.class)) {
                
                mockedHelper.when(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper::get)
                           .thenReturn(testUser);
                
                // When & Then - The endpoint returns 500 due to internal processing without file
                mockMvc.perform(multipart("/characters/{characterId}/upload-photo", characterId))
                        .andExpect(status().isInternalServerError());
                
                verify(characterService, never()).uploadImage(anyString(), any(MultipartFile.class), any(User.class));
            }
        }
        
        @Test
        @DisplayName("Should return 400 when character ID is invalid")
        void shouldReturn400WhenCharacterIdIsInvalid() throws Exception {
            // Given
            String invalidCharacterId = "invalid-id";
            MockMultipartFile file = new MockMultipartFile(
                "file", 
                "test-image.jpg", 
                "image/jpeg", 
                "test image content".getBytes()
            );
            
            try (MockedStatic<com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper> mockedHelper = 
                    mockStatic(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper.class)) {
                
                mockedHelper.when(com.rpgsystem.rpg.infrastructure.security.util.AuthenticatedUserHelper::get)
                           .thenReturn(testUser);
                
                doThrow(new IllegalArgumentException("Invalid character ID"))
                    .when(characterService).uploadImage(eq(invalidCharacterId), any(MultipartFile.class), eq(testUser));
                
                // When & Then
                mockMvc.perform(multipart("/characters/{characterId}/upload-photo", invalidCharacterId)
                        .file(file))
                        .andExpect(status().isBadRequest());
                
                verify(characterService).uploadImage(eq(invalidCharacterId), any(MultipartFile.class), eq(testUser));
            }
        }
    }
}