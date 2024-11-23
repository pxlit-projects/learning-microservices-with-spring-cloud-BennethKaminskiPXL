package be.pxl.services.catalog.services;

import be.pxl.services.catalog.domain.Category;
import be.pxl.services.catalog.domain.Product;
import be.pxl.services.catalog.domain.dto.ProductRequest;
import be.pxl.services.catalog.domain.dto.ProductResponse;
import be.pxl.services.catalog.repository.CatalogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CatalogService implements ICatalogService{
    private final CatalogRepository catalogRepository;

    @Override
    public void addProduct(ProductRequest productRequest) {
        Product product = mapToProduct(productRequest);
        catalogRepository.save(product);
    }

    @Override
    public ResponseEntity<List<ProductResponse>> getProducts() {
        List<ProductResponse> productResponses =
                catalogRepository.findAll().stream().map(product -> mapToProductResponse(product)).toList();
        return ResponseEntity.ok(productResponses);
    }

    @Override
    public ResponseEntity<ProductResponse> updateProduct(Long id, ProductRequest productRequest) {
        Product product = catalogRepository.findById(id).orElseThrow();
        product.setLabel(productRequest.getLabel());
        product.setPrice(productRequest.getPrice());
        product.setName(productRequest.getName());
        product.setCategory(productRequest.getCategory());
        product.setDescription(productRequest.getDescription());
        catalogRepository.save(product);
        return ResponseEntity.ok(mapToProductResponse(product));
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
