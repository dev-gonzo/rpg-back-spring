package com.rpgsystem.rpg.api.dto.character;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class CharacterPathsAndFormsResponse {

    private String characterId;

    private Integer understandForm;
    private Integer createForm;
    private Integer controlForm;

    private Integer fire;
    private Integer water;
    private Integer earth;
    private Integer air;
    private Integer light;
    private Integer darkness;
    private Integer plants;
    private Integer animals;
    private Integer humans;
    private Integer spiritum;
    private Integer arkanun;
    private Integer metamagic;

    private Instant createdAt;
    private Instant updatedAt;
}
