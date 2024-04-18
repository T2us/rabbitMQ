package com.twkim.rabbitmq.assignment.receiver;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class ChatListener implements MessageListener {

	@Override
	public void onMessage(Message message) {
		System.out.println(
			Thread.currentThread().getId() + " [x] Received Chat-hash: " + message.toString());
	}
}
