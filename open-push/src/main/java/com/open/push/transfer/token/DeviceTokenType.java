package com.open.push.transfer.token;

import com.open.push.service.DevicePlatform;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public enum DeviceTokenType implements PlatformMapper {

  UPush {
    @Override
    public DevicePlatform getDevicePlatform() {
      return DevicePlatform.ANDROID;
    }
  },
  ANDROID {
    @Override
    public DevicePlatform getDevicePlatform() {
      return DevicePlatform.ANDROID;
    }
  },
  iOS {
    @Override
    public DevicePlatform getDevicePlatform() {
      return DevicePlatform.iOS;
    }
  },
  XIAOMI {
    @Override
    public DevicePlatform getDevicePlatform() {
      return DevicePlatform.ANDROID;
    }
  },;

  public static DeviceTokenType of(String name) {
    try {
      for (DeviceTokenType deviceTokenType : DeviceTokenType.values()) {
        if (StringUtils.equals(name, deviceTokenType.name())) {
          return deviceTokenType;
        }
      }
    } catch (Exception e) {
      log.error("device token type map error name[{}]", name, e);
    }

    return null;
  }

}

interface PlatformMapper {

  DevicePlatform getDevicePlatform();
}

