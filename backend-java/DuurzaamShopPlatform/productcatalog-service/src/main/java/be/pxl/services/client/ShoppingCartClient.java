package be.pxl.services.client;

import be.pxl.services.domain.dto.ShoppingCartRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "shoppingcart-service")
public interface ShoppingCartClient {
    @PostMapping("/api/cart/product")
    void sendNotification(@RequestBody ShoppingCartRequest shoppingCartRequest);

    @PutMapping("/api/cart/product")
    void patchNotification(@RequestBody ShoppingCartRequest shoppingCartRequest);

    @DeleteMapping("/api/cart/product")
    void deleteNotification(@RequestBody ShoppingCartRequest shoppingCartRequest);
}
