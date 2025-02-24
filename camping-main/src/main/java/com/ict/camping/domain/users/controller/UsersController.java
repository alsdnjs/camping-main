package com.ict.camping.domain.users.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ict.camping.common.util.JwtUtil;
import com.ict.camping.domain.auth.service.MyUserDetailService;
import com.ict.camping.domain.auth.vo.DataVO;
import com.ict.camping.domain.auth.vo.UserDetailsVO;
import com.ict.camping.domain.myPage.vo.MyRegularMeetingVO;
import com.ict.camping.domain.users.service.UsersService;
import com.ict.camping.domain.users.vo.UsersVO;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Slf4j
@RestController
@RequestMapping("/api/users")
public class UsersController {

  @Autowired
  private UsersService service;
  @Autowired
  private JwtUtil jwtUtil;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private MyUserDetailService myUserDetailService;


  // 회원가입
  @PostMapping("/join")
  public DataVO usersJoin(@RequestBody UsersVO uvo){
    DataVO dataVO = new DataVO();

    System.out.println(uvo);


    // 비밀번호 암호화
    uvo.setPassword(passwordEncoder.encode(uvo.getPassword()));

    int result = service.joinUser(uvo);

    if (result > 0) {
      dataVO.setSuccess(true);
      dataVO.setMessage("회원가입 성공");
    } else {
      dataVO.setSuccess(false);
      dataVO.setMessage("회원가입 실패");
    }
    
    log.info("encoded password : " + uvo.getPassword());
    log.info(uvo + "\n");
    return dataVO;
  }

  
  // 일반 로그인 처리
  @PostMapping("/login")
  public DataVO memberLogin(@RequestBody UsersVO uvo) {
      DataVO dataVO = new DataVO();
      System.out.println("id : " + uvo.getId());
      System.out.println("password : " + uvo.getPassword());
      try {
        // 사용자 정보 조회
        UsersVO usersVO = service.getUsersById(uvo.getId());
        
        if(usersVO == null){
          dataVO.setSuccess(false);
          dataVO.setMessage("존재하지 않는 아이디입니다.");
          return dataVO;
        }
        System.out.println(usersVO);
        
        // 비밀번호 검증 받기
        if(!passwordEncoder.matches(uvo.getPassword(), usersVO.getPassword())){
          dataVO.setSuccess(false);
          dataVO.setMessage("비밀번호가 일치하지 않습니다.");
          return dataVO;
        }

        System.out.println(usersVO);
      try {

        System.out.println("제제회원");
        // 제제된 회원 찾아내기
        String warn = usersVO.getWarn();
        System.out.println(warn);
        if (usersVO.getWarn() != null) {
          // 제제 시작일
          String warnStart = usersVO.getWarn_start_at();
          // data형태 지정
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
          // 문자열을 LocalDate로 변환
          LocalDate startDate = LocalDate.parse(warnStart, formatter);
          // 7일 후 계산
          LocalDate endDate = startDate.plusDays(7);
          // 제제 사유
          String cause = usersVO.getWarn_cause();
          
          String message = "당신은 " + cause + "의 사유로 " + startDate + "부터" + endDate + "까지 이용이 정지됩니다.";
          System.out.println(message);
          dataVO.setMessage(message);
          dataVO.setSuccess(false);
          return dataVO;
        }
      } catch (NumberFormatException e) {
          System.out.println("Warn is not a valid number: " + usersVO.getWarn());
      }

        // JWT 토큰 생성 및 전송
        String token = jwtUtil.generateToken(uvo.getId());

        // SecurityContext에 인증 객체 설정
        // 다른 컨트롤러, 서비스, 또는 보안 필터에서 인증 정보를 쉽게 가져올 수 있습니다.
        // 인증된 사용자 이름이나 권한을 사용해 요청 처리.
        // 인증 객체가 설정되면, Spring Security의 유틸리티 메서드를 사용해 현재 사용자 정보를 쉽게 가져올 수 있습니다.
        // Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // String username = auth.getName(); // 인증된 사용자 이름

        // UsernamePasswordAuthenticationToken authenticationToken = new
        // UsernamePasswordAuthenticationToken(
        // membersVO.getM_id(), null, null); // 권한 정보 추가 가능
        // SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // log.info("로그인 성공, SecurityContext에 인증 객체 설정 완료");
        System.out.println("클라이언트로 보내는 member 정보");
        System.out.println(usersVO);

        dataVO.setData(usersVO);
        dataVO.setSuccess(true);
        dataVO.setMessage("로그인 성공");
        dataVO.setToken(token);
        return dataVO;

      } catch (Exception e) {
            dataVO.setSuccess(false);
            dataVO.setMessage("네트워크 오류" + e);
            return dataVO;
      }
    }


