package com.degerli.SpringBootBasics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
//@EnableAsync. It is removed since We added this annotation in AsyncConfig class.
public class SpringBootBasicsApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringBootBasicsApplication.class, args);
  }

}
