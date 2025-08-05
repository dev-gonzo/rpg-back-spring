package com.rpgsystem.rpg.domain.character.valueObject;

import com.rpgsystem.rpg.domain.enums.AttributeEnum;
import com.rpgsystem.rpg.domain.exception.DomainException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
public class AttributeLabel {

    private final AttributeEnum value;

    private AttributeLabel(AttributeEnum value) {
        this.value = value;
    }

    public static AttributeLabel of(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return new AttributeLabel(AttributeEnum.valueOf(value.trim().toUpperCase()));
        } catch (IllegalArgumentException ex) {
            throw new DomainException("Invalid attribute label: " + value);
        }
    }

    public static AttributeLabel of(AttributeEnum value) {
        if (value == null) return null;
        return new AttributeLabel(value);
    }
}
