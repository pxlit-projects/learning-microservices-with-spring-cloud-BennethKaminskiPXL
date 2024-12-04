package be.pxl.services.domain.dto;

import be.pxl.services.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest implements Serializable {
    private Long id;
    private String name;
    private String label;
    private String description;
    private Category category;
    private Double price;
    private LocalDateTime added = LocalDateTime.now();
    private String userName;
    private String action;
}
