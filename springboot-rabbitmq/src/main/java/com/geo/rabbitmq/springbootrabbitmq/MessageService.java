package com.geo.rabbitmq.springbootrabbitmq;

import com.geo.rabbitmq.springbootrabbitmq.until.RabbitMqUntil;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    public void sendReplyMsg(String message, String userCode) {
        RabbitMqUntil.sendMessage("cloudMsg", "topicId", message);
    }
}
