package ru.ivanov.intern.chartographer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class ChartographerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChartographerApplication.class, args);
    }

}
