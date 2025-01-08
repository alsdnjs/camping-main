package com.ict.camping.common.util;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.Map;

public class FileUtils {

    // 파일을 저장하고 파일 이름과 경로를 반환하는 메서드
    public String saveFile(MultipartFile file, String uploadDir) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
        }

        File dest = new File(uploadDirFile, fileName);
        file.transferTo(dest);

        return fileName;
    }
    // // 파일을 저장하고 파일 이름과 경로를 반환하는 메서드
    // public Map<String, String> saveFile(MultipartFile file, String uploadDir) throws IOException {
    //     String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

    //     File uploadDirFile = new File(uploadDir);
    //     if (!uploadDirFile.exists()) {
    //         uploadDirFile.mkdirs();
    //     }

    //     File dest = new File(uploadDirFile, fileName);
    //     file.transferTo(dest);

    //     // 파일명과 파일 경로를 Map으로 반환
    //     Map<String, String> fileInfo = new HashMap<>();
    //     fileInfo.put("fileName", fileName);
    //     fileInfo.put("filePath", dest.getAbsolutePath());

    //     return fileInfo;
    // }
}