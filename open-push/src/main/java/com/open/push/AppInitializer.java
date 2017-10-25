package com.open.push;


import com.open.push.biz.push.PushService;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AppInitializer implements LifeCycle, ApplicationContextAware {

  private ApplicationContext context;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.context = applicationContext;
  }

  @Override
  public void initialize() {

    log.info("start push initializing.");

    final PushService pushService
        = context.getBean(PushService.class);
    ((LifeCycle)pushService).initialize();

    log.info("finish push initializing.");


  }


  @Override
  public void close() {
    throw new UnsupportedOperationException();
  }

}
