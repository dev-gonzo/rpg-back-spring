package com.rpgsystem.rpg.api.dto.character;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PathsAndFormsRequest {

    @NotNull @Min(0) @Max(10)
    private Integer understandForm;

    @NotNull @Min(0) @Max(10)
    private Integer createForm;

    @NotNull @Min(0) @Max(10)
    private Integer controlForm;

    @NotNull @Min(0) @Max(4)
    private Integer fire;

    @NotNull @Min(0) @Max(4)
    private Integer water;

    @NotNull @Min(0) @Max(4)
    private Integer earth;

    @NotNull @Min(0) @Max(4)
    private Integer air;

    @NotNull @Min(0) @Max(4)
    private Integer light;

    @NotNull @Min(0) @Max(4)
    private Integer darkness;

    @NotNull @Min(0) @Max(4)
    private Integer plants;

    @NotNull @Min(0) @Max(4)
    private Integer animals;

    @NotNull @Min(0) @Max(4)
    private Integer humans;

    @NotNull @Min(0) @Max(4)
    private Integer spiritum;

    @NotNull @Min(0) @Max(4)
    private Integer arkanun;

    @NotNull @Min(0) @Max(4)
    private Integer metamagic;
}
