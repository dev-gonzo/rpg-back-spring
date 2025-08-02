package com.rpgsystem.rpg.domain.character.valueobject;

import com.rpgsystem.rpg.domain.exception.DomainException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Weight {

    private final int kilograms;

    public Weight(int kilograms) {
        if (kilograms <= 0  ) {
            throw new DomainException("Weight must be greater than 0 kg.");
        }
        this.kilograms = kilograms;
    }

    public double inGrams() {
        return kilograms * 1000.0;
    }
}
