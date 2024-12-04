package com.degerli.SpringBootBasics.domain.hashcodequals;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringComparison {

  public static void main(String[] args) {
    String str1 = "Sample";           // references the string pool object
    String str2 = "Sample";           // references the same object in the string pool
    String str3 = new String("Sample"); // creates a new object in the heap
    String str4 = "Sample";           // references the string pool object

    // Displaying hash codes. All the strings (str1, str2, str3, and str4) have the same
    // hash code because they contain the same content "Sample"
    log.info("Hash code of str1: {}", str1.hashCode());
    log.info("Hash code of str2: {}", str2.hashCode());
    log.info("Hash code of str3: {}", str3.hashCode());
    log.info("Hash code of str4: {}", str4.hashCode());

    // Comparing if "references" refer same objects in the spring pool
    log.info("str1 == str2: {}", (str1 == str2)); // true
    log.info("str1 == str3: {}", (str1 == str3)); // false
    log.info("str1 == str4: {}", (str1 == str4)); // true

    // Comparing content using equals
    log.info("str1 equals str3: {}", str1.equals(str3)); // true
  }
}

