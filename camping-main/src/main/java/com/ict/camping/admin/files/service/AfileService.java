package com.ict.camping.admin.files.service;

import com.ict.camping.admin.files.vo.AfileVO;

public interface AfileService {

    int insertFile(AfileVO fileVO); // 파일 삽입
    int updateFile(AfileVO fileVO);
} 
