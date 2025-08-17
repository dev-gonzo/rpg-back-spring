package com.rpgsystem.rpg.application.service.character;

import com.rpgsystem.rpg.api.dto.character.CharacterCurrentPointsRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterPointsRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterPointsResponse;
import com.rpgsystem.rpg.domain.character.CharacterPoints;
import com.rpgsystem.rpg.domain.common.CharacterAccessValidator;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.domain.exception.UnauthorizedActionException;
import com.rpgsystem.rpg.domain.repository.character.CharacterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CharacterPointService Tests")
class CharacterPointServiceTest {

    @Mock
    private CharacterRepository repository;

    @Mock
    private CharacterAccessValidator characterAccessValidator;

    @Mock
    private CharacterService characterService;

    @InjectMocks
    private CharacterPointService characterPointService;

    private User mockUser;
    private User mockMaster;
    private CharacterEntity mockCharacter;
    private String characterId;

    @BeforeEach
    void setUp() {
        characterId = UUID.randomUUID().toString();
        
        mockUser = User.builder()
                .id(UUID.randomUUID().toString())
                .name("Test User")
                .email("testuser@example.com")
                .password("password")
                .isMaster(false)
                .build();

        mockMaster = User.builder()
                .id(UUID.randomUUID().toString())
                .name("Master User")
                .email("master@example.com")
                .password("password")
                .isMaster(true)
                .build();

        mockCharacter = CharacterEntity.builder()
                .id(characterId)
                .name("Test Character")
                .controlUser(mockUser)
                .birthDate(LocalDate.now())
                .isKnown(true)
                .edit(true)
                .hitPoints(100)
                .currentHitPoints(80)
                .initiative(15)
                .currentInitiative(12)
                .heroPoints(5)
                .currentHeroPoints(3)
                .magicPoints(20)
                .currentMagicPoints(15)
                .faithPoints(10)
                .currentFaithPoints(8)
                .protectionIndex(5)
                .currentProtectionIndex(4)
                .build();
    }

    @Test
    @DisplayName("Should get character points info successfully")
    void shouldGetCharacterPointsInfoSuccessfully() {
        // Given
        when(characterService.getById(characterId)).thenReturn(mockCharacter);

        // When
        CharacterPointsResponse response = characterPointService.getInfo(characterId);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getCharacterId()).isEqualTo(characterId);
        assertThat(response.getHitPoints()).isEqualTo(100);
        assertThat(response.getCurrentHitPoints()).isEqualTo(80);
        assertThat(response.getInitiative()).isEqualTo(15);
        assertThat(response.getCurrentInitiative()).isEqualTo(12);
        assertThat(response.getHeroPoints()).isEqualTo(5);
        assertThat(response.getCurrentHeroPoints()).isEqualTo(3);
        assertThat(response.getMagicPoints()).isEqualTo(20);
        assertThat(response.getCurrentMagicPoints()).isEqualTo(15);
        assertThat(response.getFaithPoints()).isEqualTo(10);
        assertThat(response.getCurrentFaithPoints()).isEqualTo(8);
        assertThat(response.getProtectionIndex()).isEqualTo(5);
        assertThat(response.getCurrentProtectionIndex()).isEqualTo(4);

