package com.degerli.SpringBootBasics.domain.security.model;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false,
      unique = true)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false,
      unique = true)
  private String email;

  @ElementCollection(fetch = FetchType.EAGER)
  private Set<String> roles;

  private boolean enabled = true;
}