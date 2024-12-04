package com.degerli.SpringBootBasics.domain.collection;

import java.util.EnumSet;

public class EnumSetExample {
  public static void main(String[] args) {
    // Creating an EnumSet with all days
    EnumSet<Day> allDays = EnumSet.allOf(Day.class);
    System.out.println("All days: " + allDays);

    // Creating an EnumSet with specific days
    EnumSet<Day> weekends = EnumSet.of(Day.SATURDAY, Day.SUNDAY);
    System.out.println("Weekends: " + weekends);

    // Creating an EnumSet with a range of days
    EnumSet<Day> workdays = EnumSet.range(Day.MONDAY, Day.FRIDAY);
    System.out.println("Workdays: " + workdays);

    // Creating an empty EnumSet
    EnumSet<Day> noDays = EnumSet.noneOf(Day.class);
    System.out.println("No days: " + noDays);
  }
}


enum Day {
  MONDAY,
  TUESDAY,
  WEDNESDAY,
  THURSDAY,
  FRIDAY,
  SATURDAY,
  SUNDAY
}