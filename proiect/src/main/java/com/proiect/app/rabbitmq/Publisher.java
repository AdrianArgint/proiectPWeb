package com.proiect.app.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.proiect.app.domain.User;
import com.proiect.app.repository.UserRepository;
import com.proiect.app.service.AlertService;
import com.proiect.app.service.UserService;
import com.proiect.app.service.dto.AlertDTO;
import com.proiect.app.service.dto.MqContent;
import java.util.List;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class Publisher {

    @Value("${spring.rabbitmq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.routingkey}")
    private String routingKey;

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private AlertService alertService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/alert/send")
    public ResponseEntity<?> sendAnnouncements(@RequestBody AlertDTO alertDTO) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<User> users = userRepository.findAll();
        users.forEach(user -> {
            MqContent mqContent = new MqContent(alertDTO.getTitle(), alertDTO.getContent(), user.getEmail());
            try {
                template.convertAndSend(exchange, routingKey, objectMapper.writeValueAsString(mqContent));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

        this.alertService.save(alertDTO);
        return ResponseEntity.ok("success");
    }
}
