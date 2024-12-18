package be.pxl.services.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartProductResponse {
    private Long id;
    private String name;
    private Double price;
    private int quantity;
}
