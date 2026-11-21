package com.zone.boot.application;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.zone.boot")
@MapperScan("com.zone.boot.infrastructure.mapper")
public class ZoneBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZoneBootApplication.class, args);
        System.out.println("📚 API文档地址: http://localhost:8212/doc.html");
    }
}