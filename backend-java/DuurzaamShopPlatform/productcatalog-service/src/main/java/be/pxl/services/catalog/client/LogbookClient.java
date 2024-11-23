package be.pxl.services.catalog.client;

import be.pxl.services.catalog.domain.dto.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "logbook-service")
public interface LogbookClient {
    @PostMapping("/api/logbook")
    void sendNotification(@RequestBody NotificationRequest notificationRequest);
}
