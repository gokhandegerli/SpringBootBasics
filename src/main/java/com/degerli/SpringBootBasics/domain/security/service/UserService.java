package com.degerli.SpringBootBasics.domain.security.service;

import com.degerli.SpringBootBasics.domain.security.model.User;
import com.degerli.SpringBootBasics.infra.security.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashSet;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public User registerUser(User user) {
    // Check if username exists
    if (userRepository.findByUsername(user.getUsername()).isPresent()) {
      throw new RuntimeException("Username already exists");
    }

    // Check if email exists
    if (userRepository.findByEmail(user.getEmail()).isPresent()) {
      throw new RuntimeException("Email already exists");
    }

    // Encode password
    user.setPassword(passwordEncoder.encode(user.getPassword()));

    // Set default role
    if (user.getRoles() == null) {
      user.setRoles(new HashSet<>());
    }
    user.getRoles().add("USER");

    return userRepository.save(user);
  }
}