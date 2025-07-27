package com.rpgsystem.rpg.application.builder;

import com.rpgsystem.rpg.application.dto.UserSimpleDto;
import com.rpgsystem.rpg.domain.model.User;

public class UserSimpleDtoBuilder {

    public static UserSimpleDto build(User user) {
        if (user == null) return null;

        return UserSimpleDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }
}
