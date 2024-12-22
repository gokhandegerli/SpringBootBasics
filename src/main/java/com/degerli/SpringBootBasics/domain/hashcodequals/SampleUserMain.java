package com.degerli.SpringBootBasics.domain.hashcodequals;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SampleUserMain {
  public static void main(String[] args) {
    SampleUser sampleUser1 = new SampleUser(1, "Alice", 30); // Creating first User object
    SampleUser sampleUser2 = new SampleUser(1, "Alice", 30); // Creating second User object with same data
    SampleUser sampleUser3 = new SampleUser(2, "Bob", 25);   // Creating third User object with different data
    SampleUser sampleUser4 = sampleUser1;                    // user4 references the same object as user1

    // Logging each user's fields
    log.info("User1 - ID: {}, Name: {}, Age: {}, created new", sampleUser1.getId(), sampleUser1.getName(),
        sampleUser1.getAge());
    log.info("User2 - ID: {}, Name: {}, Age: {}, created new", sampleUser2.getId(), sampleUser2.getName(),
        sampleUser2.getAge());
    log.info("User3 - ID: {}, Name: {}, Age: {}, created new", sampleUser3.getId(), sampleUser3.getName(),
        sampleUser3.getAge());
    log.info("User4 - ID: {}, Name: {}, Age: {}, created through user1", sampleUser4.getId(),
        sampleUser4.getName(), sampleUser4.getAge());

    log.info("-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o--o-o-o");

    // Comparing using equals
    log.info("user1 equals user2: {}", sampleUser1.equals(sampleUser2)); // true
    log.info("user1 equals user3: {}", sampleUser1.equals(sampleUser3)); // false
    log.info("user2 equals user1: {}", sampleUser2.equals(sampleUser1)); // true
    log.info("user2 equals user3: {}", sampleUser2.equals(sampleUser3)); // false
    log.info("user4 equals user1: {}", sampleUser4.equals(sampleUser1)); // true

    log.info("-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o--o-o-o");

    // Comparing with ==
    log.info("user1 refers (==) same as user2: {}",
        sampleUser1 == sampleUser2); // false, different objects
    log.info("user1 refers (==) same as user3: {}",
        sampleUser1 == sampleUser3); // false, different objects
    log.info("user1 refers (==) same as user4: {}", sampleUser1 == sampleUser4); // true, same reference
    log.info("user2 refers (==) same as user3: {}",
        sampleUser2 == sampleUser3); // false, different objects
    log.info("user3 refers (==) same as user1: {}",
        sampleUser3 == sampleUser1); // false, different objects
    log.info("user3 refers (==) same as user4: {}",
        sampleUser3 == sampleUser4); // false, different objects

    log.info("-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o-o--o-o-o");

    // Displaying hash codes
    log.info("Hash code of user1: {}", sampleUser1.hashCode());
    log.info("Hash code of user2: {}", sampleUser2.hashCode());
    log.info("Hash code of user3: {}", sampleUser3.hashCode());
    log.info("Hash code of user4: {}", sampleUser4.hashCode());
  }
}
