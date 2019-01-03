package com.geo.rabbitmq.springbootrabbitmq;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * 消费者的方法
 */
@Slf4j
@RestController
@RequestMapping("/receive")
public class ConsumerService {
    @Autowired
    private Channel channel;

    /**
     * 用户上线同时消费者上线的方法
     */
    @RequestMapping("/consumerOnline")
    public String consumerOnline(String userQueue) {
        //创建一个消费者
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException
            {
                String message = new String(body, "UTF-8");
                System.out.println(userQueue + "----->>Consumer Received ：" + message);

                //手动应答。
//                channel.basicAck(envelope.getDeliveryTag(),false);
                log.info("consumerTag:" + consumerTag);
//                log.info("DeliveryTag:" + envelope.getDeliveryTag());
            }


        };
        String consumerTagStr =null;
        try {
            consumerTagStr = channel.basicConsume(userQueue, false, consumer);
            log.info("consumerTagStr----->" + consumerTagStr);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return consumerTagStr;
    }
    /**
     * 用户下线同时消费者下线的方法
     */
    @RequestMapping("/consumerOffline")
    public void consumerOffline(String consumerTag){
        //关闭消费者的连接
        try {
            //通道下线某个消费者
            channel.basicCancel(consumerTag);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
