package com.twkim.rabbitmq.assignment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPathRequest {
    private String username;
    private String password;
}
