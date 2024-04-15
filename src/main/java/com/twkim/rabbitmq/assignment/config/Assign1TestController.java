package com.twkim.rabbitmq.assignment.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@ConditionalOnProperty(value = "my.config", havingValue = "assign1", matchIfMissing = false)
public class Assign1TestController {

	private final RabbitConfig assignConfig;

	private final RabbitTemplate template;

	/*
	 * 테스트로 보낼 Routing Key값들
	 * - 11개는 성공하고, 4개는 받지 못할 것으로 예상
	 */
	private final String[] keys = {
		"command", // commandReceiver
		"command.one", // commandReceiver
		"command.one.two", // commandReceiver
		"chat.one.user", // userReceiver
		"chat.one.room", // commandReceiver
		"chat.user", // userReceiver
		"chat.user.one", // userReceiver
		"chat.user.one.two", // userReceiver
		"chat.room", // roomReceiver
		"chat.room.one", // roomReceiver
		"chat.room.one.two", // roomReceiver
		"chat", // cannot receive
		"chat.one", // cannot receive
		"chat.one.two.user", // cannot receive
		"chat.one.two.room" // cannot receive
	};

	@GetMapping("/test")
	public String test() throws InterruptedException {
		int count = 0;
		for (String key : keys) {
			template.convertAndSend(assignConfig.requestExchange().getName(),
				key,
				">>message[" + key + "]<<");
			count++;
		}

		return "총 " + count + "개의 메시지 전송 완료";
	}
}
