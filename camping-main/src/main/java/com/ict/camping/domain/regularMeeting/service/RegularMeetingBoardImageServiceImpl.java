package com.ict.camping.domain.regularMeeting.service;

import com.ict.camping.domain.regularMeeting.mapper.RegularMeetingBoardImageMapper;
import com.ict.camping.domain.regularMeeting.vo.RegularMeetingBoardImageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class RegularMeetingBoardImageServiceImpl implements RegularMeetingBoardImageService {

    private static final Logger logger = LoggerFactory.getLogger(RegularMeetingBoardImageServiceImpl.class);

    @Autowired
    private RegularMeetingBoardImageMapper imageMapper;

    private final String uploadDir = "src/main/resources/static/upload/"; // 실제 경로로 수정 필요

    @Override
    public List<String> uploadImages(List<MultipartFile> files) throws Exception {
        List<String> imageUrls = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    // 파일 형식 검증 (예: JPEG, PNG)
                    String contentType = file.getContentType();
                    if (!("image/jpeg".equals(contentType) || "image/png".equals(contentType))) {
                        throw new IllegalArgumentException("지원하지 않는 파일 형식입니다. JPEG 또는 PNG 파일을 업로드하세요.");
                    }

                    // 파일 크기 제한 (예: 5MB)
                    if (file.getSize() > 5 * 1024 * 1024) {
                        throw new IllegalArgumentException("파일 크기가 너무 큽니다. 5MB 이하의 파일을 업로드하세요.");
                    }

                    // 고유한 파일 이름 생성
                    String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
                    Path filePath = Paths.get(uploadDir, filename);
                    
                    // 디렉토리가 존재하지 않으면 생성
                    if (!Files.exists(filePath.getParent())) {
                        Files.createDirectories(filePath.getParent());
                        logger.info("업로드 디렉토리 생성: {}", filePath.getParent().toString());
                    }

                    // 파일 저장
                    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                    imageUrls.add("/upload/" + filename); // 클라이언트가 접근할 수 있는 URL 경로
                    logger.info("파일 업로드 완료: {}", filename);
                } catch (Exception e) {
                    logger.error("파일 업로드 실패: {}", e.getMessage(), e);
                    throw e;
                }
            }
        }
        return imageUrls;
    }

    @Override
    public RegularMeetingBoardImageVO saveImage(RegularMeetingBoardImageVO imageVO) {
        try {
            imageMapper.insertImage(imageVO);
            logger.info("이미지 저장 완료: imageIdx={}, imageUrl={}", imageVO.getImage_idx(), imageVO.getImage_url());
            return imageVO;
        } catch (Exception e) {
            logger.error("이미지 저장 실패: {}", e.getMessage(), e);
            throw e;
        }
    }
}
