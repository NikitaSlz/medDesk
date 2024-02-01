package com.example.meddesk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class MedDeskApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedDeskApplication.class, args);
    }

}
