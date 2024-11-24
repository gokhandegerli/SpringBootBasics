package com.degerli.SpringBootBasics.domain.greeting.decorator;

import com.degerli.SpringBootBasics.domain.greeting.service.GreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class AnotherGreetingServiceDecorator implements GreetingService {

  private final GreetingService greetingService;

  @Autowired
  public AnotherGreetingServiceDecorator(
      @Qualifier("greetingServiceDecorator")
      GreetingService greetingService) {
    this.greetingService = greetingService;
  }

  @Override
  public String greet(String name) {
    String originalGreeting = greetingService.greet(name);
    return originalGreeting + " Welcome to our platform!";
  }
}