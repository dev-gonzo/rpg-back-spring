package com.rpgsystem.rpg.application.builder;

import com.rpgsystem.rpg.api.dto.character.CharacterWeaponResponse;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.WeaponEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CharacterWeaponDtoBuilder Tests")
class CharacterWeaponDtoBuilderTest {

    @Test
    @DisplayName("Should build CharacterWeaponResponse from WeaponEntity with all fields")
    void shouldBuildCharacterWeaponResponseFromWeaponEntityWithAllFields() {
        // Given
        CharacterEntity character = CharacterEntity.builder()
                .id("char-123")
                .name("Test Character")
                .build();

        Instant createdAt = Instant.now().minusSeconds(3600);
        Instant updatedAt = Instant.now();

        WeaponEntity weapon = WeaponEntity.builder()
                .id("weapon-123")
                .character(character)
                .name("Glock 17")
                .description("Semi-automatic pistol")
                .damage("2d6+1")
                .initiative(3)
                .range("50m")
                .rof("3")
                .ammunition("9mm")
                .bookPage("p.142")
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        // When
        CharacterWeaponResponse result = CharacterWeaponDtoBuilder.from(weapon);

        // Then
        assertNotNull(result);
        assertEquals("weapon-123", result.getId());
        assertEquals("char-123", result.getCharacterId());
        assertEquals("Glock 17", result.getName());
        assertEquals("Semi-automatic pistol", result.getDescription());
        assertEquals("2d6+1", result.getDamage());
        assertEquals(3, result.getInitiative());
        assertEquals("50m", result.getRange());
        assertEquals("3", result.getRof());
        assertEquals("9mm", result.getAmmunition());
        assertEquals("p.142", result.getBookPage());
        assertEquals(createdAt, result.getCreatedAt());
        assertEquals(updatedAt, result.getUpdatedAt());
    }

    @Test
    @DisplayName("Should build CharacterWeaponResponse from WeaponEntity with null values")
    void shouldBuildCharacterWeaponResponseFromWeaponEntityWithNullValues() {
        // Given
        CharacterEntity character = CharacterEntity.builder()
                .id("char-null")
                .build();

        WeaponEntity weapon = WeaponEntity.builder()
                .id("weapon-null")
                .character(character)
                .name(null)
                .description(null)
                .damage(null)
                .initiative(null)
                .range(null)
                .rof(null)
                .ammunition(null)
                .bookPage(null)
                .createdAt(null)
                .updatedAt(null)
                .build();

        // When
        CharacterWeaponResponse result = CharacterWeaponDtoBuilder.from(weapon);

        // Then
        assertNotNull(result);
        assertEquals("weapon-null", result.getId());
        assertEquals("char-null", result.getCharacterId());
        assertNull(result.getName());
        assertNull(result.getDescription());
        assertNull(result.getDamage());
        assertNull(result.getInitiative());
        assertNull(result.getRange());
        assertNull(result.getRof());
        assertNull(result.getAmmunition());
        assertNull(result.getBookPage());
        assertNull(result.getCreatedAt());
        assertNull(result.getUpdatedAt());
    }

    @Test
    @DisplayName("Should build CharacterWeaponResponse with empty string values")
    void shouldBuildCharacterWeaponResponseWithEmptyStringValues() {
        // Given
        CharacterEntity character = CharacterEntity.builder()
                .id("char-empty")
                .build();

        Instant timestamp = Instant.now();

        WeaponEntity weapon = WeaponEntity.builder()
                .id("weapon-empty")
                .character(character)
                .name("")
                .description("")
                .damage("")
                .initiative(0)
                .range("")
                .rof("")
                .ammunition("")
                .bookPage("")
                .createdAt(timestamp)
                .updatedAt(timestamp)
                .build();

        // When
        CharacterWeaponResponse result = CharacterWeaponDtoBuilder.from(weapon);

        // Then
        assertNotNull(result);
        assertEquals("weapon-empty", result.getId());
        assertEquals("char-empty", result.getCharacterId());
        assertEquals("", result.getName());
        assertEquals("", result.getDescription());
        assertEquals("", result.getDamage());
        assertEquals(0, result.getInitiative());
        assertEquals("", result.getRange());
        assertEquals("", result.getRof());
        assertEquals("", result.getAmmunition());
        assertEquals("", result.getBookPage());
        assertEquals(timestamp, result.getCreatedAt());
        assertEquals(timestamp, result.getUpdatedAt());
    }

