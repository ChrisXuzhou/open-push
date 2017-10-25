package com.open.push.biz.push.delivery.buffer;

import com.open.push.Helper;
import com.open.push.ThreadPoolHelper;
import com.open.push.biz.push.delivery.buffer.NotificationBuffer.BufferedNotificationGroup;
import com.open.push.channel.BatchNotificationService;
import com.open.push.biz.NotificationPackage;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.RejectedExecutionException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotificationBufferWrapper {

  private final NotificationBuffer cache;
  private final BatchNotificationService batchNotificationService;

  NotificationBufferWrapper(BatchNotificationService batchNotificationService, int bufferSize) {
    this.batchNotificationService = batchNotificationService;
    this.cache = new NotificationBuffer(bufferSize);
  }

  public void insert(NotificationPackage notificationPackage) {

    synchronized (cache) {
      int size = cache.insert(notificationPackage);
      if (size > cache.ITEM_SIZE - 1) {
        flush(notificationPackage.getJobId());
      }
    }
  }

  private void flush(String key) {

    synchronized (cache) {
      final BufferedNotificationGroup bufferedMiNotificationGroup = cache.get(key);
      submit(bufferedMiNotificationGroup);
      cache.remove(key);
    }
  }

  void flushAll() {

    synchronized (cache) {
      final Map<String, BufferedNotificationGroup> notificationBuffers = cache
          .getNotificationBuffers();
      Iterator<Entry<String, BufferedNotificationGroup>> iterator
          = notificationBuffers.entrySet().iterator();

      while (iterator.hasNext()) {
        Entry<String, BufferedNotificationGroup> entry = iterator.next();
        final String key = entry.getKey();
        final BufferedNotificationGroup bufferedMiNotificationGroup = cache.get(key);
        submit(bufferedMiNotificationGroup);
        iterator.remove();
      }
    }
  }

  private void submit(BufferedNotificationGroup bufferedMiNotificationGroup) {

    for (; ; ) {
      try {
        ThreadPoolHelper.submit(() -> {
          try {
            batchNotificationService.send(bufferedMiNotificationGroup);
          } catch (Exception e) {
            log.error("send mi notification error. ", e);
          }
        });
        break;
      } catch (RejectedExecutionException e) {
        Helper.sleep(1000);
      }
    }

  }

}