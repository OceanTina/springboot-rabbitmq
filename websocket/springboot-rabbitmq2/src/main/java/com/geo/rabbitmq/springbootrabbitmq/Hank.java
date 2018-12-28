package com.geo.rabbitmq.springbootrabbitmq;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class Hank implements HandshakeInterceptor{
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> map) throws Exception {
        String jspCode = ((ServletServerHttpRequest) request).getServletRequest().getParameter("jspCode");
        if (jspCode != null) {
            map.put("jspCoe", jspCode);
        } else {
            return false;
        }

        return true;
    }
    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {

    }
}
