package be.pxl.services.catalog.services;

import be.pxl.services.catalog.client.ShoppingCartClient;
import be.pxl.services.catalog.domain.Category;
import be.pxl.services.catalog.domain.Product;
import be.pxl.services.catalog.domain.dto.ShoppingCartRequest;
import be.pxl.services.catalog.domain.dto.ProductRequest;
import be.pxl.services.catalog.domain.dto.ProductResponse;
import be.pxl.services.catalog.repository.CatalogRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogService implements ICatalogService{
    private final CatalogRepository catalogRepository;
    private final ShoppingCartClient shoppingCartClient;
    private static final Logger log = LoggerFactory.getLogger(CatalogService.class);
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void addProduct(ProductRequest productRequest) {
        Product product = mapToProduct(productRequest);
        catalogRepository.save(product);
        log.info("Product added to catalog: " + product.getName());

        productRequest.setAction("add");
        log.info("Sending add message to logbook service");
        //send rabbittemplate message to logbookservice myqueue
        rabbitTemplate.convertAndSend("AuditQueue", productRequest);

        ShoppingCartRequest shoppingCartRequest = maptoShoppingCartRequest(product);
        log.info("Adding product to shoppingcart-service database");
        shoppingCartClient.sendNotification(shoppingCartRequest);
    }

    private ShoppingCartRequest maptoShoppingCartRequest(Product product) {
        return ShoppingCartRequest.builder()
                .id(product.getId())
                .price(product.getPrice())
                .name(product.getName())
                .build();

    }

    @Override
    public ResponseEntity<List<ProductResponse>> getProducts() {
        List<ProductResponse> productResponses =
                catalogRepository.findAll().stream().map(product -> mapToProductResponse(product)).toList();
        return ResponseEntity.ok(productResponses);
    }

    @Override
    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        Product product = catalogRepository.findById(id).orElseThrow();

        //openfeign voor shoppingcart
        if (!(product.getName().equals(productRequest.getName()) || product.getPrice().equals(productRequest.getPrice()))) {
            log.info("sending update message to shoppingcart service");
            ShoppingCartRequest shoppingCartRequest = maptoShoppingCartRequest(product);
            shoppingCartClient.patchNotification(shoppingCartRequest);
        }
        product.setLabel(productRequest.getLabel());
        product.setPrice(productRequest.getPrice());
        product.setName(productRequest.getName());
        product.setCategory(productRequest.getCategory());
        product.setDescription(productRequest.getDescription());
        catalogRepository.save(product);
        log.info("Product updated: " + product.getName());
        productRequest.setAction("update");
        log.info("Sending update message to logbook service");
        rabbitTemplate.convertAndSend("AuditQueue",productRequest);
        return mapToProductResponse(product);
    }

    @Override
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(String category) {
        var products = catalogRepository.findProductsByCategory(Category.valueOf(category.toUpperCase()));
        List<ProductResponse> productResponses = products.stream().map(this::mapToProductResponse).toList();
        return ResponseEntity.ok(productResponses);
    }

    @Override
    public ResponseEntity<List<ProductResponse>> getProductsByLabel(String label) {
        var products = catalogRepository.findProductsByLabel(label.toLowerCase());
        List<ProductResponse> productResponses = products.stream().map(this::mapToProductResponse).toList();
        return ResponseEntity.ok(productResponses);
    }

    @Override
    public void deleteProduct(String userName, Long id) {
        log.info("User " + userName + " is deleting product with id: " + id);
        var product = catalogRepository.findById(id).orElseThrow();
        ShoppingCartRequest shoppingCartRequest = maptoShoppingCartRequest(product);
        ProductRequest productRequest = mapToProductRequest(product);
        productRequest.setAction("delete");
        log.info("Sending delete message to logbook service");
        rabbitTemplate.convertAndSend("AuditQueue",productRequest);  ;
        catalogRepository.deleteById(id);
        shoppingCartClient.deleteNotification(shoppingCartRequest);



    }

    //voor rabbitmq
    private ProductRequest mapToProductRequest(Product product) {
        return ProductRequest.builder()
                .label(product.getLabel())
                .added(product.getAdded())
                .price(product.getPrice())
                .id(product.getId())
                .description(product.getDescription())
                .name(product.getName())
                .category(product.getCategory())
                .build();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .label(product.getLabel())
                .added(product.getAdded())
                .price(product.getPrice())
                .id(product.getId())
                .description(product.getDescription())
                .name(product.getName())
                .category(product.getCategory())
                .build();
    }

    private Product mapToProduct(ProductRequest productRequest) {
        return Product.builder().
                label(productRequest.getLabel().toLowerCase())
                .added(productRequest.getAdded())
                .price(productRequest.getPrice())
                .name(productRequest.getName())
                .category(productRequest.getCategory())
                .description(productRequest.getDescription())
                .build();
    }
}
