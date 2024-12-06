package be.pxl.services.services;

import be.pxl.services.controller.dto.LogEntityDto;
import be.pxl.services.log.Log;
import be.pxl.services.log.LogEntity;
import be.pxl.services.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class LogService {
    private final LogRepository logRepository;
    private static final Logger logger = LoggerFactory.getLogger(LogService.class);

    public void saveLogToDatabase(Log log) {
        LogEntity newLog = LogEntity.builder()
                .label(log.getLabel())
                .name(log.getName())
                .description(log.getDescription())
                .category(log.getCategory())
                .price(log.getPrice())
                .userName(log.getUserName())
                .action(log.getAction())
                .changed(LocalDateTime.now())
                .build();
        logRepository.save(newLog);
        logger.info("Log saved to database: " + newLog);
    }

    public List<LogEntityDto> getLogBook() {
        List<LogEntity> logEntities = logRepository.findAll();
        logger.info("Getting all logs from database");
        var logEntityDtoList = logEntities.stream()
                .map(logEntity -> LogEntityDto.builder()
                        .label(logEntity.getLabel())
                        .name(logEntity.getName())
                        .description(logEntity.getDescription())
                        .category(logEntity.getCategory())
                        .price(logEntity.getPrice())
                        .changed(logEntity.getChanged())
                        .userName(logEntity.getUserName())
                        .action(logEntity.getAction())
                        .id(logEntity.getId())
                        .changed(logEntity.getChanged())
                        .build())
                .toList();
                return logEntityDtoList;
    }
}
