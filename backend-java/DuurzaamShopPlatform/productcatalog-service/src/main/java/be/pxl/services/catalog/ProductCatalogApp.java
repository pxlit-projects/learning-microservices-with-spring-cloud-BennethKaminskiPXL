package be.pxl.services.catalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * productcatalog-serviceApp
 *
 */
@EnableDiscoveryClient // discovery client tegenhanger van discovery server : eureka netflix
@SpringBootApplication
@EnableFeignClients // na in pom.xml spring-cloud-starter-openfeign
// zorgt voor communicatie met logbook door client
public class ProductCatalogApp
{
    public static void main( String[] args )
    {
        SpringApplication.run(ProductCatalogApp.class, args);
    }
}
