package com.rpgsystem.rpg.application.builder;

import com.rpgsystem.rpg.api.dto.character.CharacterHomeDto;
import com.rpgsystem.rpg.api.dto.UserSimpleResponse;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@DisplayName("CharacterHomeDtoBuilder Tests")
class CharacterHomeDtoBuilderTest {

    @Test
    @DisplayName("Should build CharacterHomeDto from CharacterEntity with all fields")
    void shouldBuildCharacterHomeDtoFromCharacterEntityWithAllFields() {
        // Given
        User controlUser = User.builder()
                .id("user-123")
                .name("Test User")
                .email("test@example.com")
                .build();

        UserSimpleResponse userResponse = UserSimpleResponse.builder()
                .id("user-123")
                .name("Test User")
                .build();

        CharacterEntity character = CharacterEntity.builder()
                .id("char-123")
                .name("Test Character")
                .age(25)
                .apparentAge(23)
                .profession("Warrior")
                .hitPoints(120)
                .currentHitPoints(100)
                .initiative(18)
                .currentInitiative(15)
                .heroPoints(8)
                .currentHeroPoints(6)
                .magicPoints(30)
                .currentMagicPoints(25)
                .faithPoints(20)
                .currentFaithPoints(18)
                .protectionIndex(7)
                .currentProtectionIndex(5)
                .image("character-image.jpg")
                .controlUser(controlUser)
                .edit(true)
                .build();

        try (MockedStatic<UserSimpleDtoBuilder> mockedBuilder = mockStatic(UserSimpleDtoBuilder.class)) {
            mockedBuilder.when(() -> UserSimpleDtoBuilder.build(any(User.class)))
                    .thenReturn(userResponse);

            // When
            CharacterHomeDto result = CharacterHomeDtoBuilder.from(character);

            // Then
            assertNotNull(result);
            assertEquals("char-123", result.getId());
            assertEquals("Test Character", result.getName());
            assertEquals(25, result.getAge());
            assertEquals(23, result.getApparentAge());
            assertEquals("Warrior", result.getProfession());
            assertEquals(120, result.getHitPoints());
            assertEquals(100, result.getCurrentHitPoints());
            assertEquals(18, result.getInitiative());
            assertEquals(15, result.getCurrentInitiative());
            assertEquals(8, result.getHeroPoints());
            assertEquals(6, result.getCurrentHeroPoints());
            assertEquals(30, result.getMagicPoints());
            assertEquals(25, result.getCurrentMagicPoints());
            assertEquals(20, result.getFaithPoints());
            assertEquals(18, result.getCurrentFaithPoints());
            assertEquals(7, result.getProtectionIndex());
            assertEquals(5, result.getCurrentProtectionIndex());
            assertEquals("character-image.jpg", result.getImage());
            assertEquals(userResponse, result.getControlUser());
            assertTrue(result.isEdit());
        }
    }

    @Test
    @DisplayName("Should build CharacterHomeDto from CharacterEntity with null values")
    void shouldBuildCharacterHomeDtoFromCharacterEntityWithNullValues() {
        // Given
        CharacterEntity character = CharacterEntity.builder()
                .id("char-null")
                .name(null)
                .age(null)
                .apparentAge(null)
                .profession(null)
                .hitPoints(null)
                .currentHitPoints(null)
                .initiative(null)
                .currentInitiative(null)
                .heroPoints(null)
                .currentHeroPoints(null)
                .magicPoints(null)
                .currentMagicPoints(null)
                .faithPoints(null)
                .currentFaithPoints(null)
                .protectionIndex(null)
                .currentProtectionIndex(null)
                .image(null)
                .controlUser(null)
                .edit(false)
                .build();

        try (MockedStatic<UserSimpleDtoBuilder> mockedBuilder = mockStatic(UserSimpleDtoBuilder.class)) {
            mockedBuilder.when(() -> UserSimpleDtoBuilder.build(null))
                    .thenReturn(null);

            // When
            CharacterHomeDto result = CharacterHomeDtoBuilder.from(character);

            // Then
            assertNotNull(result);
            assertEquals("char-null", result.getId());
            assertNull(result.getName());
            assertNull(result.getAge());
            assertNull(result.getApparentAge());
            assertNull(result.getProfession());
            assertNull(result.getHitPoints());
            assertNull(result.getCurrentHitPoints());
            assertNull(result.getInitiative());
            assertNull(result.getCurrentInitiative());
            assertNull(result.getHeroPoints());
            assertNull(result.getCurrentHeroPoints());
            assertNull(result.getMagicPoints());
            assertNull(result.getCurrentMagicPoints());
            assertNull(result.getFaithPoints());
            assertNull(result.getCurrentFaithPoints());
            assertNull(result.getProtectionIndex());
            assertNull(result.getCurrentProtectionIndex());
            assertNull(result.getImage());
            assertNull(result.getControlUser());
            assertFalse(result.isEdit());
        }
    }

