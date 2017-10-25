package com.open.push.service.mobileapp;


public interface DeviceTokenTypeMapper {

  default String getDeviceTokenType() {
    throw new UnsupportedOperationException();
  }

}
