package be.pxl.services.catalog.services;

import be.pxl.services.catalog.domain.dto.ProductRequest;
import be.pxl.services.catalog.domain.dto.ProductResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ICatalogService {
    void addProduct(ProductRequest productRequest);

    ResponseEntity<List<ProductResponse>> getProducts();
}
