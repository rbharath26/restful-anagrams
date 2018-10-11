package com.racabe.anagrams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class of the application. Using the annotation @SpringBootApplication are adding all the
 * following:
 * 
 * @Configuration tags the class as a source of bean definitions for the application context.
 * 
 * @EnableAutoConfiguration tells Spring Boot to start adding beans based on classpath settings,
 *                          other beans, and various property settings.
 * 
 *                          Normally you would add @EnableWebMvc for a Spring MVC app, but Spring
 *                          Boot adds it automatically when it sees spring-webmvc on the classpath.
 *                          This flags the application as a web application.
 * 
 * @ComponentScan tells Spring to look for other components, configurations, and services in the
 *                hello package, allowing it to find the controllers.
 */
@SpringBootApplication
public class AnagramsApplication {

  /**
   * Run the application using Spring Boot and an embedded servlet engine.
   * 
   * @param args Program arguments.
   */
  public static void main(String[] args) {
    SpringApplication.run(AnagramsApplication.class, args);
  }
}
