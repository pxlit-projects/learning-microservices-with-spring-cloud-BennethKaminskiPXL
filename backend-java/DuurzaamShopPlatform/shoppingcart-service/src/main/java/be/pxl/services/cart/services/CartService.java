package be.pxl.services.cart.services;

import be.pxl.services.cart.controller.dto.CartCheckoutResponse;
import be.pxl.services.cart.controller.dto.CartProductResponse;
import be.pxl.services.cart.domain.Product;
import be.pxl.services.cart.domain.ShoppingCart;
import be.pxl.services.cart.repository.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {
    private final ShoppingCartRepository shoppingCartRepository;
    @Override
    public long createCart() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCartRepository.save(shoppingCart);
        return shoppingCart.getId();

    }

    @Override
    public void addProductToCart(long cartId, long productId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(cartId).orElseThrow();
        var productlist = shoppingCart.getProducts();
       //todo hier met Feign client werken  productlist.add(productId);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public void removeProductFromCart(long cartId, long productId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(cartId).orElseThrow();
        var productlist = shoppingCart.getProducts();
        //todo hier met Feign client werken  productlist.remove(productId);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public List<CartProductResponse> getProductsInCart(long cartId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(cartId).orElseThrow();
        var productList = shoppingCart.getProducts();
        return productList.stream().map(this::mapToCartProductResponse).toList();
    }

    @Override
    public CartCheckoutResponse checkout(long cartId) {
        ShoppingCart cart = shoppingCartRepository.findById(cartId).orElseThrow();
        double totalPrice = cart.getProducts().stream().mapToDouble(Product::getPrice).sum();
        return CartCheckoutResponse.builder()
                .Id(cart.getId())
                .totalPrice(totalPrice)
                .products(cart.getProducts())
                .build();
    }

    private CartProductResponse mapToCartProductResponse(Product product) {
        return CartProductResponse.builder()
                .price(product.getPrice())
                .name(product.getName())
                .quantity(product.getQuantity())
                .build();
    }
}
