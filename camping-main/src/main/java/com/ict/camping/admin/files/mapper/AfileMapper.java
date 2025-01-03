package com.ict.camping.admin.files.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.ict.camping.admin.files.vo.AfileVO;

@Mapper
public interface AfileMapper {
    int insertnoticeFile(AfileVO file); // 파일 삽입
    int updatenoticeFile(AfileVO file);
}
