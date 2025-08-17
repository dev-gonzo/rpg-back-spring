package com.rpgsystem.rpg.application.builder;

import com.rpgsystem.rpg.api.dto.character.CharacterInfoResponse;
import com.rpgsystem.rpg.domain.character.CharacterCharacterInfo;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.character.valueObject.CharacterName;
import com.rpgsystem.rpg.domain.character.valueObject.Height;
import com.rpgsystem.rpg.domain.character.valueObject.Weight;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CharacterInfoDtoBuilder Tests")
class CharacterInfoDtoBuilderTest {

    @Test
    @DisplayName("Should build CharacterInfoResponse from CharacterEntity with all fields")
    void shouldBuildCharacterInfoResponseFromCharacterEntityWithAllFields() {
        // Given
        String characterId = "char-123";
        String name = "Test Character";
        String profession = "Warrior";
        LocalDate birthDate = LocalDate.of(1990, 5, 15);
        String birthPlace = "Test City";
        String gender = "Male";
        Integer age = 33;
        Integer apparentAge = 30;
        Integer heightCm = 180;
        Integer weightKg = 75;
        String religion = "Test Religion";

        CharacterEntity character = CharacterEntity.builder()
                .id(characterId)
                .name(name)
                .profession(profession)
                .birthDate(birthDate)
                .birthPlace(birthPlace)
                .gender(gender)
                .age(age)
                .apparentAge(apparentAge)
                .heightCm(heightCm)
                .weightKg(weightKg)
                .religion(religion)
                .build();

        // When
        CharacterInfoResponse response = CharacterInfoDtoBuilder.from(character);

        // Then
        assertNotNull(response);
        assertEquals(characterId, response.getId());
        assertEquals(name, response.getName());
        assertEquals(profession, response.getProfession());
        assertEquals(birthDate, response.getBirthDate());
        assertEquals(birthPlace, response.getBirthPlace());
        assertEquals(gender, response.getGender());
        assertEquals(age, response.getAge());
        assertEquals(apparentAge, response.getApparentAge());
        assertEquals(heightCm, response.getHeightCm());
        assertEquals(weightKg, response.getWeightKg());
        assertEquals(religion, response.getReligion());
    }

    @Test
    @DisplayName("Should build CharacterInfoResponse from CharacterEntity with null fields")
    void shouldBuildCharacterInfoResponseFromCharacterEntityWithNullFields() {
        // Given
        CharacterEntity character = CharacterEntity.builder()
                .id(null)
                .name(null)
                .profession(null)
                .birthDate(null)
                .birthPlace(null)
                .gender(null)
                .age(null)
                .apparentAge(null)
                .heightCm(null)
                .weightKg(null)
                .religion(null)
                .build();

        // When
        CharacterInfoResponse response = CharacterInfoDtoBuilder.from(character);

        // Then
        assertNotNull(response);
        assertNull(response.getId());
        assertNull(response.getName());
        assertNull(response.getProfession());
        assertNull(response.getBirthDate());
        assertNull(response.getBirthPlace());
        assertNull(response.getGender());
        assertNull(response.getAge());
        assertNull(response.getApparentAge());
        assertNull(response.getHeightCm());
        assertNull(response.getWeightKg());
        assertNull(response.getReligion());
    }

