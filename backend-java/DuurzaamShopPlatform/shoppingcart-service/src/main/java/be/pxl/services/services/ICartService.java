package be.pxl.services.services;

import be.pxl.services.controller.dto.CartCheckoutResponse;
import be.pxl.services.controller.dto.CartProductResponse;
import be.pxl.services.controller.dto.ShoppingCartRequest;

import java.util.List;

public interface ICartService {
    long createCart();

    void addProductToCart(long cartId, long productId);

    void removeProductFromCart(long cartId, long productId);

    List<CartProductResponse> getProductsInCart(long cartId);

    CartCheckoutResponse checkout(long cartId);

    void addProduct(ShoppingCartRequest shoppingCartRequest);

    void updateProduct(ShoppingCartRequest shoppingCartRequest);

    void deleteProduct(ShoppingCartRequest shoppingCartRequest);
}
