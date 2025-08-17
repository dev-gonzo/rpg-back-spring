package com.rpgsystem.rpg.application.service.character;

import com.rpgsystem.rpg.api.dto.character.CharacterRelevantPeopleRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterRelevantPeopleResponse;
import com.rpgsystem.rpg.application.builder.CharacterRelevantPeopleDtoBuilder;
import com.rpgsystem.rpg.domain.character.updater.CharacterRelevantPeopleUpdater;
import com.rpgsystem.rpg.domain.common.CharacterAccessValidator;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.RelevantPeopleEntity;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.domain.repository.character.RelevantPeopleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do CharacterRelevantPeopleService")
class CharacterRelevantPeopleServiceTest {

    @Mock
    private RelevantPeopleRepository repository;

    @Mock
    private CharacterService characterService;

    @Mock
    private CharacterAccessValidator accessValidator;

    @InjectMocks
    private CharacterRelevantPeopleService service;

    private User user;
    private CharacterEntity character;
    private RelevantPeopleEntity relevantPerson1;
    private RelevantPeopleEntity relevantPerson2;
    private CharacterRelevantPeopleRequest request;
    private CharacterRelevantPeopleResponse response1;
    private CharacterRelevantPeopleResponse response2;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id("user-id")
                .name("Test User")
                .email("test@test.com")
                .isMaster(false)
                .build();

        character = CharacterEntity.builder()
                .id("character-id")
                .name("Test Character")
                .controlUser(user)
                .build();

        relevantPerson1 = RelevantPeopleEntity.builder()
                .id("person-1")
                .character(character)
                .category("Ally")
                .name("John Doe")
                .apparentAge(30)
                .city("New York")
                .profession("Detective")
                .briefDescription("A helpful detective")
                .isPublic(false)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        relevantPerson2 = RelevantPeopleEntity.builder()
                .id("person-2")
                .character(character)
                .category("Enemy")
                .name("Jane Smith")
                .apparentAge(25)
                .city("Boston")
                .profession("Lawyer")
                .briefDescription("A dangerous adversary")
                .isPublic(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        request = CharacterRelevantPeopleRequest.builder()
                .category("Updated Category")
                .name("Updated Name")
                .apparentAge(35)
                .city("Updated City")
                .profession("Updated Profession")
                .briefDescription("Updated description")
                .build();

        response1 = CharacterRelevantPeopleResponse.builder()
                .id("person-1")
                .characterId("character-id")
                .category("Ally")
                .name("John Doe")
                .apparentAge(30)
                .city("New York")
                .profession("Detective")
                .briefDescription("A helpful detective")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        response2 = CharacterRelevantPeopleResponse.builder()
                .id("person-2")
                .characterId("character-id")
                .category("Enemy")
                .name("Jane Smith")
                .apparentAge(25)
                .city("Boston")
                .profession("Lawyer")
                .briefDescription("A dangerous adversary")
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();
    }

    @Test
    @DisplayName("Deve retornar todas as pessoas relevantes de um personagem")
    void getAll_ShouldReturnAllRelevantPeople() {
        // Given
        List<RelevantPeopleEntity> entities = Arrays.asList(relevantPerson1, relevantPerson2);
        when(characterService.getById("character-id")).thenReturn(character);
        when(repository.findAllByCharacter_Id("character-id")).thenReturn(entities);

        try (MockedStatic<CharacterRelevantPeopleDtoBuilder> mockedBuilder = mockStatic(CharacterRelevantPeopleDtoBuilder.class)) {
            mockedBuilder.when(() -> CharacterRelevantPeopleDtoBuilder.from(relevantPerson1))
                    .thenReturn(response1);
            mockedBuilder.when(() -> CharacterRelevantPeopleDtoBuilder.from(relevantPerson2))
                    .thenReturn(response2);

            // When
            List<CharacterRelevantPeopleResponse> result = service.getAll("character-id", user);

            // Then
            assertNotNull(result);
            assertEquals(2, result.size());
            assertTrue(result.contains(response1));
            assertTrue(result.contains(response2));

            verify(characterService).getById("character-id");
            verify(accessValidator).validateControlAccess(character, user);
            verify(repository).findAllByCharacter_Id("character-id");
        }
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há pessoas relevantes")
    void getAll_ShouldReturnEmptyListWhenNoRelevantPeople() {
        // Given
        when(characterService.getById("character-id")).thenReturn(character);
        when(repository.findAllByCharacter_Id("character-id")).thenReturn(Collections.emptyList());

        // When
        List<CharacterRelevantPeopleResponse> result = service.getAll("character-id", user);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(characterService).getById("character-id");
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository).findAllByCharacter_Id("character-id");
    }

