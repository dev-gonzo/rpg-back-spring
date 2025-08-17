package com.rpgsystem.rpg.application.service.character;

import com.rpgsystem.rpg.api.dto.character.CharacterAttributeModRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterAttributeRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterAttributeResponse;
import com.rpgsystem.rpg.domain.common.CharacterAccessValidator;
import com.rpgsystem.rpg.domain.entity.AttributeEntity;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.domain.repository.character.AttributeRepository;
import com.rpgsystem.rpg.domain.repository.character.CharacterRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CharacterAttributesService Tests")
class CharacterAttributesServiceTest {

    @Mock
    private AttributeRepository attributeRepository;

    @Mock
    private CharacterRepository characterRepository;

    @Mock
    private CharacterAccessValidator characterAccessValidator;

    @Mock
    private CharacterService characterService;

    @InjectMocks
    private CharacterAttributesService characterAttributesService;

    private User mockUser;
    private CharacterEntity mockCharacter;
    private AttributeEntity mockAttribute;
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

        mockCharacter = CharacterEntity.builder()
                .id(characterId)
                .name("Test Character")
                .controlUser(mockUser)
                .birthDate(java.time.LocalDate.now())
                .isKnown(true)
                .edit(true)
                .build();

        mockAttribute = AttributeEntity.builder()
                .id(UUID.randomUUID().toString())
                .character(mockCharacter)
                .con(10)
                .fr(12)
                .dex(14)
                .agi(16)
                .intel(18)
                .will(20)
                .per(22)
                .car(24)
                .conMod(0)
                .frMod(1)
                .dexMod(2)
                .agiMod(3)
                .intMod(4)
                .willMod(5)
                .perMod(6)
                .carMod(7)
                .build();
    }

    @Test
    @DisplayName("Should get character attributes successfully")
    void shouldGetCharacterAttributesSuccessfully() {
        // Given
        when(characterService.getById(characterId)).thenReturn(mockCharacter);
        when(attributeRepository.findByCharacter_Id(characterId)).thenReturn(Optional.of(mockAttribute));
        doNothing().when(characterAccessValidator).validateControlAccess(mockCharacter, mockUser);

        // When
        CharacterAttributeResponse response = characterAttributesService.getCharacterAttributes(characterId, mockUser);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getCharacterId()).isEqualTo(characterId);
        assertThat(response.getCon()).isEqualTo(10);
        assertThat(response.getFr()).isEqualTo(12);
        assertThat(response.getDex()).isEqualTo(14);
        assertThat(response.getAgi()).isEqualTo(16);
        assertThat(response.getIntel()).isEqualTo(18);
        assertThat(response.getWill()).isEqualTo(20);
        assertThat(response.getPer()).isEqualTo(22);
        assertThat(response.getCar()).isEqualTo(24);
        assertThat(response.getConMod()).isEqualTo(0);
        assertThat(response.getFrMod()).isEqualTo(1);
        assertThat(response.getDexMod()).isEqualTo(2);
        assertThat(response.getAgiMod()).isEqualTo(3);
        assertThat(response.getIntMod()).isEqualTo(4);
        assertThat(response.getWillMod()).isEqualTo(5);
        assertThat(response.getPerMod()).isEqualTo(6);
        assertThat(response.getCarMod()).isEqualTo(7);

        verify(characterService).getById(characterId);
        verify(characterAccessValidator).validateControlAccess(mockCharacter, mockUser);
        verify(attributeRepository).findByCharacter_Id(characterId);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when attributes not found")
    void shouldThrowEntityNotFoundExceptionWhenAttributesNotFound() {
        // Given
        when(characterService.getById(characterId)).thenReturn(mockCharacter);
        when(attributeRepository.findByCharacter_Id(characterId)).thenReturn(Optional.empty());
        doNothing().when(characterAccessValidator).validateControlAccess(mockCharacter, mockUser);

        // When & Then
        assertThatThrownBy(() -> characterAttributesService.getCharacterAttributes(characterId, mockUser))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(characterId);

        verify(characterService).getById(characterId);
        verify(characterAccessValidator).validateControlAccess(mockCharacter, mockUser);
        verify(attributeRepository).findByCharacter_Id(characterId);
    }

    @Test
    @DisplayName("Should save character attributes successfully when attribute exists")
    void shouldSaveCharacterAttributesSuccessfullyWhenAttributeExists() {
        // Given
        CharacterAttributeRequest request = CharacterAttributeRequest.builder()
                .con(15)
                .fr(17)
                .dex(19)
                .agi(21)
                .intel(23)
                .will(25)
                .per(27)
                .car(29)
                .conMod(2)
                .frMod(3)
                .dexMod(4)
                .agiMod(5)
                .build();

        when(characterService.getById(characterId)).thenReturn(mockCharacter);
        when(attributeRepository.findByCharacter_Id(characterId)).thenReturn(Optional.of(mockAttribute));
        when(attributeRepository.save(any(AttributeEntity.class))).thenReturn(mockAttribute);
        doNothing().when(characterAccessValidator).validateControlAccess(mockCharacter, mockUser);

        // When
        CharacterAttributeResponse response = characterAttributesService.save(request, characterId, mockUser);

        // Then
        assertThat(response).isNotNull();
        verify(characterService).getById(characterId);
        verify(characterAccessValidator).validateControlAccess(mockCharacter, mockUser);
        verify(attributeRepository).findByCharacter_Id(characterId);
        verify(attributeRepository).save(any(AttributeEntity.class));
    }

    @Test
    @DisplayName("Should save character attributes successfully when attribute does not exist")
    void shouldSaveCharacterAttributesSuccessfullyWhenAttributeDoesNotExist() {
        // Given
        CharacterAttributeRequest request = CharacterAttributeRequest.builder()
                .con(15)
                .fr(17)
                .dex(19)
                .agi(21)
                .intel(23)
                .will(25)
                .per(27)
                .car(29)
                .build();

        when(characterService.getById(characterId)).thenReturn(mockCharacter);
        when(attributeRepository.findByCharacter_Id(characterId)).thenReturn(Optional.empty());
        when(characterRepository.findById(characterId)).thenReturn(Optional.of(mockCharacter));
        when(attributeRepository.save(any(AttributeEntity.class))).thenReturn(mockAttribute);
        doNothing().when(characterAccessValidator).validateControlAccess(mockCharacter, mockUser);

        // When
        CharacterAttributeResponse response = characterAttributesService.save(request, characterId, mockUser);

        // Then
        assertThat(response).isNotNull();
        verify(characterService).getById(characterId);
        verify(characterAccessValidator).validateControlAccess(mockCharacter, mockUser);
        verify(attributeRepository).findByCharacter_Id(characterId);
        verify(characterRepository).findById(characterId);
        verify(attributeRepository).save(any(AttributeEntity.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when character not found during save")
    void shouldThrowEntityNotFoundExceptionWhenCharacterNotFoundDuringSave() {
        // Given
        CharacterAttributeRequest request = CharacterAttributeRequest.builder()
                .con(15)
                .fr(17)
                .dex(19)
                .agi(21)
                .intel(23)
                .will(25)
                .per(27)
                .car(29)
                .build();

        when(characterService.getById(characterId)).thenReturn(mockCharacter);
        when(attributeRepository.findByCharacter_Id(characterId)).thenReturn(Optional.empty());
        when(characterRepository.findById(characterId)).thenReturn(Optional.empty());
        doNothing().when(characterAccessValidator).validateControlAccess(mockCharacter, mockUser);

        // When & Then
        assertThatThrownBy(() -> characterAttributesService.save(request, characterId, mockUser))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Character not found: " + characterId);

        verify(characterService).getById(characterId);
        verify(characterAccessValidator).validateControlAccess(mockCharacter, mockUser);
        verify(attributeRepository).findByCharacter_Id(characterId);
        verify(characterRepository).findById(characterId);
        verify(attributeRepository, never()).save(any(AttributeEntity.class));
    }

    @Test
    @DisplayName("Should save character attribute mods successfully when attribute exists")
    void shouldSaveCharacterAttributeModsSuccessfullyWhenAttributeExists() {
        // Given
        CharacterAttributeModRequest request = CharacterAttributeModRequest.builder()
                .characterId(characterId)
                .conMod(5)
                .frMod(6)
                .dexMod(7)
                .agiMod(8)
                .intMod(9)
                .willMod(10)
                .perMod(11)
                .carMod(12)
                .build();

        when(characterService.getById(characterId)).thenReturn(mockCharacter);
        when(attributeRepository.findByCharacter_Id(characterId)).thenReturn(Optional.of(mockAttribute));
        when(attributeRepository.save(any(AttributeEntity.class))).thenReturn(mockAttribute);
        doNothing().when(characterAccessValidator).validateControlAccess(mockCharacter, mockUser);

        // When
        CharacterAttributeResponse response = characterAttributesService.saveMod(request, characterId, mockUser);

        // Then
        assertThat(response).isNotNull();
        verify(characterService).getById(characterId);
        verify(characterAccessValidator, times(2)).validateControlAccess(mockCharacter, mockUser);
        verify(attributeRepository).findByCharacter_Id(characterId);
        verify(attributeRepository).save(any(AttributeEntity.class));
    }

    @Test
    @DisplayName("Should save character attribute mods successfully when attribute does not exist")
    void shouldSaveCharacterAttributeModsSuccessfullyWhenAttributeDoesNotExist() {
        // Given
        CharacterAttributeModRequest request = CharacterAttributeModRequest.builder()
                .characterId(characterId)
                .conMod(5)
                .frMod(6)
                .dexMod(7)
                .agiMod(8)
                .intMod(9)
                .willMod(10)
                .perMod(11)
                .carMod(12)
                .build();

        when(characterService.getById(characterId)).thenReturn(mockCharacter);
        when(attributeRepository.findByCharacter_Id(characterId)).thenReturn(Optional.empty());
        when(characterRepository.findById(characterId)).thenReturn(Optional.of(mockCharacter));
        when(attributeRepository.save(any(AttributeEntity.class))).thenReturn(mockAttribute);
        doNothing().when(characterAccessValidator).validateControlAccess(mockCharacter, mockUser);

        // When
        CharacterAttributeResponse response = characterAttributesService.saveMod(request, characterId, mockUser);

        // Then
        assertThat(response).isNotNull();
        verify(characterService).getById(characterId);
        verify(characterAccessValidator, times(2)).validateControlAccess(mockCharacter, mockUser);
        verify(attributeRepository).findByCharacter_Id(characterId);
        verify(characterRepository).findById(characterId);
        verify(attributeRepository).save(any(AttributeEntity.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when character not found during saveMod")
    void shouldThrowEntityNotFoundExceptionWhenCharacterNotFoundDuringSaveMod() {
        // Given
        CharacterAttributeModRequest request = CharacterAttributeModRequest.builder()
                .characterId(characterId)
                .conMod(5)
                .build();

        when(characterService.getById(characterId)).thenReturn(mockCharacter);
        when(attributeRepository.findByCharacter_Id(characterId)).thenReturn(Optional.empty());
        when(characterRepository.findById(characterId)).thenReturn(Optional.empty());
        doNothing().when(characterAccessValidator).validateControlAccess(mockCharacter, mockUser);

        // When & Then
        assertThatThrownBy(() -> characterAttributesService.saveMod(request, characterId, mockUser))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Character not found: " + characterId);

        verify(characterService).getById(characterId);
        verify(characterAccessValidator).validateControlAccess(mockCharacter, mockUser);
        verify(attributeRepository).findByCharacter_Id(characterId);
        verify(characterRepository).findById(characterId);
        verify(attributeRepository, never()).save(any(AttributeEntity.class));
    }

    @Test
    @DisplayName("Should handle minimum values in attribute request")
    void shouldHandleMinimumValuesInAttributeRequest() {
        // Given
        CharacterAttributeRequest request = CharacterAttributeRequest.builder()
                .con(0)
                .fr(0)
                .dex(0)
                .agi(0)
                .intel(0)
                .will(0)
                .per(0)
                .car(0)
                .conMod(null)
                .frMod(null)
                .dexMod(null)
                .agiMod(null)
                .build();

        when(characterService.getById(characterId)).thenReturn(mockCharacter);
        when(attributeRepository.findByCharacter_Id(characterId)).thenReturn(Optional.of(mockAttribute));
        when(attributeRepository.save(any(AttributeEntity.class))).thenReturn(mockAttribute);
        doNothing().when(characterAccessValidator).validateControlAccess(mockCharacter, mockUser);

        // When
        CharacterAttributeResponse response = characterAttributesService.save(request, characterId, mockUser);

        // Then
        assertThat(response).isNotNull();
        verify(attributeRepository).save(any(AttributeEntity.class));
    }

    @Test
    @DisplayName("Should handle null values in attribute mod request")
    void shouldHandleNullValuesInAttributeModRequest() {
        // Given
        CharacterAttributeModRequest request = CharacterAttributeModRequest.builder()
                .characterId(characterId)
                .conMod(1)
                .frMod(null)
                .dexMod(2)
                .agiMod(null)
                .intMod(3)
                .willMod(null)
                .perMod(4)
                .carMod(null)
                .build();

        when(characterService.getById(characterId)).thenReturn(mockCharacter);
        when(attributeRepository.findByCharacter_Id(characterId)).thenReturn(Optional.of(mockAttribute));
        when(attributeRepository.save(any(AttributeEntity.class))).thenReturn(mockAttribute);
        doNothing().when(characterAccessValidator).validateControlAccess(mockCharacter, mockUser);

        // When
        CharacterAttributeResponse response = characterAttributesService.saveMod(request, characterId, mockUser);

        // Then
        assertThat(response).isNotNull();
        verify(attributeRepository).save(any(AttributeEntity.class));
    }
}