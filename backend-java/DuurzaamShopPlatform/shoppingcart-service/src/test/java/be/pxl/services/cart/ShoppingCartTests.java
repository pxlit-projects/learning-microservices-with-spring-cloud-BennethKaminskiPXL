package be.pxl.services.cart;

import be.pxl.services.cart.domain.Product;
import be.pxl.services.cart.domain.ShoppingCart;
import be.pxl.services.cart.repository.ShoppingCartRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.List;

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
    public void createShoppingCart() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/cart"))
                .andExpect(status().isCreated());

        assertEquals(1, shoppingCartRepository.findAll().size());
    }
    //todo post addproductId

    @Test
    public void removeProductFromCart() throws Exception {
        ShoppingCart shoppingCart = new ShoppingCart();
        Product product = Product.builder().id(1L).name("product").price(10.0).quantity(1).build();
        List<Product> productList = List.of(product);
        shoppingCart.setProducts(productList);
        shoppingCartRepository.save(shoppingCart);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/cart/1/removeProduct/1"))
                .andExpect(status().isAccepted());
    }

    //werkt niet
    @Test
    public void getProductsInCart() throws Exception {
        ShoppingCart shoppingCart = new ShoppingCart();
        Product product = Product.builder().id(1L).name("product").price(10.0).quantity(1).build();
        List<Product> productLister = List.of(product);
        shoppingCart.setProducts(productLister);
        shoppingCartRepository.save(shoppingCart);
        Long cartId = shoppingCart.getId();
        mockMvc.perform(MockMvcRequestBuilders.get("/api/cart/"+cartId+"/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("product"));

    }

    //todo checkout testen


}
