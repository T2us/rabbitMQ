package com.twkim.rabbitmq.assignment.error;

import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;

public class CustomFatalExceptionStrategy extends ConditionalRejectingErrorHandler.DefaultExceptionStrategy {

	@Override
	public boolean isFatal(Throwable t) {
		return !(t.getCause() instanceof BusinessException);
	}
}