package com.open.push.biz.task.pack;

import com.open.push.biz.NotificationPackage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class UPushNotificationPacker extends AbstractNotificationPacker {

  public UPushNotificationPacker(NotificationPackage template) {
    super(template);
    template.setDeviceTokenType("UPush");
    template.setChannelCode(template.getChannelCode() + "uPush");

  }

  @Override
  public boolean isTokenTypeSupported(String tokenType) {
    return StringUtils.equals(tokenType, "UPush");
  }
}
