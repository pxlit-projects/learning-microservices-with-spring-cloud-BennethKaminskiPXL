package be.pxl.services.cart.services;

import be.pxl.services.cart.controller.dto.CartCheckoutResponse;
import be.pxl.services.cart.controller.dto.CartProductResponse;
import be.pxl.services.cart.controller.dto.ProductRequest;
import be.pxl.services.cart.domain.Product;
import be.pxl.services.cart.domain.ShoppingCart;
import be.pxl.services.cart.repository.ProductRepository;
import be.pxl.services.cart.repository.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ProductRepository productRepository;
    private static final Logger log = LoggerFactory.getLogger(CartService.class);

    @Override
    public long createCart() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCartRepository.save(shoppingCart);
        return shoppingCart.getId();

    }

    @Override
    public void addProductToCart(long cartId, long productId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(cartId).orElseThrow();
        var shoppingCartItems = shoppingCart.getShoppingCartItems();

        Product product = productRepository.findById(productId).orElseThrow();

        shoppingCartItems.add(product);
        shoppingCartRepository.save(shoppingCart);
        log.info("Product with id" + productId +"  added to cart");
    }

    @Override
    public void removeProductFromCart(long cartId, long productId) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(cartId).orElseThrow();
        var productlist = shoppingCart.getShoppingCartItems();
        Product product = productRepository.findById(productId).orElseThrow();

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
 /// OpenFeign
    @Override
    public void addProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .id(productRequest.getId())
                .build();
        productRepository.save(product);
        log.info("Product added to database");
    }

    @Override
    public void updateProduct(ProductRequest productRequest) {
        Product product = productRepository.findById(productRequest.getId()).orElseThrow();
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());
        productRepository.save(product);
        log.info("Product updated in database");

    }
    @Override
    public void deleteProduct(ProductRequest productRequest) {
        Product product = productRepository.findById(productRequest.getId()).orElseThrow();
        productRepository.delete(product);
        log.info("Product deleted from database");
    }
    /// OpenFeign

    private CartProductResponse mapToCartProductResponse(Product product) {
        return CartProductResponse.builder()
                .price(product.getPrice())
                .name(product.getName())
                .quantity(product.getQuantity())
                .build();
    }
}
