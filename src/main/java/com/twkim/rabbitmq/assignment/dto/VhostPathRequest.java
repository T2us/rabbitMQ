package com.twkim.rabbitmq.assignment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VhostPathRequest {
    private String username;
    private String vhost;
    private String ip;

}
