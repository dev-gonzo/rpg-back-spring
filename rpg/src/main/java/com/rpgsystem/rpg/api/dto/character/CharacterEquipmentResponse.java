package com.rpgsystem.rpg.api.dto.character;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class CharacterEquipmentResponse {

    private String id;
    private String characterId;

    private String name;
    private Integer quantity;
    private String classification;
    private String description;

    private Integer kineticProtection;
    private Integer ballisticProtection;
    private Integer dexterityPenalty;
    private Integer agilityPenalty;
    private Integer initiative;
    private String bookPage;

    private Instant createdAt;
    private Instant updatedAt;
}