        verify(characterService).getById(characterId);
    }

    @Test
    @DisplayName("Should create points DTO from CharacterPointsRequest")
    void shouldCreatePointsDtoFromCharacterPointsRequest() {
        // Given
        CharacterPointsRequest request = CharacterPointsRequest.builder()
                .hitPoints(120)
                .initiative(18)
                .heroPoints(7)
                .magicPoints(25)
                .faithPoints(12)
                .protectionIndex(6)
                .build();

        // When
        CharacterPoints result = characterPointService.createPointsDto(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getHitPoints().getValue()).isEqualTo(120);
        assertThat(result.getInitiative().getValue()).isEqualTo(18);
        assertThat(result.getHeroPoints().getValue()).isEqualTo(7);
        assertThat(result.getMagicPoints().getValue()).isEqualTo(25);
        assertThat(result.getFaithPoints().getValue()).isEqualTo(12);
        assertThat(result.getProtectionIndex().getValue()).isEqualTo(6);
        assertThat(result.getCurrentHitPoints()).isNull();
        assertThat(result.getCurrentInitiative()).isNull();
    }

    @Test
    @DisplayName("Should create points DTO from CharacterCurrentPointsRequest")
    void shouldCreatePointsDtoFromCharacterCurrentPointsRequest() {
        // Given
        CharacterCurrentPointsRequest request = CharacterCurrentPointsRequest.builder()
                .currentHitPoints(90)
                .currentInitiative(14)
                .currentHeroPoints(4)
                .currentMagicPoints(18)
                .currentFaithPoints(9)
                .currentProtectionIndex(5)
                .build();

        // When
        CharacterPoints result = characterPointService.createPointsDto(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCurrentHitPoints().getValue()).isEqualTo(90);
        assertThat(result.getCurrentInitiative().getValue()).isEqualTo(14);
        assertThat(result.getCurrentHeroPoints().getValue()).isEqualTo(4);
        assertThat(result.getCurrentMagicPoints().getValue()).isEqualTo(18);
        assertThat(result.getCurrentFaithPoints().getValue()).isEqualTo(9);
        assertThat(result.getCurrentProtectionIndex().getValue()).isEqualTo(5);
        assertThat(result.getHitPoints()).isNull();
        assertThat(result.getInitiative()).isNull();
    }

    @Test
    @DisplayName("Should save base points successfully")
    void shouldSaveBasePointsSuccessfully() {
        // Given
        CharacterPointsRequest request = CharacterPointsRequest.builder()
                .hitPoints(120)
                .initiative(18)
                .heroPoints(7)
                .magicPoints(25)
                .faithPoints(12)
                .protectionIndex(6)
                .build();

        when(characterService.getById(characterId)).thenReturn(mockCharacter);
        when(repository.save(any(CharacterEntity.class))).thenReturn(mockCharacter);
        doNothing().when(characterAccessValidator).validateControlAccess(mockCharacter, mockUser);

        // When
        CharacterPointsResponse response = characterPointService.save(request, characterId, mockUser);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getCharacterId()).isEqualTo(characterId);

        verify(characterService).getById(characterId);
        verify(characterAccessValidator).validateControlAccess(mockCharacter, mockUser);
        verify(repository).save(any(CharacterEntity.class));
    }

    @Test
    @DisplayName("Should save current points successfully")
    void shouldSaveCurrentPointsSuccessfully() {
        // Given
        CharacterCurrentPointsRequest request = CharacterCurrentPointsRequest.builder()
                .currentHitPoints(90)
                .currentInitiative(14)
                .currentHeroPoints(4)
                .currentMagicPoints(18)
                .currentFaithPoints(9)
                .currentProtectionIndex(5)
                .build();

        when(characterService.getById(characterId)).thenReturn(mockCharacter);
        when(repository.save(any(CharacterEntity.class))).thenReturn(mockCharacter);
        doNothing().when(characterAccessValidator).validateControlAccess(mockCharacter, mockUser);

        // When
        CharacterPointsResponse response = characterPointService.saveCurrent(request, characterId, mockUser);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getCharacterId()).isEqualTo(characterId);

        verify(characterService).getById(characterId);
        verify(characterAccessValidator).validateControlAccess(mockCharacter, mockUser);
        verify(repository).save(any(CharacterEntity.class));
    }

    @Test
    @DisplayName("Should save current adjust points successfully for character owner")
    void shouldSaveCurrentAdjustPointsSuccessfullyForCharacterOwner() {
        // Given
        CharacterCurrentPointsRequest request = CharacterCurrentPointsRequest.builder()
                .currentHitPoints(85)
                .currentInitiative(13)
                .currentHeroPoints(2)
                .currentMagicPoints(16)
                .currentFaithPoints(7)
                .currentProtectionIndex(3)
                .build();

        when(characterService.getById(characterId)).thenReturn(mockCharacter);
        when(repository.save(any(CharacterEntity.class))).thenReturn(mockCharacter);

        // When
        CharacterPointsResponse response = characterPointService.saveCurrentAdjust(request, characterId, mockUser);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getCharacterId()).isEqualTo(characterId);

        verify(characterService).getById(characterId);
        verify(repository).save(any(CharacterEntity.class));
    }

    @Test
    @DisplayName("Should save current adjust points successfully for master")
    void shouldSaveCurrentAdjustPointsSuccessfullyForMaster() {
        // Given
        CharacterCurrentPointsRequest request = CharacterCurrentPointsRequest.builder()
                .currentHitPoints(85)
                .currentInitiative(13)
                .currentHeroPoints(2)
                .currentMagicPoints(16)
                .currentFaithPoints(7)
                .currentProtectionIndex(3)
                .build();

        when(characterService.getById(characterId)).thenReturn(mockCharacter);
        when(repository.save(any(CharacterEntity.class))).thenReturn(mockCharacter);

        // When
        CharacterPointsResponse response = characterPointService.saveCurrentAdjust(request, characterId, mockMaster);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getCharacterId()).isEqualTo(characterId);

        verify(characterService).getById(characterId);
        verify(repository).save(any(CharacterEntity.class));
    }

    @Test
    @DisplayName("Should throw UnauthorizedActionException when user is not owner or master")
    void shouldThrowUnauthorizedActionExceptionWhenUserIsNotOwnerOrMaster() {
        // Given
        User unauthorizedUser = User.builder()
                .id(UUID.randomUUID().toString())
                .name("Unauthorized User")
                .email("unauthorized@example.com")
                .password("password")
                .isMaster(false)
                .build();

        CharacterCurrentPointsRequest request = CharacterCurrentPointsRequest.builder()
                .currentHitPoints(85)
                .build();

        when(characterService.getById(characterId)).thenReturn(mockCharacter);

        // When & Then
        assertThatThrownBy(() -> characterPointService.saveCurrentAdjust(request, characterId, unauthorizedUser))
                .isInstanceOf(UnauthorizedActionException.class)
                .hasMessage("Action not performed, user without permission");

        verify(characterService).getById(characterId);
        verify(repository, never()).save(any(CharacterEntity.class));
    }

    @Test
    @DisplayName("Should handle zero values in points")
    void shouldHandleZeroValuesInPoints() {
        // Given
        CharacterPointsRequest request = CharacterPointsRequest.builder()
                .hitPoints(0)
                .initiative(0)
                .heroPoints(0)
                .magicPoints(0)
                .faithPoints(0)
                .protectionIndex(0)
                .build();

        when(characterService.getById(characterId)).thenReturn(mockCharacter);
        when(repository.save(any(CharacterEntity.class))).thenReturn(mockCharacter);
        doNothing().when(characterAccessValidator).validateControlAccess(mockCharacter, mockUser);

        // When
        CharacterPointsResponse response = characterPointService.save(request, characterId, mockUser);

        // Then
        assertThat(response).isNotNull();
        verify(repository).save(any(CharacterEntity.class));
    }

    @Test
    @DisplayName("Should handle maximum values in points")
    void shouldHandleMaximumValuesInPoints() {
        // Given
        CharacterCurrentPointsRequest request = CharacterCurrentPointsRequest.builder()
                .currentHitPoints(999)
                .currentInitiative(999)
                .currentHeroPoints(999)
                .currentMagicPoints(999)
                .currentFaithPoints(999)
                .currentProtectionIndex(999)
                .build();

        when(characterService.getById(characterId)).thenReturn(mockCharacter);
        when(repository.save(any(CharacterEntity.class))).thenReturn(mockCharacter);
        doNothing().when(characterAccessValidator).validateControlAccess(mockCharacter, mockUser);

        // When
        CharacterPointsResponse response = characterPointService.saveCurrent(request, characterId, mockUser);

        // Then
        assertThat(response).isNotNull();
        verify(repository).save(any(CharacterEntity.class));
    }

    @Test
    @DisplayName("Should validate character access for save operations")
    void shouldValidateCharacterAccessForSaveOperations() {
        // Given
        CharacterPointsRequest baseRequest = CharacterPointsRequest.builder()
                .hitPoints(100)
                .initiative(15)
                .heroPoints(5)
                .magicPoints(20)
                .faithPoints(10)
                .protectionIndex(5)
                .build();

        CharacterCurrentPointsRequest currentRequest = CharacterCurrentPointsRequest.builder()
                .currentHitPoints(80)
                .currentInitiative(12)
                .currentHeroPoints(3)
                .currentMagicPoints(15)
                .currentFaithPoints(8)
                .currentProtectionIndex(4)
                .build();

        when(characterService.getById(characterId)).thenReturn(mockCharacter);
        when(repository.save(any(CharacterEntity.class))).thenReturn(mockCharacter);
        doNothing().when(characterAccessValidator).validateControlAccess(mockCharacter, mockUser);

        // When
        characterPointService.save(baseRequest, characterId, mockUser);
        characterPointService.saveCurrent(currentRequest, characterId, mockUser);

        // Then
        verify(characterAccessValidator, times(2)).validateControlAccess(mockCharacter, mockUser);
        verify(characterService, times(2)).getById(characterId);
        verify(repository, times(2)).save(any(CharacterEntity.class));
    }
}