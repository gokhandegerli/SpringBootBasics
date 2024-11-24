package com.degerli.SpringBootBasics.domain.greeting.decorator;

import com.degerli.SpringBootBasics.domain.greeting.service.GreetingService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary // This annotation ensures that this implementation is used when GreetingService is autowired
public class GreetingServiceDecorator implements GreetingService {

  @Qualifier("simpleGreetingService")
  private final GreetingService greetingService;

  public GreetingServiceDecorator(
      @Qualifier("simpleGreetingService")
      GreetingService greetingService) {
    this.greetingService = greetingService;
  }

  @Override
  public String greet(String name) {
    String originalGreeting = greetingService.greet(name);
    return originalGreeting + " Have a great day!";
  }
}