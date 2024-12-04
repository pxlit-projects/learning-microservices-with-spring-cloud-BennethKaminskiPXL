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

    //openFeigns voor producten
    //product adden
    @PostMapping("/product")
    @ResponseStatus(HttpStatus.CREATED)
    public void addProduct(@RequestBody ShoppingCartRequest shoppingCartRequest) {
        log.info("Adding product with name in shoppingcart-service: " + shoppingCartRequest.getName());
        cartService.addProduct(shoppingCartRequest);
    }
    //product wijzigen
    @PutMapping("/product")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateProduct(@RequestBody ShoppingCartRequest shoppingCartRequest) {
        cartService.updateProduct(shoppingCartRequest);
    }
    //product verwijderen
    @DeleteMapping("/product")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteProduct(@RequestBody ShoppingCartRequest shoppingCartRequest) {
        cartService.deleteProduct(shoppingCartRequest);
    }


    //producten in cart toevoegen (met quantity in frontend, standaard = 1)
    @PostMapping("/{cartId}/addProduct/{productId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void addProductToCart(@PathVariable Long cartId, @PathVariable Long productId) {
        log.info("Adding product with id: " + productId + " to cart with id: " + cartId);
        cartService.addProductToCart(cartId, productId);
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
