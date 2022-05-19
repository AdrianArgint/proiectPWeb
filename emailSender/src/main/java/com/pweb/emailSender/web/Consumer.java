package com.pweb.emailSender.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pweb.emailSender.dto.MqContent;
import com.pweb.emailSender.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class Consumer {

    @Autowired
    private EmailService emailService;

    @RabbitListener(queues = "${spring.rabbitmq.queue}")
    public void consumeMessageFromQueue(String response) throws JsonProcessingException {

        System.out.println(response);
        ObjectMapper objectMapper = new ObjectMapper();
        MqContent mqContent = objectMapper.readValue(response, MqContent.class);

        Map<String, Object> content = new HashMap<>();
        content.put("title", mqContent.getTitle());
        content.put("content", mqContent.getContent());
        try {
            emailService.sendMessageUsingThymeleafTemplate(mqContent.getEmail(), "Official announcement", content);
        } catch (IOException | MessagingException ignored) {}

        System.out.println("Message received from queue : " + mqContent.getContent());
    }
}
