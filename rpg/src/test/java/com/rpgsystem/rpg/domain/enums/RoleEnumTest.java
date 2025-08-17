package com.rpgsystem.rpg.domain.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleEnumTest {

    @Test
    void testEnumValues() {
        RoleEnum[] values = RoleEnum.values();
        
        assertEquals(2, values.length);
        assertEquals(RoleEnum.MASTER, values[0]);
        assertEquals(RoleEnum.PLAYER, values[1]);
    }

    @Test
    void testValueOf() {
        assertEquals(RoleEnum.MASTER, RoleEnum.valueOf("MASTER"));
        assertEquals(RoleEnum.PLAYER, RoleEnum.valueOf("PLAYER"));
    }

    @Test
    void testValueOfInvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> {
            RoleEnum.valueOf("INVALID");
        });
    }

    @Test
    void testEnumName() {
        assertEquals("MASTER", RoleEnum.MASTER.name());
        assertEquals("PLAYER", RoleEnum.PLAYER.name());
    }

    @Test
    void testEnumOrdinal() {
        assertEquals(0, RoleEnum.MASTER.ordinal());
        assertEquals(1, RoleEnum.PLAYER.ordinal());
    }

    @Test
    void testEnumToString() {
        assertEquals("MASTER", RoleEnum.MASTER.toString());
        assertEquals("PLAYER", RoleEnum.PLAYER.toString());
    }

    @Test
    void testEnumEquality() {
        assertEquals(RoleEnum.MASTER, RoleEnum.MASTER);
        assertEquals(RoleEnum.PLAYER, RoleEnum.PLAYER);
        assertNotEquals(RoleEnum.MASTER, RoleEnum.PLAYER);
    }
}