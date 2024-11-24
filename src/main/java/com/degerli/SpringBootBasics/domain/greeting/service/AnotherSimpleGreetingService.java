package com.degerli.SpringBootBasics.domain.greeting.service;

import org.springframework.stereotype.Service;

@Service
public class AnotherSimpleGreetingService implements GreetingService {
  @Override
  public String greet(String name) {
    return "Hi, " + name;
  }
}