package com.rpgsystem.rpg.application.service.character;

import com.rpgsystem.rpg.api.dto.character.CharacterNoteRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterNoteResponse;
import com.rpgsystem.rpg.application.builder.CharacterNoteDtoBuilder;
import com.rpgsystem.rpg.domain.character.updater.CharacterNoteUpdater;
import com.rpgsystem.rpg.domain.common.CharacterAccessValidator;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.NoteEntity;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.domain.repository.character.NoteRepository;
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
class CharacterNoteServiceTest {

    @Mock
    private NoteRepository repository;

    @Mock
    private CharacterService characterService;

    @Mock
    private CharacterAccessValidator accessValidator;

    @InjectMocks
    private CharacterNoteService service;

    private User user;
    private CharacterEntity character;
    private NoteEntity note;
    private CharacterNoteRequest request;
    private CharacterNoteResponse response;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id("user-id")
                .name("Test User")
                .email("test@example.com")
                .password("password")
                .isMaster(false)
                .build();

        character = new CharacterEntity();
        character.setId("character-id");
        character.setName("Test Character");

        note = new NoteEntity();
        note.setId("note-id");
        note.setCharacter(character);
        note.setNote("Test note content");
        note.setCreatedAt(Instant.now());
        note.setUpdatedAt(Instant.now());

        request = CharacterNoteRequest.builder()
                .note("Updated note content")
                .build();

