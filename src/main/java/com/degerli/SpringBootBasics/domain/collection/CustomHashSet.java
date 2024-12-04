package com.degerli.SpringBootBasics.domain.collection;

import java.util.LinkedList;

class Node {
  int data;
  Node next;

  Node(int data) {
    this.data = data;
    this.next = null;
  }
}

class CustomHashSet {
  private static final int INITIAL_CAPACITY = 16; // Initial capacity of the buckets
  private LinkedList<Node>[] buckets;

  // Constructor
  public CustomHashSet() {
    buckets = new LinkedList[INITIAL_CAPACITY];
    for (int i = 0; i < INITIAL_CAPACITY; i++) {
      buckets[i] = new LinkedList<>();
    }
  }

  // Hash function
  private int getBucketIndex(int data) {
    return data % INITIAL_CAPACITY;
  }

  // Method to add an element to the set
  public void add(int data) {
    int index = getBucketIndex(data);
    LinkedList<Node> bucket = buckets[index];

    // Check if element already exists
    for (Node node : bucket) {
      if (node.data == data) {
        return; // Element already exists, do not add
      }
    }

    // Add new element
    Node newNode = new Node(data);
    bucket.add(newNode);
  }

  // Method to remove an element from the set
  public void remove(int data) {
    int index = getBucketIndex(data);
    LinkedList<Node> bucket = buckets[index];

    // Find and remove the element if it exists
    Node toRemove = null;
    for (Node node : bucket) {
      if (node.data == data) {
        toRemove = node;
        break;
      }
    }

    if (toRemove != null) {
      bucket.remove(toRemove);
    }
  }

  // Method to check if the set contains an element
  public boolean contains(int data) {
    int index = getBucketIndex(data);
    LinkedList<Node> bucket = buckets[index];

    for (Node node : bucket) {
      if (node.data == data) {
        return true;
      }
    }
    return false;
  }

  // Method to print the set
  public void printSet() {
    for (LinkedList<Node> bucket : buckets) {
      for (Node node : bucket) {
        System.out.print(node.data + " ");
      }
    }
    System.out.println();
  }

  public static void main(String[] args) {
    CustomHashSet set = new CustomHashSet();

    // Adding elements to the set
    set.add(1);
    set.add(2);
    set.add(3);
    set.add(4);
    set.add(2); // Duplicate element

    // Printing the set
    System.out.print("CustomHashSet elements: ");
    set.printSet();

    // Checking if elements exist in the set
    System.out.println("Set contains 2: " + set.contains(2)); // true
    System.out.println("Set contains 5: " + set.contains(5)); // false

    // Removing an element from the set
    set.remove(2);
    System.out.print("CustomHashSet elements after removing 2: ");
    set.printSet();
  }
}
