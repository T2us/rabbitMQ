package com.twkim.rabbitmq.assignment.config;

import com.twkim.rabbitmq.assignment.receiver.RabbitReceiver;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "my.config", havingValue = "assign1", matchIfMissing = false)
public class RabbitConfig {

	/*
	 * Queue -> 메시지가 최종적으로 도달하는 곳, Consumer가 메시지를 소지할 때까지 메시지를 보관
	 */
	@Bean
	public Queue commandQueue() {
		return new Queue("command");
	}

	@Bean
	public Queue userQueue() {
		return new Queue("user");
	}

	@Bean
	public Queue roomQueue() {
		return new Queue("room");
	}

	/*
	 * Exchanger -> 메시지 전달 방식 설정
	 * - Direct :: Routing Key 와 정확히 일치하는 Queue 로 메시지 전달
	 * - Fanout :: 연결된 모든 Queue에 메시지 전달, Routing key는 무시된다.
	 * - Topic :: Routing Key 와 패턴을 이용하여 일치하는 Queue 로 메시지 전달
	 * - Headers :: 메시지 헤더에 속성 값을 사용하여 메시지 전달하는 방식, 메시지의 Routing Key는 무시된다.
	 */
	@Bean
	public TopicExchange requestExchange() {
		return new TopicExchange("request");
	}

	@Bean
	public TopicExchange chatExchange() {
		return new TopicExchange("chat");
	}

	@Bean
	public TopicExchange userExchange() {
		return new TopicExchange("user");
	}

	@Bean
	public TopicExchange roomExchange() {
		return new TopicExchange("room");
	}

	/*
	 * Binding -> Exchange 와 Queue 간의 연결을 정의
	 */
	@Bean
	public Binding bindingRequestExchangeToCommandQueue(
		TopicExchange requestExchange, Queue commandQueue) {
		return BindingBuilder.bind(commandQueue).to(requestExchange).with("command.#");
	}

	@Bean
	public Binding bindingRequestExchangeToChatExchange(
		TopicExchange requestExchange, TopicExchange chatExchange) {
		return BindingBuilder.bind(chatExchange).to(requestExchange).with("chat.#");
	}

	@Bean
	public Binding bindingChatExchangeToUserExchange(
		TopicExchange chatExchange, TopicExchange userExchange) {
		return BindingBuilder.bind(userExchange).to(chatExchange).with("*.user.#");
	}

	@Bean
	public Binding bindingChatExchangeToRoomExchange(
		TopicExchange chatExchange, TopicExchange roomExchange) {
		return BindingBuilder.bind(roomExchange).to(chatExchange).with("*.room.#");
	}

	@Bean
	public Binding bindingUserExchangeToUserQueue(TopicExchange userExchange, Queue userQueue) {
		return BindingBuilder.bind(userQueue).to(userExchange).with("*.user.#");
	}

	@Bean
	public Binding bindingRoomExchangeToRoomQueue(TopicExchange roomExchange, Queue roomQueue) {
		return BindingBuilder.bind(roomQueue).to(roomExchange).with("*.room.#");
	}

	@Bean
	public RabbitReceiver receiver() {
		return new RabbitReceiver();
	}
}