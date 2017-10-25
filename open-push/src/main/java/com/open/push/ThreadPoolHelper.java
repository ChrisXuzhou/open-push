package com.open.push;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

public class ThreadPoolHelper {

  private static final int processors = Runtime.getRuntime().availableProcessors();
  private static final int corePoolSize = processors / 2 + 1;
  private static final int maximumPoolSize = processors / 2 + 1;
  private static final int keepAliveTime = 5000;
  private static ExecutorService executorService = new TimingThreadPoolExecutor(corePoolSize,
      maximumPoolSize * 8, keepAliveTime, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(256),
      new ThreadPoolExecutor.AbortPolicy());

  public static void submit(Runnable runnable) {
    executorService.submit(runnable);
  }

  private static ExecutorService taskExecutorService = new TimingThreadPoolExecutor(corePoolSize,
      maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(256),
      new ThreadPoolExecutor.AbortPolicy());

  public static void submitTask(Runnable runnable) {
    taskExecutorService.submit(runnable);
  }

  private static ExecutorService deliveryTaskExecutorService = new TimingThreadPoolExecutor(
      corePoolSize * 2,
      maximumPoolSize * 2, keepAliveTime, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(256),
      new ThreadPoolExecutor.AbortPolicy());

  public static void submitDeliveryTask(Runnable runnable) {
    deliveryTaskExecutorService.submit(runnable);
  }


  @Slf4j
  public static class TimingThreadPoolExecutor extends ThreadPoolExecutor {

    private final ThreadLocal<Long> startTime = new ThreadLocal<>();

    TimingThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,
        TimeUnit unit, BlockingQueue<Runnable> workQueue,
        RejectedExecutionHandler handler) {
      super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
      super.beforeExecute(t, r);
      startTime.set(System.currentTimeMillis());
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
      try {
        long endTime = System.currentTimeMillis();
        long taskTime = endTime - startTime.get();
        //log.debug("time spent! {}", taskTime);
      } finally {
        super.afterExecute(r, t);
      }
    }
  }

}
