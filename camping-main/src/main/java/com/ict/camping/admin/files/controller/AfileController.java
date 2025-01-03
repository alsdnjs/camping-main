package com.ict.camping.admin.files.controller;

import com.ict.camping.admin.files.service.AfileService;
import com.ict.camping.admin.files.vo.AfileVO;
import com.ict.camping.admin.notices.service.ANoticeService;
import com.ict.camping.admin.notices.vo.ANoticeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/files")
public class AfileController {

    @Autowired
    private AfileService afileService;

    @Autowired
    private ANoticeService noticeService;

    // 절대 경로 설정
    private static final String FILE_DIRECTORY = "C:/Users/5/Desktop/camping-main/camping-main/src/main/resources/images/";

    // 파일 저장 처리
    private String saveFile(MultipartFile file) throws IOException {
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename(); // 고유 파일 이름 생성
        File dest = new File(FILE_DIRECTORY, fileName); // 디렉토리와 파일 이름 결합

        // 디렉토리 생성 확인 및 생성
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        // 파일 저장
        file.transferTo(dest);
        return dest.getAbsolutePath(); // 저장된 파일의 절대 경로 반환
    }

    @PostMapping("/admin/upload")
    public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        try {
            String filePath = saveFile(file);

            AfileVO fileVO = new AfileVO();
            fileVO.setFile_name(new File(filePath).getName());
            fileVO.setFile_path(filePath);
            fileVO.setFile_size((int) file.getSize());
            fileVO.setFile_type("image");
            fileVO.setType("공지사항");

            int fileIdx = afileService.insertFile(fileVO);

            response.put("file_idx", fileIdx);
            response.put("file_path", filePath);
            response.put("message", "파일 업로드 성공");
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            response.put("message", "파일 업로드 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/admin/update-file/{notice_idx}")
    public ResponseEntity<Map<String, Object>> updateFile(
            @PathVariable("notice_idx") String noticeIdx,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        try {
            ANoticeVO existingNotice = noticeService.getNoticeDetail(noticeIdx);
            if (existingNotice == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("message", "해당 공지사항을 찾을 수 없습니다."));
            }

            if (file != null) {
                String existingFilePath = existingNotice.getFile_path();
                if (existingFilePath != null) {
                    File oldFile = new File(existingFilePath);
                    if (oldFile.exists()) {
                        oldFile.delete(); // 기존 파일 삭제
                    }
                }

                String newFilePath = saveFile(file);
                AfileVO updatedFile = new AfileVO();
                updatedFile.setFile_idx(existingNotice.getImage_idx());
                updatedFile.setFile_name(new File(newFilePath).getName());
                updatedFile.setFile_path(newFilePath);
                updatedFile.setFile_size((int) file.getSize());
                updatedFile.setFile_type("image");

                int updateResult = afileService.updateFile(updatedFile);
                if (updateResult == 0) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(Map.of("message", "파일 정보 업데이트에 실패했습니다."));
                }

                response.put("file_idx", existingNotice.getImage_idx());
                response.put("file_path", newFilePath);
                response.put("message", "파일 수정 성공 및 공지사항 파일 정보 업데이트 완료");
            } else {
                response.put("message", "새로운 파일이 업로드되지 않았습니다.");
            }

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            response.put("message", "파일 수정 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
