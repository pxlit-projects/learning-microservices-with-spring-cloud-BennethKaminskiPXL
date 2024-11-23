package be.pxl.services.cart.controller.dto;

import be.pxl.services.cart.domain.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartProductResponse {
    private String name;
    private Double price;
    private int quantity;
}
