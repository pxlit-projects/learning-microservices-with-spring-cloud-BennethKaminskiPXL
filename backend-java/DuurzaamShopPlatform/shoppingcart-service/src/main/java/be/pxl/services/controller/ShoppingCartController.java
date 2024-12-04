package be.pxl.services.controller;

import be.pxl.services.controller.dto.CartCheckoutResponse;
import be.pxl.services.controller.dto.CartProductResponse;
import be.pxl.services.controller.dto.ShoppingCartRequest;
import be.pxl.services.services.ICartService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ICartService cartService;
    private static final Logger log = LoggerFactory.getLogger(ShoppingCartController.class);

    //ok cart aanmaken
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public long createCart() {
       return cartService.createCart();
    }


    //producten in cart toevoegen (met quantity in frontend, standaard = 1)
    @PostMapping("/{cartId}/addProduct/{productId}/{quantity}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void addProductToCart(@PathVariable Long cartId, @PathVariable Long productId, @PathVariable int quantity) {
        log.info("Adding product with id: " + productId + " to cart with id: " + cartId);
        cartService.addProductToCart(cartId, productId,quantity);
    }

    //producten in cart verwijderen
    @DeleteMapping("/{cartId}/removeProduct/{productId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void removeProductFromCart(@PathVariable long cartId,@PathVariable long productId) {
        cartService.removeProductFromCart(cartId, productId);
    }

    //producten in cart zien
    @GetMapping("/{cartId}/products")
    @ResponseStatus(HttpStatus.OK)
    public List<CartProductResponse> getProductsInCart(@PathVariable Long cartId) {
      return cartService.getProductsInCart(cartId);
    }

    @PostMapping("/{cartId}/checkout")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CartCheckoutResponse checkout(@PathVariable Long cartId) {
      return   cartService.checkout(cartId);
    }

}
