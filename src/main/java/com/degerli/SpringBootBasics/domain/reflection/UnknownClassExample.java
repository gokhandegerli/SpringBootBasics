package com.degerli.SpringBootBasics.domain.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UnknownClassExample {
  public static void main(String[] args) {
    try {
      // Class name is provided at runtime (e.g., from user input or a config file)
      String className
          = "com.degerli.SpringBootBasics.domain.reflection.MyClass";// This could be any
      // class name
      Class<?> clazz = Class.forName(className); // Load the class dynamically

      // List all fields in the class
      log.info("Fields in class: {}", className);
      for (Field field : clazz.getDeclaredFields()) {
        log.info("- {}", field.getName());
      }

      // List all methods in the class
      log.info("Methods in class: {}", className);
      for (Method method : clazz.getDeclaredMethods()) {
        log.info("- {} ", method.getName());
      }

      log.info("HashCode of class '{}' is {}", clazz, clazz.hashCode());

    } catch (ClassNotFoundException e) {
      log.error("Class not found: {}", e.getMessage());
    }
  }
}

class MyClass {
  private String name;
  private int age;

  private void sayHello() {
    System.out.println("Hello!");
  }

  private void greet(String message) {
    System.out.println(message);
  }
}