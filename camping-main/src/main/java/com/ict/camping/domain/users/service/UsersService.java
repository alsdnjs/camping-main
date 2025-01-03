package com.ict.camping.domain.users.service;

import java.util.List;

import com.ict.camping.domain.users.vo.UsersVO;

public interface UsersService {
  public int joinUser(UsersVO uvo);
  public String getUserIdxById(String id);
  
  public String usersIdCheck(String id);
  public UsersVO getUserById(String id);
  public UsersVO findUserByProvider(UsersVO uvo);
  public int insertUser(UsersVO uvo);

  public String getPasswordById(String id);
  public int updatePassword(String id, String password);
  public int updateEmail(String id, String email);
}
