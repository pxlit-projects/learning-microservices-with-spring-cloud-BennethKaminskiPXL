package be.pxl.services;

import be.pxl.services.client.CatalogClient;
import be.pxl.services.controller.dto.ShoppingCartRequest;
import be.pxl.services.domain.Product;
import be.pxl.services.domain.ShoppingCart;
import be.pxl.services.repository.ShoppingCartRepository;
import be.pxl.services.services.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import static org.assertj.core.api.Assertions.catchIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class ShoppingCartTests {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @MockBean
    private CatalogClient catalogClient;

    @Autowired
    private CartService cartService;


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
        shoppingCartRepository.deleteAll();
    }

    @AfterEach
    public void cleanDatabaseAfterTest() {
        shoppingCartRepository.deleteAll();
    }

    @Test
    public void testCreateCart() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(result -> {
                    long cartId = Long.parseLong(result.getResponse().getContentAsString());
                    ShoppingCart shoppingCart = shoppingCartRepository.findById(cartId).orElse(null);
                    assertThat(shoppingCart).isNotNull();
                });
    }

    @Test
    public void testAddProductToCart() throws Exception {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCartRepository.save(shoppingCart);
        long cartId = shoppingCart.getId();
        long productId = 1L;
        int quantity = 1;
        Product product = Product.builder().id(productId).name("product1").price(10.0).build();

        when(catalogClient.getProductById(productId)).thenReturn(product);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/{cartId}/addProduct/{productId}/{quantity}", cartId, productId, quantity)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
        //returns niets
//                .andExpect(jsonPath("$.id").value(productId))
//                .andExpect(jsonPath("$.name").value("product1"))
//                .andExpect(jsonPath("$.price").value(10.0))
//                .andExpect(jsonPath("$.quantity").value(1));

    }

    @Test
    public void testAddProductToCartThrowsArgumentException() throws Exception {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCartRepository.save(shoppingCart);
        long cartId = shoppingCart.getId();
        long productId = 1L;
        int quantity = 1;
        when(catalogClient.getProductById(productId)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> {
            cartService.addProductToCart(cartId, productId, quantity);
        });
    }

    @Test
    public void testRemoveProductFromCart() throws Exception {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCartRepository.save(shoppingCart);
        long cartId = shoppingCart.getId();
        long productId = 1L;
        int quantity = 1;
        Product product = Product.builder().id(productId).name("product1").price(10.0).build();

        when(catalogClient.getProductById(productId)).thenReturn(product);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/{cartId}/addProduct/{productId}/{quantity}", cartId, productId, quantity)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/cart/{cartId}/removeProduct/{productId}", cartId, productId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }
    @Test
    public void RemovingProductFromCartWithQuantityGreaterThanOneDecreasesQuantity() throws Exception {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCartRepository.save(shoppingCart);
        long cartId = shoppingCart.getId();
        long productId = 1L;
        int quantity = 2;
        Product product = Product.builder().id(productId).name("product1").price(10.0).build();

        when(catalogClient.getProductById(productId)).thenReturn(product);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/{cartId}/addProduct/{productId}/{quantity}", cartId, productId, quantity)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/cart/{cartId}/removeProduct/{productId}", cartId, productId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }

    @Test
    public void GetProductsInCartShouldReturnAListOfCartProductResponse() throws Exception {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCartRepository.save(shoppingCart);
        long cartId = shoppingCart.getId();
        long productId = 1L;
        int quantity = 1;
        Product product = Product.builder().id(productId).name("product1").price(10.0).build();

        when(catalogClient.getProductById(productId)).thenReturn(product);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/{cartId}/addProduct/{productId}/{quantity}", cartId, productId, quantity)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());

        cartId = shoppingCart.getId();
        productId = 2L;
        product = Product.builder().id(productId).name("product2").price(10.0).build();


        when(catalogClient.getProductById(productId)).thenReturn(product);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/{cartId}/addProduct/{productId}/{quantity}", cartId, productId, quantity)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/cart/{cartId}/products", cartId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("product1"))
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    public void CheckoutShouldReturnCartCheckoutResponse() throws Exception {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCartRepository.save(shoppingCart);
        long cartId = shoppingCart.getId();
        long productId = 1L;
        int quantity = 1;
        Product product = Product.builder().id(productId).name("product1").price(10.0).build();

        when(catalogClient.getProductById(productId)).thenReturn(product);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/{cartId}/addProduct/{productId}/{quantity}", cartId, productId, quantity)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());

        cartId = shoppingCart.getId();
        productId = 2L;
        product = Product.builder().id(productId).name("product2").price(10.0).build();
        when(catalogClient.getProductById(productId)).thenReturn(product);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/{cartId}/addProduct/{productId}/{quantity}", cartId, productId, quantity)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/{cartId}/checkout", cartId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.totalPrice").value(20.0))
                .andExpect(jsonPath("$.products[0].name").value("product1"))
                .andExpect(jsonPath("$.products[1].name").value("product2"));

    }


}