package com.twkim.rabbitmq.assignment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopicPathRequest {
    private String username;
    private String vhost;
    private String resource;
    private String name;
    private String permission;
    private String routing_key;
}
