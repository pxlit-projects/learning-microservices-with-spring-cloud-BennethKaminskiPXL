package be.pxl.services.catalog.controller;

import be.pxl.services.catalog.domain.dto.ProductRequest;
import be.pxl.services.catalog.domain.dto.ProductResponse;
import be.pxl.services.catalog.services.ICatalogService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog")
@RequiredArgsConstructor
public class ProductController {
    private final ICatalogService catalogService;
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);


    //producten toevoegen
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addProduct(@RequestBody ProductRequest productRequest){
        log.info("Adding product : " + productRequest.getName() + "by user : " + productRequest.getUserName());
        catalogService.addProduct(productRequest);
    }
    //producten opvragen
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ProductResponse>> getProducts(){
        log.info("Getting all products");
        return catalogService.getProducts();
    }

    //producten verwijderen
    @DeleteMapping("/{userName}/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void deleteProduct(@PathVariable String userName, @PathVariable Long id){
        log.info("Deleting product with id : " + id);
        catalogService.deleteProduct(userName, id );
    }

    //producten veranderen
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ProductResponse updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        log.info("Updating product with id : " + id + "by user : " + productRequest.getUserName());
    return
        catalogService.updateProduct(id, productRequest);
    }
    //producten krijgen op category
    @GetMapping("/category/{category}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable String category){
        log.info("Getting products by category : " + category);
        return catalogService.getProductsByCategory(category);
    }

    //producten krijgen op label
    @GetMapping("/label/{label}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<ProductResponse>> getProductsByLabel(@PathVariable String label){
        log.info("Getting products by label : " + label);
        return catalogService.getProductsByLabel(label);
    }
}
