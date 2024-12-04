package be.pxl.services.services;

import be.pxl.services.log.Log;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class QueueService {
    @RabbitListener(queues = "AuditQueue")
    public void listen(Log log) {
        System.out.println("Message read from AuditQueue : " + log );
    }
}
