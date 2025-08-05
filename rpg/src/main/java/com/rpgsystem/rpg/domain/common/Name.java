package com.rpgsystem.rpg.domain.common;

import com.rpgsystem.rpg.domain.exception.DomainException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class Name {

    private final String value;

    public Name(String value) {
        if (value == null || value.isBlank() || value.trim().isBlank()) {
            throw new DomainException("Name must not be null or blank");
        }

        if (value.length() > 100) {
            throw new DomainException("Name must be at most 100 characters");
        }

        this.value = value.trim();
    }

    public static Name of(String value) {
        if (value == null || value.isBlank()) return null;
        return new Name(value);
    }
}
