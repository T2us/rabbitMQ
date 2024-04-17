package com.twkim.rabbitmq.assignment.controller;

import com.twkim.rabbitmq.assignment.dto.Chat;
import com.twkim.rabbitmq.assignment.dto.MessageRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/rabbit/message")
public class MessageController {

	private final RabbitTemplate rabbitTemplate;
	private final RabbitProperties rabbitProperties;

	@PostMapping(value = "/{type}")
	public String postMessage(
		@PathVariable String type, @RequestBody MessageRequestDto request) {
		String message = request.getMessage();

		if (type.equals("chat")) {
			Chat chat =
				Chat.builder().body(message).userName(rabbitProperties.getUsername()).build();

			rabbitTemplate.convertAndSend(request.getExchange(), request.getRoutingKey(), chat);

			return "send Chat Message";
		} else if (type.equals("dead-letter")) {
			rabbitTemplate.convertAndSend("dead-letter-exchange", "dead-letter", message);

			return "send dead letter queue";
		}

		rabbitTemplate.convertAndSend(request.getExchange(), request.getRoutingKey(), message);
		return "send Message";
	}
}
