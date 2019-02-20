package com.geo.rabbitmq.springbootrabbitmq.until;

import com.alibaba.fastjson.JSONArray;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

@Component
public class RabbitMqUntil {
    private static Channel channel;
    private static AMQP.BasicProperties properties;

    @Autowired
    public void setChannel(Channel channel) {
        RabbitMqUntil.channel = channel;
    }
    @Autowired
    public void setProperties(AMQP.BasicProperties properties) {
        RabbitMqUntil.properties = properties;
    }

    public static void createSubscribe(List<String> userCodes, String bindKey, String exchangeName) {
        for (String userCode : userCodes) {

            try {
                channel.exchangeDeclare(exchangeName, "direct", true);
                channel.queueDeclare(userCode, true, false, false, null);
                channel.queueBind(userCode, exchangeName, bindKey);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void createQueue(String userCode) {
        try {
//            channel.queueDeclare(userCode, true, false, false, null);
            channel.queueDeclare(userCode, false, false, false, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(String userExchange, String routingKey, String message) {
        try {
            channel.basicPublish(userExchange, routingKey, properties, message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过API获得queue信息
     * @return
     * @throws IOException
     */
    public static String getByRabbitMqApi(String urlString) throws IOException {
        //发送一个GET请求
        HttpURLConnection httpConn = null;
        BufferedReader in = null;

        URL url = new URL(urlString);
        httpConn = (HttpURLConnection) url.openConnection();
        //设置用户名密码
        String auth = "guest:guest";
        BASE64Encoder enc = new BASE64Encoder();
        String encoding = enc.encode(auth.getBytes());
        httpConn.setDoOutput(true);
        httpConn.setRequestProperty("Authorization", "Basic " + encoding);
        // 建立实际的连接
        httpConn.connect();
        //读取响应
        if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            StringBuilder content = new StringBuilder();
            String tempStr = "";
            in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            while ((tempStr = in.readLine()) != null) {
                content.append(tempStr);
            }
            in.close();
            httpConn.disconnect();
            return content.toString();
        } else {
            httpConn.disconnect();
            return "";
        }
    }

    public static Boolean userQueueIsExist(String userCode, String apiUrl) {
        try {
            String queuesList = getByRabbitMqApi(apiUrl);
            List<Object> objectList = JSONArray.parseArray(queuesList);
            for (Object obj : objectList) {
                Map<String, String> map = (Map<String, String>) obj;
                String queueName = map.get("name");
                if (queueName.equals(userCode)) {
                    return true;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 消息应答
     */
    public static void ackMsg(Channel curChannel, long deliveryTag) {
        try {
            curChannel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
