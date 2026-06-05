package com.infiren.taskmaster;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@SpringBootApplication
public class TaskMasterApplication {


    public static void main(String[] args) {
        SpringApplication.run(TaskMasterApplication.class, args);
    }




    /*
    Context pool printer
     */
//    @Bean
//    public CommandLineRunner init(ApplicationContext context) {
//        return args -> {
//            System.out.println("Context:" + context);
//
//            String[] beanDefinitionNames = context.getBeanDefinitionNames();
//            Arrays.sort(beanDefinitionNames);
//            for (String beanDefinitionName : beanDefinitionNames) {
//                System.out.println(beanDefinitionName);
//            }
//        };
//    }
}
