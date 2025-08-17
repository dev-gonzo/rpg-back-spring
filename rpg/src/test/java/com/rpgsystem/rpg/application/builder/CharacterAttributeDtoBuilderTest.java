package com.rpgsystem.rpg.application.builder;

import com.rpgsystem.rpg.api.dto.character.CharacterAttributeResponse;
import com.rpgsystem.rpg.domain.entity.AttributeEntity;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("CharacterAttributeDtoBuilder Tests")
class CharacterAttributeDtoBuilderTest {

    @Test
    @DisplayName("Should build CharacterAttributeResponse from AttributeEntity with all fields")
    void shouldBuildCharacterAttributeResponseFromAttributeEntityWithAllFields() {
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

        AttributeEntity attributeEntity = AttributeEntity.builder()
                .id("attribute-id")
                .character(character)
                .con(15)
                .fr(14)
                .dex(13)
                .agi(12)
                .intel(16)
                .will(11)
                .per(10)
                .car(9)
                .conMod(2)
                .frMod(1)
                .dexMod(0)
                .agiMod(-1)
                .intMod(3)
                .willMod(-2)
                .perMod(-3)
                .carMod(-4)
                .build();

        // When
        CharacterAttributeResponse result = CharacterAttributeDtoBuilder.from(attributeEntity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getCon()).isEqualTo(15);
        assertThat(result.getFr()).isEqualTo(14);
        assertThat(result.getDex()).isEqualTo(13);
        assertThat(result.getAgi()).isEqualTo(12);
        assertThat(result.getIntel()).isEqualTo(16);
        assertThat(result.getWill()).isEqualTo(11);
        assertThat(result.getPer()).isEqualTo(10);
        assertThat(result.getCar()).isEqualTo(9);
        assertThat(result.getConMod()).isEqualTo(2);
        assertThat(result.getFrMod()).isEqualTo(1);
        assertThat(result.getDexMod()).isEqualTo(0);
        assertThat(result.getAgiMod()).isEqualTo(-1);
        assertThat(result.getIntMod()).isEqualTo(3);
        assertThat(result.getWillMod()).isEqualTo(-2);
        assertThat(result.getPerMod()).isEqualTo(-3);
        assertThat(result.getCarMod()).isEqualTo(-4);
    }

    @Test
    @DisplayName("Should build CharacterAttributeResponse with zero values")
    void shouldBuildCharacterAttributeResponseWithZeroValues() {
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

        AttributeEntity attributeEntity = AttributeEntity.builder()
                .id("attribute-id")
                .character(character)
                .con(0)
                .fr(0)
                .dex(0)
                .agi(0)
                .intel(0)
                .will(0)
                .per(0)
                .car(0)
                .conMod(0)
                .frMod(0)
                .dexMod(0)
                .agiMod(0)
                .intMod(0)
                .willMod(0)
                .perMod(0)
                .carMod(0)
                .build();

        // When
        CharacterAttributeResponse result = CharacterAttributeDtoBuilder.from(attributeEntity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCharacterId()).isEqualTo("character-id");
        assertThat(result.getCon()).isEqualTo(0);
        assertThat(result.getFr()).isEqualTo(0);
        assertThat(result.getDex()).isEqualTo(0);
        assertThat(result.getAgi()).isEqualTo(0);
        assertThat(result.getIntel()).isEqualTo(0);
        assertThat(result.getWill()).isEqualTo(0);
        assertThat(result.getPer()).isEqualTo(0);
        assertThat(result.getCar()).isEqualTo(0);
        assertThat(result.getConMod()).isEqualTo(0);
        assertThat(result.getFrMod()).isEqualTo(0);
        assertThat(result.getDexMod()).isEqualTo(0);
        assertThat(result.getAgiMod()).isEqualTo(0);
        assertThat(result.getIntMod()).isEqualTo(0);
        assertThat(result.getWillMod()).isEqualTo(0);
        assertThat(result.getPerMod()).isEqualTo(0);
        assertThat(result.getCarMod()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should build CharacterAttributeResponse with maximum values")
    void shouldBuildCharacterAttributeResponseWithMaximumValues() {
        // Given
        User user = User.builder()
                .id("user-id")
                .name("Test User")
                .email("test@example.com")
                .build();

        CharacterEntity character = CharacterEntity.builder()
                .id("character-id-max")
                .name("Max Character")
                .controlUser(user)
                .build();

        AttributeEntity attributeEntity = AttributeEntity.builder()
                .id("attribute-id-max")
                .character(character)
                .con(Integer.MAX_VALUE)
                .fr(Integer.MAX_VALUE)
                .dex(Integer.MAX_VALUE)
                .agi(Integer.MAX_VALUE)
                .intel(Integer.MAX_VALUE)
                .will(Integer.MAX_VALUE)
                .per(Integer.MAX_VALUE)
                .car(Integer.MAX_VALUE)
                .conMod(Integer.MAX_VALUE)
                .frMod(Integer.MAX_VALUE)
                .dexMod(Integer.MAX_VALUE)
                .agiMod(Integer.MAX_VALUE)
                .intMod(Integer.MAX_VALUE)
                .willMod(Integer.MAX_VALUE)
                .perMod(Integer.MAX_VALUE)
                .carMod(Integer.MAX_VALUE)
                .build();

        // When
        CharacterAttributeResponse result = CharacterAttributeDtoBuilder.from(attributeEntity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getCharacterId()).isEqualTo("character-id-max");
        assertThat(result.getCon()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getFr()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getDex()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getAgi()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getIntel()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getWill()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getPer()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getCar()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getConMod()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getFrMod()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getDexMod()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getAgiMod()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getIntMod()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getWillMod()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getPerMod()).isEqualTo(Integer.MAX_VALUE);
        assertThat(result.getCarMod()).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    @DisplayName("Should throw NullPointerException when AttributeEntity is null")
    void shouldThrowNullPointerExceptionWhenAttributeEntityIsNull() {
        // Given
        AttributeEntity nullEntity = null;

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            CharacterAttributeDtoBuilder.from(nullEntity);
        });
    }

    @Test
    @DisplayName("Should throw NullPointerException when character is null")
    void shouldThrowNullPointerExceptionWhenCharacterIsNull() {
        // Given
        AttributeEntity attributeEntity = AttributeEntity.builder()
                .id("attribute-no-char")
                .character(null)
                .con(10)
                .fr(10)
                .dex(10)
                .agi(10)
                .intel(10)
                .will(10)
                .per(10)
                .car(10)
                .conMod(0)
                .frMod(0)
                .dexMod(0)
                .agiMod(0)
                .intMod(0)
                .willMod(0)
                .perMod(0)
                .carMod(0)
                .build();

        // When & Then
        assertThrows(NullPointerException.class, () -> {
            CharacterAttributeDtoBuilder.from(attributeEntity);
        });
    }

    @Test
    @DisplayName("Should not allow instantiation of utility class")
    void shouldNotAllowInstantiationOfUtilityClass() throws NoSuchMethodException {
        // Given
        Constructor<CharacterAttributeDtoBuilder> constructor = CharacterAttributeDtoBuilder.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // When & Then
        assertThrows(InvocationTargetException.class, constructor::newInstance);
    }
}