package com.open.push.biz.push.delivery.buffer;

import com.open.push.biz.push.Buffered;
import com.open.push.biz.push.Delivery;
import com.open.push.channel.BatchNotificationService;
import com.open.push.biz.NotificationPackage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

@Slf4j
public class BufferedDelivery implements Delivery<NotificationPackage>, Buffered {

  private NotificationBufferWrapper buffer;
  private BatchNotificationService miNotificationService;

  public BufferedDelivery(BatchNotificationService miNotificationService, int bufferSize) {
    this.buffer = new NotificationBufferWrapper(miNotificationService, bufferSize);
    this.miNotificationService = miNotificationService;
  }

  @Override
  public void handle(NotificationPackage notificationPackage) {
    buffer.insert(notificationPackage);
  }

  @Override
  public void initialize() {
    Assert.notNull(buffer, "buffer must not be null.");
    miNotificationService.initialize();
  }

  @Override
  public void close() {
    buffer.flushAll();
  }

  @Override
  public void refresh() {
    buffer.flushAll();
  }
}
