package com.cia103g5.user.chatroom.config;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import com.cia103g5.user.member.dto.SessionMemberDTO;

import jakarta.servlet.http.HttpSession;

import java.security.Principal;
import java.util.Map;

public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        // 根據路徑自動分配用戶角色
        String path = request.getURI().getPath();

        ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
        HttpSession session = servletRequest.getServletRequest().getSession();
        SessionMemberDTO sessionMember = (SessionMemberDTO)session.getAttribute("loggedInMember");
        
        if (path.contains("/ws/member")) {
            return new CustomPrincipal(sessionMember.getNickName()); // 預設會員
        } else if (path.contains("/ws/fortuneTeller")) {
            return new CustomPrincipal(sessionMember.getNickName()); // 預設占卜師
        }

        return new CustomPrincipal("anonymous"); // 預設匿名用戶
    }

    // 自訂 Principal 類別
    public static class CustomPrincipal implements Principal {
        private final String name;

        public CustomPrincipal(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
        
        
    }
}
