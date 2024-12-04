package com.degerli.SpringBootBasics.domain.collection;


class DoublyLinkedListNode {
  int data;
  DoublyLinkedListNode next;
  DoublyLinkedListNode prev;

  DoublyLinkedListNode(int data) {
    this.data = data;
    this.next = null;
    this.prev = null;
  }
}


public class CustomDoublyLinkedList {

  DoublyLinkedListNode head;


  // Function to reverse the doubly linked list
  public DoublyLinkedListNode reverse(DoublyLinkedListNode head) {
    if (head == null) {
      return null; // Return if the list is empty
    }

    DoublyLinkedListNode current = head;
    DoublyLinkedListNode temp = null;

    // Traverse the list and swap next and prev pointers for each node
    while (current != null) {
      temp = current.prev;
      current.prev = current.next;
      current.next = temp;
      current = current.prev; // Move to the next node (which is now previous)
    }

    // temp is now the last node, make it the new head
    if (temp != null) {
      head = temp.prev;
    }

    return head;
  }

  // Helper method to print the list (for testing)
  public void printList(DoublyLinkedListNode node) {
    while (node != null) {
      System.out.print(node.data + " ");
      node = node.next;
    }
    System.out.println();
  }

  // Helper method to add node to the end (for testing)
  public void append(int data) {
    DoublyLinkedListNode newNode = new DoublyLinkedListNode(data);

    if (head == null) {
      head = newNode;
      return;
    }

    DoublyLinkedListNode last = head;
    while (last.next != null) {
      last = last.next;
    }
    last.next = newNode;
    newNode.prev = last;
  }

  public static void main(String[] args) {
    CustomDoublyLinkedList list = new CustomDoublyLinkedList();

    // Sample input: 4 nodes with data 1, 2, 3, 4
    list.append(1);
    list.append(2);
    list.append(3);
    list.append(4);

    // Printing original list
    System.out.print("Original List: ");
    list.printList(list.head);

    // Reversing the list
    list.head = list.reverse(list.head);

    // Printing reversed list
    System.out.print("Reversed List: ");
    list.printList(list.head);
  }
}
