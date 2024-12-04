package be.pxl.services.services;

import be.pxl.services.controller.dto.CartCheckoutResponse;
import be.pxl.services.controller.dto.CartProductResponse;
import be.pxl.services.controller.dto.ShoppingCartRequest;
import be.pxl.services.domain.Product;
import be.pxl.services.domain.ShoppingCartItem;

import java.util.List;

public interface ICartService {
    long createCart();

    void addProductToCart(long cartId, long productId, int quantity);

    void removeProductFromCart(long cartId, long productId);

    List<CartProductResponse> getProductsInCart(long cartId);

    CartCheckoutResponse checkout(long cartId);

    ShoppingCartItem mapToShoppingCartItem(Product product, int amount);

}
