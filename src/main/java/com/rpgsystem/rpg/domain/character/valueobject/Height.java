package com.rpgsystem.rpg.domain.character.valueobject;

import com.rpgsystem.rpg.domain.exception.DomainException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Height {

    private final int centimeters;

    public Height(Integer centimeters) {
        if (centimeters <= 0  ) {
            throw new DomainException("Height must be greater than 0 kg.");
        }
        this.centimeters = centimeters;
    }
}

