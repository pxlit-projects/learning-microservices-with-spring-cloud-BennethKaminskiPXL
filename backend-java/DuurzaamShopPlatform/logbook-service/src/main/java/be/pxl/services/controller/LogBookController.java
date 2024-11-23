package be.pxl.services.controller;

import be.pxl.services.domain.AuditMessage;
import be.pxl.services.domain.Notification;
import be.pxl.services.services.LogBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/logbook")
public class LogBookController {
    private final LogBookService logBookService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void logMessage(@RequestBody Notification notification) {
        log.info("Log message: {}", notification);
        logBookService.logMessage(notification);
    }
}
