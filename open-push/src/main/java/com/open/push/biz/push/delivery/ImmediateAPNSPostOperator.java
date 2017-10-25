package com.open.push.biz.push.delivery;

import com.open.push.Helper;
import com.open.push.channel.PostSendOperator;
import com.open.push.channel.bean.Notification;
import com.open.push.channel.bean.Status;
import com.open.push.channel.pushy.BadDeviceTokenType;
import com.open.push.channel.pushy.PushyNotificationService;
import com.open.push.service.Message;
import com.open.push.service.impl.async.AsyncUserService;
import com.open.push.support.DozerHelper;
import com.open.push.biz.NotificationPackage;
import com.open.push.support.PushQueueService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Slf4j
@Service
public class ImmediateAPNSPostOperator implements PostSendOperator<Notification> {

  private AsyncUserService asyncUserService;
  private PushQueueService queueService;

  public ImmediateAPNSPostOperator(PushQueueService queueService,
      AsyncUserService asyncUserService) {
    this.queueService = queueService;
    this.asyncUserService = asyncUserService;
  }

  /**
   * <p>1. 将成功／失败的notification持久化.</p>
   * <p>2. 重发特定的notification.</p>
   */
  public void postSendOperate(Notification notification) {

    Assert.isTrue(notification instanceof NotificationPackage,
        "notification must be instance of NotificationPackage!");

    NotificationPackage notificationPackage = (NotificationPackage) notification;

    final Status status = notificationPackage.getStatus();
    final Message message = DozerHelper.map(notificationPackage, Message.class);

    if (Status.RETRY == status) {
      queueService.put(notificationPackage);
      log.debug("notification :{} should be retried!",
          ReflectionToStringBuilder.toString(notificationPackage));
      return;
    }

    PushyNotificationService.count.decrementAndGet();

    switch (status) {
      case NONE:
        message.setStatus(Status.SUCCESS.name());
        break;
      default:
        message.setStatus(Status.FAILED.name());
        break;
    }

    /*
    while (!messageService.trySave(message)) {
      CommonHelper.sleep(2000);
    }*/

    Helper.log(message);

    if (Status.FAILED == status) {
      final String failedReason = notification.getFailedReason();
      if (BadDeviceTokenType.contain(failedReason)) {
        final String deviceToken = notification.getDeviceToken();
        asyncUserService.tryDelete(deviceToken);
      }
    }
  }

}
