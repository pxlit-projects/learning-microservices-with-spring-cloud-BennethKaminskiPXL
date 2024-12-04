package be.pxl.services.services;

import be.pxl.services.domain.dto.ProductRequest;
import be.pxl.services.domain.dto.ProductResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ICatalogService {
    void addProduct(ProductRequest productRequest);

    ResponseEntity<List<ProductResponse>> getProducts();

    ProductResponse updateProduct(Long id, ProductRequest productRequest);

    ResponseEntity<List<ProductResponse>> getProductsByCategory(String category);

    ResponseEntity<List<ProductResponse>> getProductsByLabel(String label);

    void deleteProduct(String userName, Long id);

    ProductResponse getProductById(Long id);
}
