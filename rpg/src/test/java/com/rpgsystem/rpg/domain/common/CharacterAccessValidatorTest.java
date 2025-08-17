package com.rpgsystem.rpg.domain.common;

import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.User;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
@DisplayName("CharacterAccessValidator Tests")
class CharacterAccessValidatorTest {

    @InjectMocks
    private CharacterAccessValidator characterAccessValidator;

    private User masterUser;
    private User regularUser;
    private User anotherUser;
    private CharacterEntity characterEntity;

    @BeforeEach
    void setUp() {
        // Setup master user
        masterUser = new User();
        masterUser.setId("1");
        masterUser.setName("master");
        masterUser.setMaster(true);

        // Setup regular user (character owner)
        regularUser = new User();
        regularUser.setId("2");
        regularUser.setName("player");
        regularUser.setMaster(false);

        // Setup another user (not character owner)
        anotherUser = new User();
        anotherUser.setId("3");
        anotherUser.setName("otherplayer");
        anotherUser.setMaster(false);

        // Setup character entity
        characterEntity = new CharacterEntity();
        characterEntity.setId("1");
        characterEntity.setName("Test Character");
        characterEntity.setControlUser(regularUser);
    }

    @Test
    @DisplayName("Should allow access when user is master")
    void shouldAllowAccessWhenUserIsMaster() {
        // Given
        // masterUser is already set as master

        // When & Then
        assertThatNoException().isThrownBy(() -> 
            characterAccessValidator.validateControlAccess(characterEntity, masterUser)
        );
    }

    @Test
    @DisplayName("Should allow access when user is character owner")
    void shouldAllowAccessWhenUserIsCharacterOwner() {
        // Given
        // regularUser is set as character control user

        // When & Then
        assertThatNoException().isThrownBy(() -> 
            characterAccessValidator.validateControlAccess(characterEntity, regularUser)
        );
    }

