package be.pxl.services.catalog.services;

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
                label(productRequest.getLabel())
                .added(productRequest.getAdded())
                .id(productRequest.getId())
                .price(productRequest.getPrice())
                .name(productRequest.getName())
                .category(productRequest.getCategory())
                .description(productRequest.getDescription())
                .build();
    }
}
