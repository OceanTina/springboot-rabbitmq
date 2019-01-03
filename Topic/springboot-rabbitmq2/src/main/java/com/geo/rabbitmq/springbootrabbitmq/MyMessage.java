package com.geo.rabbitmq.springbootrabbitmq;

import java.io.Serializable;

public class MyMessage implements Serializable {
    private static final long serialVersionUID = 9111357402963030257L;

    private String messageId;

    private String exChange;

    private String routingKey;

    private String queue;

    private String sendTime;

    private String messageBody;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getExChange() {
        return exChange;
    }

    public void setExChange(String exChange) {
        this.exChange = exChange;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }
}
