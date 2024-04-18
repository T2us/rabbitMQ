package com.twkim.rabbitmq.assignment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twkim.rabbitmq.assignment.dto.Chat;
import com.twkim.rabbitmq.assignment.dto.Command;
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

	private final ObjectMapper objectMapper;

	private final RabbitTemplate rabbitTemplate;
	private final RabbitProperties rabbitProperties;

	@PostMapping(value = "/{type}")
	public Object postMessage(
		@PathVariable String type, @RequestBody MessageRequestDto request) {
		try {
			log.info("this Request DTO : {}", objectMapper.writeValueAsString(request));
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}

		String message = request.getMessage();

		if (message.startsWith("/")) {
			String[] commandAndArgs = message.substring(1).split("\\s", 2);

			Command command = Command.builder()
				.body(message)
				.command(commandAndArgs[0])
				.arguments(commandAndArgs[1].split("\\s"))
				.build();

			try {
				log.info("this Command : {}", objectMapper.writeValueAsString(command));
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}

			// 문자열이 "/" 로 시작하면 command.[명령어] 로 RPC로 전송
			return rabbitTemplate.convertSendAndReceive(
				"request",
				"command." + commandAndArgs[0],
				command);
		}

		if (type.equals("chat")) {
			Chat chat =
				Chat.builder().body(message).userName(rabbitProperties.getUsername()).build();

			rabbitTemplate.convertAndSend(request.getExchange(), request.getRoutingKey(), chat);

			return "send Chat Message";
		}

		rabbitTemplate.convertAndSend(request.getExchange(), request.getRoutingKey(), message);
		return "send Message";
	}
}
