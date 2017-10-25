package com.open.push;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;

@Slf4j
public class AppStartupListener implements ApplicationListener<ApplicationReadyEvent> {

  @Override
  public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
    ApplicationContext context = applicationReadyEvent.getApplicationContext();
    AppInitializer appInitializer = context.getBean(AppInitializer.class);
    System.out.print("=====================");

    if (appInitializer != null) {
      appInitializer.initialize();
    }
  }

}
