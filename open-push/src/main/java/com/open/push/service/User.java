package com.open.push.service;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * User Entity
 */
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class User {

  private Long id;

  private String userId;
  @NonNull
  private String appName;
  @NonNull
  private String appCode;
  @NonNull
  private String appVersion;
  @NonNull
  private String osVersion;
  @NonNull
  private String deviceType;
  @NonNull
  private String deviceMc;
  @NonNull
  private String deviceTokenType;
  @NonNull
  private String deviceToken;

  private Integer version;
  private transient Operation op = Operation.NONE;

  public User(String userId, String appName, String appCode, String appVersion,
      String osVersion, String deviceType, String deviceMc, String deviceTokenType,
      String deviceToken) {
    this.userId = userId;
    this.appName = appName;
    this.appCode = appCode;
    this.appVersion = appVersion;
    this.osVersion = osVersion;
    this.deviceType = deviceType;
    this.deviceMc = deviceMc;
    this.deviceTokenType = deviceTokenType;
    this.deviceToken = deviceToken;
  }

  public enum Operation {
    NONE, INSERT, UPDATE, DELETE
  }
}
