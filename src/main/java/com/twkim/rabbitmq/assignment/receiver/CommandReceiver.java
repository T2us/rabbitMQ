package com.twkim.rabbitmq.assignment.receiver;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twkim.rabbitmq.assignment.config.RabbitConfig;
import com.twkim.rabbitmq.assignment.dto.Command;
import com.twkim.rabbitmq.assignment.error.DeadLetterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;


@Slf4j
@RabbitListener(queues = "#{commandQueue.name}", messageConverter = "jsonMessageConverter")
public class CommandReceiver {

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private RabbitAdmin rabbitAdmin;
	@Autowired
	private RabbitConfig rabbitConfig;

	@RabbitHandler
	public void receiveString(Command in) throws DeadLetterException {
		String command = in.getCommand();

		try {
			log.info("this Command : {}", objectMapper.writeValueAsString(in));
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}

		if (command.equals("create")) {
			String roomName = in.getArguments()[0];

			// room.방이름 Fanout Exchange 생성
			FanoutExchange targetExchange =
				ExchangeBuilder.fanoutExchange("room." + roomName).build();
			rabbitAdmin.declareExchange(targetExchange);

			// exchange에 *.room.방이름 으로 바인딩
			// roomExchange, targetExchange 모두 Fanout인데 routing key 패턴이 가능..?
			rabbitAdmin.declareBinding(BindingBuilder.bind(targetExchange)
				.to(rabbitConfig.roomExchange()));
		} else if (command.equals("invite")) {
			String roomName = in.getArguments()[0];
			String userId = in.getArguments()[1];

			// user.[아이디] exchange를 room.[방이름] exchange에 바인딩
			FanoutExchange targetExchange =
				ExchangeBuilder.fanoutExchange("room." + roomName).build();
			rabbitAdmin.declareExchange(targetExchange);

			FanoutExchange targetUserExchange =
				ExchangeBuilder.fanoutExchange("user." + userId).build();
			rabbitAdmin.declareExchange(targetUserExchange);


			Queue targetUserQueue = QueueBuilder.durable("user." + userId).build();
			rabbitAdmin.declareQueue(targetUserQueue);

			rabbitAdmin.declareExchange(rabbitConfig.myUserExchange());
			rabbitAdmin.declareBinding(BindingBuilder.bind(targetExchange).to(rabbitConfig.myUserExchange()));
			rabbitAdmin.declareBinding(BindingBuilder.bind(targetUserQueue).to(targetUserExchange));
		}
	}

}
