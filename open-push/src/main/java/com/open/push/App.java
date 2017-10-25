package com.open.push;

import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;



@Slf4j
@EnableAsync
@SpringBootApplication
public class App extends SpringBootServletInitializer{

  private static AtomicBoolean ready = new AtomicBoolean(true);

  public static void setReady() {
    if (ready.compareAndSet(false, true)) {
      log.info("push is ready in service!");
    }
  }

  public static boolean isReady() {
    return ready.compareAndSet(true, true);
  }

  public static void close() {
    if (ready.compareAndSet(true, false)) {
      log.info("push is closed.");
    }
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
    return application.sources(App.class);
  }

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

}

