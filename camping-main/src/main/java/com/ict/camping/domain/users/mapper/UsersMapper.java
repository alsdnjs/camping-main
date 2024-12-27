package com.ict.camping.domain.users.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ict.camping.domain.auth.vo.UserDetailsVO;
import com.ict.camping.domain.users.vo.UsersVO;

@Mapper
public interface UsersMapper  {
  int joinUser(UsersVO mvo);
  int joinBusinessUser(UsersVO mvo);
  String getUserIdxById(String id);
  String usersIdCheck(String id);
  UsersVO getUsersById(String id);
  UsersVO findUserByProvider(UsersVO mvo);
  int insertUser(UsersVO mvo);
  UserDetailsVO getUserById(String id);
  String getPasswordById(String id);
  int updatePassword(Map<String, String> map);
  
}