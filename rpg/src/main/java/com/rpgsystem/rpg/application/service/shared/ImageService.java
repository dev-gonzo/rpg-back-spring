package com.rpgsystem.rpg.application.service.shared;

import com.rpgsystem.rpg.domain.exception.ImageProcessingException;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class ImageService {

    public String processToBase64(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Invalid or empty image file.");
        }

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            // Resize to 1080px max width, convert to JPEG with 80% quality
            Thumbnails.of(file.getInputStream())
                    .size(1080, 1080)
                    .outputFormat("jpeg")
                    .outputQuality(0.8)
                    .toOutputStream(outputStream);

            byte[] processedBytes = outputStream.toByteArray();
            String base64 = Base64.getEncoder().encodeToString(processedBytes);
            return "data:image/jpeg;base64," + base64;

        } catch (IOException e) {
            throw new ImageProcessingException("Failed to process image.", e);
        }
    }
}
