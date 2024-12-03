package be.pxl.services;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueueConfiguration {
    @Bean
    public Queue MyQueue() {
        return new Queue("AuditQueue",false);
        //durable = queu and messages blijven wanneer rabbitmq stopt, nu dus niet meer
    }
}
