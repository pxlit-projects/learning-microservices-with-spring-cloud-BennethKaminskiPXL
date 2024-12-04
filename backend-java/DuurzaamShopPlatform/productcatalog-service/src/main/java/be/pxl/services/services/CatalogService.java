package be.pxl.services.services;

import be.pxl.services.client.ShoppingCartClient;
import be.pxl.services.domain.Category;
import be.pxl.services.domain.Product;
import be.pxl.services.domain.dto.ShoppingCartRequest;
import be.pxl.services.domain.dto.ProductRequest;
import be.pxl.services.domain.dto.ProductResponse;
import be.pxl.services.log.Log;
import be.pxl.services.repository.CatalogRepository;
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
    private static final Logger logger = LoggerFactory.getLogger(CatalogService.class);
    private final RabbitTemplate rabbitTemplate;

    @Override
    public void addProduct(ProductRequest productRequest) {
        Product product = mapToProduct(productRequest);
        catalogRepository.save(product);
        logger.info("Product added to catalog: " + product.getName());

        productRequest.setAction("add");
        //send rabbittemplate message to logbookservice myqueue
        Log log = mapToLog(productRequest);
        rabbitTemplate.convertAndSend("AuditQueue", log);
//
       ShoppingCartRequest shoppingCartRequest = maptoShoppingCartRequest(product);
        logger.info("Adding product to shoppingcart-service database");
        shoppingCartClient.sendNotification(shoppingCartRequest);
    }

    public Log mapToLog(ProductRequest productRequest){
        return Log.builder().
                id(productRequest.getId())
                .action(productRequest.getAction())
                .userName(productRequest.getUserName())
                .label(productRequest.getLabel())
                .price(productRequest.getPrice())
                .category(productRequest.getCategory())
                .added(productRequest.getAdded())
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .build();


    }
    
    
    public ShoppingCartRequest maptoShoppingCartRequest(Product product) {
        logger.info("product id = " + product.getId());
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
            logger.info("sending update message to shoppingcart service");
            ShoppingCartRequest shoppingCartRequest = maptoShoppingCartRequest(product);
            shoppingCartClient.patchNotification(shoppingCartRequest);
        }
        product.setLabel(productRequest.getLabel());
        product.setPrice(productRequest.getPrice());
        product.setName(productRequest.getName());
        product.setCategory(productRequest.getCategory());
        product.setDescription(productRequest.getDescription());
        catalogRepository.save(product);
        logger.info("Product updated: " + product.getName());
        productRequest.setAction("update");
        logger.info("Sending update message to logbook service");
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
        logger.info("User " + userName + " is deleting product with id: " + id);
        var product = catalogRepository.findById(id).orElseThrow();
        ShoppingCartRequest shoppingCartRequest = maptoShoppingCartRequest(product);
        ProductRequest productRequest = mapToProductRequest(product);
        productRequest.setAction("delete");
        logger.info("Sending delete message to logbook service");
        Log log = mapToLog(productRequest);
        rabbitTemplate.convertAndSend("AuditQueue",log);  ;
        catalogRepository.deleteById(id);
   //    shoppingCartClient.deleteNotification(shoppingCartRequest);



    }

    @Override
    public ProductResponse getProductById(Long id) {
        var product = catalogRepository.findById(id).orElseThrow();
        return mapToProductResponse(product);
    }

    //voor rabbitmq
    public ProductRequest mapToProductRequest(Product product) {
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

    public ProductResponse mapToProductResponse(Product product) {
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

    public Product mapToProduct(ProductRequest productRequest) {
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
