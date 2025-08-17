package com.rpgsystem.rpg.application.builder;

import com.rpgsystem.rpg.api.dto.character.CharacterEquipmentResponse;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.EquipmentEntity;
import com.rpgsystem.rpg.domain.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CharacterEquipmentDtoBuilderTest {

    @Test
    @DisplayName("Should build CharacterEquipmentResponse from EquipmentEntity with all values")
    void shouldBuildFromEquipmentEntityWithAllValues() {
        // Given
        User user = User.builder()
                .id("user-id")
                .name("Test User")
                .email("test@example.com")
                .build();

        CharacterEntity character = CharacterEntity.builder()
                .id("character-id")
                .name("Test Character")
                .controlUser(user)
                .build();

        Instant now = Instant.now();
        EquipmentEntity entity = EquipmentEntity.builder()
                .id("equipment-id")
                .character(character)
                .name("Kevlar Vest")
                .quantity(1)
                .classification("Armor")
                .description("Bulletproof vest providing ballistic protection")
                .kineticProtection(5)
                .ballisticProtection(8)
                .dexterityPenalty(1)
                .agilityPenalty(2)
                .initiative(3)
                .bookPage("p.142")
                .createdAt(now)
                .updatedAt(now)
                .build();

        // When
        CharacterEquipmentResponse result = CharacterEquipmentDtoBuilder.from(entity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("equipment-id");
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getName()).isEqualTo("Kevlar Vest");
        assertThat(result.getQuantity()).isEqualTo(1);
        assertThat(result.getClassification()).isEqualTo("Armor");
        assertThat(result.getDescription()).isEqualTo("Bulletproof vest providing ballistic protection");
        assertThat(result.getKineticProtection()).isEqualTo(5);
        assertThat(result.getBallisticProtection()).isEqualTo(8);
        assertThat(result.getDexterityPenalty()).isEqualTo(1);
        assertThat(result.getAgilityPenalty()).isEqualTo(2);
        assertThat(result.getInitiative()).isEqualTo(3);
        assertThat(result.getBookPage()).isEqualTo("p.142");
        assertThat(result.getCreatedAt()).isEqualTo(now);
        assertThat(result.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Should build CharacterEquipmentResponse from EquipmentEntity with null values")
    void shouldBuildFromEquipmentEntityWithNullValues() {
        // Given
        User user = User.builder()
                .id("user-id")
                .name("Test User")
                .email("test@example.com")
                .build();

        CharacterEntity character = CharacterEntity.builder()
                .id("character-id")
                .name("Test Character")
                .controlUser(user)
                .build();

        EquipmentEntity entity = EquipmentEntity.builder()
                .id("equipment-id")
                .character(character)
                .name(null)
                .quantity(null)
                .classification(null)
                .description(null)
                .kineticProtection(null)
                .ballisticProtection(null)
                .dexterityPenalty(null)
                .agilityPenalty(null)
                .initiative(null)
                .bookPage(null)
                .createdAt(null)
                .updatedAt(null)
                .build();

        // When
        CharacterEquipmentResponse result = CharacterEquipmentDtoBuilder.from(entity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("equipment-id");
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getName()).isNull();
        assertThat(result.getQuantity()).isNull();
        assertThat(result.getClassification()).isNull();
        assertThat(result.getDescription()).isNull();
        assertThat(result.getKineticProtection()).isNull();
        assertThat(result.getBallisticProtection()).isNull();
        assertThat(result.getDexterityPenalty()).isNull();
        assertThat(result.getAgilityPenalty()).isNull();
        assertThat(result.getInitiative()).isNull();
        assertThat(result.getBookPage()).isNull();
        assertThat(result.getCreatedAt()).isNull();
        assertThat(result.getUpdatedAt()).isNull();
    }

    @Test
    @DisplayName("Should build CharacterEquipmentResponse from EquipmentEntity with empty strings")
    void shouldBuildFromEquipmentEntityWithEmptyStrings() {
        // Given
        User user = User.builder()
                .id("user-id")
                .name("Test User")
                .email("test@example.com")
                .build();

        CharacterEntity character = CharacterEntity.builder()
                .id("character-id")
                .name("Test Character")
                .controlUser(user)
                .build();

        Instant now = Instant.now();
        EquipmentEntity entity = EquipmentEntity.builder()
                .id("equipment-id")
                .character(character)
                .name("")
                .quantity(0)
                .classification("")
                .description("")
                .kineticProtection(0)
                .ballisticProtection(0)
                .dexterityPenalty(0)
                .agilityPenalty(0)
                .initiative(0)
                .bookPage("")
                .createdAt(now)
                .updatedAt(now)
                .build();

        // When
        CharacterEquipmentResponse result = CharacterEquipmentDtoBuilder.from(entity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("equipment-id");
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getName()).isEmpty();
        assertThat(result.getQuantity()).isEqualTo(0);
        assertThat(result.getClassification()).isEmpty();
        assertThat(result.getDescription()).isEmpty();
        assertThat(result.getKineticProtection()).isEqualTo(0);
        assertThat(result.getBallisticProtection()).isEqualTo(0);
        assertThat(result.getDexterityPenalty()).isEqualTo(0);
        assertThat(result.getAgilityPenalty()).isEqualTo(0);
        assertThat(result.getInitiative()).isEqualTo(0);
        assertThat(result.getBookPage()).isEmpty();
        assertThat(result.getCreatedAt()).isEqualTo(now);
        assertThat(result.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Should build CharacterEquipmentResponse from EquipmentEntity with maximum values")
    void shouldBuildFromEquipmentEntityWithMaximumValues() {
        // Given
        User user = User.builder()
                .id("user-id")
                .name("Test User")
                .email("test@example.com")
                .build();

        CharacterEntity character = CharacterEntity.builder()
                .id("character-id")
                .name("Test Character")
                .controlUser(user)
                .build();

        String longText = "A".repeat(1000); // Very long text
        Instant now = Instant.now();
        EquipmentEntity entity = EquipmentEntity.builder()
                .id("equipment-id")
                .character(character)
                .name(longText)
                .quantity(Integer.MAX_VALUE)
                .classification(longText)
                .description(longText)
                .kineticProtection(Integer.MAX_VALUE)
                .ballisticProtection(Integer.MAX_VALUE)
                .dexterityPenalty(Integer.MAX_VALUE)
                .agilityPenalty(Integer.MAX_VALUE)
                .initiative(Integer.MAX_VALUE)
                .bookPage(longText)
                .createdAt(now)
                .updatedAt(now)
                .build();

        // When
        CharacterEquipmentResponse result = CharacterEquipmentDtoBuilder.from(entity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("equipment-id");
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getName()).isEqualTo(longText);
        assertThat(result.getQuantity()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getClassification()).isEqualTo(longText);
        assertThat(result.getDescription()).isEqualTo(longText);
        assertThat(result.getKineticProtection()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getBallisticProtection()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getDexterityPenalty()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getAgilityPenalty()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getInitiative()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getBookPage()).isEqualTo(longText);
        assertThat(result.getCreatedAt()).isEqualTo(now);
        assertThat(result.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Should build CharacterEquipmentResponse from EquipmentEntity with Unicode characters")
    void shouldBuildFromEquipmentEntityWithUnicodeCharacters() {
        // Given
        User user = User.builder()
                .id("user-id")
                .name("Test User")
                .email("test@example.com")
                .build();

        CharacterEntity character = CharacterEntity.builder()
                .id("character-id")
                .name("Test Character")
                .controlUser(user)
                .build();

        Instant now = Instant.now();
        EquipmentEntity entity = EquipmentEntity.builder()
                .id("equipment-id")
                .character(character)
                .name("Armadura Samurai 鎧 ⚔️")
                .quantity(1)
                .classification("Armadura Tradicional")
                .description("Armadura tradicional japonesa forjada pelos mestres artesãos 職人 com técnicas ancestrais")
                .kineticProtection(10)
                .ballisticProtection(5)
                .dexterityPenalty(3)
                .agilityPenalty(2)
                .initiative(1)
                .bookPage("página 武器防具 📖")
                .createdAt(now)
                .updatedAt(now)
                .build();

        // When
        CharacterEquipmentResponse result = CharacterEquipmentDtoBuilder.from(entity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("equipment-id");
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getName()).isEqualTo("Armadura Samurai 鎧 ⚔️");
        assertThat(result.getQuantity()).isEqualTo(1);
        assertThat(result.getClassification()).isEqualTo("Armadura Tradicional");
        assertThat(result.getDescription()).isEqualTo("Armadura tradicional japonesa forjada pelos mestres artesãos 職人 com técnicas ancestrais");
        assertThat(result.getKineticProtection()).isEqualTo(10);
        assertThat(result.getBallisticProtection()).isEqualTo(5);
        assertThat(result.getDexterityPenalty()).isEqualTo(3);
        assertThat(result.getAgilityPenalty()).isEqualTo(2);
        assertThat(result.getInitiative()).isEqualTo(1);
        assertThat(result.getBookPage()).isEqualTo("página 武器防具 📖");
        assertThat(result.getCreatedAt()).isEqualTo(now);
        assertThat(result.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Should build CharacterEquipmentResponse from EquipmentEntity with negative values")
    void shouldBuildFromEquipmentEntityWithNegativeValues() {
        // Given
        User user = User.builder()
                .id("user-id")
                .name("Test User")
                .email("test@example.com")
                .build();

        CharacterEntity character = CharacterEntity.builder()
                .id("character-id")
                .name("Test Character")
                .controlUser(user)
                .build();

        Instant now = Instant.now();
        EquipmentEntity entity = EquipmentEntity.builder()
                .id("equipment-id")
                .character(character)
                .name("Cursed Equipment")
                .quantity(-1) // Negative quantity for cursed items
                .classification("Cursed")
                .description("Equipment with negative effects")
                .kineticProtection(-2) // Negative protection (vulnerability)
                .ballisticProtection(-3)
                .dexterityPenalty(-1) // Negative penalty (bonus)
                .agilityPenalty(-2)
                .initiative(-5) // Negative initiative modifier
                .bookPage("p.666")
                .createdAt(now)
                .updatedAt(now)
                .build();

        // When
        CharacterEquipmentResponse result = CharacterEquipmentDtoBuilder.from(entity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("equipment-id");
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getName()).isEqualTo("Cursed Equipment");
        assertThat(result.getQuantity()).isEqualTo(-1);
        assertThat(result.getClassification()).isEqualTo("Cursed");
        assertThat(result.getDescription()).isEqualTo("Equipment with negative effects");
        assertThat(result.getKineticProtection()).isEqualTo(-2);
        assertThat(result.getBallisticProtection()).isEqualTo(-3);
        assertThat(result.getDexterityPenalty()).isEqualTo(-1);
        assertThat(result.getAgilityPenalty()).isEqualTo(-2);
        assertThat(result.getInitiative()).isEqualTo(-5);
        assertThat(result.getBookPage()).isEqualTo("p.666");
        assertThat(result.getCreatedAt()).isEqualTo(now);
        assertThat(result.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Should build CharacterEquipmentResponse from EquipmentEntity with mixed values")
    void shouldBuildFromEquipmentEntityWithMixedValues() {
        // Given
        User user = User.builder()
                .id("user-id")
                .name("Test User")
                .email("test@example.com")
                .build();

        CharacterEntity character = CharacterEntity.builder()
                .id("character-id")
                .name("Test Character")
                .controlUser(user)
                .build();

        Instant now = Instant.now();
        EquipmentEntity entity = EquipmentEntity.builder()
                .id("equipment-id")
                .character(character)
                .name("Tactical Gear")
                .quantity(5)
                .classification(null) // Mixed: some null, some not
                .description("Advanced tactical equipment")
                .kineticProtection(null)
                .ballisticProtection(7)
                .dexterityPenalty(0)
                .agilityPenalty(null)
                .initiative(2)
                .bookPage("")
                .createdAt(now)
                .updatedAt(null)
                .build();

        // When
        CharacterEquipmentResponse result = CharacterEquipmentDtoBuilder.from(entity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("equipment-id");
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getName()).isEqualTo("Tactical Gear");
        assertThat(result.getQuantity()).isEqualTo(5);
        assertThat(result.getClassification()).isNull();
        assertThat(result.getDescription()).isEqualTo("Advanced tactical equipment");
        assertThat(result.getKineticProtection()).isNull();
        assertThat(result.getBallisticProtection()).isEqualTo(7);
        assertThat(result.getDexterityPenalty()).isEqualTo(0);
        assertThat(result.getAgilityPenalty()).isNull();
        assertThat(result.getInitiative()).isEqualTo(2);
        assertThat(result.getBookPage()).isEmpty();
        assertThat(result.getCreatedAt()).isEqualTo(now);
        assertThat(result.getUpdatedAt()).isNull();
    }

    @Test
    @DisplayName("Should not allow instantiation")
    void shouldNotAllowInstantiation() throws Exception {
        // Given
        Constructor<CharacterEquipmentDtoBuilder> constructor = CharacterEquipmentDtoBuilder.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // When & Then
        assertThrows(InvocationTargetException.class, () -> {
            constructor.newInstance();
        });
    }
}