    @Test
    @DisplayName("Should build CharacterInfoResponse from CharacterCharacterInfo with id")
    void shouldBuildCharacterInfoResponseFromCharacterCharacterInfoWithId() {
        // Given
        String characterId = "char-456";
        String name = "Domain Character";
        String profession = "Mage";
        LocalDate birthDate = LocalDate.of(1985, 10, 20);
        String birthPlace = "Magic City";
        String gender = "Female";
        Integer age = 38;
        Integer apparentAge = 35;
        Integer heightCm = 165;
        Integer weightKg = 60;
        String religion = "Magic Religion";

        CharacterCharacterInfo characterInfo = new CharacterCharacterInfo(
                null,
                CharacterName.of(name),
                profession,
                birthDate,
                birthPlace,
                gender,
                age,
                apparentAge,
                Height.of(heightCm),
                Weight.of(weightKg),
                religion
        );

        // When
        CharacterInfoResponse response = CharacterInfoDtoBuilder.from(characterInfo, characterId);

        // Then
        assertNotNull(response);
        assertEquals(characterId, response.getId());
        assertEquals(name, response.getName());
        assertEquals(profession, response.getProfession());
        assertEquals(birthDate, response.getBirthDate());
        assertEquals(birthPlace, response.getBirthPlace());
        assertEquals(gender, response.getGender());
        assertEquals(age, response.getAge());
        assertEquals(apparentAge, response.getApparentAge());
        assertEquals(heightCm, response.getHeightCm());
        assertEquals(weightKg, response.getWeightKg());
        assertEquals(religion, response.getReligion());
    }

