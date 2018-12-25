package com.geo.rabbitmq.springbootrabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloSender {
    @Autowired
    private AmqpTemplate template;

    @RequestMapping("/send")
    public void send() {
        template.convertAndSend("queue", "hello,rabbit");
    }
}
