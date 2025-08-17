package com.rpgsystem.rpg.domain.character.updater;

import com.rpgsystem.rpg.domain.character.CharacterCharacterInfo;
import com.rpgsystem.rpg.domain.character.valueObject.Height;
import com.rpgsystem.rpg.domain.character.valueObject.CharacterName;
import com.rpgsystem.rpg.domain.character.valueObject.Weight;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("CharacterInfoUpdater Tests")
class CharacterInfoUpdaterTest {

    private CharacterCharacterInfo info;
    private CharacterEntity entity;

    @BeforeEach
    void setUp() {
        info = mock(CharacterCharacterInfo.class);
        entity = new CharacterEntity();
    }

    @Nested
    @DisplayName("Apply Method Tests")
    class ApplyMethodTests {

        @Test
        @DisplayName("Should apply all info when all values are present")
        void shouldApplyAllInfoWhenAllValuesArePresent() {
            // Given
            CharacterName name = mock(CharacterName.class);
            Height height = mock(Height.class);
            Weight weight = mock(Weight.class);
            LocalDate birthDate = LocalDate.of(1990, 5, 15);
            
            when(info.getName()).thenReturn(name);
            when(name.getValue()).thenReturn("John Doe");
            when(info.getProfession()).thenReturn("Warrior");
            when(info.getBirthDate()).thenReturn(birthDate);
            when(info.getBirthPlace()).thenReturn("Capital City");
            when(info.getGender()).thenReturn("Male");
            when(info.getAge()).thenReturn(30);
            when(info.getApparenteAge()).thenReturn(28);
            when(info.getHeightCm()).thenReturn(height);
            when(height.getCentimeters()).thenReturn(180);
            when(info.getWeightKg()).thenReturn(weight);
            when(weight.getKilograms()).thenReturn(75);
            when(info.getReligion()).thenReturn("Ancient Gods");

            CharacterInfoUpdater updater = new CharacterInfoUpdater(info);

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getName()).isEqualTo("John Doe");
            assertThat(entity.getProfession()).isEqualTo("Warrior");
            assertThat(entity.getBirthDate()).isEqualTo(birthDate);
            assertThat(entity.getBirthPlace()).isEqualTo("Capital City");
            assertThat(entity.getGender()).isEqualTo("Male");
            assertThat(entity.getAge()).isEqualTo(30);
            assertThat(entity.getApparentAge()).isEqualTo(28);
            assertThat(entity.getHeightCm()).isEqualTo(180);
            assertThat(entity.getWeightKg()).isEqualTo(75);
            assertThat(entity.getReligion()).isEqualTo("Ancient Gods");
        }

