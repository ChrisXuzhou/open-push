package com.open.push.biz.push;


import com.open.push.biz.NotificationPackage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

public class DefaultValidator implements NotificationValidator<NotificationPackage> {

  @Override
  public void validate(NotificationPackage notificationPackage) {

    final String code = notificationPackage.getChannelCode();
    Assert.notNull(code, "batch unit code must not be null!");

    final String deviceToken = notificationPackage.getDeviceToken();
    Assert.isTrue(StringUtils.isNotEmpty(deviceToken),
        "device token must not be null or empty.");
  }
}

