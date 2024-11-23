package be.pxl.services.cart.controller.dto;

import be.pxl.services.cart.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartCheckoutResponse {
    private long Id;
    private double totalPrice;
    private List<Product> products;
}
