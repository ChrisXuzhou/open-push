package com.open.push.biz.push.delivery;

import com.open.push.Helper;
import com.open.push.biz.push.delivery.buffer.NotificationBuffer.BufferedNotificationGroup;
import com.open.push.channel.PostSendOperator;
import com.open.push.channel.bean.BatchNotification;
import com.open.push.channel.bean.Status;
import com.open.push.service.Message;
import com.open.push.service.MessageService;
import com.open.push.service.impl.async.AsyncUserService;
import com.open.push.support.DozerHelper;
import com.open.push.biz.NotificationPackage;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Slf4j
@Service
public class BufferedDefaultPostOperator implements PostSendOperator<BatchNotification> {

  private MessageService messageService;
  private AsyncUserService asyncUserService;

  public BufferedDefaultPostOperator(MessageService messageService,
      AsyncUserService asyncUserService) {
    this.messageService = messageService;
    this.asyncUserService = asyncUserService;
  }

  /**
   * <p>1. 将成功／失败的notification持久化.</p>
   * <p>2. 重发特定的notification.</p>
   */
  public void postSendOperate(final BatchNotification batchNotification) {

    Assert.isTrue(batchNotification instanceof BufferedNotificationGroup,
        "notificationWrapper must be instance of BatchNotification!");

    final BufferedNotificationGroup bufferedMiNotificationGroup = (BufferedNotificationGroup) batchNotification;
    final Map<String, NotificationPackage> packages = bufferedMiNotificationGroup.getPackages();

    final List<String> badTokens = batchNotification.getBadTokens();
    if (CollectionUtils.isNotEmpty(badTokens)) {
      for (String badToken : badTokens) {
        final NotificationPackage notificationPackage = packages.get(badToken);
        if (null != notificationPackage) {
          notificationPackage.setStatus(Status.FAILED);
        }
      }
    }

    for (Map.Entry<String, NotificationPackage> entry : packages.entrySet()) {
      final NotificationPackage notificationPackage = entry.getValue();
      final Message message = DozerHelper.map(notificationPackage, Message.class);

      final Status status = notificationPackage.getStatus();

      switch (status) {
        case NONE:
          message.setStatus(Status.SUCCESS.name());
          break;
        default:
          message.setStatus(Status.FAILED.name());
          break;
      }

      record(message);
    }
  }

  private void record(Message message) {

    final String status = message.getStatus();
    /*
    while (!messageService.trySave(message)) {
      CommonHelper.sleep(2000);
    }*/

    Helper.log(message);

    if (StringUtils.equals(Status.FAILED.name(), status)) {
      asyncUserService.tryDelete(message.getDeviceToken());
    }
  }

}
