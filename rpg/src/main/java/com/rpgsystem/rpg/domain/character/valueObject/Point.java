package com.rpgsystem.rpg.domain.character.valueObject;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class Point {

    private final Integer value;

    public static Point of(Integer value) {
        return new Point(value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
