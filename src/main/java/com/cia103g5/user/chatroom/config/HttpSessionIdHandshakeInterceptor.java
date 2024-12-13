package com.cia103g5.user.chatroom.config;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.cia103g5.user.member.dto.SessionMemberDTO;

import jakarta.servlet.http.HttpSession;

public class HttpSessionIdHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) throws Exception {
    	
    	System.out.print("ddddd");

        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpSession session = servletRequest.getServletRequest().getSession();
            SessionMemberDTO sessionMember = (SessionMemberDTO)session.getAttribute("loggedInMember");
            if (sessionMember != null) {
                // 將 session 屬性存放到 WebSocket 的 attributes 中
                attributes.put("nickname", sessionMember.getNickName());
                System.out.println("存入websocket屬性中的nickname:"+ sessionMember.getNickName());
            }
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                WebSocketHandler wsHandler, Exception ex) {
    }
}
