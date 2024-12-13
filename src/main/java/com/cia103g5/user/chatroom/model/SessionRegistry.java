package com.cia103g5.user.chatroom.model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class SessionRegistry {

    private final Map<String, String> userSessionMap = new ConcurrentHashMap<>();

    // 註冊用戶與 sessionId
    public void registerSessionId(String username, String sessionId) {
        userSessionMap.put(username, sessionId);
    }

    // 注銷 sessionId
    public void unregisterSessionId(String sessionId) {
        userSessionMap.values().removeIf(value -> value.equals(sessionId));
    }

    // 根據用戶名獲取 sessionId
    public String getSessionId(String username) {
        return userSessionMap.get(username);
    }
}
