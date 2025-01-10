package com.ict.camping.domain.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ict.camping.domain.chat.service.FileStorageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/upload")
public class FileUploadController {
  
  private final FileStorageService fileStorageService;
  
  @Autowired
  public FileUploadController(FileStorageService fileStorageService){
    this.fileStorageService = fileStorageService;
  }

  @PostMapping("/file")
  public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file){
      try {
        String fileUrl = fileStorageService.storeFile(file);
        return ResponseEntity.ok(fileUrl);
      } catch (Exception e) {
        return ResponseEntity.status(500).body("업로드 실패");
      }
  }
  

}
