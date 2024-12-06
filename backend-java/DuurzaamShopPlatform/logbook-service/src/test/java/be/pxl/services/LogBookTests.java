package be.pxl.services;


import be.pxl.services.domain.Category;
import be.pxl.services.log.Log;
import be.pxl.services.repository.LogRepository;
import be.pxl.services.services.LogService;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.testcontainers.shaded.org.hamcrest.Matchers.hasSize;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class LogBookTests {
    @Autowired
    private LogService logService;
    @Autowired
    private LogRepository logRepository;

    @Container
    private static MySQLContainer sqlContainer =
            new MySQLContainer<>("mysql:5.7.37");
    @Autowired
    private MockMvc mockMvc;


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
        logRepository.deleteAll();
    }

    @AfterEach
    public void cleanDatabaseAfterTest() {
        logRepository.deleteAll();
    }

    @Test
    public void testSaveLogToDatabase() {
        // Arrange
        Log log = Log.builder()
                .label("label")
                .name("name")
                .description("description")
                .category(Category.JACKETS)
                .price(10.0)
                .userName("user")
                .action("action")
                .build();
        // Act
        logService.saveLogToDatabase(log);
        // Assert
        assertThat(logRepository.findAll()).hasSize(1);
    }
    @Test
    public void getLogBook() {

        Log log = Log.builder()
                .label("label")
                .name("name")
                .description("description")
                .category(Category.JACKETS)
                .price(10.0)
                .userName("user")
                .action("action")
                .build();
        logService.saveLogToDatabase(log);

        var logBook = logService.getLogBook();

        assertThat(logBook).hasSize(1);
    }
    @Test
    public void getLogBookWithController() throws Exception {
        Log log = Log.builder()
                .label("label")
                .name("name")
                .description("description")
                .category(Category.JACKETS)
                .price(10.0)
                .userName("user")
                .action("action")
                .build();
        logService.saveLogToDatabase(log);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/logbook")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].label").value("label"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("description"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].category").value(Category.JACKETS.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].price").value(10.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].userName").value("user"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].action"   ).value("action"));

    }




}
