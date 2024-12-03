package be.pxl.services.cart.services;

import be.pxl.services.cart.controller.dto.CartCheckoutResponse;
import be.pxl.services.cart.controller.dto.CartProductResponse;
import be.pxl.services.cart.controller.dto.ProductRequest;

import java.util.List;

public interface ICartService {
    long createCart();

    void addProductToCart(long cartId, long productId);

    void removeProductFromCart(long cartId, long productId);

    List<CartProductResponse> getProductsInCart(long cartId);

    CartCheckoutResponse checkout(long cartId);

    void addProduct(ProductRequest productRequest);

    void updateProduct(ProductRequest productRequest);

    void deleteProduct(ProductRequest productRequest);
}
