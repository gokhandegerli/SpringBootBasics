package com.degerli.SpringBootBasics.domain.hashcodequals;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserMain {
  public static void main(String[] args) {
    User user1 = new User(1, "Alice", 30); // Creating first User object
    User user2 = new User(1, "Alice", 30); // Creating second User object with same data
    User user3 = new User(2, "Bob", 25);   // Creating third User object with different data
    User user4 = user1;                    // user4 references the same object as user1

    // Logging each user's fields
    log.info("User1 - ID: {}, Name: {}, Age: {}, created new", user1.getId(), user1.getName(),
        user1.getAge());
    log.info("User2 - ID: {}, Name: {}, Age: {}, created new", user2.getId(), user2.getName(),
        user2.getAge());
    log.info("User3 - ID: {}, Name: {}, Age: {}, created new", user3.getId(), user3.getName(),
        user3.getAge());
    log.info("User4 - ID: {}, Name: {}, Age: {}, created through user1", user4.getId(),
        user4.getName(), user4.getAge());

    log.info("-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o--o-o-o");

    // Comparing using equals
    log.info("user1 equals user2: {}", user1.equals(user2)); // true
    log.info("user1 equals user3: {}", user1.equals(user3)); // false
    log.info("user2 equals user1: {}", user2.equals(user1)); // true
    log.info("user2 equals user3: {}", user2.equals(user3)); // false
    log.info("user4 equals user1: {}", user4.equals(user1)); // true

    log.info("-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o--o-o-o");

    // Comparing with ==
    log.info("user1 refers (==) same as user2: {}",
        user1 == user2); // false, different objects
    log.info("user1 refers (==) same as user3: {}",
        user1 == user3); // false, different objects
    log.info("user1 refers (==) same as user4: {}", user1 == user4); // true, same reference
    log.info("user2 refers (==) same as user3: {}",
        user2 == user3); // false, different objects
    log.info("user3 refers (==) same as user1: {}",
        user3 == user1); // false, different objects
    log.info("user3 refers (==) same as user4: {}",
        user3 == user4); // false, different objects

    log.info("-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o--o-o-o");

    // Displaying hash codes
    log.info("Hash code of user1: {}", user1.hashCode());
    log.info("Hash code of user2: {}", user2.hashCode());
    log.info("Hash code of user3: {}", user3.hashCode());
    log.info("Hash code of user4: {}", user4.hashCode());
  }
}
