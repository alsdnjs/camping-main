package com.ict.camping.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  // STOMP 엔드포인트 등록
  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    // 예: ws://localhost:8080/ws/chat
    registry.addEndpoint("api/ws/chat")
        .setAllowedOriginPatterns("*") // 실제 배포 시 특정 도메인으로 제한 권장
        .withSockJS();
  }

  // 메시지 브로커 구성
  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    // 클라이언트에서 /app 으로 시작하면 @MessageMapping으로 라우팅
    config.setApplicationDestinationPrefixes("/app");
    // /topic, /queue로 시작하는 구독 destination은 메시지 브로커가 처리
    config.enableSimpleBroker("/topic", "/queue");
  }
}
