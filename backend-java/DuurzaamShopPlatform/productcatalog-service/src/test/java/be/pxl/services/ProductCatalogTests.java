package be.pxl.services;

import be.pxl.services.client.ShoppingCartClient;
import be.pxl.services.domain.Category;
import be.pxl.services.domain.Product;
import be.pxl.services.domain.dto.ProductRequest;
import be.pxl.services.domain.dto.ProductResponse;
import be.pxl.services.domain.dto.ShoppingCartRequest;
import be.pxl.services.log.Log;
import be.pxl.services.repository.CatalogRepository;
import be.pxl.services.services.CatalogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class ProductCatalogTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CatalogRepository catalogRepository;

    @Autowired
    private CatalogService catalogService;

    @Mock
    private ShoppingCartClient shoppingCartClient;



    @Container
    private static MySQLContainer sqlContainer =
            new MySQLContainer<>("mysql:5.7.37");

    @DynamicPropertySource
    static void registerMySQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", sqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", sqlContainer::getUsername);
        registry.add("spring.datasource.password", sqlContainer::getPassword);
        registry.add("eureka.client.enabled", () -> "false");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
        registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.MySQL8Dialect");
        registry.add("spring.datasource.driverClassName", () -> "com.mysql.cj.jdbc.Driver");

    }
    @BeforeEach
    public void cleanDatabaseBeforeTest() {
        catalogRepository.deleteAll();
    }

    @AfterEach
    public void cleanDatabaseAfterTest() {
        catalogRepository.deleteAll();
    }

    @Test
    public void createProduct() throws Exception {

        ProductRequest productRequest = ProductRequest.builder()
                .name("product")
                .label("label")
                .description("description")
                .price(10.0)
                .category(Category.JACKETS)
                .userName("user")
                .build();

        String productString = objectMapper.writeValueAsString(productRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/catalog")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productString))
                .andExpect(status().isCreated());

        assertEquals(1, catalogRepository.findAll().size());

    }

    @Test
    public void getProducts() throws Exception {
        Product product = Product.builder()
                .name("product")
                .label("label")
                .description("description")
                .price(10.0)
                .category(Category.JACKETS)
                .build();
        Product product2 = Product.builder()
                .name("product")
                .label("label")
                .description("description")
                .price(10.0)
                .category(Category.JACKETS)
                .build();
        catalogRepository.save(product);
        catalogRepository.save(product2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/catalog"))
                .andExpect(status().isOk())
                        .andExpect(jsonPath("$.length()").value(2));
        assertEquals(2, catalogRepository.findAll().size());

    }

    @Test
    public void updateProduct() throws Exception {
        Product product = Product.builder()
                .id(1L)
                .name("product")
                .label("label")
                .description("description")
                .price(10.0)
                .category(Category.JACKETS)
                .build();
        catalogRepository.save(product);

        ProductRequest updatedProd = ProductRequest.builder()
                .name("updated")
                .label("label")
                .description("description")
                .price(10.0)
                .category(Category.JACKETS)
                .userName("bob")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.put("/api/catalog/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProd)))
                .andExpect(status().isAccepted());

        assertEquals("updated", catalogRepository.findById(1L).get().getName());
        assertEquals(1, catalogRepository.findAll().size());

    }

    @Test
    public void deleteProductShouldDeleteProduct() throws Exception {
        Product product = Product.builder()
                .name("product")
                .label("label")
                .description("description")
                .price(10.0)
                .category(Category.JACKETS)
                .build();
        catalogRepository.save(product);
        assertEquals(1, catalogRepository.findAll().size());
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/catalog/user/1"))
                .andExpect(status().isAccepted());
        assertEquals(0, catalogRepository.findAll().size());
    }


    @Test
    public void getProductsByCategory() throws Exception {
        Product product = Product.builder()
                .name("product")
                .label("label")
                .description("description")
                .price(10.0)
                .category(Category.JACKETS)
                .build();
        Product product2 = Product.builder()
                .name("shirt")
                .label("label")
                .description("description")
                .price(10.0)
                .category(Category.SHIRTS)
                .build();
        catalogRepository.save(product);
        catalogRepository.save(product2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/catalog/category/jackets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("product"));
        assertEquals(2, catalogRepository.findAll().size());

    }

    @Test
    public void getProductsByLabel() throws Exception {
        Product product = Product.builder()
                .name("product")
                .label("label")
                .description("description")
                .price(10.0)
                .category(Category.JACKETS)
                .build();
        Product product2 = Product.builder()
                .name("shirt")
                .label("secondlabel")
                .description("description")
                .price(10.0)
                .category(Category.SHIRTS)
                .build();
        catalogRepository.save(product);
        catalogRepository.save(product2);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/catalog/label/secondlabel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
        assertEquals(2, catalogRepository.findAll().size());

    }
    @Test
    public void mapToShoppingCartRequestReturnsShoppingCartRequest(){
        Product product = Product.builder()
                .id(1L)
                .name("product")
                .label("label")
                .description("description")
                .price(10.0)
                .category(Category.JACKETS)
                .build();

        ShoppingCartRequest shoppingCartRequest = catalogService.maptoShoppingCartRequest(product);
        assertEquals(1L, shoppingCartRequest.getId());
        assertEquals("product", shoppingCartRequest.getName());
        assertEquals(10.0, shoppingCartRequest.getPrice());
    }

    @Test
    public void mapToLogReturnsLog(){
        ProductRequest productRequest = ProductRequest.builder()
                .id(1L)
                .name("product")
                .label("label")
                .description("description")
                .price(10.0)
                .category(Category.JACKETS)
                .userName("user")
                .build();
        Log log = catalogService.mapToLog(productRequest);
        assertEquals(1L, log.getId());
        assertEquals("product", log.getName());
        assertEquals("label", log.getLabel());
        assertEquals("description", log.getDescription());
        assertEquals(Category.JACKETS, log.getCategory());
        assertEquals(10.0, log.getPrice());
        assertEquals("user", log.getUserName());
    }

    @Test
    public void mapToProductRequestReturnsProduct(){
        Product product = Product.builder()
                .id(1L)
                .name("product")
                .label("label")
                .description("description")
                .price(10.0)
                .category(Category.JACKETS)
                .build();
        ProductRequest productRequest = catalogService.mapToProductRequest(product);
        assertEquals(1L, productRequest.getId());
        assertEquals("product", productRequest.getName());
        assertEquals("label", productRequest.getLabel());
        assertEquals("description", productRequest.getDescription());
        assertEquals(10.0, productRequest.getPrice());
        assertEquals(Category.JACKETS, productRequest.getCategory());

    }

    @Test
    public void mapToProductResponseReturnsProductResponse(){
        Product product = Product.builder()
                .id(1L)
                .name("product")
                .label("label")
                .description("description")
                .price(10.0)
                .category(Category.JACKETS)
                .build();
        ProductResponse productResponse = catalogService.mapToProductResponse(product);
        assertEquals(1L, productResponse.getId());
        assertEquals("product", productResponse.getName());
        assertEquals("label", productResponse.getLabel());
        assertEquals("description", productResponse.getDescription());
        assertEquals(10.0, productResponse.getPrice());
        assertEquals(Category.JACKETS, productResponse.getCategory());

    }

    @Test
    public void mapToProductReturnsProduct(){
        ProductRequest productRequest = ProductRequest.builder()
                .name("product")
                .label("label")
                .description("description")
                .price(10.0)
                .category(Category.JACKETS)
                .userName("user")
                .build();
        Product product = catalogService.mapToProduct(productRequest);
        assertEquals("product", product.getName());
        assertEquals("label", product.getLabel());
        assertEquals("description", product.getDescription());
        assertEquals(10.0, product.getPrice());
        assertEquals(Category.JACKETS, product.getCategory());
    }


}
