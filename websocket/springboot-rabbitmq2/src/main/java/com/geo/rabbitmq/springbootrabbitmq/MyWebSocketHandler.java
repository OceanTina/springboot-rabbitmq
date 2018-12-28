package com.geo.rabbitmq.springbootrabbitmq;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Component
public class MyWebSocketHandler implements WebSocketHandler {

    private static final Map<String , WebSocketSession> userMap=new HashMap<>();
    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        String jspCode = (String )webSocketSession.getAttributes().get("jspCode");

        if(userMap.get(jspCode)==null){
            System.out.println(jspCode);
            userMap.put(jspCode,webSocketSession);
        }
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {

    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus) throws Exception {

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
    public void sendMsgToJsp(final TextMessage message, String type) throws  Exception{
        Iterator<Map.Entry<String ,WebSocketSession>> it=userMap.entrySet().iterator();
        while (it.hasNext()){
            final Map.Entry<String ,WebSocketSession> entry=it.next();
            System.out.println(entry.getValue().isOpen());
            System.out.println(entry.getKey().contains(type));
            if(entry.getValue().isOpen()&&entry.getKey().contains(type)){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if(entry.getValue().isOpen()){
                                entry.getValue().sendMessage(message);
                            }
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    }
}
