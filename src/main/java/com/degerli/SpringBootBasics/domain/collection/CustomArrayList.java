package com.degerli.SpringBootBasics.domain.collection;

import java.util.Arrays;

class CustomArrayList {
  private int[] array;
  private int size;

  // Constructor
  public CustomArrayList(int capacity) {
    array = new int[capacity];
    size = 0;
  }

  // Method to add an element to the list
  public void add(int data) {
    if (size == array.length) {
      resize();
    }
    array[size++] = data;
  }

  // Method to resize the internal array when capacity is reached
  private void resize() {
    array = Arrays.copyOf(array, array.length * 2);
  }

  // Method to reverse the list in place
  public void reverse() {
    for (int i = 0; i < size / 2; i++) {
      int temp = array[i];
      array[i] = array[size - i - 1];
      array[size - i - 1] = temp;
    }
  }

  // Method to print the list
  public void printList() {
    for (int i = 0; i < size; i++) {
      System.out.print(array[i] + " ");
    }
    System.out.println();
  }


  public static void main(String[] args) {
    CustomArrayList list = new CustomArrayList(4);

    // Adding elements to the list
    list.add(1);
    list.add(2);
    list.add(3);
    list.add(4);

    // Printing original list
    System.out.print("Original List: ");
    list.printList();

    // Reversing the list
    list.reverse();

    // Printing reversed list
    System.out.print("Reversed List: ");
    list.printList();

  }
}
