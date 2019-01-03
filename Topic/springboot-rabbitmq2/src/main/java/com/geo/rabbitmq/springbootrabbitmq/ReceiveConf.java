package com.geo.rabbitmq.springbootrabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Configuration
@Slf4j
public class ReceiveConf {
//    @Bean
//    public Queue queue() {
//        return new Queue("queue");
//    }

//    @Bean(name="connection")
//    public Connection getConnection() {
//        ConnectionFactory factory = new ConnectionFactory();
//        factory.setHost("127.0.0.1");// MQ的IP
//        factory.setPort(5672);// MQ端口
//        factory.setUsername("guest");// MQ用户名
//        factory.setPassword("guest");// MQ密码
//
//        Connection conn = null;
//        try {
//            conn = factory.newConnection();
//            log.info("ConnectionMAX:" + conn.getChannelMax());
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (TimeoutException e) {
//            e.printStackTrace();
//        }
//        return conn;
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

            log.info("ConnectionMAX:" + conn.getChannelMax());
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
