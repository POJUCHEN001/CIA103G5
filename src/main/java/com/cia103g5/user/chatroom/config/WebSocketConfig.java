package com.cia103g5.user.chatroom.config;

import org.springframework.context.annotation.Bean;
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
    	config.enableSimpleBroker("/custom");
        config.setApplicationDestinationPrefixes("/app"); // 應用消息的前綴

    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
    	registry.addEndpoint("/ws/member")
		    	.setHandshakeHandler(new CustomHandshakeHandler()) // 使用自訂 HandshakeHandler
    			.withSockJS(); // 配置 WebSocket 端點
    	
	  registry.addEndpoint("/ws/fortuneTeller") // 註冊占卜師路徑
      		  .setHandshakeHandler(new CustomHandshakeHandler()) // 使用自訂 HandshakeHandler
              .withSockJS();

    }
      

//    @Bean
//    public HttpSessionIdHandshakeInterceptor httpSessionIdHandshakeInterceptor() {
//        return new HttpSessionIdHandshakeInterceptor(); // 將 HttpSession 傳遞到 WebSocket
//    }
    

    @Bean
    public CustomHandshakeHandler  CustomHandshakeHandler() {
        return new CustomHandshakeHandler() ; // 將 HttpSession 傳遞到 WebSocket
    }
    
    
}

