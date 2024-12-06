package be.pxl.services.controller.dto;

import be.pxl.services.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogEntityDto {
    private Long id;
    private String name;
    private String label;
    private String description;
    private Category category;
    private Double price;
    private LocalDateTime changed;
    private String userName;
    private String action;
}
