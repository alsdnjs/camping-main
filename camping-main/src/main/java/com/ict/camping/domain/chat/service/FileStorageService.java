package com.ict.camping.domain.chat.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Path;

@Service
public class FileStorageService {
    private final String uploadDir = "uploads/";

    public String storeFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IOException("파일이 비어 있습니다.");
        }

        // 파일명 중복 방지를 위해 UUID 추가 가능
        String fileName = file.getOriginalFilename();
        Path path = Paths.get(uploadDir + fileName);
        File dest = path.toFile();
        file.transferTo(dest);

        return "/uploads/" + fileName; // 프론트엔드에서 접근 가능한 URL 반환
    }
}
