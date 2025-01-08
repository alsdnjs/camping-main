package com.ict.camping.domain.regularMeeting.service;

import com.ict.camping.domain.regularMeeting.vo.RegularMeetingBoardImageVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RegularMeetingBoardImageService {
    List<String> uploadImages(List<MultipartFile> files) throws Exception;
    RegularMeetingBoardImageVO saveImage(RegularMeetingBoardImageVO imageVO);
}
