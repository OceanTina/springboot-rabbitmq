package com.geo.rabbitmq.springbootrabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Configuration
public class SenderConf {
//    @Bean(name="message")
//    public Queue queueMessage() {
//        return new Queue("topic.message");
//    }
//
//    @Bean(name="messages")
//    public Queue queueMessages() {
//        return new Queue("topic.messages");
//    }

//    @Bean
//    public TopicExchange exchange() {
//        return new TopicExchange("fanoutEx");
//    }

//    @Bean
//    Binding bindingExchangeMessage(@Qualifier("message") Queue queueMessage, TopicExchange exchange) {
//        return BindingBuilder.bind(queueMessage).to(exchange).with("topic.message");
//    }
//
//    @Bean
//    Binding bindingExchangeMessages(@Qualifier("messages") Queue queueMessages, TopicExchange exchange) {
//        return BindingBuilder.bind(queueMessages).to(exchange).with("topic.#");//*表示一个词,#表示零个或多个词
//    }

    @Bean(name="channel")
    public Channel getChannel() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");// MQ的IP
        factory.setPort(5672);// MQ端口
        factory.setUsername("guest");// MQ用户名
        factory.setPassword("guest");// MQ密码

        Connection conn = null;
        Channel chan = null;
        try {
            conn = factory.newConnection();
            chan = conn.createChannel();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        return chan;
    }

    @Bean("properties")
    public AMQP.BasicProperties getAmq() {
        AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
        builder.deliveryMode(2);
        AMQP.BasicProperties properties = builder.build();
        return properties;
    }
}
