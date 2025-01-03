package com.ict.camping.admin.files.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.camping.admin.files.mapper.AfileMapper;
import com.ict.camping.admin.files.vo.AfileVO;

@Service
public class AfileServiceImpl implements AfileService{
    @Autowired
    private AfileMapper afileMapper;

    @Override
    public int insertFile(AfileVO fileVO) {
        afileMapper.insertnoticeFile(fileVO);
        return fileVO.getFile_idx();
    }

    @Override
    public int updateFile(AfileVO fileVO) {
        return afileMapper.updatenoticeFile(fileVO);
    }
}
