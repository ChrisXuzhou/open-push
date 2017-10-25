package com.open.push.biz.task.pack;

import com.open.push.service.User;
import com.open.push.support.DozerHelper;
import com.open.push.biz.NotificationPackage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

/**
 * <p>用来将消息依据'平台'、'channel'、'应用'打包为消息.</p>
 *
 * <p>Not multi-thread safe!</p>
 */
@Slf4j
public abstract class AbstractNotificationPacker {

  private NotificationPackage template;

  AbstractNotificationPacker(NotificationPackage template) {
    this.template = template;
  }

  public NotificationPackage pack(final User user) {

    final String tokenType = user.getDeviceTokenType();
    Assert.isTrue(isTokenTypeSupported(tokenType),
        "device token type not supported. " + tokenType);

    return doPackByDozer(user);
  }

  private NotificationPackage doPackByDozer(User user){
    final NotificationPackage notificationPackage = DozerHelper
        .map(template, NotificationPackage.class);

    DozerHelper.map(user, notificationPackage);
    return notificationPackage;
  }

  public abstract boolean isTokenTypeSupported(final String devicePlatform);

}
