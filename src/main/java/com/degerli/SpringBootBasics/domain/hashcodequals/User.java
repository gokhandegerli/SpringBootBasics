package com.degerli.SpringBootBasics.domain.hashcodequals;

import java.util.Objects;
import lombok.Getter;

@Getter
public class User {
  // Getters and setters (if needed)
  private int id;
  private String name;
  private int age;

  // Constructor
  public User(int id, String name, int age) {
    this.id = id;
    this.name = name;
    this.age = age;
  }

  // Override equals() method
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return id == user.id && age == user.age && Objects.equals(name, user.name);
  }

  // Override hashCode() method
  @Override
  public int hashCode() {
    return Objects.hash(id, name, age);
  }
}
