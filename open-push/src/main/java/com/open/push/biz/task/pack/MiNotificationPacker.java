package com.open.push.biz.task.pack;

import com.open.push.biz.NotificationPackage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class MiNotificationPacker extends AbstractNotificationPacker {

  public MiNotificationPacker(NotificationPackage template) {

    super(template);
    template.setDeviceTokenType("MI");
    template.setChannelCode(template.getChannelCode() + "mi");

  }

  @Override
  public boolean isTokenTypeSupported(String tokenType) {
    return StringUtils.equals(tokenType, "MI");
  }

}
