package be.pxl.services.catalog.domain.dto;

import be.pxl.services.catalog.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
    private String name;
    private String label;
    private String description;
    private Category category;
    private Double price;
    private LocalDate added = LocalDate.now();
}
