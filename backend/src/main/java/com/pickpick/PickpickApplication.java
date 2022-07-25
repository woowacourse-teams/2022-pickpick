package com.pickpick;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PickpickApplication {

    public static void main(String[] args) {
        SpringApplication.run(PickpickApplication.class, args);
    }

    // channel, member, message
    // ui > dto
    // application > dto
    // domain << entity + repository
    // 너무 복잡할 필요 없자나~ 👍

}