    @Test
    @DisplayName("Should build CharacterWeaponResponse with maximum values")
    void shouldBuildCharacterWeaponResponseWithMaximumValues() {
        // Given
        CharacterEntity character = CharacterEntity.builder()
                .id("char-max")
                .build();

        Instant timestamp = Instant.now();
        String longDescription = "This is a very long description that contains many details about the weapon including its history, manufacturing process, technical specifications, and usage instructions. ".repeat(10);

        WeaponEntity weapon = WeaponEntity.builder()
                .id("weapon-max")
                .character(character)
                .name("Very Long Weapon Name With Special Characters @#$%^&*()_+")
                .description(longDescription)
                .damage("10d20+100")
                .initiative(Integer.MAX_VALUE)
                .range("999999km")
                .rof("∞")
                .ammunition("Unlimited Special Ammo Type")
                .bookPage("p.999999")
                .createdAt(timestamp)
                .updatedAt(timestamp)
                .build();

        // When
        CharacterWeaponResponse result = CharacterWeaponDtoBuilder.from(weapon);

        // Then
        assertNotNull(result);
        assertEquals("weapon-max", result.getId());
        assertEquals("char-max", result.getCharacterId());
        assertEquals("Very Long Weapon Name With Special Characters @#$%^&*()_+", result.getName());
        assertEquals(longDescription, result.getDescription());
        assertEquals("10d20+100", result.getDamage());
        assertEquals(Integer.MAX_VALUE, result.getInitiative());
        assertEquals("999999km", result.getRange());
        assertEquals("∞", result.getRof());
        assertEquals("Unlimited Special Ammo Type", result.getAmmunition());
        assertEquals("p.999999", result.getBookPage());
        assertEquals(timestamp, result.getCreatedAt());
        assertEquals(timestamp, result.getUpdatedAt());
    }

    @Test
    @DisplayName("Should build CharacterWeaponResponse with Unicode characters")
    void shouldBuildCharacterWeaponResponseWithUnicodeCharacters() {
        // Given
        CharacterEntity character = CharacterEntity.builder()
                .id("char-unicode")
                .build();

        Instant timestamp = Instant.now();

        WeaponEntity weapon = WeaponEntity.builder()
                .id("weapon-unicode")
                .character(character)
                .name("Katana 刀 🗡️")
                .description("Espada japonesa tradicional forjada pelos mestres ferreiros 鍛冶師 com técnicas ancestrais")
                .damage("2d8+força 💪")
                .initiative(5)
                .range("Corpo a corpo 近接")
                .rof("1 ataque/turno")
                .ammunition("N/A 該当なし")
                .bookPage("página 武器 🔖")
                .createdAt(timestamp)
                .updatedAt(timestamp)
                .build();

        // When
        CharacterWeaponResponse result = CharacterWeaponDtoBuilder.from(weapon);

        // Then
        assertNotNull(result);
        assertEquals("weapon-unicode", result.getId());
        assertEquals("char-unicode", result.getCharacterId());
        assertEquals("Katana 刀 🗡️", result.getName());
        assertTrue(result.getName().contains("刀"));
        assertTrue(result.getName().contains("🗡️"));
        assertEquals("Espada japonesa tradicional forjada pelos mestres ferreiros 鍛冶師 com técnicas ancestrais", result.getDescription());
        assertTrue(result.getDescription().contains("鍛冶師"));
        assertEquals("2d8+força 💪", result.getDamage());
        assertTrue(result.getDamage().contains("💪"));
        assertEquals(5, result.getInitiative());
        assertEquals("Corpo a corpo 近接", result.getRange());
        assertTrue(result.getRange().contains("近接"));
        assertEquals("1 ataque/turno", result.getRof());
        assertEquals("N/A 該当なし", result.getAmmunition());
        assertTrue(result.getAmmunition().contains("該当なし"));
        assertEquals("página 武器 🔖", result.getBookPage());
        assertTrue(result.getBookPage().contains("武器"));
        assertTrue(result.getBookPage().contains("🔖"));
        assertEquals(timestamp, result.getCreatedAt());
        assertEquals(timestamp, result.getUpdatedAt());
    }

    @Test
    @DisplayName("Should build CharacterWeaponResponse with negative initiative")
    void shouldBuildCharacterWeaponResponseWithNegativeInitiative() {
        // Given
        CharacterEntity character = CharacterEntity.builder()
                .id("char-negative")
                .build();

        Instant timestamp = Instant.now();

        WeaponEntity weapon = WeaponEntity.builder()
                .id("weapon-negative")
                .character(character)
                .name("Heavy Weapon")
                .description("Very heavy weapon that reduces initiative")
                .damage("5d10")
                .initiative(-10)
                .range("100m")
                .rof("1/3")
                .ammunition(".50 cal")
                .bookPage("p.200")
                .createdAt(timestamp)
                .updatedAt(timestamp)
                .build();

        // When
        CharacterWeaponResponse result = CharacterWeaponDtoBuilder.from(weapon);

        // Then
        assertNotNull(result);
        assertEquals("weapon-negative", result.getId());
        assertEquals("char-negative", result.getCharacterId());
        assertEquals("Heavy Weapon", result.getName());
        assertEquals("Very heavy weapon that reduces initiative", result.getDescription());
        assertEquals("5d10", result.getDamage());
        assertEquals(-10, result.getInitiative());
        assertEquals("100m", result.getRange());
        assertEquals("1/3", result.getRof());
        assertEquals(".50 cal", result.getAmmunition());
        assertEquals("p.200", result.getBookPage());
        assertEquals(timestamp, result.getCreatedAt());
        assertEquals(timestamp, result.getUpdatedAt());
    }

    @Test
    @DisplayName("Should not allow instantiation")
    void shouldNotAllowInstantiation() throws Exception {
        // Given
        Constructor<CharacterWeaponDtoBuilder> constructor = CharacterWeaponDtoBuilder.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // When & Then
        assertThrows(InvocationTargetException.class, () -> {
            constructor.newInstance();
        });
    }
}