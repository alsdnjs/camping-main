package com.ict.camping.domain.auth.service;

import java.util.ArrayList;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.ict.camping.domain.auth.mapper.AuthMapper;
import com.ict.camping.domain.auth.vo.UserDetailsVO;
import com.ict.camping.domain.users.mapper.UsersMapper;
import com.ict.camping.domain.users.vo.UsersVO;

@Service
public class MyUserDetailService implements UserDetailsService{

  @Autowired
  private AuthMapper authMapper;

  @Autowired
  private UsersMapper usersMapper;

  @Override
  public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
    UserDetailsVO user = authMapper.getUserDetail(id);
    if (user == null) {
      throw new UsernameNotFoundException("없는 아이디 입니다.");
    }
    
    return new User(user.getId(), user.getPassword(), new ArrayList<>());
  }

  // DB에서 개인 정보 추출
  public UserDetailsVO getUserDetail(String id){
    return authMapper.getUserDetail(id);
  }

  // 정보를 받아서 DB에 저장 하기
  public UserDetails loadUserByOAuth2User(OAuth2User oAuth2User, String provider){
    String email = oAuth2User.getAttribute("email");
    String name = oAuth2User.getAttribute("name");
    String phone = oAuth2User.getAttribute("phone");
    String id = oAuth2User.getAttribute("id");
    String user_idx = oAuth2User.getAttribute("user_idx");
    System.out.println("provider : " + provider);

    UsersVO uvo = new UsersVO();
    if (provider.equals("kakao")) {
        //카카오 ID는 Long 타입
        uvo.setSns_email_kakao(email);
        uvo.setUsername(name);
        uvo.setId(id);
        uvo.setUser_idx(user_idx);
        uvo.setSns_provider("kakao");
      } else if (provider.equals("naver")) {
        // 네이버 ID는 String 타입으로 반환됨
        uvo.setSns_email_naver(email);
        uvo.setUsername(name);
        uvo.setId(id);
        uvo.setUser_idx(user_idx);
        uvo.setPhone(phone.replace("-", ""));
        uvo.setSns_provider("naver");
      } else if (provider.equals("google")) {
        // google ID 는 sub로 받지만 CustomerOAuth2UserService에서 {id : getAttribute("sub")}로 이미 받음
        uvo.setSns_email_google(email);
        uvo.setUsername(name);
        uvo.setId(id);
        uvo.setUser_idx(user_idx);
        uvo.setSns_provider("google");
    }

    // 이미 존재하는 아이디인지 판단
    UsersVO uvo2 = usersMapper.findUserByProvider(uvo);

    System.out.println("uvo 제공받은 개인정보들 : " + uvo);
    System.out.println("uvo2 제공받은 개인정보 + 내가 입력한 정보들 : " + uvo2);
    // 처음 로그인 시 DB에 내용 저장장
    if(uvo2 == null){
      usersMapper.insertUser(uvo);
    }
    return new User(uvo.getId(), "", Collections.emptyList()); // 빈 리스트로 권한 설정
  }

  public UserDetailsVO getUserProfile(String userId) {
    // userId로 사용자 정보를 조회하여 반환
    UserDetailsVO user = usersMapper.getUserById(userId);
    if (user != null) {
        UserDetailsVO uvo = new UserDetailsVO();
        uvo.setId(user.getId());
        uvo.setUsername(user.getUsername());
        uvo.setEmail(user.getEmail());
        // 더 필요한 필드들을 설정
        String user_idx = usersMapper.getUserIdxById(userId);

        uvo.setUser_idx(user_idx);
        return uvo;
    }
    return null;
  }
}