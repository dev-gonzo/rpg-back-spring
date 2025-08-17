package com.rpgsystem.rpg.application.service.character;

import com.rpgsystem.rpg.api.dto.character.CharacterImprovementRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterImprovementResponse;
import com.rpgsystem.rpg.application.builder.CharacterImprovementDtoBuilder;
import com.rpgsystem.rpg.domain.character.updater.CharacterImprovementUpdater;
import com.rpgsystem.rpg.domain.common.CharacterAccessValidator;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.ImprovementEntity;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.domain.repository.character.ImprovementRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CharacterImprovementServiceTest {

    @Mock
    private ImprovementRepository improvementRepository;

    @Mock
    private CharacterAccessValidator characterAccessValidator;

    @Mock
    private CharacterService characterService;

    @InjectMocks
    private CharacterImprovementService service;

    private User user;
    private CharacterEntity character;
    private ImprovementEntity improvement;
    private CharacterImprovementRequest request;
    private CharacterImprovementResponse response;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id("user-id")
                .name("Test User")
                .email("testuser@example.com")
                .password("password123")
                .isMaster(false)
                .build();

        character = new CharacterEntity();
        character.setId("character-id");
        character.setName("Test Character");

        improvement = new ImprovementEntity();
        improvement.setId("improvement-id");
        improvement.setCharacter(character);
        improvement.setName("Test Improvement");
        improvement.setCost(10);
        improvement.setKitValue(5);
        improvement.setBookPage("100");
        improvement.setCreatedAt(Instant.now());
        improvement.setUpdatedAt(Instant.now());

        request = CharacterImprovementRequest.builder()
                .name("Updated Improvement")
                .cost(15)
                .kitValue(8)
                .build();

        response = CharacterImprovementResponse.builder()
                .characterId(character.getId())
                .name(improvement.getName())
                .cost(improvement.getCost())
                .kitCost(improvement.getKitValue())
                .createdAt(improvement.getCreatedAt())
                .updatedAt(improvement.getUpdatedAt())
                .build();
    }

    @Test
    void getImprovement_ShouldReturnImprovement_WhenUserHasAccessAndImprovementExists() {
        // Given
        String characterId = "character-id";
        String improvementId = "improvement-id";
        
        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(characterAccessValidator).validateControlAccess(character, user);
        when(improvementRepository.findById(improvementId)).thenReturn(Optional.of(improvement));

        try (MockedStatic<CharacterImprovementDtoBuilder> builderMock = mockStatic(CharacterImprovementDtoBuilder.class)) {
            builderMock.when(() -> CharacterImprovementDtoBuilder.from(improvement)).thenReturn(response);

            // When
            CharacterImprovementResponse result = service.getImprovement(improvementId, characterId, user);

            // Then
            assertNotNull(result);
            assertEquals(response, result);
            verify(characterService).getById(characterId);
            verify(characterAccessValidator).validateControlAccess(character, user);
            verify(improvementRepository).findById(improvementId);
        }
    }

    @Test
    void getImprovement_ShouldThrowEntityNotFoundException_WhenImprovementNotFound() {
        // Given
        String characterId = "character-id";
        String improvementId = "non-existent-id";
        
        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(characterAccessValidator).validateControlAccess(character, user);
        when(improvementRepository.findById(improvementId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            service.getImprovement(improvementId, characterId, user);
        });
        
        verify(characterService).getById(characterId);
        verify(characterAccessValidator).validateControlAccess(character, user);
        verify(improvementRepository).findById(improvementId);
    }

    @Test
    void getImprovements_ShouldReturnAllImprovements_WhenUserHasAccess() {
        // Given
        String characterId = "character-id";
        List<ImprovementEntity> improvements = Arrays.asList(improvement);
        
        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(characterAccessValidator).validateControlAccess(character, user);
        when(improvementRepository.findAllByCharacter_Id(characterId)).thenReturn(improvements);

        try (MockedStatic<CharacterImprovementDtoBuilder> builderMock = mockStatic(CharacterImprovementDtoBuilder.class)) {
            builderMock.when(() -> CharacterImprovementDtoBuilder.from(improvement)).thenReturn(response);

            // When
            List<CharacterImprovementResponse> result = service.getImprovements(characterId, user);

            // Then
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(response, result.get(0));
            verify(characterService).getById(characterId);
            verify(characterAccessValidator).validateControlAccess(character, user);
            verify(improvementRepository).findAllByCharacter_Id(characterId);
        }
    }

    @Test
    void getImprovements_ShouldReturnEmptyList_WhenNoImprovementsFound() {
        // Given
        String characterId = "character-id";
        
        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(characterAccessValidator).validateControlAccess(character, user);
        when(improvementRepository.findAllByCharacter_Id(characterId)).thenReturn(Arrays.asList());

        // When
        List<CharacterImprovementResponse> result = service.getImprovements(characterId, user);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(characterService).getById(characterId);
        verify(characterAccessValidator).validateControlAccess(character, user);
        verify(improvementRepository).findAllByCharacter_Id(characterId);
    }

    @Test
    void save_ShouldUpdateAndReturnImprovement_WhenValidRequest() {
        // Given
        String characterId = "character-id";
        String improvementId = "improvement-id";
        ImprovementEntity updatedImprovement = new ImprovementEntity();
        updatedImprovement.setId(improvementId);
        updatedImprovement.setCharacter(character);
        updatedImprovement.setName(request.getName());
        updatedImprovement.setCost(request.getCost());
        updatedImprovement.setKitValue(request.getKitValue());
        updatedImprovement.setCreatedAt(improvement.getCreatedAt());
        updatedImprovement.setUpdatedAt(Instant.now());
        
        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(characterAccessValidator).validateControlAccess(character, user);
        when(improvementRepository.findById(improvementId)).thenReturn(Optional.of(improvement));
        when(improvementRepository.save(any(ImprovementEntity.class))).thenReturn(updatedImprovement);

        CharacterImprovementResponse updatedResponse = CharacterImprovementResponse.builder()
                .characterId(character.getId())
                .name(updatedImprovement.getName())
                .cost(updatedImprovement.getCost())
                .kitCost(updatedImprovement.getKitValue())
                .createdAt(updatedImprovement.getCreatedAt())
                .updatedAt(updatedImprovement.getUpdatedAt())
                .build();

        try (MockedStatic<CharacterImprovementDtoBuilder> builderMock = mockStatic(CharacterImprovementDtoBuilder.class)) {
            builderMock.when(() -> CharacterImprovementDtoBuilder.from(updatedImprovement)).thenReturn(updatedResponse);

            // When
            CharacterImprovementResponse result = service.save(request, improvementId, characterId, user);

            // Then
            assertNotNull(result);
            assertEquals(updatedResponse, result);
            verify(characterService).getById(characterId);
            verify(characterAccessValidator).validateControlAccess(character, user);
            verify(improvementRepository).findById(improvementId);
            verify(improvementRepository).save(any(ImprovementEntity.class));
        }
    }

    @Test
    void save_ShouldThrowEntityNotFoundException_WhenImprovementNotFound() {
        // Given
        String characterId = "character-id";
        String improvementId = "non-existent-id";
        
        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(characterAccessValidator).validateControlAccess(character, user);
        when(improvementRepository.findById(improvementId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            service.save(request, improvementId, characterId, user);
        });
        
        verify(characterService).getById(characterId);
        verify(characterAccessValidator).validateControlAccess(character, user);
        verify(improvementRepository).findById(improvementId);
        verify(improvementRepository, never()).save(any(ImprovementEntity.class));
    }

    // Métodos privados getById e getCharacterImprovements são testados indiretamente através dos métodos públicos

    @Test
    void save_ShouldApplyUpdaterCorrectly_WhenValidRequest() {
        // Given
        String characterId = "character-id";
        String improvementId = "improvement-id";
        
        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(characterAccessValidator).validateControlAccess(character, user);
        when(improvementRepository.findById(improvementId)).thenReturn(Optional.of(improvement));
        when(improvementRepository.save(any(ImprovementEntity.class))).thenReturn(improvement);

        try (MockedStatic<CharacterImprovementDtoBuilder> builderMock = mockStatic(CharacterImprovementDtoBuilder.class)) {
            builderMock.when(() -> CharacterImprovementDtoBuilder.from(improvement)).thenReturn(response);

            // When
            service.save(request, improvementId, characterId, user);

            // Then
            // Verify that the updater was applied by checking the improvement entity was saved
            verify(improvementRepository).save(improvement);
            // The CharacterImprovementUpdater should have been instantiated and applied
            // This is verified indirectly through the save operation
        }
    }

    @Test
    void getImprovements_ShouldHandleMultipleImprovements_WhenCharacterHasMultipleImprovements() {
        // Given
        String characterId = "character-id";
        ImprovementEntity improvement2 = new ImprovementEntity();
        improvement2.setId("improvement-2");
        improvement2.setCharacter(character);
        improvement2.setName("Second Improvement");
        improvement2.setCost(20);
        improvement2.setKitValue(10);
        
        List<ImprovementEntity> improvements = Arrays.asList(improvement, improvement2);
        
        CharacterImprovementResponse response2 = CharacterImprovementResponse.builder()
                .characterId(character.getId())
                .name(improvement2.getName())
                .cost(improvement2.getCost())
                .kitCost(improvement2.getKitValue())
                .build();
        
        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(characterAccessValidator).validateControlAccess(character, user);
        when(improvementRepository.findAllByCharacter_Id(characterId)).thenReturn(improvements);

        try (MockedStatic<CharacterImprovementDtoBuilder> builderMock = mockStatic(CharacterImprovementDtoBuilder.class)) {
            builderMock.when(() -> CharacterImprovementDtoBuilder.from(improvement)).thenReturn(response);
            builderMock.when(() -> CharacterImprovementDtoBuilder.from(improvement2)).thenReturn(response2);

            // When
            List<CharacterImprovementResponse> result = service.getImprovements(characterId, user);

            // Then
            assertNotNull(result);
            assertEquals(2, result.size());
            assertTrue(result.contains(response));
            assertTrue(result.contains(response2));
            verify(characterService).getById(characterId);
            verify(characterAccessValidator).validateControlAccess(character, user);
            verify(improvementRepository).findAllByCharacter_Id(characterId);
        }
    }

    // Método privado getById é testado indiretamente através dos métodos públicos
}