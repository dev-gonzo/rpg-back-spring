package com.rpgsystem.rpg.api.dto.character;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CharacterRelevantPeopleRequest {

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Name is required")
    private String name;

    private Integer apparentAge;
    private String city;
    private String profession;
    private String briefDescription;
}
