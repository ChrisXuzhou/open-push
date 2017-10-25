package com.open.push.biz.push.delivery.immediate;

import com.open.push.Helper;
import com.open.push.LifeCycle;
import com.open.push.biz.NotificationPackage;
import com.open.push.biz.push.Delivery;
import com.open.push.channel.NotificationService;
import lombok.extern.slf4j.Slf4j;


/**
 * <p>完成1000条device token 逐条发送 .</p>
 *
 * <p> 完成DeliveryUnit 向具体的Notification 转换.</p>
 */
@Slf4j
public class ImmediateDelivery implements Delivery<NotificationPackage>, LifeCycle {

  private final NotificationService notificationService;

  public ImmediateDelivery(final NotificationService notificationService) {
    this.notificationService = notificationService;
  }

  @Override
  public void handle(final NotificationPackage deliveryPackage) {

    deliveryPackage.setTraceId(Helper.generateUniqueId());
    notificationService.send(deliveryPackage);
  }

  @Override
  public void initialize() {
    notificationService.initialize();
  }

  @Override
  public void close() {
    notificationService.close();
  }
}
