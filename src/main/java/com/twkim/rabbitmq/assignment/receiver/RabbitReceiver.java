package com.twkim.rabbitmq.assignment.receiver;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Bean;

public class RabbitReceiver {

	@RabbitListener(queues = "#{deadLetterQueue.name}")
	public void deadLetterReceiver(String in) throws InterruptedException {
		System.out.println(" [x] Received DeadLetter : " + in.toString());
	}

	@RabbitListener(queues = "#{roomQueue.name}")
	public void roomReceiver(String in) throws InterruptedException {
		receive(in, "roomReceiver");
	}

	public void receive(String in, String receiver) throws InterruptedException {
		System.out.println("instance " + receiver + " [x] Received '" + in + "'");
	}

	public void receive(Message in, String receiver) throws InterruptedException {
		System.out.println("instance " + receiver + " [x] Received '" + in.toString() + "'");
	}
}
