package com.twkim.rabbitmq.assignment.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twkim.rabbitmq.assignment.dto.ResourcePathRequest;
import com.twkim.rabbitmq.assignment.dto.TopicPathRequest;
import com.twkim.rabbitmq.assignment.dto.UserPathRequest;
import com.twkim.rabbitmq.assignment.dto.VhostPathRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.regex.Pattern;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/rabbit/auth")
public class AuthController {

	private final ObjectMapper objectMapper;

	@Value("${spring.rabbitmq.username}")
	private String USER;

	@Value("${spring.rabbitmq.password}")
	private String PASS;

	private final static String VHOST = "chat";

	@GetMapping
	public String index() {
		return "ok";
	}

	@PostMapping(path = "/user", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String postUser(UserPathRequest request) throws JsonProcessingException {

		log.info("postUser : {}", objectMapper.writeValueAsString(request));

		if (request.getUsername().startsWith(USER) && PASS.equals(request.getPassword())) {
			return "allow administrator";
		} else {
			return "deny";
		}
	}

	@PostMapping("/vhost")
	public String postVhost(VhostPathRequest request) throws JsonProcessingException {

		log.info("postVhost : {}", objectMapper.writeValueAsString(request));

		if (request.getUsername().startsWith(USER) && VHOST.equals(request.getVhost())) {
			return "allow";
		} else {
			return "deny";
		}
	}

	@PostMapping("/resource")
	public String postResource(ResourcePathRequest request) throws JsonProcessingException {

		log.info("postResource : {}", objectMapper.writeValueAsString(request));

		if (request.getUsername().startsWith(USER) && VHOST.equals(request.getVhost())) {
			if ("exchange".equals(request.getResource())) {
				if ("request".equals(request.getName()) && Arrays.asList("configure", "write")
					.stream()
					.anyMatch(request.getPermission()::equals)) {
					// "request" exchange에 보내기(configure, write, read) 허가 
					return "allow";
				} else if ("user".equals(request.getName()) && Arrays.asList("read")
					.stream()
					.anyMatch(request.getPermission()::equals)) {
					// "user" exchange 에 대해서 read 권한만 허가
					return "allow";
				} else if ("amq.default".equals(request.getName())) {
					return "allow";
				}
			} else if ("queue".equals(request.getResource())) {
				if (("user." + request.getUsername()).equals(request.getName()) && Arrays.asList(
					"read").stream().anyMatch(request.getPermission()::equals)) {
					// user.[자신의 아이디] 이름으로 큐 생성 허가
					return "allow";
				}
			}
		}

		return "deny";
	}

	@PostMapping("/topic")
	public String postTopic(TopicPathRequest request) throws JsonProcessingException {

		log.info("postTopic : {}", objectMapper.writeValueAsString(request));

		Pattern pattern = Pattern.compile("^(chat)\\.\\w+");
		// 패턴 :: 문자열이 chat.* 과 일치하는지 확인

		if (request.getUsername().startsWith(USER) && VHOST.equals(request.getVhost())
			&& "topic".equals(request.getResource())) {
			if ("request".equals(request.getName()) && "write".equals(request.getPermission()) && (
				request.getRouting_key() == null || pattern.matcher(request.getRouting_key())
					.find())) {
				return "allow";
			} else if (USER.equals(request.getName()) && "read".equals(request.getPermission()) && (
				request.getRouting_key() == null || ("*.user." + request.getUsername()).equals(
					request.getRouting_key()))) {
				return "allow";
			}
		}
		return "deny";
	}

}
