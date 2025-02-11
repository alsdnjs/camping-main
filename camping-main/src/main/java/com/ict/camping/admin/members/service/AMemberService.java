package com.ict.camping.admin.members.service;

import java.util.List;

import com.ict.camping.admin.members.vo.AMemberVO;
import com.ict.camping.admin.members.vo.AOperatorVO;

public interface AMemberService {
    // 리스트
    List<AMemberVO> getAllMembers();
    // 디테일
    AMemberVO getMemberDetail(String user_idx);
    // 사업자 리스트
    List<AOperatorVO> getAllOperators();
    // 사업자 디테일
    AOperatorVO getOperatorDetail(String business_idx);
    // 회원 등록
    int insertMembers(AMemberVO memberVO);
    // 사업자 등록
    int insertOperators(AOperatorVO operatorVO);
    // 아이디 중복체크
    boolean isIdDuplicate(String id);
    // 회원 존재여부 확인
    boolean isIdxDuplicate(String user_idx);
    // 회원 수정
    int updateMembers(AMemberVO memberVO);
    // 사업자 존재여부 확인
    boolean isBuisnessDuplicate(String user_idx);
    // 사업자 수정
    int updateBusiness(AOperatorVO operatorVO);
    // 사업자 삭제
    boolean deleteOperator(String business_idx);
    // 회원 삭제
    boolean deleteUser(String user_idx);
    // 회원 idx로 사업자 idx 불러오기
    int getBusinessIdxByUserIdx(String user_idx);
    // 회원 제재
    int warnUser(AMemberVO memberVO);
    // 회원 제재 취소
    int warnDelete(int user_idx);
} 
