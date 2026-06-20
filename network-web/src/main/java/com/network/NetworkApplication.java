package com.network;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class NetworkApplication {

    public static void main(String[] args) {
        System.out.println("111");
        SpringApplication.run(NetworkApplication.class, args);

    }

}
