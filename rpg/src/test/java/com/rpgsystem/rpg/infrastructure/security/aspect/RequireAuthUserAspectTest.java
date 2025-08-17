package com.rpgsystem.rpg.infrastructure.security.aspect;

import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.infrastructure.security.AuthenticatedUserProvider;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RequireAuthUserAspect Tests")
class RequireAuthUserAspectTest {

    @Mock
    private AuthenticatedUserProvider authenticatedUserProvider;

    @Mock
    private JoinPoint joinPoint;

    @Mock
    private RequestAttributes requestAttributes;

    @InjectMocks
    private RequireAuthUserAspect requireAuthUserAspect;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId("1");
        mockUser.setName("Test User");
        mockUser.setEmail("test@example.com");
    }

    @Test
    @DisplayName("Should allow access when user is authenticated")
    void shouldAllowAccessWhenUserIsAuthenticated() {
        // Given
        when(authenticatedUserProvider.getAuthenticatedUser()).thenReturn(mockUser);

        try (MockedStatic<RequestContextHolder> mockedStatic = mockStatic(RequestContextHolder.class)) {
            mockedStatic.when(RequestContextHolder::currentRequestAttributes).thenReturn(requestAttributes);

            // When
            requireAuthUserAspect.checkUserAuthentication(joinPoint);

            // Then
            verify(authenticatedUserProvider).getAuthenticatedUser();
            verify(requestAttributes).setAttribute("authenticatedUser", mockUser, RequestAttributes.SCOPE_REQUEST);
        }
    }

    @Test
    @DisplayName("Should throw unauthorized exception when user is not authenticated")
    void shouldThrowUnauthorizedExceptionWhenUserIsNotAuthenticated() {
        // Given
        when(authenticatedUserProvider.getAuthenticatedUser()).thenReturn(null);

        // When & Then
        assertThatThrownBy(() -> requireAuthUserAspect.checkUserAuthentication(joinPoint))
                .isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("Authentication required")
                .extracting(ex -> ((ResponseStatusException) ex).getStatusCode())
                .isEqualTo(HttpStatus.UNAUTHORIZED);

        verify(authenticatedUserProvider).getAuthenticatedUser();
        verifyNoInteractions(requestAttributes);
    }

    @Test
    @DisplayName("Should set authenticated user in request attributes")
    void shouldSetAuthenticatedUserInRequestAttributes() {
        // Given
        when(authenticatedUserProvider.getAuthenticatedUser()).thenReturn(mockUser);

        try (MockedStatic<RequestContextHolder> mockedStatic = mockStatic(RequestContextHolder.class)) {
            mockedStatic.when(RequestContextHolder::currentRequestAttributes).thenReturn(requestAttributes);

            // When
            requireAuthUserAspect.checkUserAuthentication(joinPoint);

            // Then
            verify(requestAttributes).setAttribute(
                    eq("authenticatedUser"), 
                    eq(mockUser), 
                    eq(RequestAttributes.SCOPE_REQUEST)
            );
        }
    }

    @Test
    @DisplayName("Should handle different user instances")
    void shouldHandleDifferentUserInstances() {
        // Given
        User anotherUser = new User();
        anotherUser.setId("2");
        anotherUser.setName("Another User");
        anotherUser.setEmail("another@example.com");
        
        when(authenticatedUserProvider.getAuthenticatedUser()).thenReturn(anotherUser);

        try (MockedStatic<RequestContextHolder> mockedStatic = mockStatic(RequestContextHolder.class)) {
            mockedStatic.when(RequestContextHolder::currentRequestAttributes).thenReturn(requestAttributes);

            // When
            requireAuthUserAspect.checkUserAuthentication(joinPoint);

            // Then
            verify(requestAttributes).setAttribute("authenticatedUser", anotherUser, RequestAttributes.SCOPE_REQUEST);
        }
    }

    @Test
    @DisplayName("Should verify join point is passed correctly")
    void shouldVerifyJoinPointIsPassedCorrectly() {
        // Given
        when(authenticatedUserProvider.getAuthenticatedUser()).thenReturn(mockUser);

        try (MockedStatic<RequestContextHolder> mockedStatic = mockStatic(RequestContextHolder.class)) {
            mockedStatic.when(RequestContextHolder::currentRequestAttributes).thenReturn(requestAttributes);

            // When
            requireAuthUserAspect.checkUserAuthentication(joinPoint);

            // Then
            // The join point should be passed to the method without issues
            verify(authenticatedUserProvider).getAuthenticatedUser();
        }
    }
}