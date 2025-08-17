package com.rpgsystem.rpg.domain.character.updater;

import com.rpgsystem.rpg.api.dto.character.CharacterRelevantPeopleRequest;
import com.rpgsystem.rpg.domain.entity.RelevantPeopleEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("CharacterRelevantPeopleUpdater Tests")
class CharacterRelevantPeopleUpdaterTest {

    private CharacterRelevantPeopleRequest request;
    private RelevantPeopleEntity entity;

    @BeforeEach
    void setUp() {
        entity = new RelevantPeopleEntity();
    }

    @Nested
    @DisplayName("Apply Method Tests")
    class ApplyMethodTests {

        @Test
        @DisplayName("Should apply all relevant people info when all values are present")
        void shouldApplyAllRelevantPeopleInfoWhenAllValuesArePresent() {
            // Given
            request = CharacterRelevantPeopleRequest.builder()
                    .category("Friend")
                    .name("John Smith")
                    .apparentAge(35)
                    .city("Capital City")
                    .profession("Merchant")
                    .briefDescription("A trusted friend and business partner")
                    .build();

            CharacterRelevantPeopleUpdater updater = new CharacterRelevantPeopleUpdater(request);

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getCategory()).isEqualTo("Friend");
            assertThat(entity.getName()).isEqualTo("John Smith");
            assertThat(entity.getApparentAge()).isEqualTo(35);
            assertThat(entity.getCity()).isEqualTo("Capital City");
            assertThat(entity.getProfession()).isEqualTo("Merchant");
            assertThat(entity.getBriefDescription()).isEqualTo("A trusted friend and business partner");
        }

