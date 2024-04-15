package com.twkim.rabbitmq.assignment.receiver;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class RabbitReceiver {

	@RabbitListener(queues = "#{commandQueue.name}")
	public void commandReceiver(String in) throws InterruptedException {

		receive(in, "commandReceiver");
	}

	@RabbitListener(queues = "#{userQueue.name}")
	public void userReceiver(String in) throws InterruptedException {
		receive(in, "userReceiver");
	}

	@RabbitListener(queues = "#{roomQueue.name}")
	public void roomReceiver(String in) throws InterruptedException {
		receive(in, "roomReceiver");
	}

	public void receive(String in, String receiver) throws InterruptedException {
		System.out.println("instance " + receiver + " [x] Received '" + in + "'");
	}
}
