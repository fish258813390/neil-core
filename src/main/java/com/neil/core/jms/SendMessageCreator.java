package com.neil.core.jms;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.jms.core.MessageCreator;

public class SendMessageCreator implements MessageCreator{
    private String message;
    public SendMessageCreator(String message){
        this.message = message;
    }
    public Message createMessage(Session session) throws JMSException {
        TextMessage textMessage = session.createTextMessage(message);
        return textMessage;
    }
}
