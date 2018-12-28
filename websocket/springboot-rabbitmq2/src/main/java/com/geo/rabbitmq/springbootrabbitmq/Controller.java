package com.geo.rabbitmq.springbootrabbitmq;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @Autowired
    private AmqpTemplate amqpTemplate;
    @RequestMapping("/send")
    public void SennerMsg(String msg){
        amqpTemplate.convertAndSend("exchange","a",msg);

    }
}

