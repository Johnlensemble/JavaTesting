package com.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Run with: mvn spring-boot:run
 * Or build and run the jar: mvn package && java -jar target/inventory-api-1.0.0.jar
 */
// Java 21: Enable virtual threads by adding spring.threads.virtual.enabled=true to application.properties
@SpringBootApplication
public class InventoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(InventoryApplication.class, args);
    }
}
