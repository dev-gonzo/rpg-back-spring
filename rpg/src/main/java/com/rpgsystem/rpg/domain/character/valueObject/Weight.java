package com.rpgsystem.rpg.domain.character.valueObject;

import com.rpgsystem.rpg.domain.exception.DomainException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Weight {

    private final int kilograms;

    private Weight(int kilograms) {
        if (kilograms <= 0) {
            throw new DomainException("Weight must be greater than 0 kg.");
        }
        this.kilograms = kilograms;
    }

    public static Weight of(Integer kilograms) {
        if (kilograms == null) return null;
        return new Weight(kilograms);
    }

    public static Weight from(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return new Weight(Integer.parseInt(value));
        } catch (NumberFormatException ex) {
            throw new DomainException("Weight must be a valid number.");
        }
    }

    public double inGrams() {
        return kilograms * 1000.0;
    }
}
