package be.pxl.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * shoppingcart service
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ShoppingCartServiceApp
{
    public static void main( String[] args )
    {
        SpringApplication.run(ShoppingCartServiceApp.class, args);
    }
}
