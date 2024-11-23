package be.pxl.services.cart.controller;

import be.pxl.services.cart.controller.dto.CartCheckoutResponse;
import be.pxl.services.cart.controller.dto.CartProductResponse;
import be.pxl.services.cart.services.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ICartService cartService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public long createCart() {
       return cartService.createCart();
    }

    @PostMapping("/{cartId}/addProduct/{productId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void addProductToCart(long cartId, long productId) {
        cartService.addProductToCart(cartId, productId);
    }

    @DeleteMapping("/{cartId}/removeProduct/{productId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void removeProductFromCart(long cartId, long productId) {
        cartService.removeProductFromCart(cartId, productId);
    }

    //producten in cart zien
    @GetMapping("/{cartId}/products")
    @ResponseStatus(HttpStatus.OK)
    public List<CartProductResponse> getProductsInCart(long cartId) {
      return cartService.getProductsInCart(cartId);
    }

    @PostMapping("/{cartId}/checkout")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CartCheckoutResponse checkout(long cartId) {
      return   cartService.checkout(cartId);
    }

}
