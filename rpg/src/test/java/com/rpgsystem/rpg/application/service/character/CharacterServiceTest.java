package com.rpgsystem.rpg.application.service.character;

import com.rpgsystem.rpg.api.dto.character.CharacterInfoRequest;
import com.rpgsystem.rpg.api.dto.character.CharacterInfoResponse;
import com.rpgsystem.rpg.application.service.shared.ImageService;
import com.rpgsystem.rpg.domain.common.CharacterAccessValidator;
import com.rpgsystem.rpg.domain.entity.CharacterEntity;
import com.rpgsystem.rpg.domain.entity.User;

import com.rpgsystem.rpg.domain.repository.character.CharacterRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do CharacterService")
class CharacterServiceTest {

    @Mock
    private CharacterRepository repository;

    @Mock
    private ImageService imageService;

    @Mock
    private CharacterAccessValidator characterAccessValidator;

    @InjectMocks
    private CharacterService characterService;

    private CharacterEntity testCharacter;
    private User testUser;
    private User masterUser;
    private CharacterInfoRequest characterInfoRequest;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(UUID.randomUUID().toString())
                .name("Test User")
                .email("test@example.com")
                .password("password")
                .isMaster(false)
                .build();

        masterUser = User.builder()
                .id(UUID.randomUUID().toString())
                .name("Master User")
                .email("master@example.com")
                .password("password")
                .isMaster(true)
                .build();

        testCharacter = CharacterEntity.builder()
                .id("character-id")
                .name("Test Character")
                .profession("Warrior")
                .birthDate(LocalDate.of(1990, 1, 1))
                .birthPlace("Test City")
                .gender("MALE")
                .age(30)
                .apparentAge(28)
                .heightCm(180)
                .weightKg(75)
                .religion("Test Religion")
                .isKnown(false)
                .edit(true)
                .controlUser(testUser)
                .build();