    @Test
    @DisplayName("Should not allow instantiation")
    void shouldNotAllowInstantiation() throws Exception {
        // Given
        Constructor<CharacterHomeDtoBuilder> constructor = CharacterHomeDtoBuilder.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // When & Then
        assertThrows(InvocationTargetException.class, () -> {
            constructor.newInstance();
        });
    }

    @Test
    @DisplayName("Should build CharacterHomeDto with zero values")
    void shouldBuildCharacterHomeDtoWithZeroValues() {
        // Given
        User controlUser = User.builder()
                .id("user-zero")
                .name("Zero User")
                .email("zero@example.com")
                .build();

        UserSimpleResponse userResponse = UserSimpleResponse.builder()
                .id("user-zero")
                .name("Zero User")
                .build();

        CharacterEntity character = CharacterEntity.builder()
                .id("char-zero")
                .name("Zero Character")
                .age(0)
                .apparentAge(0)
                .profession("Novice")
                .hitPoints(0)
                .currentHitPoints(0)
                .initiative(0)
                .currentInitiative(0)
                .heroPoints(0)
                .currentHeroPoints(0)
                .magicPoints(0)
                .currentMagicPoints(0)
                .faithPoints(0)
                .currentFaithPoints(0)
                .protectionIndex(0)
                .currentProtectionIndex(0)
                .image("")
                .controlUser(controlUser)
                .edit(false)
                .build();

        try (MockedStatic<UserSimpleDtoBuilder> mockedBuilder = mockStatic(UserSimpleDtoBuilder.class)) {
            mockedBuilder.when(() -> UserSimpleDtoBuilder.build(any(User.class)))
                    .thenReturn(userResponse);

            // When
            CharacterHomeDto result = CharacterHomeDtoBuilder.from(character);

            // Then
            assertNotNull(result);
            assertEquals("char-zero", result.getId());
            assertEquals("Zero Character", result.getName());
            assertEquals(0, result.getAge());
            assertEquals(0, result.getApparentAge());
            assertEquals("Novice", result.getProfession());
            assertEquals(0, result.getHitPoints());
            assertEquals(0, result.getCurrentHitPoints());
            assertEquals(0, result.getInitiative());
            assertEquals(0, result.getCurrentInitiative());
            assertEquals(0, result.getHeroPoints());
            assertEquals(0, result.getCurrentHeroPoints());
            assertEquals(0, result.getMagicPoints());
            assertEquals(0, result.getCurrentMagicPoints());
            assertEquals(0, result.getFaithPoints());
            assertEquals(0, result.getCurrentFaithPoints());
            assertEquals(0, result.getProtectionIndex());
            assertEquals(0, result.getCurrentProtectionIndex());
            assertEquals("", result.getImage());
            assertEquals(userResponse, result.getControlUser());
            assertFalse(result.isEdit());
        }
    }

