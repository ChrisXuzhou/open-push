package com.open.push.channel.pushy;

import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BadDeviceTokenType {

  private static final ImmutableSet<String> BAD_DEVICE_TOKEN_TYPE = ImmutableSet.of(
      "Unregistered",
      "BadDeviceToken",
      "DeviceTokenNotForTopic");

  public static boolean contain(String value) {
    //log.debug("failed reason: {}", value);
    return BAD_DEVICE_TOKEN_TYPE.contains(value.trim());
  }

}
