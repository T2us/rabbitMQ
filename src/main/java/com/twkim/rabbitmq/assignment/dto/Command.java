package com.twkim.rabbitmq.assignment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Command {
	String body;
	String command;
	String[] arguments;
}
