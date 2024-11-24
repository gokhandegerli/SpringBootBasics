package com.degerli.SpringBootBasics.domain.service;

import com.degerli.SpringBootBasics.domain.order.Order;
import com.degerli.SpringBootBasics.domain.order.ResponseDto;
import com.degerli.SpringBootBasics.domain.order.Status;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  public boolean processPayment(Order order) {
    System.out.println("Processing payment for order: " + order.getId());
    return true; // Assume payment is successful
  }

  @Async
  public CompletableFuture<ResponseDto> cookPizza(Order order) {
    System.out.println("Cooking pizza for order: " + order.getId());
    try {
      Thread.sleep(5000); // Simulate time taken to cook pizza
      return CompletableFuture.completedFuture(
          new ResponseDto("Pizza cooked", Status.SUCCESS));
    } catch (InterruptedException e) {
      return CompletableFuture.completedFuture(
          new ResponseDto("Failed to cook pizza", Status.FAILURE));
    }
  }

  @Async
  public CompletableFuture<ResponseDto> preparePizzaBox(Order order) {
    System.out.println("Preparing pizza box for order: " + order.getId());
    try {
      Thread.sleep(2000); // Simulate time taken to prepare pizza box
      return CompletableFuture.completedFuture(
          new ResponseDto("Pizza box prepared", Status.SUCCESS));
    } catch (InterruptedException e) {
      return CompletableFuture.completedFuture(
          new ResponseDto("Failed to prepare pizza box", Status.FAILURE));
    }
  }

  @Async
  public CompletableFuture<ResponseDto> prepareDrinks(Order order) {
    System.out.println("Preparing drinks for order: " + order.getId());
    try {
      Thread.sleep(3000); // Simulate time taken to prepare drinks
      return CompletableFuture.completedFuture(
          new ResponseDto("Drinks prepared", Status.SUCCESS));
    } catch (InterruptedException e) {
      return CompletableFuture.completedFuture(
          new ResponseDto("Failed to prepare drinks", Status.FAILURE));
    }
  }

  public List<ResponseDto> processOrder(Order order)
      throws InterruptedException, ExecutionException {
    List<ResponseDto> responses = new ArrayList<>();

    if (processPayment(order)) {
      CompletableFuture<ResponseDto> pizzaFuture = cookPizza(order);
      CompletableFuture<ResponseDto> boxFuture = preparePizzaBox(order);
      CompletableFuture<ResponseDto> drinksFuture = prepareDrinks(order);

      // Wait for all tasks to complete and collect their results
      CompletableFuture.allOf(pizzaFuture, boxFuture, drinksFuture).join();

      responses.add(pizzaFuture.get());
      responses.add(boxFuture.get());
      responses.add(drinksFuture.get());
    } else {
      responses.add(new ResponseDto("Payment failed", Status.FAILURE));
    }

    return responses;
  }
}