package com.degerli.SpringBootBasics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync // Enables Spring's asynchronous method execution capability, no more need to
// add this on Main App class.
public class AsyncConfig {
  @Bean(name = "taskExecutor")
  // This indicates that the method taskExecutor() will produce a bean that will be managed
  // by the Spring container. The bean is named "taskExecutor", which allows it to be
  // referenced by this name elsewhere in the application. Beans are reusable components
  // that can be injected into other classes.
  public Executor taskExecutor() { // This defines a method named taskExecutor(), which
    // returns an object of type Executor. The Executor interface provides methods for
    // executing tasks asynchronously. This method will be called by Spring to create and
    // configure the task executor bean.
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(5); // Minimum number of threads
    executor.setMaxPoolSize(10); // Maximum number of threads
    executor.setQueueCapacity(100); // Queue size
    executor.initialize();
    return executor;
  }
}