package com.cia103g5.user.chatroom.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue"); // 啟用主題和隊列
        config.setApplicationDestinationPrefixes("/app"); // 應用消息的前綴
        config.setUserDestinationPrefix("/user"); // 如果有用戶目標需要這行
    }

//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//    	registry.addEndpoint("/ws").withSockJS(); // 配置 WebSocket 端點
//    }
    
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/member") // 註冊會員路徑
                .setHandshakeHandler(new CustomHandshakeHandler()) // 使用自訂 HandshakeHandler
                .withSockJS();

        registry.addEndpoint("/ws/fortuneTeller") // 註冊占卜師路徑
                .setHandshakeHandler(new CustomHandshakeHandler()) // 使用自訂 HandshakeHandler
                .withSockJS();
    }
    
}

