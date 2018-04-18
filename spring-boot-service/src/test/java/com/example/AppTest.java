package com.example;

import junit.framework.TestCase;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * Unit test for simple App.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.example.service"})
@ServletComponentScan(value = {})
@EnableAutoConfiguration
public class AppTest 
    extends TestCase
{
    public static void main(String[] args){
        SpringApplication.run(AppTest.class,args);
    }
}
