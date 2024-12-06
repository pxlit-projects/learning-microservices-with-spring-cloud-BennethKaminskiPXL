package be.pxl.services.services;

import be.pxl.services.controller.dto.LogEntityDto;
import be.pxl.services.log.Log;

import java.util.List;

public interface ILogService {
    void saveLogToDatabase(Log log);
    List<LogEntityDto> getLogBook();
}
