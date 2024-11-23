package be.pxl.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * LogbookServiceApp
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
public class LogbookServiceApp
{
    public static void main( String[] args )
    {
        SpringApplication.run(LogbookServiceApp.class, args);

    }
}
