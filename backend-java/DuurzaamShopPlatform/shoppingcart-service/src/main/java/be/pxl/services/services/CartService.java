package be.pxl.services.services;

import be.pxl.services.client.CatalogClient;
import be.pxl.services.controller.dto.CartCheckoutResponse;
import be.pxl.services.controller.dto.CartProductResponse;
import be.pxl.services.controller.dto.ShoppingCartRequest;
import be.pxl.services.domain.Product;
import be.pxl.services.domain.ShoppingCart;
import be.pxl.services.domain.ShoppingCartItem;
import be.pxl.services.repository.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private static final Logger log = LoggerFactory.getLogger(CartService.class);
    private final CatalogClient catalogClient;

    @Override
    public long createCart() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCartRepository.save(shoppingCart);
        return shoppingCart.getId();

    }

    @Override
    public void addProductToCart(long cartId, long productId,int quantity) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(cartId).orElseThrow();
        var shoppingCartItems = shoppingCart.getShoppingCartItems();

        var product = catalogClient.getProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product with id " + productId + " not found");
        }
        //al in de cart
        ShoppingCartItem shoppingCartItem = mapToShoppingCartItem(product, quantity);

        var productInCart = shoppingCartItems.stream().filter(p -> p.getProdId() == productId).findFirst();
        if (productInCart.isPresent()) {
            productInCart.get().setQuantity(productInCart.get().getQuantity() + quantity);
            log.info("Product with id" + productId +"  added to cart");
            shoppingCartRepository.save(shoppingCart);
            return;
        }
        //in shoppingcart en saven
        shoppingCartItem.setQuantity(quantity);
        shoppingCartItems.add(shoppingCartItem);
        shoppingCartRepository.save(shoppingCart);
        log.info("Product with id" + productId +"  added to cart");
        log.info("Shoppingcart has item : " + shoppingCartItems.size() + " items" + shoppingCartItems.get(0).getName());
    }

    @Override
    public void removeProductFromCart(long cartId, long productId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(cartId).orElseThrow();
        var productlist = shoppingCart.getShoppingCartItems();
        Product product = catalogClient.getProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product with id " + productId + " not found");
        }

        //productlist mappen op product in de cart
        var productInCart = productlist.stream().filter(p -> p.getId() == productId).findFirst().orElseThrow();
        if (productInCart.getQuantity() > 1) {
            productInCart.setQuantity(productInCart.getQuantity() - 1);
            log.info("1 Product with id" + productId +"  removed from cart with id" + cartId);
        } else {
            productlist.remove(productInCart);
            log.info("Product with id" + productId +"  removed from cart with id" + cartId);
        }
        shoppingCartRepository.save(shoppingCart);
    }



    @Override
    public List<CartProductResponse> getProductsInCart(long cartId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(cartId).orElseThrow();
        var productList = shoppingCart.getShoppingCartItems();
     return productList.stream().map(this::mapToCartProductResponse).toList();
    }

    @Override
    public CartCheckoutResponse checkout(long cartId) {
        ShoppingCart cart = shoppingCartRepository.findById(cartId).orElseThrow();
     //   double totalPrice = cart.getProducts().stream().mapToDouble(Product::getPrice).sum();
        // calculate totalprice by multiplying price with quantity of each product in cart.getShtoppingCartItems
        double totalPrice = cart.getShoppingCartItems().stream().mapToDouble(p -> p.getPrice() * p.getQuantity()).sum();

        return CartCheckoutResponse.builder()
                .Id(cart.getId())
                .totalPrice(totalPrice)
                .products(cart.getShoppingCartItems())
                .build();
    }

    @Override
    public ShoppingCartItem mapToShoppingCartItem(Product product, int amount) {
        return ShoppingCartItem.builder()
                .prodId(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .quantity(amount)
                .build();
    }

    private CartProductResponse mapToCartProductResponse(ShoppingCartItem product) {
        return CartProductResponse.builder()
                .price(product.getPrice())
                .name(product.getName())
                .quantity(product.getQuantity())
                .build();
    }
}
