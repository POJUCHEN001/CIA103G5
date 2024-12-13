package com.cia103g5.user.chatroom.model;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

    private final SessionRegistry sessionRegistry;

    // 注入 SessionRegistry（用於管理用戶與 session 的關係）
    public WebSocketEventListener(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    // 監聽連線事件
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        // 獲取用戶名（從 Spring Security 或 STOMP 標頭中）
        String username = headerAccessor.getUser().getName();

        // 獲取 WebSocket 的 sessionId
        String sessionId = headerAccessor.getSessionId();

        // 記錄用戶與 sessionId 的映射
        sessionRegistry.registerSessionId(username, sessionId);
        System.out.println("用戶 " + username + " 已連接, sessionId: " + sessionId);
    }

    // 監聽斷線事件，清除對應的 session
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        sessionRegistry.unregisterSessionId(sessionId);
        System.out.println("sessionId " + sessionId + " 已斷線");
    }
}
