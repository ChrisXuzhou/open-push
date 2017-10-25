package com.open.push.channel.pushy;

import com.open.push.Helper;
import com.open.push.channel.ChannelException;
import com.open.push.biz.push.delivery.ImmediateAPNSPostOperator;
import com.open.push.channel.NotificationService;
import com.open.push.biz.NotificationPackage;
import java.util.concurrent.atomic.AtomicLong;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PushyNotificationService implements NotificationService {

  private PushyChannel pushyChannel;
  private PushyRegistry registry;

  private ImmediateAPNSPostOperator immediateAPNSPostOperator;

  public PushyNotificationService(PushyRegistry registry,
      ImmediateAPNSPostOperator immediateAPNSPostOperator) {
    this.registry = registry;
    this.immediateAPNSPostOperator = immediateAPNSPostOperator;
  }

  public void send(NotificationPackage notification) {

    if (null == pushyChannel) {
      log.error("pushy initialize error! pushyChannel is nul. try initialize..");
      initialize();
      return;
    }

    if (1000 < PushyNotificationService.count.get()) {
      log.info("pushy buffer count: {}", PushyNotificationService.count.get());
      Helper.sleep(500);
    }

    PushyNotificationService.count.incrementAndGet();
    pushyChannel.send(notification);
  }

  public static AtomicLong count = new AtomicLong(0);

  @Override
  public void initialize() {
    try {
      pushyChannel = new PushyChannelBuilder(registry, immediateAPNSPostOperator).build();
    } catch (ChannelException e) {
      log.error("Pushy initialize error!", e);
      throw new RuntimeException();
    }

  }

  @Override
  public void close() {
    if (null != pushyChannel) {
      pushyChannel.close();
    }
  }


}
