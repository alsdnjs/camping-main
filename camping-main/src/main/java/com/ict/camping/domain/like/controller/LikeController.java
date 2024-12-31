package com.ict.camping.domain.like.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.util.Data;
import com.ict.camping.domain.auth.vo.DataVO;
import com.ict.camping.domain.like.service.LikeService;
import com.ict.camping.domain.like.vo.LikeVO;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/api/like")
public class LikeController {
    @Autowired LikeService likeService;

    @PostMapping("/update")
    public DataVO getLikeUpdata(@ModelAttribute("data") LikeVO lvo) {
        DataVO dataVO = new DataVO();
        try {
            int result = likeService.checkLike(lvo);
            if (result>0) {
                int deleteLike = likeService.deleteLike(lvo);
                
            } else {
                int insertLike = likeService.insertLike(lvo);
            }
            dataVO.setSuccess(true);
        } catch (Exception e) {
            System.out.println(e);
            dataVO.setSuccess(false);
        }
        return dataVO;
    }
    @GetMapping("/status")
    public DataVO getLikeStatus(@ModelAttribute("data") LikeVO lvo) {
        DataVO dataVO = new DataVO();
        try {
            int result = likeService.checkLike(lvo);
            if(result>0){
                dataVO.setData("true");
            } else{
                dataVO.setData("false");
            }
            dataVO.setSuccess(true);
        } catch (Exception e) {
            dataVO.setMessage("찜 데이터를 불러오는 중 오류 발생");
        }
        return dataVO;
    }
    
}
