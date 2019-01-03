package com.geo.rabbitmq.springbootrabbitmq;

import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 生产者发送消息
 */
@Slf4j
@RestController
@RequestMapping("/send")
public class ProducerService {
    @Autowired
    private Channel channel;
    @Autowired
    private AMQP.BasicProperties properties;
    /**
     * 发送消息
     */
    @RequestMapping("/sendMessage")
    public void sendMessage(String userQueue) {
        String mess = "This is a message  From " + userQueue;

        MyMessage myMessage = new MyMessage();
        myMessage.setMessageBody(mess);
        myMessage.setExChange("userExchange");
        myMessage.setRoutingKey("wuhan");
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        myMessage.setSendTime(sdf.format(date));


        try {
            channel.basicPublish("userExchange", "wuhan", properties, JSON.toJSONBytes(myMessage));
            log.info("SendMessage:------------》" + JSON.toJSONString(myMessage));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
