package be.pxl.services.catalog.controller;

import be.pxl.services.catalog.domain.dto.ProductRequest;
import be.pxl.services.catalog.domain.dto.ProductResponse;
import be.pxl.services.catalog.services.ICatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog")
@RequiredArgsConstructor
public class ProductController {
    private final ICatalogService catalogService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addProduct(@RequestBody ProductRequest productRequest){
        catalogService.addProduct(productRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ProductResponse>> getProducts(){
        return catalogService.getProducts();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
    return
        catalogService.updateProduct(id, productRequest);
    }

    @GetMapping("/category/{category}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable String category){
        return catalogService.getProductsByCategory(category);
    }

    @GetMapping("/label/{label}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ProductResponse>> getProductsByLabel(@PathVariable String label){
        return catalogService.getProductsByLabel(label);
    }
}
