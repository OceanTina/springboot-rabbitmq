package com.geo.rabbitmq.springbootrabbitmq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/create")
public class CreateInfo {
    @Autowired
    private Channel channel;

    @RequestMapping("")
    public void createInfo() {

        try {
            //声明交换机
            //参数意义,1 交换机名称,2 类型:fanout,direct,topic
            //exchangeDeclare(String exchange, String type, boolean durable)
            channel.exchangeDeclare("userExchange", "direct");
//            channel.queueDeclare("user2018");

            /**
             * 将交换器与队列通过路由键绑定
             * 消息直接发送到交换机
             */
            //channel.queueBind(QUEUR_NAME, EXCHANGE_NAME, ROUTING_KEY);
            channel.queueBind("user2018", "userExchange", "wuhan");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
