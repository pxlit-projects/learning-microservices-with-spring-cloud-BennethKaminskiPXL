package be.pxl.services.services;

import be.pxl.services.domain.AuditMessage;
import be.pxl.services.domain.Notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LogBookService {
    private static final Logger log = LoggerFactory.getLogger(LogBookService.class);
    public void logMessage(Notification notification) {

        log.info("Log message: {}", notification);
        log.info("Sending .. {}", notification.getMessage());
        log.info("To {}", notification.getSender());
    }
}