        @Test
        @DisplayName("Should not update when request is null")
        void shouldNotUpdateWhenRequestIsNull() {
            // Given
            request = null;
            CharacterRelevantPeopleUpdater updater = new CharacterRelevantPeopleUpdater(request);
            entity = RelevantPeopleEntity.builder()
                    .name("Original Name")
                    .build();

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getName()).isEqualTo("Original Name"); // Should remain unchanged
        }

        @Test
        @DisplayName("Should not update when entity is null")
        void shouldNotUpdateWhenEntityIsNull() {
            // Given
            request = CharacterRelevantPeopleRequest.builder()
                    .category("Friend")
                    .name("John Doe")
                    .apparentAge(30)
                    .city("New York")
                    .profession("Detective")
                    .briefDescription("A helpful detective")
                    .build();
            CharacterRelevantPeopleUpdater updater = new CharacterRelevantPeopleUpdater(request);

            // When & Then
            assertDoesNotThrow(() -> updater.apply(null));
        }

        @Test
        @DisplayName("Should not update when both request and entity are null")
        void shouldNotUpdateWhenBothRequestAndEntityAreNull() {
            // Given
            CharacterRelevantPeopleUpdater updater = new CharacterRelevantPeopleUpdater(null);

            // When & Then
            assertDoesNotThrow(() -> updater.apply(null));
        }

        @Test
        @DisplayName("Should handle null category")
        void shouldHandleNullCategory() {
            // Given
            request = CharacterRelevantPeopleRequest.builder()
                    .category(null)
                    .name("John Smith")
                    .apparentAge(35)
                    .city("Capital City")
                    .profession("Merchant")
                    .briefDescription("A trusted friend")
                    .build();

            CharacterRelevantPeopleUpdater updater = new CharacterRelevantPeopleUpdater(request);
            entity = RelevantPeopleEntity.builder()
                    .category("Original Category")
                    .build();

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getCategory()).isNull();
            assertThat(entity.getName()).isEqualTo("John Smith");
            assertThat(entity.getApparentAge()).isEqualTo(35);
            assertThat(entity.getCity()).isEqualTo("Capital City");
            assertThat(entity.getProfession()).isEqualTo("Merchant");
            assertThat(entity.getBriefDescription()).isEqualTo("A trusted friend");
        }

        @Test
        @DisplayName("Should handle null name")
        void shouldHandleNullName() {
            // Given
            request = CharacterRelevantPeopleRequest.builder()
                    .category("Friend")
                    .name(null)
                    .apparentAge(35)
                    .city("Capital City")
                    .profession("Merchant")
                    .briefDescription("A trusted friend")
                    .build();

            CharacterRelevantPeopleUpdater updater = new CharacterRelevantPeopleUpdater(request);
            entity = RelevantPeopleEntity.builder()
                    .name("Original Name")
                    .build();

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getCategory()).isEqualTo("Friend");
            assertThat(entity.getName()).isNull(); // Name.of(null) should return null value
            assertThat(entity.getApparentAge()).isEqualTo(35);
            assertThat(entity.getCity()).isEqualTo("Capital City");
            assertThat(entity.getProfession()).isEqualTo("Merchant");
            assertThat(entity.getBriefDescription()).isEqualTo("A trusted friend");
        }

        @Test
        @DisplayName("Should handle empty name")
        void shouldHandleEmptyName() {
            // Given
            request = CharacterRelevantPeopleRequest.builder()
                    .category("Friend")
                    .name("")
                    .apparentAge(35)
                    .city("Capital City")
                    .profession("Merchant")
                    .briefDescription("A trusted friend")
                    .build();

            CharacterRelevantPeopleUpdater updater = new CharacterRelevantPeopleUpdater(request);

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getCategory()).isEqualTo("Friend");
            assertThat(entity.getName()).isNull(); // Name.of("") should return null value
            assertThat(entity.getApparentAge()).isEqualTo(35);
            assertThat(entity.getCity()).isEqualTo("Capital City");
            assertThat(entity.getProfession()).isEqualTo("Merchant");
            assertThat(entity.getBriefDescription()).isEqualTo("A trusted friend");
        }

        @Test
        @DisplayName("Should handle whitespace-only name")
        void shouldHandleWhitespaceOnlyName() {
            // Given
            request = CharacterRelevantPeopleRequest.builder()
                    .category("Friend")
                    .name("   ")
                    .apparentAge(35)
                    .city("Capital City")
                    .profession("Merchant")
                    .briefDescription("A trusted friend")
                    .build();

            CharacterRelevantPeopleUpdater updater = new CharacterRelevantPeopleUpdater(request);

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getCategory()).isEqualTo("Friend");
            assertThat(entity.getName()).isNull(); // Name.of("   ") should return null value
            assertThat(entity.getApparentAge()).isEqualTo(35);
            assertThat(entity.getCity()).isEqualTo("Capital City");
            assertThat(entity.getProfession()).isEqualTo("Merchant");
            assertThat(entity.getBriefDescription()).isEqualTo("A trusted friend");
        }

        @Test
        @DisplayName("Should handle null apparent age")
        void shouldHandleNullApparentAge() {
            // Given
            request = CharacterRelevantPeopleRequest.builder()
                    .category("Friend")
                    .name("John Smith")
                    .apparentAge(null)
                    .city("Capital City")
                    .profession("Merchant")
                    .briefDescription("A trusted friend")
                    .build();

            CharacterRelevantPeopleUpdater updater = new CharacterRelevantPeopleUpdater(request);
            entity = RelevantPeopleEntity.builder()
                    .apparentAge(30)
                    .build();

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getCategory()).isEqualTo("Friend");
            assertThat(entity.getName()).isEqualTo("John Smith");
            assertThat(entity.getApparentAge()).isNull();
            assertThat(entity.getCity()).isEqualTo("Capital City");
            assertThat(entity.getProfession()).isEqualTo("Merchant");
            assertThat(entity.getBriefDescription()).isEqualTo("A trusted friend");
        }

        @Test
        @DisplayName("Should handle null city")
        void shouldHandleNullCity() {
            // Given
            request = CharacterRelevantPeopleRequest.builder()
                    .category("Friend")
                    .name("John Smith")
                    .apparentAge(35)
                    .city(null)
                    .profession("Merchant")
                    .briefDescription("A trusted friend")
                    .build();

            CharacterRelevantPeopleUpdater updater = new CharacterRelevantPeopleUpdater(request);
            entity = RelevantPeopleEntity.builder()
                    .city("Original City")
                    .build();

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getCategory()).isEqualTo("Friend");
            assertThat(entity.getName()).isEqualTo("John Smith");
            assertThat(entity.getApparentAge()).isEqualTo(35);
            assertThat(entity.getCity()).isNull();
            assertThat(entity.getProfession()).isEqualTo("Merchant");
            assertThat(entity.getBriefDescription()).isEqualTo("A trusted friend");
        }

        @Test
        @DisplayName("Should handle null profession")
        void shouldHandleNullProfession() {
            // Given
            request = CharacterRelevantPeopleRequest.builder()
                    .category("Friend")
                    .name("John Smith")
                    .apparentAge(35)
                    .city("Capital City")
                    .profession(null)
                    .briefDescription("A trusted friend")
                    .build();

            CharacterRelevantPeopleUpdater updater = new CharacterRelevantPeopleUpdater(request);
            entity = RelevantPeopleEntity.builder()
                    .profession("Original Profession")
                    .build();

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getCategory()).isEqualTo("Friend");
            assertThat(entity.getName()).isEqualTo("John Smith");
            assertThat(entity.getApparentAge()).isEqualTo(35);
            assertThat(entity.getCity()).isEqualTo("Capital City");
            assertThat(entity.getProfession()).isNull();
            assertThat(entity.getBriefDescription()).isEqualTo("A trusted friend");
        }

        @Test
        @DisplayName("Should handle null brief description")
        void shouldHandleNullBriefDescription() {
            // Given
            request = CharacterRelevantPeopleRequest.builder()
                    .category("Friend")
                    .name("John Smith")
                    .apparentAge(35)
                    .city("Capital City")
                    .profession("Merchant")
                    .briefDescription(null)
                    .build();

            CharacterRelevantPeopleUpdater updater = new CharacterRelevantPeopleUpdater(request);
            entity = RelevantPeopleEntity.builder()
                    .briefDescription("Original Description")
                    .build();

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getCategory()).isEqualTo("Friend");
            assertThat(entity.getName()).isEqualTo("John Smith");
            assertThat(entity.getApparentAge()).isEqualTo(35);
            assertThat(entity.getCity()).isEqualTo("Capital City");
            assertThat(entity.getProfession()).isEqualTo("Merchant");
            assertThat(entity.getBriefDescription()).isNull();
        }

        @Test
        @DisplayName("Should handle all null values")
        void shouldHandleAllNullValues() {
            // Given
            request = CharacterRelevantPeopleRequest.builder()
                    .category(null)
                    .name(null)
                    .apparentAge(null)
                    .city(null)
                    .profession(null)
                    .briefDescription(null)
                    .build();

            CharacterRelevantPeopleUpdater updater = new CharacterRelevantPeopleUpdater(request);
            
            // Set initial values
            entity = RelevantPeopleEntity.builder()
                    .category("Original Category")
                    .name("Original Name")
                    .apparentAge(30)
                    .city("Original City")
                    .profession("Original Profession")
                    .briefDescription("Original Description")
                    .build();

            // When
            updater.apply(entity);

            // Then - All values should be set to null
            assertThat(entity.getCategory()).isNull();
            assertThat(entity.getName()).isNull();
            assertThat(entity.getApparentAge()).isNull();
            assertThat(entity.getCity()).isNull();
            assertThat(entity.getProfession()).isNull();
            assertThat(entity.getBriefDescription()).isNull();
        }

        @Test
        @DisplayName("Should handle mixed null and valid values")
        void shouldHandleMixedNullAndValidValues() {
            // Given
            request = CharacterRelevantPeopleRequest.builder()
                    .category("Friend")
                    .name(null)
                    .apparentAge(35)
                    .city(null)
                    .profession("Merchant")
                    .briefDescription(null)
                    .build();

            CharacterRelevantPeopleUpdater updater = new CharacterRelevantPeopleUpdater(request);
            
            // Set initial values
            entity = RelevantPeopleEntity.builder()
                    .name("Original Name")
                    .city("Original City")
                    .briefDescription("Original Description")
                    .build();

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getCategory()).isEqualTo("Friend"); // Updated
            assertThat(entity.getName()).isNull(); // Updated to null
            assertThat(entity.getApparentAge()).isEqualTo(35); // Updated
            assertThat(entity.getCity()).isNull(); // Updated to null
            assertThat(entity.getProfession()).isEqualTo("Merchant"); // Updated
            assertThat(entity.getBriefDescription()).isNull(); // Updated to null
        }

        @Test
        @DisplayName("Should handle zero apparent age")
        void shouldHandleZeroApparentAge() {
            // Given
            request = CharacterRelevantPeopleRequest.builder()
                    .category("Friend")
                    .name("John Smith")
                    .apparentAge(0)
                    .city("Capital City")
                    .profession("Merchant")
                    .briefDescription("A trusted friend")
                    .build();

            CharacterRelevantPeopleUpdater updater = new CharacterRelevantPeopleUpdater(request);

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getCategory()).isEqualTo("Friend");
            assertThat(entity.getName()).isEqualTo("John Smith");
            assertThat(entity.getApparentAge()).isEqualTo(0);
            assertThat(entity.getCity()).isEqualTo("Capital City");
            assertThat(entity.getProfession()).isEqualTo("Merchant");
            assertThat(entity.getBriefDescription()).isEqualTo("A trusted friend");
        }

        @Test
        @DisplayName("Should handle negative apparent age")
        void shouldHandleNegativeApparentAge() {
            // Given
            request = CharacterRelevantPeopleRequest.builder()
                    .category("Friend")
                    .name("John Smith")
                    .apparentAge(-5)
                    .city("Capital City")
                    .profession("Merchant")
                    .briefDescription("A trusted friend")
                    .build();

            CharacterRelevantPeopleUpdater updater = new CharacterRelevantPeopleUpdater(request);

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getCategory()).isEqualTo("Friend");
            assertThat(entity.getName()).isEqualTo("John Smith");
            assertThat(entity.getApparentAge()).isEqualTo(-5);
            assertThat(entity.getCity()).isEqualTo("Capital City");
            assertThat(entity.getProfession()).isEqualTo("Merchant");
            assertThat(entity.getBriefDescription()).isEqualTo("A trusted friend");
        }

        @Test
        @DisplayName("Should handle empty strings")
        void shouldHandleEmptyStrings() {
            // Given
            request = CharacterRelevantPeopleRequest.builder()
                    .category("")
                    .name("")
                    .apparentAge(35)
                    .city("")
                    .profession("")
                    .briefDescription("")
                    .build();

            CharacterRelevantPeopleUpdater updater = new CharacterRelevantPeopleUpdater(request);

            // When
            updater.apply(entity);

            // Then
            assertThat(entity.getCategory()).isEqualTo("");
            assertThat(entity.getName()).isNull(); // Name.of("") should return null
            assertThat(entity.getApparentAge()).isEqualTo(35);
            assertThat(entity.getCity()).isEqualTo("");
            assertThat(entity.getProfession()).isEqualTo("");
            assertThat(entity.getBriefDescription()).isEqualTo("");
        }
    }
}