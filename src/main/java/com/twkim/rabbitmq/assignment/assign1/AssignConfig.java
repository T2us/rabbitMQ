package com.twkim.rabbitmq.assignment.assign1;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sound.midi.Receiver;

@Configuration
public class AssignConfig {

    /*
     * Exchanger -> 메시지 전달 방식 설정
     * - Direct :: Routing Key 와 정확히 일치하는 Queue 로 메시지 전달
     * - Fanout :: 연결된 모든 Queue에 메시지 전달, Routing key는 무시된다.
     * - Topic :: Routing Key 와 패턴을 이용하여 일치하는 Queue 로 메시지 전달
     * - Headers :: 메시지 헤더에 속성 값을 사용하여 메시지 전달하는 방식, 메시지의 Routing Key는 무시된다.
     *
     * > 과제 상에서는 특정 Queue ( Command, User, Room 에 보내야 하므로 TopicExchange 사용 )
     */
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("topicExchange");
    }

    /*
     * Queue -> 메시지가 최종적으로 도달하는 곳, Consumer가 메시지를 소지할 때까지 메시지를 보관
     * > 과제 상에서는 Command, User, Room 의 Queue가 필요하다.
     */
    @Bean
    public Queue command() {
        return new Queue("command");
    }

    @Bean
    public Queue user() {
        return new Queue("user");
    }

    @Bean
    public Queue room() {
        return new Queue("room");
    }

    /*
     * Binding -> Exchange 와 Queue 간의 연결을 정의
     *  - Binding 에서는 어떤 Queue에 어떤 Exchange와 Routing Key를 이용하여 메시지를 전달할지 지정한다.
     *
     * >
     */
    @Bean
    public Binding bindingCommand(TopicExchange topic, Queue command) {
        return BindingBuilder.bind(command).to(topic).with("command.#");
    }

    @Bean
    public Binding bindingUser(TopicExchange topic, Queue user) {
        return BindingBuilder.bind(user).to(topic).with("*.user.#");
    }

    @Bean
    public Binding bindingRoom(TopicExchange topic, Queue room) {
        return BindingBuilder.bind(room).to(topic).with("*.room.#");
    }

    @Bean
    public AssignReceiver receiver() {
        return new AssignReceiver();
    }

//	@Bean
//	@Profile("sender1")
//	public AssignSender sender() {
//		return new AssignSender();
//	}

}