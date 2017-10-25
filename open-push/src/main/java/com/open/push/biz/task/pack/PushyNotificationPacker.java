package com.open.push.biz.task.pack;

import com.open.push.biz.NotificationPackage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class PushyNotificationPacker extends AbstractNotificationPacker {

  public PushyNotificationPacker(NotificationPackage template) {
    super(template);

    template.setDeviceTokenType("iOS");
    template.setChannelCode(template.getChannelCode() + "pushy");
  }

  @Override
  public boolean isTokenTypeSupported(String devicePlatform) {
    return StringUtils.equals(devicePlatform, "iOS");
  }

}
