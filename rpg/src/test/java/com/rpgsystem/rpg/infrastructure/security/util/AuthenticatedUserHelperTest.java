package com.rpgsystem.rpg.infrastructure.security.util;

import com.rpgsystem.rpg.domain.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@DisplayName("AuthenticatedUserHelper Tests")
class AuthenticatedUserHelperTest {

    private RequestAttributes mockRequestAttributes;
    private User mockUser;
    private MockedStatic<RequestContextHolder> mockedRequestContextHolder;

    @BeforeEach
    void setUp() {
        mockRequestAttributes = mock(RequestAttributes.class);
        mockUser = new User();
        mockUser.setId("1");
        mockUser.setName("Test User");
        mockUser.setEmail("test@example.com");
        
        mockedRequestContextHolder = mockStatic(RequestContextHolder.class);
    }

    @AfterEach
    void tearDown() {
        mockedRequestContextHolder.close();
    }

    @Test
    @DisplayName("Should return authenticated user when user exists in request context")
    void shouldReturnAuthenticatedUserWhenUserExistsInRequestContext() {
        // Given
        mockedRequestContextHolder.when(RequestContextHolder::currentRequestAttributes)
                .thenReturn(mockRequestAttributes);
        when(mockRequestAttributes.getAttribute("authenticatedUser", RequestAttributes.SCOPE_REQUEST))
                .thenReturn(mockUser);

        // When
        User result = AuthenticatedUserHelper.get();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(mockUser);
        assertThat(result.getId()).isEqualTo("1");
        assertThat(result.getName()).isEqualTo("Test User");
        assertThat(result.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Should throw IllegalStateException when user attribute is null")
    void shouldThrowIllegalStateExceptionWhenUserAttributeIsNull() {
        // Given
        mockedRequestContextHolder.when(RequestContextHolder::currentRequestAttributes)
                .thenReturn(mockRequestAttributes);
        when(mockRequestAttributes.getAttribute("authenticatedUser", RequestAttributes.SCOPE_REQUEST))
                .thenReturn(null);

        // When & Then
        assertThatThrownBy(AuthenticatedUserHelper::get)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Authenticated user not found in request context.");
    }

    @Test
    @DisplayName("Should throw IllegalStateException when user attribute is not User instance")
    void shouldThrowIllegalStateExceptionWhenUserAttributeIsNotUserInstance() {
        // Given
        String notAUser = "not-a-user-object";
        mockedRequestContextHolder.when(RequestContextHolder::currentRequestAttributes)
                .thenReturn(mockRequestAttributes);
        when(mockRequestAttributes.getAttribute("authenticatedUser", RequestAttributes.SCOPE_REQUEST))
                .thenReturn(notAUser);

        // When & Then
        assertThatThrownBy(AuthenticatedUserHelper::get)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Authenticated user not found in request context.");
    }

    @Test
    @DisplayName("Should handle different User instances correctly")
    void shouldHandleDifferentUserInstancesCorrectly() {
        // Given
        User anotherUser = new User();
        anotherUser.setId("2");
        anotherUser.setName("Another User");
        anotherUser.setEmail("another@example.com");
        
        mockedRequestContextHolder.when(RequestContextHolder::currentRequestAttributes)
                .thenReturn(mockRequestAttributes);
        when(mockRequestAttributes.getAttribute("authenticatedUser", RequestAttributes.SCOPE_REQUEST))
                .thenReturn(anotherUser);

        // When
        User result = AuthenticatedUserHelper.get();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(anotherUser);
        assertThat(result.getId()).isEqualTo("2");
        assertThat(result.getName()).isEqualTo("Another User");
        assertThat(result.getEmail()).isEqualTo("another@example.com");
    }

    @Test
    @DisplayName("Should handle User with minimal data")
    void shouldHandleUserWithMinimalData() {
        // Given
        User minimalUser = new User();
        minimalUser.setId("0");
        
        mockedRequestContextHolder.when(RequestContextHolder::currentRequestAttributes)
                .thenReturn(mockRequestAttributes);
        when(mockRequestAttributes.getAttribute("authenticatedUser", RequestAttributes.SCOPE_REQUEST))
                .thenReturn(minimalUser);

        // When
        User result = AuthenticatedUserHelper.get();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(minimalUser);
        assertThat(result.getId()).isEqualTo("0");
        assertThat(result.getName()).isNull();
        assertThat(result.getEmail()).isNull();
    }

    @Test
    @DisplayName("Should handle User with large ID")
    void shouldHandleUserWithLargeId() {
        // Given
        User userWithLargeId = new User();
        userWithLargeId.setId("999999999");
        userWithLargeId.setName("User with Large ID");
        
        mockedRequestContextHolder.when(RequestContextHolder::currentRequestAttributes)
                .thenReturn(mockRequestAttributes);
        when(mockRequestAttributes.getAttribute("authenticatedUser", RequestAttributes.SCOPE_REQUEST))
                .thenReturn(userWithLargeId);

        // When
        User result = AuthenticatedUserHelper.get();

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("999999999");
        assertThat(result.getName()).isEqualTo("User with Large ID");
    }

    @Test
    @DisplayName("Should verify RequestContextHolder interaction")
    void shouldVerifyRequestContextHolderInteraction() {
        // Given
        mockedRequestContextHolder.when(RequestContextHolder::currentRequestAttributes)
                .thenReturn(mockRequestAttributes);
        when(mockRequestAttributes.getAttribute("authenticatedUser", RequestAttributes.SCOPE_REQUEST))
                .thenReturn(mockUser);

        // When
        AuthenticatedUserHelper.get();

        // Then
        mockedRequestContextHolder.verify(RequestContextHolder::currentRequestAttributes);
        verify(mockRequestAttributes).getAttribute("authenticatedUser", RequestAttributes.SCOPE_REQUEST);
    }

    @Test
    @DisplayName("Should handle multiple consecutive calls consistently")
    void shouldHandleMultipleConsecutiveCallsConsistently() {
        // Given
        mockedRequestContextHolder.when(RequestContextHolder::currentRequestAttributes)
                .thenReturn(mockRequestAttributes);
        when(mockRequestAttributes.getAttribute("authenticatedUser", RequestAttributes.SCOPE_REQUEST))
                .thenReturn(mockUser);

        // When
        User firstCall = AuthenticatedUserHelper.get();
        User secondCall = AuthenticatedUserHelper.get();
        User thirdCall = AuthenticatedUserHelper.get();

        // Then
        assertThat(firstCall).isEqualTo(mockUser);
        assertThat(secondCall).isEqualTo(mockUser);
        assertThat(thirdCall).isEqualTo(mockUser);
        assertThat(firstCall).isEqualTo(secondCall).isEqualTo(thirdCall);
        
        // Verify interactions
        mockedRequestContextHolder.verify(() -> RequestContextHolder.currentRequestAttributes(), times(3));
        verify(mockRequestAttributes, times(3)).getAttribute("authenticatedUser", RequestAttributes.SCOPE_REQUEST);
    }

    @Test
    @DisplayName("Should handle Integer object as non-User attribute")
    void shouldHandleIntegerObjectAsNonUserAttribute() {
        // Given
        Integer integerObject = 123;
        mockedRequestContextHolder.when(RequestContextHolder::currentRequestAttributes)
                .thenReturn(mockRequestAttributes);
        when(mockRequestAttributes.getAttribute("authenticatedUser", RequestAttributes.SCOPE_REQUEST))
                .thenReturn(integerObject);

        // When & Then
        assertThatThrownBy(AuthenticatedUserHelper::get)
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Authenticated user not found in request context.");
    }

    @Test
    @DisplayName("Should create instance of AuthenticatedUserHelper")
    void shouldCreateInstanceOfAuthenticatedUserHelper() {
        // When
        AuthenticatedUserHelper helper = new AuthenticatedUserHelper();

        // Then
        assertThat(helper).isNotNull();
        assertThat(helper).isInstanceOf(AuthenticatedUserHelper.class);
    }
}