    @Test
    @DisplayName("Should throw SecurityException when user is not master and not character owner")
    void shouldThrowSecurityExceptionWhenUserIsNotMasterAndNotCharacterOwner() {
        // Given
        // anotherUser is not master and not character owner

        // When & Then
        assertThatThrownBy(() -> 
            characterAccessValidator.validateControlAccess(characterEntity, anotherUser)
        )
        .isInstanceOf(SecurityException.class)
        .hasMessage("User does not have permission to modify this character.");
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when character is null")
    void shouldThrowEntityNotFoundExceptionWhenCharacterIsNull() {
        // Given
        CharacterEntity nullCharacter = null;

        // When & Then
        assertThatThrownBy(() -> 
            characterAccessValidator.validateControlAccess(nullCharacter, regularUser)
        )
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage("Character not found");
    }

    @Test
    @DisplayName("Should throw SecurityException when character has no control user and user is not master")
    void shouldThrowSecurityExceptionWhenCharacterHasNoControlUserAndUserIsNotMaster() {
        // Given
        characterEntity.setControlUser(null);

        // When & Then
        assertThatThrownBy(() -> 
            characterAccessValidator.validateControlAccess(characterEntity, regularUser)
        )
        .isInstanceOf(SecurityException.class)
        .hasMessage("User does not have permission to modify this character.");
    }

    @Test
    @DisplayName("Should allow access when character has no control user but user is master")
    void shouldAllowAccessWhenCharacterHasNoControlUserButUserIsMaster() {
        // Given
        characterEntity.setControlUser(null);

        // When & Then
        assertThatNoException().isThrownBy(() -> 
            characterAccessValidator.validateControlAccess(characterEntity, masterUser)
        );
    }

    @Test
    @DisplayName("Should handle null user gracefully")
    void shouldHandleNullUserGracefully() {
        // Given
        User nullUser = null;

        // When & Then
        assertThatThrownBy(() -> 
            characterAccessValidator.validateControlAccess(characterEntity, nullUser)
        )
        .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Should validate access with different user IDs")
    void shouldValidateAccessWithDifferentUserIds() {
        // Given
        User userWithSameId = new User();
        userWithSameId.setId("2"); // Same ID as regularUser
        userWithSameId.setName("differentusername");
        userWithSameId.setMaster(false);

        // When & Then
        assertThatNoException().isThrownBy(() -> 
            characterAccessValidator.validateControlAccess(characterEntity, userWithSameId)
        );
    }

    @Test
    @DisplayName("Should deny access when user ID is different even with same username")
    void shouldDenyAccessWhenUserIdIsDifferentEvenWithSameUsername() {
        // Given
        User userWithDifferentId = new User();
        userWithDifferentId.setId("999"); // Different ID
        userWithDifferentId.setName("player"); // Same name as regularUser
        userWithDifferentId.setMaster(false);

        // When & Then
        assertThatThrownBy(() -> 
            characterAccessValidator.validateControlAccess(characterEntity, userWithDifferentId)
        )
        .isInstanceOf(SecurityException.class)
        .hasMessage("User does not have permission to modify this character.");
    }

    @Test
    @DisplayName("Should handle character with null control user ID")
    void shouldHandleCharacterWithNullControlUserId() {
        // Given
        User controlUserWithNullId = new User();
        controlUserWithNullId.setId(null);
        controlUserWithNullId.setName("nulliduser");
        characterEntity.setControlUser(controlUserWithNullId);

        // When & Then
        assertThatThrownBy(() -> 
            characterAccessValidator.validateControlAccess(characterEntity, regularUser)
        )
        .isInstanceOf(SecurityException.class)
        .hasMessage("User does not have permission to modify this character.");
    }

    @Test
    @DisplayName("Should handle user with null ID")
    void shouldHandleUserWithNullId() {
        // Given
        User userWithNullId = new User();
        userWithNullId.setId(null);
        userWithNullId.setName("nulliduser");
        userWithNullId.setMaster(false);

        // When & Then
        assertThatThrownBy(() -> 
            characterAccessValidator.validateControlAccess(characterEntity, userWithNullId)
        )
        .isInstanceOf(SecurityException.class)
        .hasMessage("User does not have permission to modify this character.");
    }

    @Test
    @DisplayName("Should allow master user even when both user and character control user have null IDs")
    void shouldAllowMasterUserEvenWhenBothUserAndCharacterControlUserHaveNullIds() {
        // Given
        User masterWithNullId = new User();
        masterWithNullId.setId(null);
        masterWithNullId.setName("masternullid");
        masterWithNullId.setMaster(true);
        
        User controlUserWithNullId = new User();
        controlUserWithNullId.setId(null);
        characterEntity.setControlUser(controlUserWithNullId);

        // When & Then
        assertThatNoException().isThrownBy(() -> 
            characterAccessValidator.validateControlAccess(characterEntity, masterWithNullId)
        );
    }

    @Test
    @DisplayName("Should validate access for character with zero ID")
    void shouldValidateAccessForCharacterWithZeroId() {
        // Given
        User userWithZeroId = new User();
        userWithZeroId.setId("0");
        userWithZeroId.setMaster(false);
        
        characterEntity.getControlUser().setId("0");

        // When & Then
        assertThatNoException().isThrownBy(() -> 
            characterAccessValidator.validateControlAccess(characterEntity, userWithZeroId)
        );
    }

    @Test
    @DisplayName("Should validate access for character with negative ID")
    void shouldValidateAccessForCharacterWithNegativeId() {
        // Given
        User userWithNegativeId = new User();
        userWithNegativeId.setId("-1");
        userWithNegativeId.setMaster(false);
        
        characterEntity.getControlUser().setId("-1");

        // When & Then
        assertThatNoException().isThrownBy(() -> 
            characterAccessValidator.validateControlAccess(characterEntity, userWithNegativeId)
        );
    }

    @Test
    @DisplayName("Should validate access for character with very large ID")
    void shouldValidateAccessForCharacterWithVeryLargeId() {
        // Given
        String largeId = String.valueOf(Long.MAX_VALUE);
        User userWithLargeId = new User();
        userWithLargeId.setId(largeId);
        userWithLargeId.setMaster(false);
        
        characterEntity.getControlUser().setId(largeId);

        // When & Then
        assertThatNoException().isThrownBy(() -> 
            characterAccessValidator.validateControlAccess(characterEntity, userWithLargeId)
        );
    }

    @Test
    @DisplayName("Should prioritize master status over character ownership")
    void shouldPrioritizeMasterStatusOverCharacterOwnership() {
        // Given
        // Set character to be owned by anotherUser
        characterEntity.setControlUser(anotherUser);
        
        // masterUser should still have access despite not being the owner

        // When & Then
        assertThatNoException().isThrownBy(() -> 
            characterAccessValidator.validateControlAccess(characterEntity, masterUser)
        );
    }

    @Test
    @DisplayName("Should handle concurrent access validation")
    void shouldHandleConcurrentAccessValidation() throws InterruptedException {
        // Given
        int threadCount = 10;
        Thread[] threads = new Thread[threadCount];
        Exception[] exceptions = new Exception[threadCount];

        // When
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            threads[i] = new Thread(() -> {
                try {
                    characterAccessValidator.validateControlAccess(characterEntity, regularUser);
                } catch (Exception e) {
                    exceptions[index] = e;
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        // Then
        for (Exception exception : exceptions) {
            if (exception != null) {
                throw new AssertionError("Unexpected exception in concurrent access", exception);
            }
        }
    }
}