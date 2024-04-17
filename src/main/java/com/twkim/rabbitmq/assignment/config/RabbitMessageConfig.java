package com.twkim.rabbitmq.assignment.config;

import com.twkim.rabbitmq.assignment.error.CustomErrorHandler;
import com.twkim.rabbitmq.assignment.error.CustomFatalExceptionStrategy;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.amqp.rabbit.listener.FatalExceptionStrategy;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ErrorHandler;

@Configuration
public class RabbitMessageConfig {

	@Bean
	RabbitTemplate rabbitTemplate(
		ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {

		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(jsonMessageConverter);

		return rabbitTemplate;
	}

	@Bean
	ContentTypeDelegatingMessageConverter jsonMessageConverter() {
		ContentTypeDelegatingMessageConverter converter =
			new ContentTypeDelegatingMessageConverter(new Jackson2JsonMessageConverter());

		MessageConverter simple = (MessageConverter) new SimpleMessageConverter();
		converter.addDelegate("text/plain", simple);
		converter.addDelegate(null, simple);

		return converter;
	}

	@Bean
	public ErrorHandler customErrorHandler() {
		return new CustomErrorHandler();
	}

	@Bean
	public ErrorHandler rejectErrorHandler() {
		return new ConditionalRejectingErrorHandler(customExceptionStrategy());
	}

	@Bean
	public FatalExceptionStrategy customExceptionStrategy() {
		return new CustomFatalExceptionStrategy();
	}
}
