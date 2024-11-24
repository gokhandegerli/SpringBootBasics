package com.degerli.SpringBootBasics.interfaces.greeting;

import com.degerli.SpringBootBasics.domain.greeting.service.GreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

  private final GreetingService greetingService;

  @Autowired
  public GreetingController(GreetingService greetingService) {
    this.greetingService = greetingService;
  }

  @GetMapping("/greet")
  public String greet(
      @RequestParam
      String name) {
    return greetingService.greet(name);
  }
}