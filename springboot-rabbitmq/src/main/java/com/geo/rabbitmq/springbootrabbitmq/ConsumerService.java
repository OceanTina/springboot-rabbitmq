package com.geo.rabbitmq.springbootrabbitmq;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.geo.rabbitmq.springbootrabbitmq.constants.RabbitMqConstant;
import com.geo.rabbitmq.springbootrabbitmq.controller.WebSocketController;
import com.geo.rabbitmq.springbootrabbitmq.until.RabbitMqUntil;
import com.rabbitmq.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import sun.misc.BASE64Encoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 消费者的方法
 */
@RestController
@RequestMapping("/receive")
public class ConsumerService {
    @Value("${rabbitmq.manage.host}")
    private String rabbitmqManageHost;
    @Value("${rabbitmq.manage.port}")
    private String rabbitmqManagePort;

    @Autowired
    private Channel channel;
    @Autowired
    private  Connection connection;


    @Autowired
    private RestTemplate restTemplate;

    /**
     * 用户上线同时消费者上线的方法
     */
    @RequestMapping("/consumerOnline")
    public String consumerOnline(String userQueue) {
        String rabbitmqApiUrl = "http://" + rabbitmqManageHost + ":" + rabbitmqManagePort + RabbitMqConstant.RABBITMQ_API_GET_QUEUES_LIST;
        Boolean booleanQueue = RabbitMqUntil.userQueueIsExist(userQueue, rabbitmqApiUrl);
        if (!booleanQueue) {
            RabbitMqUntil.createQueue(userQueue);
        }
        //获取用户对应的channel
        Channel channel = WebSocketController.mqChannelMap.get(userQueue);
        //创建一个消费者
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException
            {
                String message = new String(body, "UTF-8");
                System.out.println(userQueue + "----->>Consumer Received ：" + message);

                //将消息通过websocket推送到web端
                WebSocketController.webSocketList.get(userQueue).sendMessage(message);
                //手动应答。
//                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };

        String consumerTagStr =null;
        try {
            consumerTagStr = channel.basicConsume(userQueue, false, consumer);

//            channel.basicCancel(consumerTagStr);
//            channel.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return consumerTagStr;
    }


    /**
     * 获得消息数量
     */
    @RequestMapping("/getMessageNum")
    public void getMessageNum(String userQueue) {
        try {
            Object queueObj = new Object();
//            String queuesStr = getApiMessage();
            String queuesStr = "";
            List<Object> objectList = JSONArray.parseArray(queuesStr);
            for (Object obj : objectList) {
                Map<String, String> map = (Map<String, String>) obj;
                String queueName = map.get("name");
                if (queueName.equals(userQueue)) {
                    queueObj = obj;
                    System.out.println(obj);
                }
            }

            //userQueue的OBJECT
            Map<String, Object> userQueueMap = (Map<String, Object>) queueObj;
            String messageNum = userQueueMap.get("messages_ready").toString();

            System.out.println("未读消息：" + messageNum);




        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示的获得消息
     */
    @RequestMapping("/getMessageByPullAPI")
    public void getMessageByPullAPI(String userQueue) {
        try {
            boolean autoAck = false;
            GetResponse response = channel.basicGet(userQueue, autoAck);
            if (response == null) {
                System.out.println("No message retrieved!");
            } else {
                AMQP.BasicProperties props = response.getProps();
                byte[] body = response.getBody();
                long deliveryTag = response.getEnvelope().getDeliveryTag();
                System.out.println(deliveryTag);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 消费者应答
     */
    @RequestMapping("/consumerAck")
    public void consumerAck(String userQueue) {
        //创建一个消费者
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException
            {
                String message = new String(body, "UTF-8");
                System.out.println(userQueue + "----->>Consumer Received ：" + message);

                //手动应答。
                channel.basicAck(envelope.getDeliveryTag(),false);
            }


        };
    }




    public void consumerOffline(String consumerTag, String userQueue) {
        Channel channel = WebSocketController.mqChannelMap.get(userQueue);

        try {
            channel.basicCancel(consumerTag);
//            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
//        } catch (TimeoutException e) {
//            e.printStackTrace();
        }
    }


}
