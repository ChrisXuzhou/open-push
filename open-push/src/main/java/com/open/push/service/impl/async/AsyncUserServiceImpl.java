package com.open.push.service.impl.async;

import com.open.push.Helper;
import com.open.push.dao.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AsyncUserServiceImpl implements AsyncUserService, Runnable {

  private final ReentrantLock lock;

  private final List<String> cache;
  private final static int DEFAULT_CACHE_SIZE = 10000;

  private UserRepository userRepository;

  public AsyncUserServiceImpl(UserRepository userRepository) {
    this.cache = new ArrayList<>(DEFAULT_CACHE_SIZE);
    this.lock = new ReentrantLock();
    this.userRepository = userRepository;
  }

  @Override
  public boolean tryDelete(String deviceToken) {

    //log.debug("cache : {}", ReflectionToStringBuilder.toString(cache));

    if (service && lock.tryLock()) {
      try {
        if (DEFAULT_CACHE_SIZE - 100 > cache.size() && StringUtils.isNotEmpty(deviceToken)) {
          cache.add(deviceToken);
        }
      } finally {
        lock.unlock();
      }
      return true;
    }
    return false;
  }

  private boolean service = true;

  @Override
  public void run() {

    log.debug("async user service running.");

    for (; ; ) {
      while (0 < cache.size()) {
        log.debug("sync push uer info.");
        flush();
      }
      Helper.sleep(200);

      if (!service) {
        log.info("async user service closed.");
        break;
      }
    }
  }

  public void flush() {
    lock.lock();
    try {
      userRepository.deleteUserPoByDeviceTokenIn(cache);
      cache.clear();
    } finally {
      lock.unlock();
    }
  }

  private ExecutorService executorService;

  @PostConstruct
  public void initialize() {
    executorService = Executors.newSingleThreadExecutor();
    executorService.submit(this);
  }

  @PreDestroy
  public void close() {

    service = false;
    while (0 < cache.size()) {
      Helper.sleep(2000);
    }
    executorService.shutdownNow();
    log.info("async bulk user service closed..");
  }
}
