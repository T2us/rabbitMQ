package com.twkim.rabbitmq.assignment.receiver;

import com.twkim.rabbitmq.assignment.dto.Chat;
import com.twkim.rabbitmq.assignment.error.BusinessException;
import com.twkim.rabbitmq.assignment.error.DeadLetterException;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

// RabbitListener를 이용한 messageConverter 설정
@RabbitListener(queues = "#{myUserQueue.name}", messageConverter = "jsonMessageConverter")
public class UserReceiver {

	// Dead Letter  테스트를 위해 String으로 전달받을 경우 에러 발생
	@RabbitHandler
	public void receiveString(String in) throws DeadLetterException {
		throw new DeadLetterException();
//		System.out.println(" [x] Received String '" + in + "'");
	}

	@RabbitHandler
	public void receiveChat(
		Chat in) {
		System.out.println(" [x] Received Chat '" + in.getBody());
	}
}
