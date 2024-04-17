package com.twkim.rabbitmq.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MessageRequestDto {
	private String exchange;
	private String routingKey;
	private String message;
}
