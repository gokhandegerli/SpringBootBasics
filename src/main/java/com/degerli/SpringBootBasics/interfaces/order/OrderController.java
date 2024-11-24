package com.degerli.SpringBootBasics.interfaces.order;


import com.degerli.SpringBootBasics.application.order.OrderApplicationService;
import com.degerli.SpringBootBasics.domain.order.model.Order;
import com.degerli.SpringBootBasics.domain.order.model.ResponseDto;
import com.degerli.SpringBootBasics.domain.order.model.Status;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderApplicationService orderApplicationService;

  @PostMapping
  public ResponseEntity<List<ResponseDto>> createOrder(
      @RequestBody
      Order order) {
    try {
      List<ResponseDto> result = orderApplicationService.processOrder(order);
      return ResponseEntity.ok(result);
    } catch (Exception e) {
      List<ResponseDto> errorResponse = Collections.singletonList(
          new ResponseDto("Error processing order", Status.FAILURE));
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
  }
}