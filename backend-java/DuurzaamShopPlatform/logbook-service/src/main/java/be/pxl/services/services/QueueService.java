package be.pxl.services.services;

import be.pxl.services.log.Log;
import be.pxl.services.log.LogEntity;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class QueueService {
    private final LogService logService;

    @RabbitListener(queues = "AuditQueue")
    public void listen(Log log) {
        System.out.println("Message read from AuditQueue : " + log );
        logService.saveLogToDatabase(log);
    }


}
