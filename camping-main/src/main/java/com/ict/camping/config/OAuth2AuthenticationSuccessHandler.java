package com.ict.camping.config;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.ict.camping.common.util.JwtUtil;
import com.ict.camping.domain.auth.service.MyUserDetailService;
import com.ict.camping.domain.users.service.UsersService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

// sns로그인 성공시 실행하는 클래스
@Slf4j
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
  private final JwtUtil jwtUtil;
  private final MyUserDetailService userDetailService;

  @Autowired
  private UsersService usersService;
  private String firstLogin = "false";

    public OAuth2AuthenticationSuccessHandler(JwtUtil jwtUtil, MyUserDetailService userDetailService) {
        this.jwtUtil = jwtUtil;
        this.userDetailService = userDetailService;
    }

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
    Authentication authentication) throws IOException, ServletException {
      OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

    try { 
      System.out.println("SuccessHandler");
        //insteadof = authentication.getPrincipal() = 인증 주체/ 타입이 OAuth2User 인지 확인한다.
        //authentication 객체는 현재 사용자의 인증상태
        //getPrincipal() 가지고 있는 사용자 정보를 반환
      if(authentication.getPrincipal() instanceof OAuth2User){

        oAuth2User = (OAuth2User) authentication.getPrincipal();
        String uri = request.getRequestURI();
        String id = "";
        String provider = "";
        String phone = "";
        System.out.println("oAuth2User" + oAuth2User);

        //id 는 provider마다 받는 방식이 달라서 if 문 안에 작성
        //카카오
        if (uri.contains("kakao")) {
          provider = "kakao";
          id = oAuth2User.getAttribute("id");

        //네이버
        } else if (uri.contains("naver")) {
          provider = "naver";
          id = oAuth2User.getAttribute("id");
          phone = oAuth2User.getAttribute("phone");
          phone.replace("-", "");

        //구글
        } else if (uri.contains("google")) {
          provider = "google";
          id = oAuth2User.getAttribute("id");

        } else {
          provider = "unknown";
        }
        log.info("OAuth user : " + oAuth2User);
        log.info("provider : " + provider);

        //성공 후 토큰 만들어서 클라이언트에게 리다이렉트한다.
        UserDetails userDetails = userDetailService.loadUserByOAuth2User(oAuth2User, provider);

        String name = oAuth2User.getAttribute("name");
        String email = oAuth2User.getAttribute("email");
        String token = jwtUtil.generateToken(id);

        String user_idx = usersService.getUserIdxById(id);
        
        // 로그인 처음인가 판단
        Optional<String> passwordOpt = Optional.ofNullable(usersService.getPasswordById(id));
        passwordOpt.ifPresentOrElse(
          password -> firstLogin = "false",
          () -> firstLogin = "true"
        );

        // String password = usersService.getPasswordById(id);  //처음 로그인이면 password 없음

        // System.out.println(password);

        // if(password == null){
        //   firstLogin = "true";
        // }

        System.out.println("소셜 로그인 토큰 : " + token);
        System.out.println("소셜 로그인 아이디 : " + id);
        // 토큰을 클라이언트에 전달하기 위해 Authorization 헤더로 설정
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Authorization", "Bearer " + token);
        String redirectUrl = String.format(
          //%s => String이 들어간다는 뜻
          //%b => boolean이 들어간다는 뜻
          "http://localhost:3000/authentication/snsLoginRedirect?token=%s&user_idx=%s&id=%s&name=%s&email=%s&phone=%s&firstLogin=%s",
          URLEncoder.encode(token, StandardCharsets.UTF_8),
          URLEncoder.encode(user_idx, StandardCharsets.UTF_8),
          URLEncoder.encode(id , StandardCharsets.UTF_8),
          URLEncoder.encode(name, StandardCharsets.UTF_8),
          URLEncoder.encode(email, StandardCharsets.UTF_8),
          URLEncoder.encode(phone, StandardCharsets.UTF_8),
          URLEncoder.encode(firstLogin, StandardCharsets.UTF_8)
        );

        // String redirectUrl = "http://localhost:3000";

        System.out.println("id : " + id);
        System.out.println("name : " + name);
        System.out.println("email : " + email);
        System.out.println("user_idx : " + user_idx);
        System.out.println("token : " + token);

        // 클라이언트에 토큰, 이름, email 등 정보를 가지고 간다.
        response.sendRedirect(redirectUrl);
      }
    } catch (Exception e) {
      e.printStackTrace();
      response.sendRedirect("/login?error");
    }
      
  }
}