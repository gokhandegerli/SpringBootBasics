package com.degerli.SpringBootBasics.domain.hashcodequals;

import java.util.Objects;
import lombok.Getter;

@Getter
public class SampleUser {
  private int id;
  private String name;
  private int age;

  public SampleUser(int id, String name, int age) {
    this.id = id;
    this.name = name;
    this.age = age;
  }

  /*  We override both of equals and hashcode methods since If two objects are equal
   according to the equals(Object) method, they must have the same hash code. This is crucial
   for the proper functioning of hash-based collections. If two objects are not equal, they
   may have the same or different hash codes. However, it is generally good practice to try
   to minimize collisions (different objects having the same hash code) for performance
   reasons. Below, that is why we use same fields for both equals and hashing logics */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SampleUser sampleUser = (SampleUser) o;
    return id == sampleUser.id && age == sampleUser.age && Objects.equals(name, sampleUser.name);
  }

  /*Override hashCode() method
  Objects.hash(...): This is a static utility method that takes one or more arguments (in
  this case, id, name, and age) and computes a hash code for them.
  */

  @Override
  public int hashCode() {
    return Objects.hash(id, name, age);
  }
}
