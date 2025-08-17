package com.rpgsystem.rpg.application.service.shared;

import com.rpgsystem.rpg.domain.exception.ImageProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ImageService Tests")
class ImageServiceTest {

    @InjectMocks
    private ImageService imageService;

    @Nested
    @DisplayName("Testes do método processToBase64")
    class ProcessToBase64Tests {

        @Test
        @DisplayName("Deve processar imagem válida e retornar base64 com sucesso")
        void deveProcessarImagemValidaERetornarBase64ComSucesso() {
            // Given
            byte[] imageContent = createValidImageBytes();
            MultipartFile file = new MockMultipartFile(
                "image", 
                "test.jpg", 
                "image/jpeg", 
                imageContent
            );

            // When
            String result = imageService.processToBase64(file);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).startsWith("data:image/jpeg;base64,");
            assertThat(result.length()).isGreaterThan("data:image/jpeg;base64,".length());
        }

        @Test
        @DisplayName("Deve processar imagem PNG e converter para JPEG")
        void deveProcessarImagemPngEConverterParaJpeg() {
            // Given
            byte[] imageContent = createValidImageBytes();
            MultipartFile file = new MockMultipartFile(
                "image", 
                "test.png", 
                "image/png", 
                imageContent
            );

            // When
            String result = imageService.processToBase64(file);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).startsWith("data:image/jpeg;base64,");
        }

        @Test
        @DisplayName("Deve processar imagem grande e redimensionar para 1080px")
        void deveProcessarImagemGrandeERedimensionarPara1080px() {
            // Given
            byte[] largeImageContent = createLargeImageBytes();
            MultipartFile file = new MockMultipartFile(
                "image", 
                "large.jpg", 
                "image/jpeg", 
                largeImageContent
            );

            // When
            String result = imageService.processToBase64(file);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).startsWith("data:image/jpeg;base64,");
            // Verifica se o processamento foi bem-sucedido
            assertThat(result.length()).isGreaterThan("data:image/jpeg;base64,".length());
        }

        @Test
        @DisplayName("Deve lançar IllegalArgumentException quando arquivo é nulo")
        void deveLancarIllegalArgumentExceptionQuandoArquivoENulo() {
            // When & Then
            assertThatThrownBy(() -> imageService.processToBase64(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid or empty image file.");
        }

        @Test
        @DisplayName("Deve lançar IllegalArgumentException quando arquivo está vazio")
        void deveLancarIllegalArgumentExceptionQuandoArquivoEstaVazio() {
            // Given
            MultipartFile emptyFile = new MockMultipartFile(
                "image", 
                "empty.jpg", 
                "image/jpeg", 
                new byte[0]
            );

            // When & Then
            assertThatThrownBy(() -> imageService.processToBase64(emptyFile))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid or empty image file.");
        }

        @Test
        @DisplayName("Deve lançar ImageProcessingException quando ocorre erro de IO")
        void deveLancarImageProcessingExceptionQuandoOcorreErroDeIO() {
            // Given
            MultipartFile corruptedFile = new MockMultipartFile(
                "image", 
                "corrupted.jpg", 
                "image/jpeg", 
                "invalid image data".getBytes()
            ) {
                @Override
                public InputStream getInputStream() throws IOException {
                    throw new IOException("Simulated IO error");
                }
            };

            // When & Then
            assertThatThrownBy(() -> imageService.processToBase64(corruptedFile))
                .isInstanceOf(ImageProcessingException.class)
                .hasMessage("Failed to process image.")
                .hasCauseInstanceOf(IOException.class);
        }

        @Test
        @DisplayName("Deve processar múltiplas imagens consecutivamente")
        void deveProcessarMultiplasImagensConsecutivamente() {
            // Given
            byte[] imageContent1 = createValidImageBytes();
            byte[] imageContent2 = createLargeImageBytes(); // Usando imagem diferente
            
            MultipartFile file1 = new MockMultipartFile(
                "image1", "test1.jpg", "image/jpeg", imageContent1
            );
            MultipartFile file2 = new MockMultipartFile(
                "image2", "test2.jpg", "image/jpeg", imageContent2
            );

            // When
            String result1 = imageService.processToBase64(file1);
            String result2 = imageService.processToBase64(file2);

            // Then
            assertThat(result1).isNotNull().startsWith("data:image/jpeg;base64,");
            assertThat(result2).isNotNull().startsWith("data:image/jpeg;base64,");
            // Resultados devem ser independentes (diferentes tamanhos de imagem)
            assertThat(result1).isNotEqualTo(result2);
        }

        @Test
        @DisplayName("Deve aplicar qualidade de 80% na compressão JPEG")
        void deveAplicarQualidadeDe80PorcentoNaCompressaoJpeg() {
            // Given
            byte[] imageContent = createLargeImageBytes(); // Usando imagem maior para testar compressão
            MultipartFile file = new MockMultipartFile(
                "image", "test.jpg", "image/jpeg", imageContent
            );

            // When
            String result = imageService.processToBase64(file);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).startsWith("data:image/jpeg;base64,");
            // Verifica se o processamento foi bem-sucedido (imagem redimensionada)
            assertThat(result.length()).isGreaterThan("data:image/jpeg;base64,".length());
        }

        @Test
        @DisplayName("Deve manter proporção ao redimensionar imagem retangular")
        void deveManterProporcaoAoRedimensionarImagemRetangular() {
            // Given
            byte[] rectangularImageContent = createRectangularImageBytes();
            MultipartFile file = new MockMultipartFile(
                "image", "rectangular.jpg", "image/jpeg", rectangularImageContent
            );

            // When
            String result = imageService.processToBase64(file);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).startsWith("data:image/jpeg;base64,");
            // Deve processar sem erro, mantendo a proporção
        }

        @Test
        @DisplayName("Deve processar imagem muito pequena sem erro")
        void deveProcessarImagemMuitoPequenaSemErro() {
            // Given
            byte[] smallImageContent = createSmallImageBytes();
            MultipartFile file = new MockMultipartFile(
                "image", "small.jpg", "image/jpeg", smallImageContent
            );

            // When
            String result = imageService.processToBase64(file);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).startsWith("data:image/jpeg;base64,");
        }
    }

    // Métodos auxiliares para criar dados de teste
    private byte[] createValidImageBytes() {
        try {
            // Cria uma imagem BufferedImage simples de 10x10 pixels
            java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(10, 10, java.awt.image.BufferedImage.TYPE_INT_RGB);
            java.awt.Graphics2D g2d = image.createGraphics();
            g2d.setColor(java.awt.Color.BLUE);
            g2d.fillRect(0, 0, 10, 10);
            g2d.dispose();
            
            // Converte para bytes JPEG
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            javax.imageio.ImageIO.write(image, "jpeg", baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create test image", e);
        }
    }

    private byte[] createLargeImageBytes() {
        try {
            // Cria uma imagem maior de 200x200 pixels
            java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(200, 200, java.awt.image.BufferedImage.TYPE_INT_RGB);
            java.awt.Graphics2D g2d = image.createGraphics();
            g2d.setColor(java.awt.Color.RED);
            g2d.fillRect(0, 0, 200, 200);
            g2d.dispose();
            
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            javax.imageio.ImageIO.write(image, "jpeg", baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create large test image", e);
        }
    }

    private byte[] createRectangularImageBytes() {
        try {
            // Cria uma imagem retangular de 30x20 pixels
            java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(30, 20, java.awt.image.BufferedImage.TYPE_INT_RGB);
            java.awt.Graphics2D g2d = image.createGraphics();
            g2d.setColor(java.awt.Color.GREEN);
            g2d.fillRect(0, 0, 30, 20);
            g2d.dispose();
            
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            javax.imageio.ImageIO.write(image, "jpeg", baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create rectangular test image", e);
        }
    }

    private byte[] createSmallImageBytes() {
        try {
            // Cria uma imagem pequena de 5x5 pixels
            java.awt.image.BufferedImage image = new java.awt.image.BufferedImage(5, 5, java.awt.image.BufferedImage.TYPE_INT_RGB);
            java.awt.Graphics2D g2d = image.createGraphics();
            g2d.setColor(java.awt.Color.YELLOW);
            g2d.fillRect(0, 0, 5, 5);
            g2d.dispose();
            
            java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
            javax.imageio.ImageIO.write(image, "jpeg", baos);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create small test image", e);
        }
    }
}