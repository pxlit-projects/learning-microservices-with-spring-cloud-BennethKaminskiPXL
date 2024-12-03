package be.pxl.services.catalog.domain.dto;

import be.pxl.services.catalog.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartRequest {
    private long id;
    private String name;
    private Double price;
}
