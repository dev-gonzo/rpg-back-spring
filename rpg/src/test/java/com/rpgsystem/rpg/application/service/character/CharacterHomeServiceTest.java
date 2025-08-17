package com.rpgsystem.rpg.application.service.character;

import com.rpgsystem.rpg.api.dto.character.CharacterHomeDto;
import com.rpgsystem.rpg.application.builder.CharacterHomeDtoBuilder;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.User;
import com.rpgsystem.rpg.domain.repository.character.CharacterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do CharacterHomeService")
class CharacterHomeServiceTest {

    @Mock
    private CharacterRepository repository;

    @InjectMocks
    private CharacterHomeService service;

    private User masterUser;
    private User regularUser;
    private CharacterEntity character1;
    private CharacterEntity character2;
    private CharacterEntity character3;
    private CharacterHomeDto characterDto1;
    private CharacterHomeDto characterDto2;
    private CharacterHomeDto characterDto3;

    @BeforeEach
    void setUp() {
        masterUser = User.builder()
                .id("master-id")
                .name("Master User")
                .email("master@test.com")
                .isMaster(true)
                .build();

        regularUser = User.builder()
                .id("regular-id")
                .name("Regular User")
                .email("regular@test.com")
                .isMaster(false)
                .build();

        character1 = CharacterEntity.builder()
                .id("char-1")
                .name("Character 1")
                .age(25)
                .profession("Warrior")
                .controlUser(regularUser)
                .isKnown(true)
                .edit(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        character2 = CharacterEntity.builder()
                .id("char-2")
                .name("Character 2")
                .age(30)
                .profession("Mage")
                .controlUser(null)
                .isKnown(true)
                .edit(false)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        character3 = CharacterEntity.builder()
                .id("char-3")
                .name("Character 3")
                .age(35)
                .profession("Rogue")
                .controlUser(masterUser)
                .isKnown(false)
                .edit(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        characterDto1 = CharacterHomeDto.builder()
                .id("char-1")
                .name("Character 1")
                .age(25)
                .profession("Warrior")
                .edit(true)
                .build();

        characterDto2 = CharacterHomeDto.builder()
                .id("char-2")
                .name("Character 2")
                .age(30)
                .profession("Mage")
                .edit(false)
                .build();

        characterDto3 = CharacterHomeDto.builder()
                .id("char-3")
                .name("Character 3")
                .age(35)
                .profession("Rogue")
                .edit(true)
                .build();
    }

    @Test
    @DisplayName("Deve retornar personagens para usuário master")
    void listByUser_ShouldReturnCharactersForMasterUser() {
        // Given
        List<CharacterEntity> privateControlledByOthers = Arrays.asList(character3);
        List<CharacterEntity> notControlledByUser = Arrays.asList(character2);

        when(repository.findAllPrivateControlledByOthersOrdered(masterUser))
                .thenReturn(privateControlledByOthers);
        when(repository.findAllByNotControlUserOrdered())
                .thenReturn(notControlledByUser);

        try (MockedStatic<CharacterHomeDtoBuilder> mockedBuilder = mockStatic(CharacterHomeDtoBuilder.class)) {
            mockedBuilder.when(() -> CharacterHomeDtoBuilder.from(character3))
                    .thenReturn(characterDto3);
            mockedBuilder.when(() -> CharacterHomeDtoBuilder.from(character2))
                    .thenReturn(characterDto2);

            // When
            List<CharacterHomeDto> result = service.listByUser(masterUser);

            // Then
            assertNotNull(result);
            assertEquals(2, result.size());
            assertTrue(result.contains(characterDto3));
            assertTrue(result.contains(characterDto2));

            verify(repository).findAllPrivateControlledByOthersOrdered(masterUser);
            verify(repository).findAllByNotControlUserOrdered();
            verify(repository, never()).findAllByControlUserOrdered(any());
            verify(repository, never()).findAllByControlUserIsNullAndIsKnownTrueOrdered();
        }
    }

    @Test
    @DisplayName("Deve retornar personagens para usuário regular")
    void listByUser_ShouldReturnCharactersForRegularUser() {
        // Given
        List<CharacterEntity> controlledByUser = Arrays.asList(character1);
        List<CharacterEntity> privateControlledByOthers = Arrays.asList(character3);
        List<CharacterEntity> knownPublicCharacters = Arrays.asList(character2);

        when(repository.findAllByControlUserOrdered(regularUser))
                .thenReturn(controlledByUser);
        when(repository.findAllPrivateControlledByOthersOrdered(regularUser))
                .thenReturn(privateControlledByOthers);
        when(repository.findAllByControlUserIsNullAndIsKnownTrueOrdered())
                .thenReturn(knownPublicCharacters);

        try (MockedStatic<CharacterHomeDtoBuilder> mockedBuilder = mockStatic(CharacterHomeDtoBuilder.class)) {
            mockedBuilder.when(() -> CharacterHomeDtoBuilder.from(character1))
                    .thenReturn(characterDto1);
            mockedBuilder.when(() -> CharacterHomeDtoBuilder.from(character3))
                    .thenReturn(characterDto3);
            mockedBuilder.when(() -> CharacterHomeDtoBuilder.from(character2))
                    .thenReturn(characterDto2);

            // When
            List<CharacterHomeDto> result = service.listByUser(regularUser);

            // Then
            assertNotNull(result);
            assertEquals(3, result.size());
            assertTrue(result.contains(characterDto1));
            assertTrue(result.contains(characterDto2));
            assertTrue(result.contains(characterDto3));

            verify(repository).findAllByControlUserOrdered(regularUser);
            verify(repository).findAllPrivateControlledByOthersOrdered(regularUser);
            verify(repository).findAllByControlUserIsNullAndIsKnownTrueOrdered();
            verify(repository, never()).findAllByNotControlUserOrdered();
        }
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando master não tem personagens")
    void listByUser_ShouldReturnEmptyListWhenMasterHasNoCharacters() {
        // Given
        when(repository.findAllPrivateControlledByOthersOrdered(masterUser))
                .thenReturn(Collections.emptyList());
        when(repository.findAllByNotControlUserOrdered())
                .thenReturn(Collections.emptyList());

        // When
        List<CharacterHomeDto> result = service.listByUser(masterUser);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(repository).findAllPrivateControlledByOthersOrdered(masterUser);
        verify(repository).findAllByNotControlUserOrdered();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando usuário regular não tem personagens")
    void listByUser_ShouldReturnEmptyListWhenRegularUserHasNoCharacters() {
        // Given
        when(repository.findAllByControlUserOrdered(regularUser))
                .thenReturn(Collections.emptyList());
        when(repository.findAllPrivateControlledByOthersOrdered(regularUser))
                .thenReturn(Collections.emptyList());
        when(repository.findAllByControlUserIsNullAndIsKnownTrueOrdered())
                .thenReturn(Collections.emptyList());

        // When
        List<CharacterHomeDto> result = service.listByUser(regularUser);

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(repository).findAllByControlUserOrdered(regularUser);
        verify(repository).findAllPrivateControlledByOthersOrdered(regularUser);
        verify(repository).findAllByControlUserIsNullAndIsKnownTrueOrdered();
    }

    @Test
    @DisplayName("Deve chamar CharacterHomeDtoBuilder.from para cada personagem")
    void listByUser_ShouldCallBuilderForEachCharacter() {
        // Given
        List<CharacterEntity> characters = Arrays.asList(character1, character2);
        when(repository.findAllByControlUserOrdered(regularUser))
                .thenReturn(characters);
        when(repository.findAllPrivateControlledByOthersOrdered(regularUser))
                .thenReturn(Collections.emptyList());
        when(repository.findAllByControlUserIsNullAndIsKnownTrueOrdered())
                .thenReturn(Collections.emptyList());

        try (MockedStatic<CharacterHomeDtoBuilder> mockedBuilder = mockStatic(CharacterHomeDtoBuilder.class)) {
            mockedBuilder.when(() -> CharacterHomeDtoBuilder.from(character1))
                    .thenReturn(characterDto1);
            mockedBuilder.when(() -> CharacterHomeDtoBuilder.from(character2))
                    .thenReturn(characterDto2);

            // When
            service.listByUser(regularUser);

            // Then
            mockedBuilder.verify(() -> CharacterHomeDtoBuilder.from(character1));
            mockedBuilder.verify(() -> CharacterHomeDtoBuilder.from(character2));
        }
    }

    @Test
    @DisplayName("Deve manter ordem dos personagens retornados pelo repositório")
    void listByUser_ShouldMaintainOrderFromRepository() {
        // Given
        List<CharacterEntity> orderedCharacters = Arrays.asList(character1, character2, character3);
        when(repository.findAllByControlUserOrdered(regularUser))
                .thenReturn(orderedCharacters);
        when(repository.findAllPrivateControlledByOthersOrdered(regularUser))
                .thenReturn(Collections.emptyList());
        when(repository.findAllByControlUserIsNullAndIsKnownTrueOrdered())
                .thenReturn(Collections.emptyList());

        try (MockedStatic<CharacterHomeDtoBuilder> mockedBuilder = mockStatic(CharacterHomeDtoBuilder.class)) {
            mockedBuilder.when(() -> CharacterHomeDtoBuilder.from(character1))
                    .thenReturn(characterDto1);
            mockedBuilder.when(() -> CharacterHomeDtoBuilder.from(character2))
                    .thenReturn(characterDto2);
            mockedBuilder.when(() -> CharacterHomeDtoBuilder.from(character3))
                    .thenReturn(characterDto3);

            // When
            List<CharacterHomeDto> result = service.listByUser(regularUser);

            // Then
            assertNotNull(result);
            assertEquals(3, result.size());
            assertEquals(characterDto1, result.get(0));
            assertEquals(characterDto2, result.get(1));
            assertEquals(characterDto3, result.get(2));
        }
    }

    @Test
    @DisplayName("Deve lidar com personagens duplicados entre diferentes consultas")
    void listByUser_ShouldHandleDuplicateCharactersBetweenQueries() {
        // Given - Mesmo personagem retornado em diferentes consultas
        when(repository.findAllByControlUserOrdered(regularUser))
                .thenReturn(Arrays.asList(character1));
        when(repository.findAllPrivateControlledByOthersOrdered(regularUser))
                .thenReturn(Arrays.asList(character1)); // Mesmo personagem
        when(repository.findAllByControlUserIsNullAndIsKnownTrueOrdered())
                .thenReturn(Collections.emptyList());

        try (MockedStatic<CharacterHomeDtoBuilder> mockedBuilder = mockStatic(CharacterHomeDtoBuilder.class)) {
            mockedBuilder.when(() -> CharacterHomeDtoBuilder.from(character1))
                    .thenReturn(characterDto1);

            // When
            List<CharacterHomeDto> result = service.listByUser(regularUser);

            // Then
            assertNotNull(result);
            assertEquals(2, result.size()); // Deve incluir duplicatas
            assertEquals(characterDto1, result.get(0));
            assertEquals(characterDto1, result.get(1));

            // Verifica que o builder foi chamado duas vezes
            mockedBuilder.verify(() -> CharacterHomeDtoBuilder.from(character1), times(2));
        }
    }

    @Test
    @DisplayName("Deve processar corretamente diferentes tipos de usuário")
    void listByUser_ShouldProcessDifferentUserTypesCorrectly() {
        // Given - Teste com usuário master
        when(repository.findAllPrivateControlledByOthersOrdered(masterUser))
                .thenReturn(Arrays.asList(character1));
        when(repository.findAllByNotControlUserOrdered())
                .thenReturn(Arrays.asList(character2));

        try (MockedStatic<CharacterHomeDtoBuilder> mockedBuilder = mockStatic(CharacterHomeDtoBuilder.class)) {
            mockedBuilder.when(() -> CharacterHomeDtoBuilder.from(any()))
                    .thenReturn(characterDto1);

            // When
            List<CharacterHomeDto> masterResult = service.listByUser(masterUser);

            // Then
            assertNotNull(masterResult);
            assertEquals(2, masterResult.size());

            // Verifica que apenas os métodos corretos foram chamados para master
            verify(repository).findAllPrivateControlledByOthersOrdered(masterUser);
            verify(repository).findAllByNotControlUserOrdered();
            verify(repository, never()).findAllByControlUserOrdered(masterUser);
            verify(repository, never()).findAllByControlUserIsNullAndIsKnownTrueOrdered();
        }
    }

    @Test
    @DisplayName("Deve lidar com listas grandes de personagens")
    void listByUser_ShouldHandleLargeCharacterLists() {
        // Given - Simula uma lista grande de personagens
        List<CharacterEntity> largeList = Collections.nCopies(100, character1);
        when(repository.findAllByControlUserOrdered(regularUser))
                .thenReturn(largeList);
        when(repository.findAllPrivateControlledByOthersOrdered(regularUser))
                .thenReturn(Collections.emptyList());
        when(repository.findAllByControlUserIsNullAndIsKnownTrueOrdered())
                .thenReturn(Collections.emptyList());

        try (MockedStatic<CharacterHomeDtoBuilder> mockedBuilder = mockStatic(CharacterHomeDtoBuilder.class)) {
            mockedBuilder.when(() -> CharacterHomeDtoBuilder.from(character1))
                    .thenReturn(characterDto1);

            // When
            List<CharacterHomeDto> result = service.listByUser(regularUser);

            // Then
            assertNotNull(result);
            assertEquals(100, result.size());
            assertTrue(result.stream().allMatch(dto -> dto.equals(characterDto1)));

            // Verifica que o builder foi chamado 100 vezes
            mockedBuilder.verify(() -> CharacterHomeDtoBuilder.from(character1), times(100));
        }
    }

    @DisplayName("Testes de Performance e Paginação")
    class PerformanceAndPaginationTests {

        @Test
        @DisplayName("Deve processar múltiplas consultas de repositório de forma eficiente")
        void deveProcessarMultiplasConsultasDeFormaEficiente() {
            // Given
            List<CharacterEntity> controlledChars = Arrays.asList(character1);
            List<CharacterEntity> privateChars = Arrays.asList(character2);
            List<CharacterEntity> publicChars = Arrays.asList(character3);

            when(repository.findAllByControlUserOrdered(regularUser))
                    .thenReturn(controlledChars);
            when(repository.findAllPrivateControlledByOthersOrdered(regularUser))
                    .thenReturn(privateChars);
            when(repository.findAllByControlUserIsNullAndIsKnownTrueOrdered())
                    .thenReturn(publicChars);

            try (MockedStatic<CharacterHomeDtoBuilder> mockedBuilder = mockStatic(CharacterHomeDtoBuilder.class)) {
                mockedBuilder.when(() -> CharacterHomeDtoBuilder.from(any()))
                        .thenReturn(characterDto1);

                // When
                long startTime = System.currentTimeMillis();
                List<CharacterHomeDto> result = service.listByUser(regularUser);
                long endTime = System.currentTimeMillis();

                // Then
                assertNotNull(result);
                assertEquals(3, result.size());
                assertTrue((endTime - startTime) < 1000); // Deve executar em menos de 1 segundo

                // Verifica que todas as consultas foram feitas exatamente uma vez
                verify(repository, times(1)).findAllByControlUserOrdered(regularUser);
                verify(repository, times(1)).findAllPrivateControlledByOthersOrdered(regularUser);
                verify(repository, times(1)).findAllByControlUserIsNullAndIsKnownTrueOrdered();
            }
        }

        @Test
        @DisplayName("Deve simular comportamento de paginação com listas grandes")
        void deveSimularComportamentoDePaginacaoComListasGrandes() {
            // Given - Simula cenário de paginação com 500 personagens
            List<CharacterEntity> hugeMasterList = Collections.nCopies(500, character1);
            when(repository.findAllPrivateControlledByOthersOrdered(masterUser))
                    .thenReturn(hugeMasterList);
            when(repository.findAllByNotControlUserOrdered())
                    .thenReturn(Collections.emptyList());

            try (MockedStatic<CharacterHomeDtoBuilder> mockedBuilder = mockStatic(CharacterHomeDtoBuilder.class)) {
                mockedBuilder.when(() -> CharacterHomeDtoBuilder.from(character1))
                        .thenReturn(characterDto1);

                // When
                List<CharacterHomeDto> result = service.listByUser(masterUser);

                // Then
                assertNotNull(result);
                assertEquals(500, result.size());
                
                // Simula verificação de "primeira página" (primeiros 20 itens)
                List<CharacterHomeDto> firstPage = result.subList(0, Math.min(20, result.size()));
                assertEquals(20, firstPage.size());
                assertTrue(firstPage.stream().allMatch(dto -> dto.equals(characterDto1)));

                mockedBuilder.verify(() -> CharacterHomeDtoBuilder.from(character1), times(500));
            }
        }

        @Test
        @DisplayName("Deve manter performance consistente com diferentes tamanhos de lista")
        void deveManterPerformanceConsistenteComDiferentesTamanhos() {
            // Given - Testa com diferentes tamanhos: 1, 10, 100 personagens
            List<Integer> testSizes = Arrays.asList(1, 10, 100);
            
            for (Integer size : testSizes) {
                List<CharacterEntity> testList = Collections.nCopies(size, character1);
                when(repository.findAllByControlUserOrdered(regularUser))
                        .thenReturn(testList);
                when(repository.findAllPrivateControlledByOthersOrdered(regularUser))
                        .thenReturn(Collections.emptyList());
                when(repository.findAllByControlUserIsNullAndIsKnownTrueOrdered())
                        .thenReturn(Collections.emptyList());

                try (MockedStatic<CharacterHomeDtoBuilder> mockedBuilder = mockStatic(CharacterHomeDtoBuilder.class)) {
                    mockedBuilder.when(() -> CharacterHomeDtoBuilder.from(character1))
                            .thenReturn(characterDto1);

                    // When
                    long startTime = System.nanoTime();
                    List<CharacterHomeDto> result = service.listByUser(regularUser);
                    long endTime = System.nanoTime();
                    long executionTime = (endTime - startTime) / 1_000_000; // Convert to milliseconds

                    // Then
                    assertNotNull(result);
                    assertEquals(size.intValue(), result.size());
                    assertTrue(executionTime < 100, 
                            String.format("Execution time %d ms exceeded limit for size %d", executionTime, size));
                }
            }
        }
    }

    @DisplayName("Testes de Filtros e Ordenação")
    class FilterAndSortingTests {

        @Test
        @DisplayName("Deve manter ordem específica entre diferentes tipos de personagens")
        void deveManterOrdemEspecificaEntreDiferentesTipos() {
            // Given - Personagens com diferentes características para testar ordenação
            CharacterEntity youngWarrior = CharacterEntity.builder()
                    .id("young-warrior")
                    .name("Young Warrior")
                    .age(18)
                    .profession("Warrior")
                    .controlUser(regularUser)
                    .build();

            CharacterEntity oldMage = CharacterEntity.builder()
                    .id("old-mage")
                    .name("Old Mage")
                    .age(80)
                    .profession("Mage")
                    .controlUser(regularUser)
                    .build();

            when(repository.findAllByControlUserOrdered(regularUser))
                    .thenReturn(Arrays.asList(youngWarrior, oldMage)); // Ordem específica
            when(repository.findAllPrivateControlledByOthersOrdered(regularUser))
                    .thenReturn(Collections.emptyList());
            when(repository.findAllByControlUserIsNullAndIsKnownTrueOrdered())
                    .thenReturn(Collections.emptyList());

            CharacterHomeDto youngDto = CharacterHomeDto.builder()
                    .id("young-warrior")
                    .name("Young Warrior")
                    .age(18)
                    .profession("Warrior")
                    .build();

            CharacterHomeDto oldDto = CharacterHomeDto.builder()
                    .id("old-mage")
                    .name("Old Mage")
                    .age(80)
                    .profession("Mage")
                    .build();

            try (MockedStatic<CharacterHomeDtoBuilder> mockedBuilder = mockStatic(CharacterHomeDtoBuilder.class)) {
                mockedBuilder.when(() -> CharacterHomeDtoBuilder.from(youngWarrior))
                        .thenReturn(youngDto);
                mockedBuilder.when(() -> CharacterHomeDtoBuilder.from(oldMage))
                        .thenReturn(oldDto);

                // When
                List<CharacterHomeDto> result = service.listByUser(regularUser);

                // Then
                assertNotNull(result);
                assertEquals(2, result.size());
                assertEquals("Young Warrior", result.get(0).getName());
                assertEquals("Old Mage", result.get(1).getName());
                assertEquals(18, result.get(0).getAge());
                assertEquals(80, result.get(1).getAge());
            }
        }

        @Test
        @DisplayName("Deve processar personagens com profissões diversas")
        void deveProcessarPersonagensComProfissoesDiversas() {
            // Given - Personagens com profissões variadas
            List<String> professions = Arrays.asList("Warrior", "Mage", "Rogue", "Cleric", "Ranger", "Bard");
            List<CharacterEntity> diverseCharacters = new ArrayList<>();
            List<CharacterHomeDto> expectedDtos = new ArrayList<>();

            for (int i = 0; i < professions.size(); i++) {
                CharacterEntity character = CharacterEntity.builder()
                        .id("char-" + i)
                        .name("Character " + i)
                        .profession(professions.get(i))
                        .controlUser(regularUser)
                        .build();
                diverseCharacters.add(character);

                CharacterHomeDto dto = CharacterHomeDto.builder()
                        .id("char-" + i)
                        .name("Character " + i)
                        .profession(professions.get(i))
                        .build();
                expectedDtos.add(dto);
            }

            when(repository.findAllByControlUserOrdered(regularUser))
                    .thenReturn(diverseCharacters);
            when(repository.findAllPrivateControlledByOthersOrdered(regularUser))
                    .thenReturn(Collections.emptyList());
            when(repository.findAllByControlUserIsNullAndIsKnownTrueOrdered())
                    .thenReturn(Collections.emptyList());

            try (MockedStatic<CharacterHomeDtoBuilder> mockedBuilder = mockStatic(CharacterHomeDtoBuilder.class)) {
                for (int i = 0; i < diverseCharacters.size(); i++) {
                    final CharacterEntity character = diverseCharacters.get(i);
                    final CharacterHomeDto dto = expectedDtos.get(i);
                    mockedBuilder.when(() -> CharacterHomeDtoBuilder.from(character))
                            .thenReturn(dto);
                }

                // When
                List<CharacterHomeDto> result = service.listByUser(regularUser);

                // Then
                assertNotNull(result);
                assertEquals(6, result.size());
                
                for (int i = 0; i < professions.size(); i++) {
                    assertEquals(professions.get(i), result.get(i).getProfession());
                    assertEquals("Character " + i, result.get(i).getName());
                }
            }
        }

        @Test
        @DisplayName("Deve filtrar corretamente personagens por tipo de usuário")
        void deveFiltrarCorretamentePersonagensPorTipoDeUsuario() {
            // Given - Cenário complexo com diferentes tipos de acesso
            CharacterEntity ownedByRegular = CharacterEntity.builder()
                    .id("owned-regular")
                    .name("Owned by Regular")
                    .controlUser(regularUser)
                    .isKnown(true)
                    .build();

            CharacterEntity ownedByMaster = CharacterEntity.builder()
                    .id("owned-master")
                    .name("Owned by Master")
                    .controlUser(masterUser)
                    .isKnown(false)
                    .build();

            CharacterEntity publicCharacter = CharacterEntity.builder()
                    .id("public-char")
                    .name("Public Character")
                    .controlUser(null)
                    .isKnown(true)
                    .build();

            // Para usuário regular
            when(repository.findAllByControlUserOrdered(regularUser))
                    .thenReturn(Arrays.asList(ownedByRegular));
            when(repository.findAllPrivateControlledByOthersOrdered(regularUser))
                    .thenReturn(Arrays.asList(ownedByMaster));
            when(repository.findAllByControlUserIsNullAndIsKnownTrueOrdered())
                    .thenReturn(Arrays.asList(publicCharacter));

            try (MockedStatic<CharacterHomeDtoBuilder> mockedBuilder = mockStatic(CharacterHomeDtoBuilder.class)) {
                mockedBuilder.when(() -> CharacterHomeDtoBuilder.from(any()))
                        .thenReturn(characterDto1);

                // When
                List<CharacterHomeDto> regularResult = service.listByUser(regularUser);

                // Then
                assertNotNull(regularResult);
                assertEquals(3, regularResult.size()); // Deve ver todos os 3 tipos

                verify(repository).findAllByControlUserOrdered(regularUser);
                verify(repository).findAllPrivateControlledByOthersOrdered(regularUser);
                verify(repository).findAllByControlUserIsNullAndIsKnownTrueOrdered();
                verify(repository, never()).findAllByNotControlUserOrdered();
            }
        }
    }

    @DisplayName("Testes de Edge Cases Avançados")
    class AdvancedEdgeCasesTests {

        @Test
        @DisplayName("Deve lidar com personagens com nomes especiais e caracteres Unicode")
        void deveLidarComPersonagensComNomesEspeciais() {
            // Given - Personagens com nomes complexos
            CharacterEntity unicodeChar = CharacterEntity.builder()
                    .id("unicode-char")
                    .name("Ñoël 龍王 José-María")
                    .profession("Mágico Élfico")
                    .controlUser(regularUser)
                    .build();

            CharacterEntity specialChar = CharacterEntity.builder()
                    .id("special-char")
                    .name("Character with \"Quotes\" & Symbols!@#$%")
                    .profession("Rogue/Assassin")
                    .controlUser(regularUser)
                    .build();

            when(repository.findAllByControlUserOrdered(regularUser))
                    .thenReturn(Arrays.asList(unicodeChar, specialChar));
            when(repository.findAllPrivateControlledByOthersOrdered(regularUser))
                    .thenReturn(Collections.emptyList());
            when(repository.findAllByControlUserIsNullAndIsKnownTrueOrdered())
                    .thenReturn(Collections.emptyList());

            CharacterHomeDto unicodeDto = CharacterHomeDto.builder()
                    .id("unicode-char")
                    .name("Ñoël 龍王 José-María")
                    .profession("Mágico Élfico")
                    .build();

            CharacterHomeDto specialDto = CharacterHomeDto.builder()
                    .id("special-char")
                    .name("Character with \"Quotes\" & Symbols!@#$%")
                    .profession("Rogue/Assassin")
                    .build();

            try (MockedStatic<CharacterHomeDtoBuilder> mockedBuilder = mockStatic(CharacterHomeDtoBuilder.class)) {
                mockedBuilder.when(() -> CharacterHomeDtoBuilder.from(unicodeChar))
                        .thenReturn(unicodeDto);
                mockedBuilder.when(() -> CharacterHomeDtoBuilder.from(specialChar))
                        .thenReturn(specialDto);

                // When
                List<CharacterHomeDto> result = service.listByUser(regularUser);

                // Then
                assertNotNull(result);
                assertEquals(2, result.size());
                assertEquals("Ñoël 龍王 José-María", result.get(0).getName());
                assertEquals("Character with \"Quotes\" & Symbols!@#$%", result.get(1).getName());
                assertEquals("Mágico Élfico", result.get(0).getProfession());
                assertEquals("Rogue/Assassin", result.get(1).getProfession());
            }
        }

        @Test
        @DisplayName("Deve processar personagens com idades extremas")
        void deveProcessarPersonagensComIdadesExtremas() {
            // Given - Personagens com idades nos limites
            CharacterEntity veryYoung = CharacterEntity.builder()
                    .id("very-young")
                    .name("Very Young")
                    .age(0)
                    .controlUser(regularUser)
                    .build();

            CharacterEntity veryOld = CharacterEntity.builder()
                    .id("very-old")
                    .name("Ancient One")
                    .age(9999)
                    .controlUser(regularUser)
                    .build();

            when(repository.findAllByControlUserOrdered(regularUser))
                    .thenReturn(Arrays.asList(veryYoung, veryOld));
            when(repository.findAllPrivateControlledByOthersOrdered(regularUser))
                    .thenReturn(Collections.emptyList());
            when(repository.findAllByControlUserIsNullAndIsKnownTrueOrdered())
                    .thenReturn(Collections.emptyList());

            CharacterHomeDto youngDto = CharacterHomeDto.builder()
                    .id("very-young")
                    .name("Very Young")
                    .age(0)
                    .build();

            CharacterHomeDto oldDto = CharacterHomeDto.builder()
                    .id("very-old")
                    .name("Ancient One")
                    .age(9999)
                    .build();

            try (MockedStatic<CharacterHomeDtoBuilder> mockedBuilder = mockStatic(CharacterHomeDtoBuilder.class)) {
                mockedBuilder.when(() -> CharacterHomeDtoBuilder.from(veryYoung))
                        .thenReturn(youngDto);
                mockedBuilder.when(() -> CharacterHomeDtoBuilder.from(veryOld))
                        .thenReturn(oldDto);

                // When
                List<CharacterHomeDto> result = service.listByUser(regularUser);

                // Then
                assertNotNull(result);
                assertEquals(2, result.size());
                assertEquals(0, result.get(0).getAge());
                assertEquals(9999, result.get(1).getAge());
                assertEquals("Very Young", result.get(0).getName());
                assertEquals("Ancient One", result.get(1).getName());
            }
        }

        @Test
        @DisplayName("Deve lidar com falhas no CharacterHomeDtoBuilder")
        void deveLidarComFalhasNoBuilder() {
            // Given
            when(repository.findAllByControlUserOrdered(regularUser))
                    .thenReturn(Arrays.asList(character1));
            when(repository.findAllPrivateControlledByOthersOrdered(regularUser))
                    .thenReturn(Collections.emptyList());
            when(repository.findAllByControlUserIsNullAndIsKnownTrueOrdered())
                    .thenReturn(Collections.emptyList());

            try (MockedStatic<CharacterHomeDtoBuilder> mockedBuilder = mockStatic(CharacterHomeDtoBuilder.class)) {
                mockedBuilder.when(() -> CharacterHomeDtoBuilder.from(character1))
                        .thenThrow(new RuntimeException("Builder error"));

                // When & Then
                assertThrows(RuntimeException.class, () -> {
                    service.listByUser(regularUser);
                });

                mockedBuilder.verify(() -> CharacterHomeDtoBuilder.from(character1));
            }
        }

        @Test
        @DisplayName("Deve processar múltiplas chamadas simultâneas")
        void deveProcessarMultiplasChamadasSimultaneas() throws InterruptedException {
            // Given
            when(repository.findAllByControlUserOrdered(regularUser))
                    .thenReturn(Arrays.asList(character1));
            when(repository.findAllPrivateControlledByOthersOrdered(regularUser))
                    .thenReturn(Collections.emptyList());
            when(repository.findAllByControlUserIsNullAndIsKnownTrueOrdered())
                    .thenReturn(Collections.emptyList());

            try (MockedStatic<CharacterHomeDtoBuilder> mockedBuilder = mockStatic(CharacterHomeDtoBuilder.class)) {
                mockedBuilder.when(() -> CharacterHomeDtoBuilder.from(character1))
                        .thenReturn(characterDto1);

                // When - Executa múltiplas chamadas simultâneas
                List<Thread> threads = new ArrayList<>();
                List<List<CharacterHomeDto>> results = Collections.synchronizedList(new ArrayList<>());

                for (int i = 0; i < 10; i++) {
                    Thread thread = new Thread(() -> {
                        List<CharacterHomeDto> result = service.listByUser(regularUser);
                        results.add(result);
                    });
                    threads.add(thread);
                    thread.start();
                }

                // Aguarda todas as threads terminarem
                for (Thread thread : threads) {
                    thread.join();
                }

                // Then
                assertEquals(10, results.size());
                for (List<CharacterHomeDto> result : results) {
                    assertNotNull(result);
                    assertEquals(1, result.size());
                    assertEquals(characterDto1, result.get(0));
                }

                // Verifica que o builder foi chamado 10 vezes (uma para cada thread)
                mockedBuilder.verify(() -> CharacterHomeDtoBuilder.from(character1), times(10));
            }
        }
    }
}