package com.rpgsystem.rpg.infrastructure.security.util;

import com.rpgsystem.rpg.domain.entity.User;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class AuthenticatedUserHelper {

    public static User get() {
        RequestAttributes attributes = RequestContextHolder.currentRequestAttributes();
        Object userObj = attributes.getAttribute("authenticatedUser", RequestAttributes.SCOPE_REQUEST);

        if (userObj instanceof User user) {
            return user;
        }

        throw new IllegalStateException("Authenticated user not found in request context.");
    }
}
