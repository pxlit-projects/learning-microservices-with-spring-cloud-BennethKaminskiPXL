package be.pxl.services.controller;

import be.pxl.services.controller.dto.LogEntityDto;
import be.pxl.services.log.LogEntity;
import be.pxl.services.services.LogService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/logbook")
@RequiredArgsConstructor
public class LogbookController {
    private final LogService logService;
    private static final Logger log = LoggerFactory.getLogger(LogbookController.class);
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<LogEntityDto> getLogBook(){
        log.info("Getting all products");
        return logService.getLogBook();
    }
}
