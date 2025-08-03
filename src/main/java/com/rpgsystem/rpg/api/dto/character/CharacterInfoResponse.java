package com.rpgsystem.rpg.api.dto.character;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class CharacterInfoResponse {

    private String id;
    private String name;
    private String profession;
    private LocalDate birthDate;
    private String birthPlace;
    private String gender;
    private Integer age;
    private Integer apparentAge;
    private Integer heightCm;
    private Integer weightKg;
    private String religion;
}
