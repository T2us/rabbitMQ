//package com.twkim.rabbitmq.assignment.config;
//
//import com.twkim.rabbitmq.assignment.receiver.ChatListener;
//import lombok.RequiredArgsConstructor;
//import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
//import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
//import org.springframework.amqp.rabbit.config.SimpleRabbitListenerEndpoint;
//import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@RequiredArgsConstructor
//public class RabbitListenerConfig implements RabbitListenerConfigurer {
//
//	@Value("${rabbitmq.server.chat-concurrent}")
//	int concurrent;
//
////	@Autowired
//	private final ChatListener chatListener;
//
//	@Autowired
//	SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory;
//
//	@Override
//	public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
//
//		for (int i = 0; i < concurrent; i++) {
//			String queueName = "chat." + Integer.toString(i);
//			SimpleRabbitListenerEndpoint endpoint = new SimpleRabbitListenerEndpoint();
//			endpoint.setId(queueName);
//			endpoint.setQueueNames(queueName);
//			endpoint.setMessageListener(chatListener);
//			endpoint.setExclusive(false);
//
//			registrar.registerEndpoint(endpoint, rabbitListenerContainerFactory);
//
//			System.out.println("\n\n 생성 하나 완료 ! [" + i + "]\n\n");
//		}
//	}
//}
