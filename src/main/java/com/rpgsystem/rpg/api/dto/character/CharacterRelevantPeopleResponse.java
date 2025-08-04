package com.rpgsystem.rpg.api.dto.character;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class CharacterRelevantPeopleResponse {

    private String id;
    private String characterId;

    private String category;
    private String name;
    private Integer apparentAge;
    private String city;
    private String profession;
    private String briefDescription;

    private Instant createdAt;
    private Instant updatedAt;
}
