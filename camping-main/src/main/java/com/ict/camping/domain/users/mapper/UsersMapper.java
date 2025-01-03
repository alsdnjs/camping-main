package com.ict.camping.domain.users.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.ict.camping.domain.auth.vo.UserDetailsVO;
import com.ict.camping.domain.users.vo.UsersVO;

@Mapper
public interface UsersMapper  {
  int joinUser(UsersVO uvo);
  int joinBusinessUser(UsersVO uvo);
  String getUserIdxById(String id);
  String usersIdCheck(String id);
  UsersVO getUsersById(String id);
  UsersVO findUserByProvider(UsersVO uvo);
  int insertUser(UsersVO uvo);
  UserDetailsVO getUserById(String id);
  String getPasswordById(String id);
  int updatePassword(Map<String, String> map);
  int updateEmail(Map<String, String> map);

}
