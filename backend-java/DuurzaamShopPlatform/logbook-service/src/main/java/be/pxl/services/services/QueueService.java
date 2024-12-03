package be.pxl.services.services;

import be.pxl.services.domain.ProductRequest;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class QueueService {
    @RabbitListener(queues = "MyQueue")
    public void listen(ProductRequest in) {
        System.out.println("Message read from MyQueue : " + in);
    }
}
