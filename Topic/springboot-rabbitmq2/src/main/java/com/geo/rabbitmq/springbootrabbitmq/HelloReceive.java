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

//  SimpleMessageListenerContainer类
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
   public void getMessage(String userQueue,String routingKey) {
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


//           //声明队列
           //queueDeclare(String queue, boolean durable, boolean exclusive, boolean autoDelete,Map<String, Object> arguments)
           //durable： 是不持久化， true ，表示持久化，会存盘，服务器重启仍然存在，false，非持久化
           //exclusive ： 是否排他的，true，排他。如果一个队列声明为排他队列，该队列公对首次声明它的连接可见，并在连接断开时自动删除，
           channel.queueDeclare(userQueue, true, false, false, null);
//           //声明交换机
           //exchangeDeclare(String exchange, String type, boolean durable)
//           channel.exchangeDeclare("userExchange", "direct");
           channel.exchangeDeclare("userExchange", "direct", true);
           //绑定队列到交换机
           //参数 1 队列名称,2 交换机名称 3 路由key
           //channel.queueBind(QUEUR_NAME, EXCHANGE_NAME, ROUTING_KEY);
           channel.queueBind(userQueue, "userExchange", routingKey);
           channel.basicQos(10);

           //定义消费者
           Consumer consumer = new DefaultConsumer(channel) {
               @Override
               public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException
               {
                   String message = new String(body, "UTF-8");
                   System.out.println("Customer Received '" + message + "'" + userQueue);

                   //手动应答。
//                   channel.basicAck(envelope.getDeliveryTag(),false);
               }
           };


           //自动回复队列应答 -- RabbitMQ中的消息确认机制
           //消费者与队列绑定
           //确认收到消息    进行消费false 手动应答；true：自动应答
           channel.basicConsume(userQueue, false, consumer);

           //关闭消费者的连接
//           try {
//               channel.close();
//           } catch (TimeoutException e) {
//               e.printStackTrace();
//           }

       }catch (IOException e) {
           e.printStackTrace();
       }


   }
}
