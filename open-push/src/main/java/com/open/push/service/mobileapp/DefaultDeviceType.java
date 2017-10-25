package com.open.push.service.mobileapp;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum DefaultDeviceType implements DeviceTokenTypeMapper {
  IPHONE {
    @Override
    public String getDeviceTokenType() {
      return "iOS";
    }
  },
  IPAD {
    @Override
    public String getDeviceTokenType() {
      return "iOS";
    }
  },
  ANDROID {
    @Override
    public String getDeviceTokenType() {
      return "ANDROID";
    }
  },
  OTHER,;


  public static DefaultDeviceType belong2(String type) {

    final String upperType = type.toUpperCase();
    try {
      for (DefaultDeviceType deviceType : DefaultDeviceType.values()) {
        if (upperType.startsWith(deviceType.name())) {

          return deviceType;
        }
      }

    } catch (Exception e) {
      log.error("belong2 check error! type: {} ", type, e);
    }
    return null;
  }
}
