package com.rpgsystem.rpg.api.dto.character;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CharacterEquipmentRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    @NotBlank(message = "Classification is required")
    private String classification;

    private String description;

    private Integer kineticProtection;
    private Integer ballisticProtection;
    private Integer dexterityPenalty;
    private Integer agilityPenalty;
    private Integer initiative;

    private String bookPage;
}
