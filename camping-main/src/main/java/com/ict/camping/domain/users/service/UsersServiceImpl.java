package com.ict.camping.domain.users.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ict.camping.domain.users.mapper.UsersMapper;
import com.ict.camping.domain.users.vo.UsersVO;



@Service
public class UsersServiceImpl implements UsersService{
  @Autowired
  private UsersMapper usersMapper;

  @Override
  public int joinUser(UsersVO uvo) {

    // 일반회원가입
    int result = usersMapper.joinUser(uvo);
    
    // uvo에 사업자번호가 없다면 바로 반환
    if(uvo.getBusiness_number().isEmpty() || uvo.getBusiness_number().equals("")){
      return result;
    }
    
    // 아니면 사업자 테이블에 사업자 가입
    String idx = usersMapper.getUserIdxById(uvo.getId());
    uvo.setUser_idx(idx);
    return usersMapper.joinBusinessUser(uvo);
  }


  @Override
  public String usersIdCheck(String id) {
    return usersMapper.usersIdCheck(id);
  }

  @Override
  public UsersVO getUserById(String id) {
    return usersMapper.getUsersById(id);
  }

  @Override
  public UsersVO findUserByProvider(UsersVO uvo) {
    return usersMapper.findUserByProvider(uvo);
  }

  @Override
  public int insertUser(UsersVO uvo) {
    return usersMapper.insertUser(uvo);
  }

  @Override
  public String getUserIdxById(String id) {
    return usersMapper.getUserIdxById(id);
  }


  @Override
  public String getPasswordById(String id) {
    return usersMapper.getPasswordById(id);
  }


  @Override
  public int updatePassword(String id, String password) {
    Map<String, String> map = new HashMap<>();
    map.put("id", id);
    map.put("password", password);
    return usersMapper.updatePassword(map);
  }


  @Override
  public int updateEmail(String id, String email) {
    Map<String, String> map = new HashMap<>();
    map.put("id", id);
    map.put("email", email);
    return usersMapper.updateEmail(map);
  }
}
