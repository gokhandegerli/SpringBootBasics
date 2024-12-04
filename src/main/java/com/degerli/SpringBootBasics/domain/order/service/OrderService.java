package com.degerli.SpringBootBasics.domain.order.service;

/**
 * OrderService is a Spring service class responsible for processing customer orders.
 * It handles payment processing, pizza preparation, and other related tasks.
 * The class uses asynchronous programming to execute certain tasks in parallel,
 * improving efficiency and reducing overall processing time.
 *
 * Key Features:
 * - Uses Spring's @Service annotation to mark it as a service component.
 * - Leverages Lombok's @Slf4j annotation for logging.
 * - Utilizes asynchronous programming for non-blocking operations.
 * - Supports two approaches for asynchronous execution:
 *    1. Using @Async annotation.
 *    2. Using CompletableFuture.supplyAsync() with a custom thread pool.
 *
 * Comparison of Approaches:
 * - With @Async:
 *    - Methods run in separate threads managed by Spring's default or custom thread pool.
 * - With supplyAsync():
 *    - Methods are executed asynchronously using a custom thread pool (taskExecutor).
 *    - Provides more control over thread management and error handling.
 *
 * Execution time is the same for both @Async and supplyAsync() because the tasks are executed in parallel, and the total time is determined by the longest-running task.
 *
 * Methods:
 * 1. processPayment(Order order):
 *    - Processes the payment for the given order.
 *    - Logs the payment processing status.
 *    - Returns a boolean indicating whether the payment was successful.
 *
 * 2. cookPizza(Order order):
 *    - Simulates the process of cooking a pizza for the given order.
 *    - Can be executed asynchronously using @Async or supplyAsync().
 *    - Returns a CompletableFuture<ResponseDto> with the result of the operation.
 *
 * 3. preparePizzaBox(Order order):
 *    - Simulates the preparation of a pizza box for the given order.
 *    - Can be executed asynchronously using @Async or supplyAsync().
 *    - Returns a CompletableFuture<ResponseDto> with the result of the operation.
 *
 * 4. prepareDrinks(Order order):
 *    - Simulates the preparation of drinks for the given order.
 *    - Can be executed asynchronously using @Async or supplyAsync().
 *    - Returns a CompletableFuture<ResponseDto> with the result of the operation.
 *
 * 5. processOrder(Order order):
 *    - Orchestrates the entire order processing workflow.
 *    - First, processes the payment for the order.
 *    - If payment is successful, it initiates the following tasks asynchronously:
 *        - Cooking the pizza
 *        - Preparing the pizza box
 *        - Preparing the drinks
 *    - Waits for all asynchronous tasks to complete using CompletableFuture.allOf().
 *    - Collects the results of the tasks and returns them as a list of ResponseDto objects.
 *    - If payment fails, it returns a failure response.
 *
 * Usage:
 * - This service is designed to handle order processing in a non-blocking, efficient manner.
 * - Ensure that the application is configured to enable asynchronous execution by adding
 *   @EnableAsync to a configuration class (if using @Async).
 * - Alternatively, configure a custom thread pool for supplyAsync().
 *
 * Dependencies:
 * - Spring Framework for @Service and @Async annotations.
 * - Lombok for @Slf4j annotation.
 * - CompletableFuture for asynchronous task handling.
 * - ResponseDto and Status classes for encapsulating task results.
 */

import com.degerli.SpringBootBasics.domain.order.model.Order;
import com.degerli.SpringBootBasics.domain.order.model.ResponseDto;
import com.degerli.SpringBootBasics.domain.order.model.Status;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class OrderService {

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

  private boolean processPayment(Order order) {
    log.info("Processing payment for order:{}", order.getId());
    return true; // Assume payment is successful
  }

  @Async
  // This annotation allows the method to be executed asynchronously in a separate thread.
  public CompletableFuture<ResponseDto> cookPizza(Order order) {
    log.info("Cooking pizza for order: {}", order.getId());
    try {
      Thread.sleep(5000); // Simulate time taken to cook the pizza. In a real implementation,
      // replace this with the actual cooking logic.
      return CompletableFuture.completedFuture(
          new ResponseDto("Pizza cooked", Status.SUCCESS));
    } catch (InterruptedException e) {
      return CompletableFuture.completedFuture(
          new ResponseDto("Failed to cook pizza", Status.FAILURE));
    }
  }

  @Async
  public CompletableFuture<ResponseDto> preparePizzaBox(Order order) {
    log.info("Preparing pizza box for order: {}", order.getId());
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
    log.info("Preparing drinks for order: {}", order.getId());
    try {
      Thread.sleep(3000); // Simulate time taken to prepare drinks
      return CompletableFuture.completedFuture(
          new ResponseDto("Drinks prepared", Status.SUCCESS));
    } catch (InterruptedException e) {
      return CompletableFuture.completedFuture(
          new ResponseDto("Failed to prepare drinks", Status.FAILURE));
    }
  }
}

/*
// the one format which is managed by means of supplyAsync instead on @Async on each method.
@Service
@Slf4j
public class OrderService {

  @Autowired
  private Executor taskExecutor; // Inject a custom thread pool executor

  public List<ResponseDto> processOrder(Order order) {
    List<ResponseDto> responses = new ArrayList<>();

    if (processPayment(order)) {
      // Use supplyAsync to execute tasks asynchronously
      CompletableFuture<ResponseDto> pizzaFuture = CompletableFuture.supplyAsync(
          () -> processTask("Cooking pizza", 5000, "Pizza cooked"), taskExecutor);

      CompletableFuture<ResponseDto> boxFuture = CompletableFuture.supplyAsync(
          () -> processTask("Preparing pizza box", 2000, "Pizza box prepared"), taskExecutor);

      CompletableFuture<ResponseDto> drinksFuture = CompletableFuture.supplyAsync(
          () -> processTask("Preparing drinks", 3000, "Drinks prepared"), taskExecutor);

      // Wait for all tasks to complete
      CompletableFuture.allOf(pizzaFuture, boxFuture, drinksFuture).join();

      // Collect results
      responses.add(pizzaFuture.join());
      responses.add(boxFuture.join());
      responses.add(drinksFuture.join());
    } else {
      responses.add(new ResponseDto("Payment failed", Status.FAILURE));
    }

    return responses;
  }

  private boolean processPayment(Order order) {
    log.info("Processing payment for order: {}", order.getId());
    return true; // Assume payment is successful
  }

  private ResponseDto processTask(String taskName, int delay, String successMessage) {
    log.info("{} started", taskName);
    try {
      Thread.sleep(delay); // Simulate task delay
      return new ResponseDto(successMessage, Status.SUCCESS);
    } catch (InterruptedException e) {
      log.error("{} failed", taskName, e);
      return new ResponseDto(taskName + " failed", Status.FAILURE);
    }
  }
}
}*/
