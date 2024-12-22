package com.degerli.SpringBootBasics.infra.security;


import com.degerli.SpringBootBasics.domain.security.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);
}