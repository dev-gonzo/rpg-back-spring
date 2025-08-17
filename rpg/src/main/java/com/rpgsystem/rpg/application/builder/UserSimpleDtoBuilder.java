package com.rpgsystem.rpg.application.builder;

import com.rpgsystem.rpg.api.dto.UserSimpleResponse;
import com.rpgsystem.rpg.domain.entity.User;

public class UserSimpleDtoBuilder {

    private UserSimpleDtoBuilder() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static UserSimpleResponse build(User user) {
        if (user == null) return null;

        return UserSimpleResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
