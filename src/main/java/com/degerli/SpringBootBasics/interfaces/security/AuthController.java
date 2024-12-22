package com.degerli.SpringBootBasics.interfaces.security;

import com.degerli.SpringBootBasics.domain.security.model.User;
import com.degerli.SpringBootBasics.domain.security.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

  private final UserService userService;

  public AuthController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/register")
  public String showRegistrationForm(Model model) {
    model.addAttribute("user", new User());
    return "register";
  }

  @PostMapping("/register")
  public String registerUser(
      @ModelAttribute("user")
      User user, BindingResult result) {
    if (result.hasErrors()) {
      return "register";
    }

    try {
      userService.registerUser(user);
      return "redirect:/login?registered";
    } catch (RuntimeException e) {
      result.rejectValue("username", "error.user", e.getMessage());
      return "register";
    }
  }
}