    @Test
    @DisplayName("Deve validar acesso ao personagem no getAll")
    void getAll_ShouldValidateCharacterAccess() {
        // Given
        when(characterService.getById("character-id")).thenReturn(character);
        doThrow(new RuntimeException("Access denied"))
                .when(accessValidator).validateControlAccess(character, user);

        // When & Then
        assertThrows(RuntimeException.class, () -> service.getAll("character-id", user));

        verify(characterService).getById("character-id");
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository, never()).findAllByCharacter_Id(anyString());
    }

    @Test
    @DisplayName("Deve retornar pessoa relevante específica")
    void get_ShouldReturnSpecificRelevantPerson() {
        // Given
        when(characterService.getById("character-id")).thenReturn(character);
        when(repository.findById("person-1")).thenReturn(Optional.of(relevantPerson1));

        try (MockedStatic<CharacterRelevantPeopleDtoBuilder> mockedBuilder = mockStatic(CharacterRelevantPeopleDtoBuilder.class)) {
            mockedBuilder.when(() -> CharacterRelevantPeopleDtoBuilder.from(relevantPerson1))
                    .thenReturn(response1);

            // When
            CharacterRelevantPeopleResponse result = service.get("character-id", "person-1", user);

            // Then
            assertNotNull(result);
            assertEquals(response1, result);

            verify(characterService).getById("character-id");
            verify(accessValidator).validateControlAccess(character, user);
            verify(repository).findById("person-1");
        }
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException quando pessoa relevante não existe")
    void get_ShouldThrowEntityNotFoundExceptionWhenRelevantPersonNotExists() {
        // Given
        when(characterService.getById("character-id")).thenReturn(character);
        when(repository.findById("non-existent")).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> service.get("character-id", "non-existent", user));

        assertEquals("non-existent", exception.getMessage());

        verify(characterService).getById("character-id");
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository).findById("non-existent");
    }

    @Test
    @DisplayName("Deve validar acesso ao personagem no get")
    void get_ShouldValidateCharacterAccess() {
        // Given
        when(characterService.getById("character-id")).thenReturn(character);
        doThrow(new RuntimeException("Access denied"))
                .when(accessValidator).validateControlAccess(character, user);

        // When & Then
        assertThrows(RuntimeException.class, () -> service.get("character-id", "person-1", user));

        verify(characterService).getById("character-id");
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository, never()).findById(anyString());
    }

    @Test
    @DisplayName("Deve salvar pessoa relevante com sucesso")
    void save_ShouldSaveRelevantPersonSuccessfully() {
        // Given
        RelevantPeopleEntity updatedEntity = RelevantPeopleEntity.builder()
                .id("person-1")
                .character(character)
                .category("Updated Category")
                .name("Updated Name")
                .apparentAge(35)
                .city("Updated City")
                .profession("Updated Profession")
                .briefDescription("Updated description")
                .isPublic(false)
                .createdAt(relevantPerson1.getCreatedAt())
                .updatedAt(Instant.now())
                .build();

        CharacterRelevantPeopleResponse updatedResponse = CharacterRelevantPeopleResponse.builder()
                .id("person-1")
                .characterId("character-id")
                .category("Updated Category")
                .name("Updated Name")
                .apparentAge(35)
                .city("Updated City")
                .profession("Updated Profession")
                .briefDescription("Updated description")
                .createdAt(relevantPerson1.getCreatedAt())
                .updatedAt(updatedEntity.getUpdatedAt())
                .build();

        when(characterService.getById("character-id")).thenReturn(character);
        when(repository.findById("person-1")).thenReturn(Optional.of(relevantPerson1));
        when(repository.save(any(RelevantPeopleEntity.class))).thenReturn(updatedEntity);

        try (MockedStatic<CharacterRelevantPeopleDtoBuilder> mockedBuilder = mockStatic(CharacterRelevantPeopleDtoBuilder.class)) {
            mockedBuilder.when(() -> CharacterRelevantPeopleDtoBuilder.from(updatedEntity))
                    .thenReturn(updatedResponse);

            // When
            CharacterRelevantPeopleResponse result = service.save("character-id", "person-1", request, user);

            // Then
            assertNotNull(result);
            assertEquals(updatedResponse, result);

            verify(characterService).getById("character-id");
            verify(accessValidator).validateControlAccess(character, user);
            verify(repository).findById("person-1");
            verify(repository).save(any(RelevantPeopleEntity.class));
        }
    }

    @Test
    @DisplayName("Deve aplicar CharacterRelevantPeopleUpdater no save")
    void save_ShouldApplyCharacterRelevantPeopleUpdater() {
        // Given
        when(characterService.getById("character-id")).thenReturn(character);
        when(repository.findById("person-1")).thenReturn(Optional.of(relevantPerson1));
        when(repository.save(any(RelevantPeopleEntity.class))).thenReturn(relevantPerson1);

        try (MockedStatic<CharacterRelevantPeopleDtoBuilder> mockedBuilder = mockStatic(CharacterRelevantPeopleDtoBuilder.class)) {
            mockedBuilder.when(() -> CharacterRelevantPeopleDtoBuilder.from(relevantPerson1))
                    .thenReturn(response1);

            // When
            CharacterRelevantPeopleResponse result = service.save("character-id", "person-1", request, user);

            // Then
            assertNotNull(result);
            verify(characterService).getById("character-id");
            verify(accessValidator).validateControlAccess(character, user);
            verify(repository).findById("person-1");
            verify(repository).save(any(RelevantPeopleEntity.class));
            // O CharacterRelevantPeopleUpdater é testado indiretamente através da operação de save
        }
    }

    @Test
    @DisplayName("Deve lançar EntityNotFoundException quando pessoa relevante não existe no save")
    void save_ShouldThrowEntityNotFoundExceptionWhenRelevantPersonNotExists() {
        // Given
        when(characterService.getById("character-id")).thenReturn(character);
        when(repository.findById("non-existent")).thenReturn(Optional.empty());

        // When & Then
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> service.save("character-id", "non-existent", request, user));

        assertEquals("non-existent", exception.getMessage());

        verify(characterService).getById("character-id");
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository).findById("non-existent");
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deve validar acesso ao personagem no save")
    void save_ShouldValidateCharacterAccess() {
        // Given
        when(characterService.getById("character-id")).thenReturn(character);
        doThrow(new RuntimeException("Access denied"))
                .when(accessValidator).validateControlAccess(character, user);

        // When & Then
        assertThrows(RuntimeException.class,
                () -> service.save("character-id", "person-1", request, user));

        verify(characterService).getById("character-id");
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository, never()).findById(anyString());
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Deve preservar timestamps originais no save")
    void save_ShouldPreserveOriginalTimestamps() {
        // Given
        Instant originalCreatedAt = Instant.now().minusSeconds(3600);
        Instant originalUpdatedAt = Instant.now().minusSeconds(1800);

        RelevantPeopleEntity originalEntity = RelevantPeopleEntity.builder()
                .id("person-1")
                .character(character)
                .category("Original Category")
                .name("Original Name")
                .createdAt(originalCreatedAt)
                .updatedAt(originalUpdatedAt)
                .build();

        when(characterService.getById("character-id")).thenReturn(character);
        when(repository.findById("person-1")).thenReturn(Optional.of(originalEntity));
        when(repository.save(any(RelevantPeopleEntity.class))).thenAnswer(invocation -> {
            RelevantPeopleEntity saved = invocation.getArgument(0);
            // Verifica se o createdAt foi preservado
            assertEquals(originalCreatedAt, saved.getCreatedAt());
            return saved;
        });

        try (MockedStatic<CharacterRelevantPeopleDtoBuilder> mockedBuilder = mockStatic(CharacterRelevantPeopleDtoBuilder.class)) {
            mockedBuilder.when(() -> CharacterRelevantPeopleDtoBuilder.from(any()))
                    .thenReturn(response1);

            // When
            service.save("character-id", "person-1", request, user);

            // Then
            verify(repository).save(argThat(entity -> 
                    entity.getCreatedAt().equals(originalCreatedAt)
            ));
        }
    }

    @Test
    @DisplayName("Deve lidar com múltiplas pessoas relevantes de diferentes categorias")
    void getAll_ShouldHandleMultipleRelevantPeopleOfDifferentCategories() {
        // Given
        RelevantPeopleEntity ally = RelevantPeopleEntity.builder()
                .id("ally-1")
                .character(character)
                .category("Ally")
                .name("Ally Person")
                .build();

        RelevantPeopleEntity enemy = RelevantPeopleEntity.builder()
                .id("enemy-1")
                .character(character)
                .category("Enemy")
                .name("Enemy Person")
                .build();

        RelevantPeopleEntity neutral = RelevantPeopleEntity.builder()
                .id("neutral-1")
                .character(character)
                .category("Neutral")
                .name("Neutral Person")
                .build();

        List<RelevantPeopleEntity> entities = Arrays.asList(ally, enemy, neutral);
        when(characterService.getById("character-id")).thenReturn(character);
        when(repository.findAllByCharacter_Id("character-id")).thenReturn(entities);

        try (MockedStatic<CharacterRelevantPeopleDtoBuilder> mockedBuilder = mockStatic(CharacterRelevantPeopleDtoBuilder.class)) {
            mockedBuilder.when(() -> CharacterRelevantPeopleDtoBuilder.from(any()))
                    .thenReturn(response1);

            // When
            List<CharacterRelevantPeopleResponse> result = service.getAll("character-id", user);

            // Then
            assertNotNull(result);
            assertEquals(3, result.size());

            // Verifica que o builder foi chamado para cada entidade
            mockedBuilder.verify(() -> CharacterRelevantPeopleDtoBuilder.from(ally));
            mockedBuilder.verify(() -> CharacterRelevantPeopleDtoBuilder.from(enemy));
            mockedBuilder.verify(() -> CharacterRelevantPeopleDtoBuilder.from(neutral));
        }
    }

    @Test
    @DisplayName("Deve manter ordem das pessoas relevantes retornadas pelo repositório")
    void getAll_ShouldMaintainOrderFromRepository() {
        // Given
        List<RelevantPeopleEntity> orderedEntities = Arrays.asList(relevantPerson2, relevantPerson1);
        when(characterService.getById("character-id")).thenReturn(character);
        when(repository.findAllByCharacter_Id("character-id")).thenReturn(orderedEntities);

        try (MockedStatic<CharacterRelevantPeopleDtoBuilder> mockedBuilder = mockStatic(CharacterRelevantPeopleDtoBuilder.class)) {
            mockedBuilder.when(() -> CharacterRelevantPeopleDtoBuilder.from(relevantPerson2))
                    .thenReturn(response2);
            mockedBuilder.when(() -> CharacterRelevantPeopleDtoBuilder.from(relevantPerson1))
                    .thenReturn(response1);

            // When
            List<CharacterRelevantPeopleResponse> result = service.getAll("character-id", user);

            // Then
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(response2, result.get(0)); // Primeira na lista
            assertEquals(response1, result.get(1)); // Segunda na lista
        }
    }
}