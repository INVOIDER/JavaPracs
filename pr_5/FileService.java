package com.example.simple.services;

import com.example.simple.dto.FileInfoDTO;
import com.example.simple.dto.FileUploadDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;
// Import statements

@Service
@RequiredArgsConstructor
public class FileService {

    // Existing code

    public void save(FileUploadDTO fileUploadDTO) {
        for (MultipartFile multipartFile : fileUploadDTO.getFiles()) {
            String newFileName = UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();
            saveFile(multipartFile, newFileName);
        }
    }

    // Existing code

    private void saveFile(MultipartFile file, String fileName) {
        try {
            Path root = Paths.get(uploadPath);
            if (!Files.exists(root)) {
                init();
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, root.resolve(fileName));
            }
        } catch (IOException e) {
            // Log the error instead of throwing a RuntimeException
            e.printStackTrace();
        }
    }

    // Existing code

}
