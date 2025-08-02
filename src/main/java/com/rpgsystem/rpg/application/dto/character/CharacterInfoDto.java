package com.rpgsystem.rpg.application.dto.character;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class CharacterInfoDto {
    private String id;
    private String name;
    private String profession;
    private LocalDate birthDate;
    private String birthPlace;
    private String gender;
    private Integer age;
    private Integer apparenteAge;
    private Integer heightCm;
    private Integer weightKg;
    private String religion;
}
