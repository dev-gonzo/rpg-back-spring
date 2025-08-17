package com.rpgsystem.rpg.domain.character.valueObject;

import com.rpgsystem.rpg.domain.exception.DomainException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Cost {

    private final int value;

    private Cost(int value) {
        if (value <= 0) {
            throw new DomainException("Cost must be greater than 0.");
        }
        this.value = value;
    }

    public static Cost of(Integer value) {
        if (value == null) return null;
        return new Cost(value);
    }

    public static Cost from(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return new Cost(Integer.parseInt(value.trim()));
        } catch (NumberFormatException ex) {
            throw new DomainException("Cost must be a valid number.");
        }
    }
}
