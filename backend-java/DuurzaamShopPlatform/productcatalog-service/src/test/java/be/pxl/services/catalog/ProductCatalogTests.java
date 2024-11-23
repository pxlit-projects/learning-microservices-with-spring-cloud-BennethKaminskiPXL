package be.pxl.services.catalog;

import be.pxl.services.catalog.domain.Category;
import be.pxl.services.catalog.domain.Product;
import be.pxl.services.catalog.repository.CatalogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
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



    @Container
    private static MySQLContainer sqlContainer =
            new MySQLContainer<>("mysql:5.7.37");

    @DynamicPropertySource
    static void registerMySQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", sqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", sqlContainer::getUsername);
        registry.add("spring.datasource.password", sqlContainer::getPassword);
    }

    @Test
    public void createProduct() throws Exception {
        Product product = Product.builder()
                .name("product")
                .label("label")
                .description("description")
                .price(10.0)
                .category(Category.JACKETS)
                .build();

        String productString = objectMapper.writeValueAsString(product);

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
                .name("product")
                .label("label")
                .description("description")
                .price(10.0)
                .category(Category.JACKETS)
                .build();
        catalogRepository.save(product);

        Product updatedProd = Product.builder()
                .name("updated")
                .label("label")
                .description("description")
                .price(10.0)
                .category(Category.JACKETS)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.put("/api/catalog/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProd)))
                .andExpect(status().isAccepted());

        assertEquals("updated", catalogRepository.findById(1L).get().getName());
        assertEquals(1, catalogRepository.findAll().size());

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

}
