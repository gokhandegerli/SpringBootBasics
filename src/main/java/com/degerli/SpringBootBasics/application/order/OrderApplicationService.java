package com.degerli.SpringBootBasics.application.order;

import com.degerli.SpringBootBasics.domain.order.Order;
import com.degerli.SpringBootBasics.domain.order.ResponseDto;
import com.degerli.SpringBootBasics.domain.service.OrderService;
import java.util.List;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderApplicationService {

  private final OrderService orderService;

  @Autowired
  public OrderApplicationService(OrderService orderService) {
    this.orderService = orderService;
  }

  public List<ResponseDto> processOrder(Order order)
      throws InterruptedException, ExecutionException {
    // Orchestrate the order processing
    return orderService.processOrder(order);
  }
}