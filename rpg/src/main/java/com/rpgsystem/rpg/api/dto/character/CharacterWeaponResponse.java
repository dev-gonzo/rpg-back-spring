package com.rpgsystem.rpg.api.dto.character;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class CharacterWeaponResponse {

    private String id;
    private String characterId;

    private String name;
    private String description;
    private String damage;
    private Integer initiative;
    private String range;
    private String rof;
    private String ammunition;
    private String bookPage;

    private Instant createdAt;
    private Instant updatedAt;
}
