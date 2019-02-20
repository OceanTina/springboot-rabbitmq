package com.geo.rabbitmq.springbootrabbitmq.controller;

import com.geo.rabbitmq.springbootrabbitmq.ConsumerService;
import com.geo.rabbitmq.springbootrabbitmq.MessageService;
import com.geo.rabbitmq.springbootrabbitmq.until.ApplicationContextRegister;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint(value = "/webSocketConnect/{userCode}")
@Component
public class WebSocketController {
    private static int onlineCount = 0;

    private Session session;

    public static ConcurrentHashMap<String, WebSocketController> webSocketList = new ConcurrentHashMap<>();

    private Channel channel;

    public static ConcurrentHashMap<String, Channel> mqChannelMap = new ConcurrentHashMap<>();

    private String userCode = null;
    private String consumerTagStr = null;

    @OnOpen
    public void onOpen(Session session, @PathParam("userCode") String userCode) {
        this.session = session;
        webSocketList.put(userCode, this);

        try {
            Connection connection = ApplicationContextRegister.getApplicationContext().getBean(Connection.class);
            this.channel = ApplicationContextRegister.getApplicationContext().getBean(Connection.class).createChannel();
            mqChannelMap.put(userCode, this.channel);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.userCode = userCode;

        consumerTagStr = ApplicationContextRegister.getApplicationContext().getBean(ConsumerService.class).consumerOnline(userCode);
    }

    @OnClose
    public void onClose() {
        if (webSocketList.get(this.userCode) != null) {
            webSocketList.remove(this.userCode);
            ApplicationContextRegister.getApplicationContext().getBean(ConsumerService.class).consumerOffline(consumerTagStr, this.userCode);
        }
    }

    @OnMessage
    public void onMessage(String message) {
        ApplicationContextRegister.getApplicationContext().getBean(MessageService.class).sendReplyMsg(message, this.userCode);
    }

    /**
     * 实现服务器主动往web端推送消息
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


}