        @Test
        @DisplayName("Should not update when info is null")
        void shouldNotUpdateWhenInfoIsNull() {
            // Given
            CharacterInfoUpdater updater = new CharacterInfoUpdater(null);
            entity.setName("Original Name"); // Set initial value

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getName()).isEqualTo("Original Name"); // Should remain unchanged
        }

        @Test
        @DisplayName("Should not update when entity is null")
        void shouldNotUpdateWhenEntityIsNull() {
            // Given
            CharacterInfoUpdater updater = new CharacterInfoUpdater(info);

            // When & Then
            assertDoesNotThrow(() -> updater.apply(null));
        }

        @Test
        @DisplayName("Should not update when both info and entity are null")
        void shouldNotUpdateWhenBothInfoAndEntityAreNull() {
            // Given
            CharacterInfoUpdater updater = new CharacterInfoUpdater(null);

            // When & Then
            assertDoesNotThrow(() -> updater.apply(null));
        }

        @Test
        @DisplayName("Should handle null name")
        void shouldHandleNullName() {
            // Given
            when(info.getName()).thenReturn(null);
            when(info.getProfession()).thenReturn("Warrior");

            CharacterInfoUpdater updater = new CharacterInfoUpdater(info);
            entity.setName("Original Name"); // Set initial value

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getName()).isNull();
            assertThat(entity.getProfession()).isEqualTo("Warrior");
        }

        @Test
        @DisplayName("Should handle name with null value")
        void shouldHandleNameWithNullValue() {
            // Given
            CharacterName name = mock(CharacterName.class);
            when(info.getName()).thenReturn(name);
            when(name.getValue()).thenReturn(null);
            when(info.getProfession()).thenReturn("Warrior");

            CharacterInfoUpdater updater = new CharacterInfoUpdater(info);
            entity.setName("Original Name"); // Set initial value

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getName()).isNull();
            assertThat(entity.getProfession()).isEqualTo("Warrior");
        }

        @Test
        @DisplayName("Should handle null profession")
        void shouldHandleNullProfession() {
            // Given
            CharacterName name = mock(CharacterName.class);
            when(info.getName()).thenReturn(name);
            when(name.getValue()).thenReturn("John Doe");
            when(info.getProfession()).thenReturn(null);

            CharacterInfoUpdater updater = new CharacterInfoUpdater(info);
            entity.setProfession("Original Profession"); // Set initial value

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getName()).isEqualTo("John Doe");
            assertThat(entity.getProfession()).isNull();
        }

        @Test
        @DisplayName("Should handle null birth date")
        void shouldHandleNullBirthDate() {
            // Given
            CharacterName name = mock(CharacterName.class);
            when(info.getName()).thenReturn(name);
            when(name.getValue()).thenReturn("John Doe");
            when(info.getBirthDate()).thenReturn(null);

            CharacterInfoUpdater updater = new CharacterInfoUpdater(info);
            entity.setBirthDate(LocalDate.of(1990, 1, 1)); // Set initial value

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getName()).isEqualTo("John Doe");
            assertThat(entity.getBirthDate()).isNull();
        }

        @Test
        @DisplayName("Should handle null birth place")
        void shouldHandleNullBirthPlace() {
            // Given
            CharacterName name = mock(CharacterName.class);
            when(info.getName()).thenReturn(name);
            when(name.getValue()).thenReturn("John Doe");
            when(info.getBirthPlace()).thenReturn(null);

            CharacterInfoUpdater updater = new CharacterInfoUpdater(info);
            entity.setBirthPlace("Original Place"); // Set initial value

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getName()).isEqualTo("John Doe");
            assertThat(entity.getBirthPlace()).isNull();
        }

        @Test
        @DisplayName("Should handle null gender")
        void shouldHandleNullGender() {
            // Given
            CharacterName name = mock(CharacterName.class);
            when(info.getName()).thenReturn(name);
            when(name.getValue()).thenReturn("John Doe");
            when(info.getGender()).thenReturn(null);

            CharacterInfoUpdater updater = new CharacterInfoUpdater(info);
            entity.setGender("Original Gender"); // Set initial value

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getName()).isEqualTo("John Doe");
            assertThat(entity.getGender()).isNull();
        }

        @Test
        @DisplayName("Should handle null age")
        void shouldHandleNullAge() {
            // Given
            CharacterName name = mock(CharacterName.class);
            when(info.getName()).thenReturn(name);
            when(name.getValue()).thenReturn("John Doe");
            when(info.getAge()).thenReturn(null);

            CharacterInfoUpdater updater = new CharacterInfoUpdater(info);
            entity.setAge(25); // Set initial value

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getName()).isEqualTo("John Doe");
            assertThat(entity.getAge()).isNull();
        }

        @Test
        @DisplayName("Should handle null apparent age")
        void shouldHandleNullApparentAge() {
            // Given
            CharacterName name = mock(CharacterName.class);
            when(info.getName()).thenReturn(name);
            when(name.getValue()).thenReturn("John Doe");
            when(info.getApparenteAge()).thenReturn(null);

            CharacterInfoUpdater updater = new CharacterInfoUpdater(info);
            entity.setApparentAge(25); // Set initial value

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getName()).isEqualTo("John Doe");
            assertThat(entity.getApparentAge()).isNull();
        }

        @Test
        @DisplayName("Should handle null height")
        void shouldHandleNullHeight() {
            // Given
            CharacterName name = mock(CharacterName.class);
            when(info.getName()).thenReturn(name);
            when(name.getValue()).thenReturn("John Doe");
            when(info.getHeightCm()).thenReturn(null);

            CharacterInfoUpdater updater = new CharacterInfoUpdater(info);
            entity.setHeightCm(180); // Set initial value

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getName()).isEqualTo("John Doe");
            assertThat(entity.getHeightCm()).isNull();
        }

        @Test
        @DisplayName("Should handle height with valid centimeters")
        void shouldHandleHeightWithValidCentimeters() {
            // Given
            CharacterName name = mock(CharacterName.class);
            Height height = mock(Height.class);
            when(info.getName()).thenReturn(name);
            when(name.getValue()).thenReturn("John Doe");
            when(info.getHeightCm()).thenReturn(height);
            when(height.getCentimeters()).thenReturn(175);

            CharacterInfoUpdater updater = new CharacterInfoUpdater(info);
            entity.setHeightCm(180); // Set initial value

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getName()).isEqualTo("John Doe");
            assertThat(entity.getHeightCm()).isEqualTo(175);
        }

        @Test
        @DisplayName("Should handle null weight")
        void shouldHandleNullWeight() {
            // Given
            CharacterName name = mock(CharacterName.class);
            when(info.getName()).thenReturn(name);
            when(name.getValue()).thenReturn("John Doe");
            when(info.getWeightKg()).thenReturn(null);

            CharacterInfoUpdater updater = new CharacterInfoUpdater(info);
            entity.setWeightKg(75); // Set initial value

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getName()).isEqualTo("John Doe");
            assertThat(entity.getWeightKg()).isNull();
        }

        @Test
        @DisplayName("Should handle weight with valid kilograms")
        void shouldHandleWeightWithValidKilograms() {
            // Given
            CharacterName name = mock(CharacterName.class);
            Weight weight = mock(Weight.class);
            when(info.getName()).thenReturn(name);
            when(name.getValue()).thenReturn("John Doe");
            when(info.getWeightKg()).thenReturn(weight);
            when(weight.getKilograms()).thenReturn(70);

            CharacterInfoUpdater updater = new CharacterInfoUpdater(info);
            entity.setWeightKg(75); // Set initial value

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getName()).isEqualTo("John Doe");
            assertThat(entity.getWeightKg()).isEqualTo(70);
        }

        @Test
        @DisplayName("Should handle null religion")
        void shouldHandleNullReligion() {
            // Given
            CharacterName name = mock(CharacterName.class);
            when(info.getName()).thenReturn(name);
            when(name.getValue()).thenReturn("John Doe");
            when(info.getReligion()).thenReturn(null);

            CharacterInfoUpdater updater = new CharacterInfoUpdater(info);
            entity.setReligion("Original Religion"); // Set initial value

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getName()).isEqualTo("John Doe");
            assertThat(entity.getReligion()).isNull();
        }

        @Test
        @DisplayName("Should handle all null values")
        void shouldHandleAllNullValues() {
            // Given
            when(info.getName()).thenReturn(null);
            when(info.getProfession()).thenReturn(null);
            when(info.getBirthDate()).thenReturn(null);
            when(info.getBirthPlace()).thenReturn(null);
            when(info.getGender()).thenReturn(null);
            when(info.getAge()).thenReturn(null);
            when(info.getApparenteAge()).thenReturn(null);
            when(info.getHeightCm()).thenReturn(null);
            when(info.getWeightKg()).thenReturn(null);
            when(info.getReligion()).thenReturn(null);

            CharacterInfoUpdater updater = new CharacterInfoUpdater(info);
            
            // Set initial values
            entity.setName("Original Name");
            entity.setProfession("Original Profession");
            entity.setBirthDate(LocalDate.of(1990, 1, 1));
            entity.setBirthPlace("Original Place");
            entity.setGender("Original Gender");
            entity.setAge(25);
            entity.setApparentAge(23);
            entity.setHeightCm(180);
            entity.setWeightKg(75);
            entity.setReligion("Original Religion");

            // When
            updater.apply(entity);

            // Then - All values should be set to null
            assertThat(entity.getName()).isNull();
            assertThat(entity.getProfession()).isNull();
            assertThat(entity.getBirthDate()).isNull();
            assertThat(entity.getBirthPlace()).isNull();
            assertThat(entity.getGender()).isNull();
            assertThat(entity.getAge()).isNull();
            assertThat(entity.getApparentAge()).isNull();
            assertThat(entity.getHeightCm()).isNull();
            assertThat(entity.getWeightKg()).isNull();
            assertThat(entity.getReligion()).isNull();
        }
    }
}