        characterInfoRequest = CharacterInfoRequest.builder()
                .name("New Character")
                .profession("Mage")
                .birthDate(LocalDate.of(1985, 5, 15))
                .birthPlace("Magic City")
                .gender("FEMALE")
                .age(35)
                .apparentAge(33)
                .heightCm(165)
                .weightKg(60)
                .religion("Magic Religion")
                .build();
    }

    @Nested
    @DisplayName("Testes do método getById")
    class GetByIdTests {

        @Test
        @DisplayName("Deve retornar personagem quando ID existe")
        void deveRetornarPersonagemQuandoIdExiste() {
            // Given
            when(repository.findById("character-id")).thenReturn(Optional.of(testCharacter));

            // When
            CharacterEntity result = characterService.getById("character-id");

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo("character-id");
            assertThat(result.getName()).isEqualTo("Test Character");
            verify(repository).findById("character-id");
        }

        @Test
        @DisplayName("Deve lançar EntityNotFoundException quando ID não existe")
        void deveLancarEntityNotFoundExceptionQuandoIdNaoExiste() {
            // Given
            when(repository.findById("invalid-id")).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> characterService.getById("invalid-id"))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage("invalid-id");
            
            verify(repository).findById("invalid-id");
        }

        @Test
        @DisplayName("Deve lançar exceção quando ID é nulo")
        void deveLancarExcecaoQuandoIdENulo() {
            // Given
            String nullId = null;

            // When & Then
            assertThatThrownBy(() -> characterService.getById(nullId))
                    .isInstanceOf(EntityNotFoundException.class);
        }
    }

    @Nested
    @DisplayName("Testes do método getInfo")
    class GetInfoTests {

        @Test
        @DisplayName("Deve retornar informações do personagem quando ID existe")
        void deveRetornarInformacoesDoPersonagemQuandoIdExiste() {
            // Given
            when(repository.findById("character-id")).thenReturn(Optional.of(testCharacter));

            // When
            CharacterInfoResponse result = characterService.getInfo("character-id");

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(testCharacter.getId());
            assertThat(result.getName()).isEqualTo(testCharacter.getName());
            assertThat(result.getProfession()).isEqualTo(testCharacter.getProfession());
            verify(repository).findById("character-id");
        }

        @Test
        @DisplayName("Deve lançar EntityNotFoundException quando personagem não existe")
        void deveLancarEntityNotFoundExceptionQuandoPersonagemNaoExiste() {
            // Given
            when(repository.findById("invalid-id")).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> characterService.getInfo("invalid-id"))
                    .isInstanceOf(EntityNotFoundException.class);
            
            verify(repository).findById("invalid-id");
        }
    }

    @Nested
    @DisplayName("Testes do método create")
    class CreateTests {

        @Test
        @DisplayName("Deve criar personagem com sucesso para usuário PLAYER")
        void deveCriarPersonagemComSucessoParaUsuarioPlayer() {
            // Given
            when(repository.save(any(CharacterEntity.class))).thenAnswer(invocation -> {
                CharacterEntity character = invocation.getArgument(0);
                return CharacterEntity.builder()
                        .id(character.getId())
                        .name(character.getName())
                        .profession(character.getProfession())
                        .birthDate(character.getBirthDate())
                        .birthPlace(character.getBirthPlace())
                        .gender(character.getGender())
                        .age(character.getAge())
                        .apparentAge(character.getApparentAge())
                        .heightCm(character.getHeightCm())
                        .weightKg(character.getWeightKg())
                        .religion(character.getReligion())
                        .isKnown(false)
                        .edit(true)
                        .controlUser(character.getControlUser())
                        .build();
            });

            // When
            CharacterInfoResponse result = characterService.create(characterInfoRequest, testUser);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo(characterInfoRequest.getName());
            assertThat(result.getProfession()).isEqualTo(characterInfoRequest.getProfession());
            
            verify(repository).save(argThat(character -> {
                return character.getName().equals(characterInfoRequest.getName()) &&
                       character.getProfession().equals(characterInfoRequest.getProfession()) &&
                       character.getControlUser().equals(testUser) &&
                       !character.isKnown() &&
                       character.isEdit() &&
                       character.getId() != null;
            }));
        }

        @Test
        @DisplayName("Deve criar personagem sem controlUser para usuário MASTER")
        void deveCriarPersonagemSemControlUserParaUsuarioMaster() {
            // Given
            when(repository.save(any(CharacterEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // When
            CharacterInfoResponse result = characterService.create(characterInfoRequest, masterUser);

            // Then
            assertThat(result).isNotNull();
            
            verify(repository).save(argThat(character -> 
                character.getControlUser() == null
            ));
        }

        @Test
        @DisplayName("Deve gerar ID único para novo personagem")
        void deveGerarIdUnicoParaNovoPersonagem() {
            // Given
            when(repository.save(any(CharacterEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // When
            characterService.create(characterInfoRequest, testUser);

            // Then
            verify(repository).save(argThat(character -> {
                String id = character.getId();
                return id != null && !id.isEmpty();
            }));
        }

        @Test
        @DisplayName("Deve definir valores padrão corretos")
        void deveDefinirValoresPadraoCorretos() {
            // Given
            when(repository.save(any(CharacterEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // When
            characterService.create(characterInfoRequest, testUser);

            // Then
            verify(repository).save(argThat(character -> 
                !character.isKnown() && character.isEdit()
            ));
        }

        @Test
        @DisplayName("Deve criar personagem com altura e peso nulos")
        void deveCriarPersonagemComAlturaEPesoNulos() {
            // Given
            CharacterInfoRequest requestComNulos = CharacterInfoRequest.builder()
                    .name("Character Null Values")
                    .profession("Test Profession")
                    .birthDate(LocalDate.of(1990, 1, 1))
                    .birthPlace("Test City")
                    .gender("MALE")
                    .age(30)
                    .apparentAge(28)
                    .heightCm(null) // Valor nulo para testar branch
                    .weightKg(null) // Valor nulo para testar branch
                    .religion("Test Religion")
                    .build();

            when(repository.save(any(CharacterEntity.class))).thenAnswer(invocation -> {
                CharacterEntity character = invocation.getArgument(0);
                return CharacterEntity.builder()
                        .id(character.getId())
                        .name(character.getName())
                        .profession(character.getProfession())
                        .birthDate(character.getBirthDate())
                        .birthPlace(character.getBirthPlace())
                        .gender(character.getGender())
                        .age(character.getAge())
                        .apparentAge(character.getApparentAge())
                        .heightCm(character.getHeightCm()) // null
                        .weightKg(character.getWeightKg()) // null
                        .religion(character.getReligion())
                        .isKnown(character.isKnown())
                        .edit(character.isEdit())
                        .controlUser(character.getControlUser())
                        .build();
            });

            // When
            CharacterInfoResponse result = characterService.create(requestComNulos, testUser);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Character Null Values");
            assertThat(result.getHeightCm()).isNull();
            assertThat(result.getWeightKg()).isNull();
            verify(repository).save(any(CharacterEntity.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando request é nulo")
        void deveLancarExcecaoQuandoRequestENulo() {
            // Given
            CharacterInfoRequest nullRequest = null;

            // When & Then
            assertThatThrownBy(() -> characterService.create(nullRequest, testUser))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Deve lançar exceção quando user é nulo")
        void deveLancarExcecaoQuandoUserENulo() {
            // Given
            User nullUser = null;

            // When & Then
            assertThatThrownBy(() -> characterService.create(characterInfoRequest, nullUser))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Testes do método save")
    class SaveTests {

        @Test
        @DisplayName("Deve atualizar personagem com sucesso quando usuário tem permissão")
        void deveAtualizarPersonagemComSucessoQuandoUsuarioTemPermissao() {
            // Given
            when(repository.findById("character-id")).thenReturn(Optional.of(testCharacter));
            doNothing().when(characterAccessValidator).validateControlAccess(testCharacter, testUser);
            when(repository.save(any(CharacterEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // When
            CharacterInfoResponse result = characterService.save(characterInfoRequest, "character-id", testUser);

            // Then
            assertThat(result).isNotNull();
            verify(repository).findById("character-id");
            verify(characterAccessValidator).validateControlAccess(testCharacter, testUser);
            verify(repository).save(testCharacter);
        }

        @Test
        @DisplayName("Deve lançar exceção quando usuário não tem permissão")
        void deveLancarExcecaoQuandoUsuarioNaoTemPermissao() {
            // Given
            when(repository.findById("character-id")).thenReturn(Optional.of(testCharacter));
            doThrow(new SecurityException("Access denied"))
                    .when(characterAccessValidator).validateControlAccess(testCharacter, testUser);

            // When & Then
            assertThatThrownBy(() -> characterService.save(characterInfoRequest, "character-id", testUser))
                    .isInstanceOf(SecurityException.class)
                    .hasMessage("Access denied");
            
            verify(repository).findById("character-id");
            verify(characterAccessValidator).validateControlAccess(testCharacter, testUser);
            verify(repository, never()).save(any(CharacterEntity.class));
        }

        @Test
        @DisplayName("Deve lançar EntityNotFoundException quando personagem não existe")
        void deveLancarEntityNotFoundExceptionQuandoPersonagemNaoExiste() {
            // Given
            when(repository.findById("invalid-id")).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> characterService.save(characterInfoRequest, "invalid-id", testUser))
                    .isInstanceOf(EntityNotFoundException.class);
            
            verify(repository).findById("invalid-id");
            verify(characterAccessValidator, never()).validateControlAccess(any(), any());
            verify(repository, never()).save(any(CharacterEntity.class));
        }
    }

    @Nested
    @DisplayName("Testes do método uploadImage")
    class UploadImageTests {

        @Test
        @DisplayName("Deve fazer upload de imagem com sucesso quando usuário tem permissão")
        void deveFazerUploadDeImagemComSucessoQuandoUsuarioTemPermissao() {
            // Given
            MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());
            String expectedImagePath = "/images/character-id.jpg";
            
            when(repository.findById("character-id")).thenReturn(Optional.of(testCharacter));
            doNothing().when(characterAccessValidator).validateControlAccess(testCharacter, testUser);
            when(imageService.processToBase64(file)).thenReturn(expectedImagePath);
            when(repository.save(any(CharacterEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // When
            characterService.uploadImage("character-id", file, testUser);

            // Then
            verify(repository).findById("character-id");
            verify(characterAccessValidator).validateControlAccess(testCharacter, testUser);
            verify(imageService).processToBase64(file);
            verify(repository).save(argThat(character -> 
                expectedImagePath.equals(character.getImage())
            ));
        }

        @Test
        @DisplayName("Deve lançar exceção quando usuário não tem permissão para upload")
        void deveLancarExcecaoQuandoUsuarioNaoTemPermissaoParaUpload() {
            // Given
            MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test image content".getBytes());
            
            when(repository.findById("character-id")).thenReturn(Optional.of(testCharacter));
            doThrow(new SecurityException("Access denied"))
                    .when(characterAccessValidator).validateControlAccess(testCharacter, testUser);

            // When & Then
            assertThatThrownBy(() -> characterService.uploadImage("character-id", file, testUser))
                    .isInstanceOf(SecurityException.class)
                    .hasMessage("Access denied");
            
            verify(repository).findById("character-id");
            verify(characterAccessValidator).validateControlAccess(testCharacter, testUser);
            verify(imageService, never()).processToBase64(any());
            verify(repository, never()).save(any(CharacterEntity.class));
        }

        @Test
        @DisplayName("Deve lançar exceção quando arquivo é nulo")
        void deveLancarExcecaoQuandoArquivoENulo() {
            // Given
            when(repository.findById("character-id")).thenReturn(Optional.of(testCharacter));
            doNothing().when(characterAccessValidator).validateControlAccess(testCharacter, testUser);
            when(imageService.processToBase64(null)).thenThrow(new IllegalArgumentException("Invalid or empty image file."));

            // When & Then
            assertThatThrownBy(() -> characterService.uploadImage("character-id", null, testUser))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Invalid or empty image file.");

            verify(repository).findById("character-id");
            verify(characterAccessValidator).validateControlAccess(testCharacter, testUser);
            verify(imageService).processToBase64(null);
            verify(repository, never()).save(any(CharacterEntity.class));
        }
    }

    @Nested
    @DisplayName("Testes de integração entre métodos")
    class IntegrationTests {

        @Test
        @DisplayName("Deve criar e depois buscar personagem consistentemente")
        void deveCriarEDepoisBuscarPersonagemConsistentemente() {
            // Given - Criar personagem
            when(repository.save(any(CharacterEntity.class))).thenAnswer(invocation -> {
                CharacterEntity character = invocation.getArgument(0);
                character.setId("new-character-id");
                return character;
            });

            CharacterInfoResponse createdCharacter = characterService.create(characterInfoRequest, testUser);
            
            // Given - Buscar personagem criado
            CharacterEntity savedCharacter = CharacterEntity.builder()
                    .id("new-character-id")
                    .name(characterInfoRequest.getName())
                    .profession(characterInfoRequest.getProfession())
                    .build();
            
            when(repository.findById("new-character-id")).thenReturn(Optional.of(savedCharacter));

            // When
            CharacterInfoResponse foundCharacter = characterService.getInfo("new-character-id");

            // Then
            assertThat(foundCharacter.getName()).isEqualTo(createdCharacter.getName());
            assertThat(foundCharacter.getProfession()).isEqualTo(createdCharacter.getProfession());
        }

        @Test
        @DisplayName("Deve validar acesso antes de qualquer operação de modificação")
        void deveValidarAcessoAntesDeQualquerOperacaoDeModificacao() {
            // Given
            MultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "content".getBytes());
            when(repository.findById("character-id")).thenReturn(Optional.of(testCharacter));
            doNothing().when(characterAccessValidator).validateControlAccess(testCharacter, testUser);
            when(imageService.processToBase64(any())).thenReturn("/path/image.jpg");
            when(repository.save(any(CharacterEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // When
            characterService.save(characterInfoRequest, "character-id", testUser);
            characterService.uploadImage("character-id", file, testUser);

            // Then
            verify(characterAccessValidator, times(2)).validateControlAccess(testCharacter, testUser);
        }
    }

    @Nested
    @DisplayName("Testes de Edge Cases e Validação Avançada")
    class EdgeCasesAndValidationTests {

        @Test
        @DisplayName("Deve lidar com caracteres especiais no nome do personagem")
        void deveLidarComCaracteresEspeciaisNoNomeDoPersonagem() {
            // Given
            CharacterInfoRequest requestWithSpecialChars = CharacterInfoRequest.builder()
                    .name("Ælfred Ñoñó-Müller")
                    .profession("Warrior")
                    .birthDate(LocalDate.of(1990, 1, 1))
                    .birthPlace("Test City")
                    .gender("MALE")
                    .age(30)
                    .apparentAge(28)
                    .heightCm(180)
                    .weightKg(75)
                    .religion("Test Religion")
                    .build();

            when(repository.save(any(CharacterEntity.class))).thenAnswer(invocation -> {
                CharacterEntity saved = invocation.getArgument(0);
                saved.setId("special-char-id");
                return saved;
            });

            // When
            CharacterInfoResponse response = characterService.create(requestWithSpecialChars, testUser);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.getName()).isEqualTo("Ælfred Ñoñó-Müller");
            verify(repository).save(any(CharacterEntity.class));
        }

        @Test
        @DisplayName("Deve validar limites extremos de idade")
        void deveValidarLimitesExtremosDeIdade() {
            // Given
            CharacterInfoRequest requestWithExtremeAge = CharacterInfoRequest.builder()
                    .name("Ancient Character")
                    .profession("Elder")
                    .birthDate(LocalDate.of(1000, 1, 1))
                    .birthPlace("Ancient City")
                    .gender("UNKNOWN")
                    .age(1000)
                    .apparentAge(999)
                    .heightCm(150)
                    .weightKg(50)
                    .religion("Ancient Religion")
                    .build();

            when(repository.save(any(CharacterEntity.class))).thenAnswer(invocation -> {
                CharacterEntity saved = invocation.getArgument(0);
                saved.setId("ancient-id");
                return saved;
            });

            // When
            CharacterInfoResponse response = characterService.create(requestWithExtremeAge, testUser);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.getAge()).isEqualTo(1000);
            assertThat(response.getApparentAge()).isEqualTo(999);
            verify(repository).save(any(CharacterEntity.class));
        }

        @Test
        @DisplayName("Deve lidar com valores extremos de altura e peso")
        void deveLidarComValoresExtremosDeAlturaEPeso() {
            // Given
            CharacterInfoRequest requestWithExtremeValues = CharacterInfoRequest.builder()
                    .name("Giant Character")
                    .profession("Giant")
                    .birthDate(LocalDate.of(1990, 1, 1))
                    .birthPlace("Giant Land")
                    .gender("MALE")
                    .age(30)
                    .apparentAge(28)
                    .heightCm(300) // 3 metros
                    .weightKg(500) // 500kg
                    .religion("Giant Religion")
                    .build();

            when(repository.save(any(CharacterEntity.class))).thenAnswer(invocation -> {
                CharacterEntity saved = invocation.getArgument(0);
                saved.setId("giant-id");
                return saved;
            });

            // When
            CharacterInfoResponse response = characterService.create(requestWithExtremeValues, testUser);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.getHeightCm()).isEqualTo(300);
            assertThat(response.getWeightKg()).isEqualTo(500);
            verify(repository).save(any(CharacterEntity.class));
        }

        @Test
        @DisplayName("Deve processar texto longo em campos de string")
        void deveProcessarTextoLongoEmCamposDeString() {
            // Given
            String longName = "A".repeat(99); // String com 99 caracteres (dentro do limite de 100)
            String longProfession = "B".repeat(200); // String com 200 caracteres
            String longBirthPlace = "C".repeat(300); // String com 300 caracteres
            String longReligion = "D".repeat(150); // String com 150 caracteres
            
            CharacterInfoRequest requestWithLongText = CharacterInfoRequest.builder()
                    .name(longName)
                    .profession(longProfession)
                    .birthDate(LocalDate.of(1990, 1, 1))
                    .birthPlace(longBirthPlace)
                    .gender("MALE")
                    .age(30)
                    .apparentAge(28)
                    .heightCm(180)
                    .weightKg(75)
                    .religion(longReligion)
                    .build();

            when(repository.save(any(CharacterEntity.class))).thenAnswer(invocation -> {
                CharacterEntity saved = invocation.getArgument(0);
                saved.setId("long-text-id");
                return saved;
            });

            // When
            CharacterInfoResponse response = characterService.create(requestWithLongText, testUser);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.getName()).hasSize(99);
            assertThat(response.getProfession()).hasSize(200);
            verify(repository).save(any(CharacterEntity.class));
        }

        @Test
        @DisplayName("Deve lidar com datas futuras")
        void deveLidarComDatasFuturas() {
            // Given
            LocalDate futureDate = LocalDate.now().plusYears(100);
            CharacterInfoRequest requestWithFutureDate = CharacterInfoRequest.builder()
                    .name("Future Character")
                    .profession("Time Traveler")
                    .birthDate(futureDate)
                    .birthPlace("Future City")
                    .gender("UNKNOWN")
                    .age(-50) // Idade negativa para personagem do futuro
                    .apparentAge(25)
                    .heightCm(180)
                    .weightKg(75)
                    .religion("Future Religion")
                    .build();

            when(repository.save(any(CharacterEntity.class))).thenAnswer(invocation -> {
                CharacterEntity saved = invocation.getArgument(0);
                saved.setId("future-id");
                return saved;
            });

            // When
            CharacterInfoResponse response = characterService.create(requestWithFutureDate, testUser);

            // Then
            assertThat(response).isNotNull();
            assertThat(response.getBirthDate()).isEqualTo(futureDate);
            assertThat(response.getAge()).isEqualTo(-50);
            verify(repository).save(any(CharacterEntity.class));
        }

        @Test
        @DisplayName("Deve manter consistência em múltiplas operações simultâneas")
        void deveManterConsistenciaEmMultiplasOperacoesSimultaneas() {
            // Given
            when(repository.findById("character-id")).thenReturn(Optional.of(testCharacter));
            when(repository.save(any(CharacterEntity.class))).thenReturn(testCharacter);
            doNothing().when(characterAccessValidator).validateControlAccess(testCharacter, testUser);

            // When - Simular múltiplas operações
            CharacterInfoResponse getResponse = characterService.getInfo("character-id");
            CharacterInfoResponse saveResponse = characterService.save(characterInfoRequest, "character-id", testUser);
            CharacterEntity getByIdResponse = characterService.getById("character-id");

            // Then
            assertThat(getResponse).isNotNull();
            assertThat(saveResponse).isNotNull();
            assertThat(getByIdResponse).isNotNull();
            
            // Verificar que todas as operações foram executadas
            verify(repository, times(3)).findById("character-id");
            verify(repository).save(any(CharacterEntity.class));
            verify(characterAccessValidator).validateControlAccess(testCharacter, testUser);
        }
    }

    @Nested
    @DisplayName("Testes de Performance e Otimização")
    class PerformanceTests {

        @Test
        @DisplayName("Deve executar busca por ID em tempo aceitável")
        void deveExecutarBuscaPorIdEmTempoAceitavel() {
            // Given
            when(repository.findById("character-id")).thenReturn(Optional.of(testCharacter));

            // When
            long startTime = System.currentTimeMillis();
            CharacterEntity result = characterService.getById("character-id");
            long endTime = System.currentTimeMillis();

            // Then
            assertThat(result).isNotNull();
            assertThat(endTime - startTime).isLessThan(100); // Menos de 100ms
            verify(repository).findById("character-id");
        }

        @Test
        @DisplayName("Deve otimizar múltiplas chamadas de busca")
        void deveOtimizarMultiplasChamadasDeBusca() {
            // Given
            when(repository.findById("character-id")).thenReturn(Optional.of(testCharacter));

            // When
            long startTime = System.currentTimeMillis();
            for (int i = 0; i < 10; i++) {
                characterService.getById("character-id");
            }
            long endTime = System.currentTimeMillis();

            // Then
            assertThat(endTime - startTime).isLessThan(500); // Menos de 500ms para 10 chamadas
            verify(repository, times(10)).findById("character-id");
        }

        @Test
        @DisplayName("Deve processar criação de personagem eficientemente")
        void deveProcessarCriacaoDePersonagemEficientemente() {
            // Given
            when(repository.save(any(CharacterEntity.class))).thenAnswer(invocation -> {
                CharacterEntity saved = invocation.getArgument(0);
                saved.setId("performance-id");
                return saved;
            });

            // When
            long startTime = System.currentTimeMillis();
            CharacterInfoResponse response = characterService.create(characterInfoRequest, testUser);
            long endTime = System.currentTimeMillis();

            // Then
            assertThat(response).isNotNull();
            assertThat(endTime - startTime).isLessThan(200); // Menos de 200ms
            verify(repository).save(any(CharacterEntity.class));
        }
    }
}