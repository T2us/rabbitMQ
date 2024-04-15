package com.twkim.rabbitmq.assignment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourcePathRequest {
    private String username;
    private String vhost;
    private String resource;
    private String name;
    private String permission;
}