    @Test
    @DisplayName("Should build CharacterInfoResponse from CharacterCharacterInfo with null fields")
    void shouldBuildCharacterInfoResponseFromCharacterCharacterInfoWithNullFields() {
        // Given
        String characterId = "char-null";
        
        CharacterCharacterInfo characterInfo = new CharacterCharacterInfo(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        // When
        CharacterInfoResponse response = CharacterInfoDtoBuilder.from(characterInfo, characterId);

        // Then
        assertNotNull(response);
        assertEquals(characterId, response.getId());
        assertNull(response.getName());
        assertNull(response.getProfession());
        assertNull(response.getBirthDate());
        assertNull(response.getBirthPlace());
        assertNull(response.getGender());
        assertNull(response.getAge());
        assertNull(response.getApparentAge());
        assertNull(response.getHeightCm());
        assertNull(response.getWeightKg());
        assertNull(response.getReligion());
    }

    @Test
    @DisplayName("Should build CharacterInfoResponse from CharacterCharacterInfo without id")
    void shouldBuildCharacterInfoResponseFromCharacterCharacterInfoWithoutId() {
        // Given
        String name = "No ID Character";
        String profession = "Rogue";
        LocalDate birthDate = LocalDate.of(1995, 3, 8);
        String birthPlace = "Shadow City";
        String gender = "Non-binary";
        Integer age = 28;
        Integer apparentAge = 25;
        Integer heightCm = 170;
        Integer weightKg = 65;
        String religion = "Shadow Religion";

        CharacterCharacterInfo characterInfo = new CharacterCharacterInfo(
                null,
                CharacterName.of(name),
                profession,
                birthDate,
                birthPlace,
                gender,
                age,
                apparentAge,
                Height.of(heightCm),
                Weight.of(weightKg),
                religion
        );

        // When
        CharacterInfoResponse response = CharacterInfoDtoBuilder.from(characterInfo);

        // Then
        assertNotNull(response);
        assertNull(response.getId()); // ID não é definido neste método
        assertEquals(name, response.getName());
        assertEquals(profession, response.getProfession());
        assertEquals(birthDate, response.getBirthDate());
        assertEquals(birthPlace, response.getBirthPlace());
        assertEquals(gender, response.getGender());
        assertEquals(age, response.getAge());
        assertEquals(apparentAge, response.getApparentAge());
        assertEquals(heightCm, response.getHeightCm());
        assertEquals(weightKg, response.getWeightKg());
        assertEquals(religion, response.getReligion());
    }

    @Test
    @DisplayName("Should build CharacterInfoResponse from CharacterCharacterInfo without id with null fields")
    void shouldBuildCharacterInfoResponseFromCharacterCharacterInfoWithoutIdWithNullFields() {
        // Given
        CharacterCharacterInfo characterInfo = new CharacterCharacterInfo(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        // When
        CharacterInfoResponse response = CharacterInfoDtoBuilder.from(characterInfo);

        // Then
        assertNotNull(response);
        assertNull(response.getId());
        assertNull(response.getName());
        assertNull(response.getProfession());
        assertNull(response.getBirthDate());
        assertNull(response.getBirthPlace());
        assertNull(response.getGender());
        assertNull(response.getAge());
        assertNull(response.getApparentAge());
        assertNull(response.getHeightCm());
        assertNull(response.getWeightKg());
        assertNull(response.getReligion());
    }

    @Test
    @DisplayName("Should handle edge case values correctly")
    void shouldHandleEdgeCaseValuesCorrectly() {
        // Given
        CharacterEntity character = CharacterEntity.builder()
                .id("")
                .name("")
                .profession("")
                .birthDate(LocalDate.MIN)
                .birthPlace("")
                .gender("")
                .age(0)
                .apparentAge(0)
                .heightCm(0)
                .weightKg(0)
                .religion("")
                .build();

        // When
        CharacterInfoResponse response = CharacterInfoDtoBuilder.from(character);

        // Then
        assertNotNull(response);
        assertEquals("", response.getId());
        assertEquals("", response.getName());
        assertEquals("", response.getProfession());
        assertEquals(LocalDate.MIN, response.getBirthDate());
        assertEquals("", response.getBirthPlace());
        assertEquals("", response.getGender());
        assertEquals(0, response.getAge());
        assertEquals(0, response.getApparentAge());
        assertEquals(0, response.getHeightCm());
        assertEquals(0, response.getWeightKg());
        assertEquals("", response.getReligion());
    }

    @Test
    @DisplayName("Should handle maximum values correctly")
    void shouldHandleMaximumValuesCorrectly() {
        // Given
        CharacterEntity character = CharacterEntity.builder()
                .id("a".repeat(255))
                .name("b".repeat(255))
                .profession("c".repeat(255))
                .birthDate(LocalDate.MAX)
                .birthPlace("d".repeat(255))
                .gender("e".repeat(255))
                .age(Integer.MAX_VALUE)
                .apparentAge(Integer.MAX_VALUE)
                .heightCm(Integer.MAX_VALUE)
                .weightKg(Integer.MAX_VALUE)
                .religion("f".repeat(255))
                .build();

        // When
        CharacterInfoResponse response = CharacterInfoDtoBuilder.from(character);

        // Then
        assertNotNull(response);
        assertEquals("a".repeat(255), response.getId());
        assertEquals("b".repeat(255), response.getName());
        assertEquals("c".repeat(255), response.getProfession());
        assertEquals(LocalDate.MAX, response.getBirthDate());
        assertEquals("d".repeat(255), response.getBirthPlace());
        assertEquals("e".repeat(255), response.getGender());
        assertEquals(Integer.MAX_VALUE, response.getAge());
        assertEquals(Integer.MAX_VALUE, response.getApparentAge());
        assertEquals(Integer.MAX_VALUE, response.getHeightCm());
        assertEquals(Integer.MAX_VALUE, response.getWeightKg());
        assertEquals("f".repeat(255), response.getReligion());
    }

    @Test
    @DisplayName("Should handle null CharacterEntity gracefully")
    void shouldHandleNullCharacterEntityGracefully() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            CharacterInfoDtoBuilder.from((CharacterEntity) null);
        });
    }

    @Test
    @DisplayName("Should handle null CharacterCharacterInfo gracefully")
    void shouldHandleNullCharacterCharacterInfoGracefully() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            CharacterInfoDtoBuilder.from((CharacterCharacterInfo) null);
        });
    }

    @Test
    @DisplayName("Should handle null CharacterCharacterInfo with id gracefully")
    void shouldHandleNullCharacterCharacterInfoWithIdGracefully() {
        // When & Then
        assertThrows(NullPointerException.class, () -> {
            CharacterInfoDtoBuilder.from((CharacterCharacterInfo) null, "test-id");
        });
    }

    @Test
    @DisplayName("Should handle empty string values in CharacterEntity")
    void shouldHandleEmptyStringValuesInCharacterEntity() {
        // Given
        CharacterEntity character = CharacterEntity.builder()
                .id("")
                .name("")
                .profession("")
                .birthPlace("")
                .gender("")
                .religion("")
                .age(0)
                .apparentAge(0)
                .heightCm(0)
                .weightKg(0)
                .build();

        // When
        CharacterInfoResponse response = CharacterInfoDtoBuilder.from(character);

        // Then
        assertNotNull(response);
        assertEquals("", response.getId());
        assertEquals("", response.getName());
        assertEquals("", response.getProfession());
        assertEquals("", response.getBirthPlace());
        assertEquals("", response.getGender());
        assertEquals("", response.getReligion());
        assertEquals(0, response.getAge());
        assertEquals(0, response.getApparentAge());
        assertEquals(0, response.getHeightCm());
        assertEquals(0, response.getWeightKg());
    }

    @Test
    @DisplayName("Should handle CharacterCharacterInfo with null id parameter")
    void shouldHandleCharacterCharacterInfoWithNullIdParameter() {
        // Given
        CharacterCharacterInfo characterInfo = new CharacterCharacterInfo(
                null,
                CharacterName.of("Test Character"),
                "Warrior",
                LocalDate.of(1990, 5, 15),
                "Test City",
                "Male",
                33,
                30,
                Height.of(180),
                Weight.of(75),
                "Test Religion"
        );

        // When
        CharacterInfoResponse response = CharacterInfoDtoBuilder.from(characterInfo, null);

        // Then
        assertNotNull(response);
        assertNull(response.getId());
        assertEquals("Test Character", response.getName());
        assertEquals("Warrior", response.getProfession());
        assertEquals(LocalDate.of(1990, 5, 15), response.getBirthDate());
        assertEquals("Test City", response.getBirthPlace());
        assertEquals("Male", response.getGender());
        assertEquals(33, response.getAge());
        assertEquals(30, response.getApparentAge());
        assertEquals(180, response.getHeightCm());
        assertEquals(75, response.getWeightKg());
        assertEquals("Test Religion", response.getReligion());
    }

    @Test
    @DisplayName("Should handle CharacterCharacterInfo with empty string id parameter")
    void shouldHandleCharacterCharacterInfoWithEmptyStringIdParameter() {
        // Given
        CharacterCharacterInfo characterInfo = new CharacterCharacterInfo(
                null,
                CharacterName.of("Test Character"),
                "Warrior",
                null,
                null,
                null,
                null,
                null,
                Height.of(180),
                Weight.of(75),
                null
        );

        // When
        CharacterInfoResponse response = CharacterInfoDtoBuilder.from(characterInfo, "");

        // Then
        assertNotNull(response);
        assertEquals("", response.getId());
        assertEquals("Test Character", response.getName());
        assertEquals("Warrior", response.getProfession());
        assertEquals(180, response.getHeightCm());
        assertEquals(75, response.getWeightKg());
    }

    @Test
    @DisplayName("Should handle negative values in CharacterEntity")
    void shouldHandleNegativeValuesInCharacterEntity() {
        // Given
        CharacterEntity character = CharacterEntity.builder()
                .id("char-123")
                .name("Test Character")
                .age(-5)
                .apparentAge(-10)
                .heightCm(-180)
                .weightKg(-75)
                .build();

        // When
        CharacterInfoResponse response = CharacterInfoDtoBuilder.from(character);

        // Then
        assertNotNull(response);
        assertEquals(-5, response.getAge());
        assertEquals(-10, response.getApparentAge());
        assertEquals(-180, response.getHeightCm());
        assertEquals(-75, response.getWeightKg());
    }

    @Test
    @DisplayName("Should not allow instantiation of utility class")
    void shouldNotAllowInstantiationOfUtilityClass() throws NoSuchMethodException {
        // Given
        Constructor<CharacterInfoDtoBuilder> constructor = CharacterInfoDtoBuilder.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        // When & Then
        assertThrows(InvocationTargetException.class, constructor::newInstance);
    }
}