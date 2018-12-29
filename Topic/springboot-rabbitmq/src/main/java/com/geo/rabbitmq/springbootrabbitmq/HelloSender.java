package com.geo.rabbitmq.springbootrabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api")
public class HelloSender {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Channel channel;
    @Autowired
    private AMQP.BasicProperties properties;

    @RequestMapping("/send")
    public void send(String message) {
        String mess = "This is a message 20181228";
//        rabbitTemplate.convertAndSend("fanoutEx", "fanout",mess);


        try {
            //获取连接
//            ConnectionFactory factory = new ConnectionFactory();
//            factory.setHost("127.0.0.1");// MQ的IP
//            factory.setPort(5672);// MQ端口
//            factory.setUsername("guest");// MQ用户名
//            factory.setPassword("guest");// MQ密码
//
//            Connection conn = factory.newConnection();
//            Channel chan = connection.createChannel();


            //声明交换机
            //参数意义,1 交换机名称,2 类型:fanout,direct,topic
            //exchangeDeclare(String exchange, String type, boolean durable)
//            channel.exchangeDeclare("userExchange", "direct");

            /**
             * 将交换器与队列通过路由键绑定
             * 消息直接发送到交换机
             */
            //channel.queueBind(QUEUR_NAME, EXCHANGE_NAME, ROUTING_KEY);
//            channel.queueBind("user201812", "userExchange", "wuhan");
            //发送消息
            // channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN , message.getBytes());

            channel.basicPublish("userExchange", "wuhan", properties, mess.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }








//    //回调函数: confirm确认
//    final RabbitTemplate.ConfirmCallback confirmCallback = new RabbitTemplate.ConfirmCallback() {
//
//        @Override
//        public void confirm(CorrelationData correlationData, boolean ack, String s) {
//            System.err.println("correlationData: " + correlationData);
//            String messageId = correlationData.getId();
//            if(ack){
//                //如果confirm返回成功 则进行更新
//                System.out.println("OK!");
//            } else {
//                //失败则进行具体的后续操作:重试 或者补偿等手段
//                System.err.println("异常处理...");
//            }
//        }
//    };
//
//    //发送消息方法调用: 构建自定义对象消息
//    public void sendOrder(Order order) throws Exception {
//        rabbitTemplate.setConfirmCallback((RabbitTemplate.ConfirmCallback) confirmCallback);
//        //消息唯一ID
//        CorrelationData correlationData = new CorrelationData(order.getMessageId());
//        rabbitTemplate.convertAndSend("order-exchange", "order.ABC", order, correlationData);
//    }
}