    @Test
    @DisplayName("Should build CharacterHomeDto with maximum values")
    void shouldBuildCharacterHomeDtoWithMaximumValues() {
        // Given
        User controlUser = User.builder()
                .id("user-max")
                .name("Max User")
                .email("max@example.com")
                .build();

        UserSimpleResponse userResponse = UserSimpleResponse.builder()
                .id("user-max")
                .name("Max User")
                .build();

        CharacterEntity character = CharacterEntity.builder()
                .id("char-max")
                .name("Maximum Character")
                .age(Integer.MAX_VALUE)
                .apparentAge(Integer.MAX_VALUE)
                .profession("Legendary Master")
                .hitPoints(Integer.MAX_VALUE)
                .currentHitPoints(Integer.MAX_VALUE)
                .initiative(Integer.MAX_VALUE)
                .currentInitiative(Integer.MAX_VALUE)
                .heroPoints(Integer.MAX_VALUE)
                .currentHeroPoints(Integer.MAX_VALUE)
                .magicPoints(Integer.MAX_VALUE)
                .currentMagicPoints(Integer.MAX_VALUE)
                .faithPoints(Integer.MAX_VALUE)
                .currentFaithPoints(Integer.MAX_VALUE)
                .protectionIndex(Integer.MAX_VALUE)
                .currentProtectionIndex(Integer.MAX_VALUE)
                .image("very-long-image-name-with-special-characters-@#$%^&*()_+.jpg")
                .controlUser(controlUser)
                .edit(true)
                .build();

        try (MockedStatic<UserSimpleDtoBuilder> mockedBuilder = mockStatic(UserSimpleDtoBuilder.class)) {
            mockedBuilder.when(() -> UserSimpleDtoBuilder.build(any(User.class)))
                    .thenReturn(userResponse);

            // When
            CharacterHomeDto result = CharacterHomeDtoBuilder.from(character);

            // Then
            assertNotNull(result);
            assertEquals("char-max", result.getId());
            assertEquals("Maximum Character", result.getName());
            assertEquals(Integer.MAX_VALUE, result.getAge());
            assertEquals(Integer.MAX_VALUE, result.getApparentAge());
            assertEquals("Legendary Master", result.getProfession());
            assertEquals(Integer.MAX_VALUE, result.getHitPoints());
            assertEquals(Integer.MAX_VALUE, result.getCurrentHitPoints());
            assertEquals(Integer.MAX_VALUE, result.getInitiative());
            assertEquals(Integer.MAX_VALUE, result.getCurrentInitiative());
            assertEquals(Integer.MAX_VALUE, result.getHeroPoints());
            assertEquals(Integer.MAX_VALUE, result.getCurrentHeroPoints());
            assertEquals(Integer.MAX_VALUE, result.getMagicPoints());
            assertEquals(Integer.MAX_VALUE, result.getCurrentMagicPoints());
            assertEquals(Integer.MAX_VALUE, result.getFaithPoints());
            assertEquals(Integer.MAX_VALUE, result.getCurrentFaithPoints());
            assertEquals(Integer.MAX_VALUE, result.getProtectionIndex());
            assertEquals(Integer.MAX_VALUE, result.getCurrentProtectionIndex());
            assertEquals("very-long-image-name-with-special-characters-@#$%^&*()_+.jpg", result.getImage());
            assertEquals(userResponse, result.getControlUser());
            assertTrue(result.isEdit());
        }
    }

    @Test
    @DisplayName("Should build CharacterHomeDto with Unicode characters")
    void shouldBuildCharacterHomeDtoWithUnicodeCharacters() {
        // Given
        User controlUser = User.builder()
                .id("user-unicode")
                .name("José María Ñoël 龍王")
                .email("unicode@example.com")
                .build();

        UserSimpleResponse userResponse = UserSimpleResponse.builder()
                .id("user-unicode")
                .name("José María Ñoël 龍王")
                .build();

        CharacterEntity character = CharacterEntity.builder()
                .id("char-unicode")
                .name("Personagem Élfico 魔法師 🧙‍♂️")
                .age(150)
                .apparentAge(25)
                .profession("Mágico Élfico 🔮")
                .hitPoints(80)
                .currentHitPoints(75)
                .initiative(20)
                .currentInitiative(18)
                .heroPoints(5)
                .currentHeroPoints(4)
                .magicPoints(100)
                .currentMagicPoints(90)
                .faithPoints(50)
                .currentFaithPoints(45)
                .protectionIndex(3)
                .currentProtectionIndex(2)
                .image("élfico_龍王_🧙‍♂️.jpg")
                .controlUser(controlUser)
                .edit(true)
                .build();

        try (MockedStatic<UserSimpleDtoBuilder> mockedBuilder = mockStatic(UserSimpleDtoBuilder.class)) {
            mockedBuilder.when(() -> UserSimpleDtoBuilder.build(any(User.class)))
                    .thenReturn(userResponse);

            // When
            CharacterHomeDto result = CharacterHomeDtoBuilder.from(character);

            // Then
            assertNotNull(result);
            assertEquals("char-unicode", result.getId());
            assertEquals("Personagem Élfico 魔法師 🧙‍♂️", result.getName());
            assertTrue(result.getName().contains("魔法師"));
            assertTrue(result.getName().contains("🧙‍♂️"));
            assertEquals(150, result.getAge());
            assertEquals(25, result.getApparentAge());
            assertEquals("Mágico Élfico 🔮", result.getProfession());
            assertTrue(result.getProfession().contains("🔮"));
            assertEquals("élfico_龍王_🧙‍♂️.jpg", result.getImage());
            assertTrue(result.getImage().contains("龍王"));
            assertEquals(userResponse, result.getControlUser());
            assertTrue(result.isEdit());
        }
    }
}