  // 회원가입 아이디 중복 체크
  @GetMapping(value = "/idCheck", produces = "application/json")
  public DataVO getIdCheck(@RequestParam("id") String id) {
    System.out.println(id);
    DataVO dataVO = new DataVO();
    String user_id = service.usersIdCheck(id);
    if (user_id == null || user_id.isEmpty()) {
      dataVO.setSuccess(true);
      dataVO.setMessage("사용 가능한 아이디 입니다.");
      System.out.println("존재하지 않는 아이디");
    } else {
      dataVO.setSuccess(false);  // 실패 상태로 설정
      dataVO.setMessage("사용 중인 아이디 입니다.");
      System.out.println("존재하는 아이디");
    }
    System.out.println(dataVO);

    return dataVO;
  }

  public UsersController(JwtUtil jwtUtil, MyUserDetailService myUserDetailService) {
    this.jwtUtil = jwtUtil;
    this.myUserDetailService = myUserDetailService;
}


  // 내 정보 페이지에서 토큰으로 내 정보 불러오기
  @GetMapping("/profile")
  public ResponseEntity<DataVO> getUserProfile(@RequestHeader("Authorization") String authorizationHeader) {
    DataVO dataVO = new DataVO();

    try {
        String userId = getIdFromToken(authorizationHeader, dataVO);

        // 사용자 정보 조회
        UsersVO userProfile = service.getUsersById(userId);
        System.out.println(userProfile);

        if (userProfile != null) {
            dataVO.setSuccess(true);
            dataVO.setData(userProfile);
            dataVO.setMessage("성공");
            return ResponseEntity.ok(dataVO);
        } else {
            dataVO.setSuccess(false);
            dataVO.setMessage("사용자 정보를 찾을 수 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dataVO);
        }

    } catch (Exception e) {
        // logger.error("Server error: ", e);
        dataVO.setSuccess(false);
        dataVO.setMessage("서버에서 오류가 발생했습니다.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dataVO);
    }
  }

  @PostMapping("/getUserInfo")
  public DataVO getUserInfo(@RequestBody String password, @RequestHeader("Authorization") String authorizationHeader) {
    DataVO dataVO = new DataVO();
    try {
      // 토큰 추출
      String token = authorizationHeader.replace("Bearer ", "");
      System.out.println("토큰 : " + token);

      // 토큰 검증
      if (!jwtUtil.validateToken(token)) {
        dataVO.setSuccess(false);
        dataVO.setMessage("유효하지 않은 토큰입니다.");
        return dataVO;
      }

      // 사용자 ID 추출
      String userId = jwtUtil.getUserIdFromToken(token);
      System.out.println("유저 아이디: "+  userId);

      // 사용자 정보 조회
      UsersVO userProfile = service.getUsersById(userId);
      System.out.println(userProfile);

      if (userProfile != null) {
        dataVO.setSuccess(true);
        dataVO.setData(userProfile);
        dataVO.setMessage("성공");
      } else {
        dataVO.setSuccess(false);
        dataVO.setMessage("사용자 정보를 찾을 수 없습니다.");
      }

    } catch(Exception e){
      dataVO.setSuccess(false);
      dataVO.setMessage("error");
    }
      
    return dataVO;
  }
  // 비밀번호 체크
  @PostMapping("/passwordCheck")
  public DataVO userPasswordCheck(@RequestParam("password") String password, @RequestHeader("Authorization") String authorizationHeader){

    DataVO dataVO = new DataVO();
    System.out.println("비밀번호 : " + password);
    System.out.println(password);
    try {
      // 토큰 추출
      String token = authorizationHeader.replace("Bearer ", "");
      System.out.println("토큰 : " + token);
      // 토큰 검증
      if (!jwtUtil.validateToken(token)) {
        dataVO.setSuccess(false);
        dataVO.setMessage("유효하지 않은 토큰입니다.");
        return dataVO;
      }
      
      // 사용자 ID 추출
      String userId = jwtUtil.getUserIdFromToken(token);
      System.out.println("유저 아이디: "+  userId);
      System.out.println("유저 비밀번호: "+  password);
      String encodedPassword = service.getPasswordById(userId);

      System.out.println("암호화된 비밀번호 ㅣ " + encodedPassword);
      // 비밀번호 검증 받기
      if(!passwordEncoder.matches(password, encodedPassword)){
        dataVO.setSuccess(false);
        dataVO.setMessage("비밀번호가 일치하지 않습니다.");
      } else{
        dataVO.setSuccess(true);
        dataVO.setMessage("비밀번호 맞음.");
      }
    } catch (Exception e) {
      dataVO.setSuccess(false);
      dataVO.setMessage("error");
    }
    return dataVO;
  }

  // 이메일 변경
  @PostMapping("/updateEmail")
  public DataVO updateEmail(@RequestBody Map<String, String> request, @RequestHeader("Authorization") String authorizationHeader){
    DataVO dataVO = new DataVO();
    String email = request.get("email"); // "email" 키로 값을 가져옴
    System.out.println(email);
    try {
      // 토큰 인증 및 아이디 추출
      String userId = getIdFromToken(authorizationHeader, dataVO);

      // 이메일 유무 확인
      int emailCount = service.getEmailCount(email);
      System.out.println(emailCount);

      if(emailCount >= 1){
        dataVO.setMessage("이미 사용 중인 이메일 입니다.");
        dataVO.setSuccess(false);
        return dataVO;
      }

      // DB에서 비밀번호 변경
      int result = service.updateEmail(userId, email);
      System.out.println(result);
      if(result > 0){
        dataVO.setSuccess(true);
        dataVO.setMessage("이메일 변경에 성공했습니다.");
      } else {
        dataVO.setSuccess(false);
        dataVO.setMessage("이메일 변경에 실패했습니다.");
      }
    } catch(Exception e){
      dataVO.setSuccess(false);
      dataVO.setMessage("error : " + e );
    }
    return dataVO;
  }

  // 비밀번호 변경
  @PostMapping("/updatePassword")
  public DataVO updatePassword(   
    @RequestBody String password,
    @RequestHeader("Authorization") String authorizationHeader){
    DataVO dataVO = new DataVO();
    
    // 비밀번호 암호화
    String encodedPassword = passwordEncoder.encode(password);

    try {
      // 토큰 인증 및 아이디 추출
      String userId = getIdFromToken(authorizationHeader, dataVO);

      // DB에서 비밀번호 변경
      int result = service.updatePassword(userId, encodedPassword);
      System.out.println(result);
      if(result > 0){
        dataVO.setSuccess(true);
        dataVO.setMessage("비밀번호 변경에 성공했습니다.");
      } else {
        dataVO.setSuccess(false);
        dataVO.setMessage("비밀번호 변경에 실패했습니다.");
      }
    } catch(Exception e){
      dataVO.setSuccess(false);
      dataVO.setMessage("error : " + e );
      System.out.println(e);
    }
    return dataVO;
  }

  @PostMapping("/snsLoginSetPassword")
  public DataVO snsLoginSetPassword( @RequestHeader("Authorization") String authorizationHeader, @RequestBody String password) {
    DataVO dataVO = new DataVO();
    System.out.println(authorizationHeader);
    // 비밀번호 암호화
    String encodedPassword = passwordEncoder.encode(password);

    try {
      String userId = getIdFromToken(authorizationHeader, dataVO);

      int result = service.updatePassword(userId, encodedPassword);

      if(result > 0){
        dataVO.setSuccess(true);
      } else {
        dataVO.setSuccess(false);
      }

    } catch (Exception e) {
      dataVO.setSuccess(false);
    }
    return dataVO;
  }
  

  @PostMapping("/updatePhone")
  public DataVO updatePhone(@RequestBody Map<String, String> request, @RequestHeader("Authorization") String authorizationHeader) {
    DataVO dataVO = new DataVO();
    String phone = request.get("phone"); // "email" 키로 값을 가져옴
    System.out.println(phone);
    
    try {
      // 토큰 인증 및 아이디 추출
      String userId = getIdFromToken(authorizationHeader, dataVO);
      
      // DB에서 비밀번호 변경
      int result = service.updatePhone(userId, phone);
      if(result > 0){
        dataVO.setSuccess(true);
        dataVO.setMessage("핸드폰 번호 변경에 성공했습니다.");
      } else {
        dataVO.setSuccess(false);
        dataVO.setMessage("핸드폰 번호 변경에 실패했습니다.");
      }
    } catch (Exception e) {
      dataVO.setSuccess(false);
      dataVO.setMessage("error : " + e );
    }
    return dataVO;
  }
  // 아이디 찾기
  @PostMapping("/getForgotId")
  public DataVO getForgotId(@RequestBody String email){
    DataVO dataVO = new DataVO();
    System.out.println(email);

    System.out.println("아이디 찾기 실행");
    try {
      String id = service.getIdFromEmail(email);
      if(!id.isEmpty()){
        dataVO.setData(id);
        dataVO.setSuccess(true);
      }
      System.out.println("id : " + id);
    } catch (Exception e) {
      System.out.println(e);
    }
    return dataVO;
  }

  // 계정 삭제
  @GetMapping("/deleteAccount")
  public DataVO deleteAccount(@RequestHeader("Authorization") String authorizationHeader) {
    DataVO dataVO = new DataVO();
    System.out.println("아이디 찾기 실행");
    try {
      // 토큰 인증 및 아이디 추출
      String userId = getIdFromToken(authorizationHeader, dataVO);

      int result = service.deleteAccount(userId);
      if(result > 0){
        dataVO.setSuccess(true);
      }
      System.out.println("id : " + userId);
    } catch (Exception e) {
      System.out.println(e);
    }
    return dataVO;
  }
  

  // 내가 가입한 정규모임 불러오기
  public String getIdFromToken(String authorizationHeader, DataVO dataVO){
    // 토큰 추출
    String token = authorizationHeader.replace("Bearer ", "");
    // 토큰 검증
    if (!jwtUtil.validateToken(token)) {
        dataVO.setSuccess(false);
        dataVO.setMessage("유효하지 않은 토큰입니다.");
        return "";
    }
    // 사용자 ID 추출
    String userId = jwtUtil.getUserIdFromToken(token);
    System.out.println("유저 아이디 : "+  userId);
    return userId;
  }


  
}