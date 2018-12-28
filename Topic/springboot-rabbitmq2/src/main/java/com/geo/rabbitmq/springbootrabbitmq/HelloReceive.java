package com.geo.rabbitmq.springbootrabbitmq;


import com.rabbitmq.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/receive")
public class HelloReceive {


//    @RabbitListener(queues="topic.message")    //监听器监听指定的Queue
//    public void process1(String str) {
//        System.out.println("message:"+str);
//    }
//    @RabbitListener(queues="topic.messages")    //监听器监听指定的Queue
//    public void process2(String str) {
//        System.out.println("messages:"+str);
//    }



    @Autowired
    private Channel channel;

    @RequestMapping("/getMessage")
   public void getMessage(String userQueue) {
       try {
//           //获取连接
//           ConnectionFactory factory = new ConnectionFactory();
//           factory.setHost("127.0.0.1");// MQ的IP
//           factory.setPort(5672);// MQ端口
//           factory.setUsername("guest");// MQ用户名
//           factory.setPassword("guest");// MQ密码
//
//           Channel chan = null;
//           Connection conn = factory.newConnection();
//           chan = conn.createChannel();


           //声明队列
           channel.queueDeclare(userQueue, false, false, false, null);
           //声明交换机
           channel.exchangeDeclare("userExchange", "fanout");
           //绑定队列到交换机
           //参数 1 队列名称,2 交换机名称 3 路由key
           channel.queueBind(userQueue, "userExchange", "");
           channel.basicQos(1);

           //定义消费者
           Consumer consumer = new DefaultConsumer(channel) {
               @Override
               public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException
               {
                   String message = new String(body, "UTF-8");
                   System.out.println("Customer Received '" + message + "'" + userQueue);

                   //查询Redis中消费者对应的用户是否在线，如果在线则消费并手动应答，如果不在线则不应答。
//                   channel.basicAck(envelope.getDeliveryTag(),false);
               }
           };


           //自动回复队列应答 -- RabbitMQ中的消息确认机制
           //消费者与队列绑定
           //确认收到消息    进行消费false 手动应答；true：自动应答
           channel.basicConsume(userQueue, false, consumer);

       }catch (IOException e) {
           e.printStackTrace();
       }


   }
}
