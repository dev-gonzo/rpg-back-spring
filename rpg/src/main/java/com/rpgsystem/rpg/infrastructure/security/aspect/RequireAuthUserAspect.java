package com.rpgsystem.rpg.infrastructure.security.aspect;

import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.infrastructure.security.AuthenticatedUserProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.server.ResponseStatusException;

@Aspect
@Component
@RequiredArgsConstructor
public class RequireAuthUserAspect {

    private final AuthenticatedUserProvider authenticatedUserProvider;

    @Before("@annotation(com.rpgsystem.rpg.infrastructure.security.annotation.RequireAuthUser)")
    public void checkUserAuthentication(JoinPoint joinPoint) {
        User user = authenticatedUserProvider.getAuthenticatedUser();
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication required");
        }

        RequestContextHolder.currentRequestAttributes()
                .setAttribute("authenticatedUser", user, RequestAttributes.SCOPE_REQUEST);
    }
}
