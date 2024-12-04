package be.pxl.services;

import be.pxl.services.controller.dto.ShoppingCartRequest;
import be.pxl.services.domain.Product;
import be.pxl.services.domain.ShoppingCart;
import be.pxl.services.repository.ProductRepository;
import be.pxl.services.repository.ShoppingCartRepository;
import be.pxl.services.services.CartService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class ShoppingCartTests {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private CartService shoppingCartService;



    @Container
    private static MySQLContainer sqlContainer =
            new MySQLContainer<>("mysql:5.7.37");
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartService cartService;

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
        shoppingCartRepository.deleteAll();
        productRepository.deleteAll();
    }

    @AfterEach
    public void cleanDatabaseAfterTest() {
        shoppingCartRepository.deleteAll();
        productRepository.deleteAll();
    }
    @Test
    public void createShoppingCart() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/cart"))
                .andExpect(status().isCreated());

        assertEquals(1, shoppingCartRepository.findAll().size());
    }
    //todo post addproductId
    @Test
    public void addProductAddsProduct() throws Exception {
        ShoppingCartRequest shoppingCartRequest = new ShoppingCartRequest(1L,"product",10.0);
        String cartString = objectMapper.writeValueAsString(shoppingCartRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(cartString))
                .andExpect(status().isCreated());
    }

    @Test
    public void testAddProductToCart_ProductAddedSuccessfully() throws Exception {
        // Arrange
        Product product = new Product();
        product.setId(1L);
        long productId = product.getId();
        product = productRepository.save(product);

        ShoppingCart shoppingCart = new ShoppingCart();

        shoppingCart = shoppingCartRepository.save(shoppingCart);
        long shoppingCartId = shoppingCart.getId();
        // Act
        mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/" + shoppingCartId + "/addProduct/" + productId))
                .andExpect(status().isAccepted());

    }


    @Test
    public void updateProduct() throws Exception {
        ShoppingCartRequest shoppingCartRequest = new ShoppingCartRequest(1L,"prod",10.0);
        Product product = Product.builder().id(1L).name("product").price(10.0).quantity(1).build();
        productRepository.save(product);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/cart/product")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(shoppingCartRequest)))
                .andExpect(status().isAccepted());
        assertEquals("prod", productRepository.findById(1L).get().getName());
    }
    @Test
    public void removeProduct() throws Exception {
        ShoppingCartRequest shoppingCartRequest = new ShoppingCartRequest(1L,"product",10.0);
        Product product = Product.builder().id(1L).name("product").price(10.0).quantity(1).build();
        productRepository.save(product);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/cart/product")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(shoppingCartRequest)))
                .andExpect(status().isAccepted());
        assertEquals(0, productRepository.findAll().size());
    }


    @Test
    public void addProductToCartAddsProduct() throws Exception {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCartRepository.save(shoppingCart);
        Product product = Product.builder().id(1L).name("product").price(10.0).quantity(1).build();
        productRepository.save(product);
        Long cartId = shoppingCart.getId();
        Long productId = product.getId();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/"+cartId+"/addProduct/"+productId))
                .andExpect(status().isAccepted());

        assertEquals(1, shoppingCartRepository.findAll().size());
        assertEquals(1, productRepository.findAll().size());
    }

    @Test
    public void removeProductFromCart() throws Exception {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        Product product = Product.builder().id(1L).name("product").price(10.0).quantity(1).build();
        productRepository.save(product);
        List<Product> productList = List.of(product);
        shoppingCart.setShoppingCartItems(productList);
        shoppingCartRepository.save(shoppingCart);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/cart/1/removeProduct/1"))
                .andExpect(status().isAccepted());
    }

    @Test
    public void removeProductFromCart2() throws Exception {
        // Create and save a product
        Product product = Product.builder()
                .id(1L)
                .name("Test Product")
                .price(10.0)
                .quantity(2)
                .build();
        productRepository.save(product);

        // Create shopping cart with the product
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        shoppingCart.setShoppingCartItems(new ArrayList<>(Arrays.asList(product)));
        shoppingCartRepository.save(shoppingCart);

        // Perform remove product request and verify
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/cart/1/removeProduct/1"))
                .andExpect(status().isAccepted());

        // Verify that the product is removed from the shopping cart
        ShoppingCart updatedShoppingCart = shoppingCartRepository.findById(1L).orElseThrow();
        assertThat(updatedShoppingCart.getShoppingCartItems()).isEmpty();
    }

    @Test
    public void getProductsInCart() throws Exception {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        Product product = Product.builder().id(1L).name("product").price(10.0).quantity(1).build();
        Product product2 = Product.builder().id(2L).name("product2").price(10.0).quantity(1).build();
        productRepository.save(product);
        productRepository.save(product2);
        List<Product> productList = List.of(product,product2);

        shoppingCart.setShoppingCartItems(productList);
        shoppingCartRepository.save(shoppingCart);
        long cartId = shoppingCart.getId();

        mockMvc.perform(MockMvcRequestBuilders.get("/api/cart/"+cartId+"/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("product"))
                .andExpect(jsonPath("$[1].name").value("product2"))
                .andExpect(jsonPath("$[0].price").value(10.0))
                .andExpect(jsonPath("$[1].price").value(10.0))
                .andExpect(jsonPath("$.size()").value(2));
              //  .andExpect(jsonPath("$[0].name").value("product"));

    }

    @Test
    public void checkout() throws Exception {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);
        Product product = Product.builder().id(1L).name("product").price(10.0).quantity(1).build();
        Product product2 = Product.builder().id(2L).name("product2").price(10.0).quantity(1).build();
        List<Product> productList = List.of(product,product2);
        shoppingCart.setShoppingCartItems(productList);
        shoppingCartRepository.save(shoppingCart);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/"+1L+"/checkout"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.totalPrice").value(20.0))
                .andExpect(jsonPath("$.products[0].name").value("product"))
                .andExpect(jsonPath("$.products[1].name").value("product2"))
                .andExpect(jsonPath("$.products[0].price").value(10.0))
                .andExpect(jsonPath("$.products[1].price").value(10.0))
                .andExpect(jsonPath("$.products.size()").value(2));
    }

    //todo checkout testen


}
