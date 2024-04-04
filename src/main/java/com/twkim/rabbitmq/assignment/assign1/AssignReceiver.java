package com.twkim.rabbitmq.assignment.assign1;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class AssignReceiver {

	@RabbitListener(queues = "#{command.name}")
	public void commandReceiver(String in) throws InterruptedException {
		receive(in, "commandReceiver");
	}

	@RabbitListener(queues = "#{user.name}")
	public void userReceiver(String in) throws InterruptedException {
		receive(in, "userReceiver");
	}

	@RabbitListener(queues = "#{room.name}")
	public void roomReceiver(String in) throws InterruptedException {
		receive(in, "roomReceiver");
	}

	public void receive(String in, String receiver) throws InterruptedException {
		System.out.println("instance " + receiver + " [x] Received '" + in + "'");
	}
}