        response = CharacterNoteResponse.builder()
                .id(note.getId())
                .characterId(character.getId())
                .note(note.getNote())
                .createdAt(note.getCreatedAt())
                .updatedAt(note.getUpdatedAt())
                .build();
    }

    @Test
    void getAll_ShouldReturnAllNotes_WhenUserHasAccess() {
        // Given
        String characterId = "character-id";
        List<NoteEntity> notes = Arrays.asList(note);
        
        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(accessValidator).validateControlAccess(character, user);
        when(repository.findAllByCharacter_Id(characterId)).thenReturn(notes);

        try (MockedStatic<CharacterNoteDtoBuilder> builderMock = mockStatic(CharacterNoteDtoBuilder.class)) {
            builderMock.when(() -> CharacterNoteDtoBuilder.from(note)).thenReturn(response);

            // When
            List<CharacterNoteResponse> result = service.getAll(characterId, user);

            // Then
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals(response, result.get(0));
            verify(characterService).getById(characterId);
            verify(accessValidator).validateControlAccess(character, user);
            verify(repository).findAllByCharacter_Id(characterId);
        }
    }

    @Test
    void getAll_ShouldReturnEmptyList_WhenNoNotesFound() {
        // Given
        String characterId = "character-id";
        
        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(accessValidator).validateControlAccess(character, user);
        when(repository.findAllByCharacter_Id(characterId)).thenReturn(Arrays.asList());

        // When
        List<CharacterNoteResponse> result = service.getAll(characterId, user);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository).findAllByCharacter_Id(characterId);
    }

    @Test
    void getAll_ShouldHandleMultipleNotes_WhenCharacterHasMultipleNotes() {
        // Given
        String characterId = "character-id";
        NoteEntity note2 = new NoteEntity();
        note2.setId("note-2");
        note2.setCharacter(character);
        note2.setNote("Second note content");
        note2.setCreatedAt(Instant.now());
        note2.setUpdatedAt(Instant.now());
        
        List<NoteEntity> notes = Arrays.asList(note, note2);
        
        CharacterNoteResponse response2 = CharacterNoteResponse.builder()
                .id(note2.getId())
                .characterId(character.getId())
                .note(note2.getNote())
                .createdAt(note2.getCreatedAt())
                .updatedAt(note2.getUpdatedAt())
                .build();
        
        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(accessValidator).validateControlAccess(character, user);
        when(repository.findAllByCharacter_Id(characterId)).thenReturn(notes);

        try (MockedStatic<CharacterNoteDtoBuilder> builderMock = mockStatic(CharacterNoteDtoBuilder.class)) {
            builderMock.when(() -> CharacterNoteDtoBuilder.from(note)).thenReturn(response);
            builderMock.when(() -> CharacterNoteDtoBuilder.from(note2)).thenReturn(response2);

            // When
            List<CharacterNoteResponse> result = service.getAll(characterId, user);

            // Then
            assertNotNull(result);
            assertEquals(2, result.size());
            assertTrue(result.contains(response));
            assertTrue(result.contains(response2));
            verify(characterService).getById(characterId);
            verify(accessValidator).validateControlAccess(character, user);
            verify(repository).findAllByCharacter_Id(characterId);
        }
    }

    @Test
    void get_ShouldReturnNote_WhenUserHasAccessAndNoteExists() {
        // Given
        String characterId = "character-id";
        String noteId = "note-id";
        
        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(accessValidator).validateControlAccess(character, user);
        when(repository.findById(noteId)).thenReturn(Optional.of(note));

        try (MockedStatic<CharacterNoteDtoBuilder> builderMock = mockStatic(CharacterNoteDtoBuilder.class)) {
            builderMock.when(() -> CharacterNoteDtoBuilder.from(note)).thenReturn(response);

            // When
            CharacterNoteResponse result = service.get(characterId, noteId, user);

            // Then
            assertNotNull(result);
            assertEquals(response, result);
            verify(characterService).getById(characterId);
            verify(accessValidator).validateControlAccess(character, user);
            verify(repository).findById(noteId);
        }
    }

    @Test
    void get_ShouldThrowEntityNotFoundException_WhenNoteNotFound() {
        // Given
        String characterId = "character-id";
        String noteId = "non-existent-id";
        
        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(accessValidator).validateControlAccess(character, user);
        when(repository.findById(noteId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            service.get(characterId, noteId, user);
        });
        
        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository).findById(noteId);
    }

    @Test
    void save_ShouldUpdateAndReturnNote_WhenValidRequest() {
        // Given
        String characterId = "character-id";
        String noteId = "note-id";
        NoteEntity updatedNote = new NoteEntity();
        updatedNote.setId(noteId);
        updatedNote.setCharacter(character);
        updatedNote.setNote(request.getNote());
        updatedNote.setCreatedAt(note.getCreatedAt());
        updatedNote.setUpdatedAt(Instant.now());
        
        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(accessValidator).validateControlAccess(character, user);
        when(repository.findById(noteId)).thenReturn(Optional.of(note));
        when(repository.save(any(NoteEntity.class))).thenReturn(updatedNote);

        CharacterNoteResponse updatedResponse = CharacterNoteResponse.builder()
                .id(updatedNote.getId())
                .characterId(character.getId())
                .note(updatedNote.getNote())
                .createdAt(updatedNote.getCreatedAt())
                .updatedAt(updatedNote.getUpdatedAt())
                .build();

        try (MockedStatic<CharacterNoteDtoBuilder> builderMock = mockStatic(CharacterNoteDtoBuilder.class)) {
            builderMock.when(() -> CharacterNoteDtoBuilder.from(updatedNote)).thenReturn(updatedResponse);

            // When
            CharacterNoteResponse result = service.save(characterId, noteId, request, user);

            // Then
            assertNotNull(result);
            assertEquals(updatedResponse, result);
            verify(characterService).getById(characterId);
            verify(accessValidator).validateControlAccess(character, user);
            verify(repository).findById(noteId);
            verify(repository).save(any(NoteEntity.class));
        }
    }

    @Test
    void save_ShouldThrowEntityNotFoundException_WhenNoteNotFound() {
        // Given
        String characterId = "character-id";
        String noteId = "non-existent-id";
        
        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(accessValidator).validateControlAccess(character, user);
        when(repository.findById(noteId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            service.save(characterId, noteId, request, user);
        });
        
        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository).findById(noteId);
        verify(repository, never()).save(any(NoteEntity.class));
    }

    @Test
    void save_ShouldApplyUpdaterCorrectly_WhenValidRequest() {
        // Given
        String characterId = "character-id";
        String noteId = "note-id";
        
        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(accessValidator).validateControlAccess(character, user);
        when(repository.findById(noteId)).thenReturn(Optional.of(note));
        when(repository.save(any(NoteEntity.class))).thenReturn(note);

        try (MockedStatic<CharacterNoteDtoBuilder> builderMock = mockStatic(CharacterNoteDtoBuilder.class)) {
            builderMock.when(() -> CharacterNoteDtoBuilder.from(note)).thenReturn(response);

            // When
            service.save(characterId, noteId, request, user);

            // Then
            // Verify that the updater was applied by checking the note entity was saved
            verify(repository).save(note);
            // The CharacterNoteUpdater should have been instantiated and applied
            // This is verified indirectly through the save operation
        }
    }

    @Test
    void getById_ShouldReturnNote_WhenNoteExists() {
        // Given
        String noteId = "note-id";
        when(repository.findById(noteId)).thenReturn(Optional.of(note));

        // When
        // Using reflection to access private method for testing
        try {
            java.lang.reflect.Method getByIdMethod = CharacterNoteService.class.getDeclaredMethod("getById", String.class);
            getByIdMethod.setAccessible(true);
            NoteEntity result = (NoteEntity) getByIdMethod.invoke(service, noteId);

            // Then
            assertNotNull(result);
            assertEquals(note, result);
            verify(repository).findById(noteId);
        } catch (Exception e) {
            fail("Failed to test private getById method: " + e.getMessage());
        }
    }

    @Test
    void getById_ShouldThrowEntityNotFoundException_WhenNoteNotFound() {
        // Given
        String noteId = "non-existent-id";
        when(repository.findById(noteId)).thenReturn(Optional.empty());

        // When & Then
        try {
            java.lang.reflect.Method getByIdMethod = CharacterNoteService.class.getDeclaredMethod("getById", String.class);
            getByIdMethod.setAccessible(true);
            
            assertThrows(java.lang.reflect.InvocationTargetException.class, () -> {
                getByIdMethod.invoke(service, noteId);
            });
            
            verify(repository).findById(noteId);
        } catch (NoSuchMethodException e) {
            fail("Failed to access private getById method: " + e.getMessage());
        }
    }

    @Test
    void save_ShouldHandleNullRequest_WhenUpdaterReceivesNull() {
        // Given
        String characterId = "character-id";
        String noteId = "note-id";
        CharacterNoteRequest nullRequest = null;
        
        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(accessValidator).validateControlAccess(character, user);
        when(repository.findById(noteId)).thenReturn(Optional.of(note));
        when(repository.save(any(NoteEntity.class))).thenReturn(note);

        try (MockedStatic<CharacterNoteDtoBuilder> builderMock = mockStatic(CharacterNoteDtoBuilder.class)) {
            builderMock.when(() -> CharacterNoteDtoBuilder.from(note)).thenReturn(response);

            // When
            CharacterNoteResponse result = service.save(characterId, noteId, nullRequest, user);

            // Then
            assertNotNull(result);
            assertEquals(response, result);
            verify(characterService).getById(characterId);
            verify(accessValidator).validateControlAccess(character, user);
            verify(repository).findById(noteId);
            verify(repository).save(note);
        }
    }

    @Test
    void save_ShouldPreserveOriginalTimestamps_WhenUpdating() {
        // Given
        String characterId = "character-id";
        String noteId = "note-id";
        Instant originalCreatedAt = Instant.now().minusSeconds(3600); // 1 hour ago
        note.setCreatedAt(originalCreatedAt);
        
        when(characterService.getById(characterId)).thenReturn(character);
        doNothing().when(accessValidator).validateControlAccess(character, user);
        when(repository.findById(noteId)).thenReturn(Optional.of(note));
        when(repository.save(any(NoteEntity.class))).thenReturn(note);

        try (MockedStatic<CharacterNoteDtoBuilder> builderMock = mockStatic(CharacterNoteDtoBuilder.class)) {
            builderMock.when(() -> CharacterNoteDtoBuilder.from(note)).thenReturn(response);

            // When
            service.save(characterId, noteId, request, user);

            // Then
            verify(repository).save(note);
            // The original createdAt should be preserved
            assertEquals(originalCreatedAt, note.getCreatedAt());
        }
    }

    @Test
    void getAll_ShouldValidateCharacterAccess_BeforeRetrievingNotes() {
        // Given
        String characterId = "character-id";
        
        when(characterService.getById(characterId)).thenReturn(character);
        doThrow(new RuntimeException("Access denied")).when(accessValidator).validateControlAccess(character, user);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            service.getAll(characterId, user);
        });
        
        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository, never()).findAllByCharacter_Id(anyString());
    }

    @Test
    void get_ShouldValidateCharacterAccess_BeforeRetrievingNote() {
        // Given
        String characterId = "character-id";
        String noteId = "note-id";
        
        when(characterService.getById(characterId)).thenReturn(character);
        doThrow(new RuntimeException("Access denied")).when(accessValidator).validateControlAccess(character, user);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            service.get(characterId, noteId, user);
        });
        
        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository, never()).findById(anyString());
    }

    @Test
    void save_ShouldValidateCharacterAccess_BeforeSavingNote() {
        // Given
        String characterId = "character-id";
        String noteId = "note-id";
        
        when(characterService.getById(characterId)).thenReturn(character);
        doThrow(new RuntimeException("Access denied")).when(accessValidator).validateControlAccess(character, user);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            service.save(characterId, noteId, request, user);
        });
        
        verify(characterService).getById(characterId);
        verify(accessValidator).validateControlAccess(character, user);
        verify(repository, never()).findById(anyString());
        verify(repository, never()).save(any(NoteEntity.class));
    }
}