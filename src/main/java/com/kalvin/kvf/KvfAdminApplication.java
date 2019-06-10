package com.kalvin.kvf;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
@MapperScan(value = "com.kalvin.kvf.gen.mapper")
public class KvfAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(KvfAdminApplication.class, args);
    }

}
