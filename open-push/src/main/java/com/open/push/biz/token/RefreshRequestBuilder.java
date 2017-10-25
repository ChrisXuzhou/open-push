package com.open.push.biz.token;

/**
 * Build token pushRequest.
 *
 * userId、deviceType、deviceToken都不为空.
 */
public class RefreshRequestBuilder {

  private String userId;
  private String deviceToken;
  private String deviceType;
  private String appName;

  private String appCode;
  private String appVersion;
  private String osVersion;
  private String deviceMc;
  private String deviceTokenType;

  public RefreshRequestBuilder userId(String userId) {
    this.userId = userId;
    return this;
  }

  public RefreshRequestBuilder deviceToken(String deviceToken) {
    this.deviceToken = deviceToken;
    return this;
  }

  public RefreshRequestBuilder deviceType(String deviceType) {
    this.deviceType = deviceType;
    return this;
  }

  public RefreshRequestBuilder appName(String appName) {
    this.appName = appName;
    return this;
  }

  public RefreshRequestBuilder appCode(String appCode) {
    this.appCode = appCode;
    return this;
  }

  public RefreshRequestBuilder appVersion(String appVersion) {
    this.appVersion = appVersion;
    return this;
  }

  public RefreshRequestBuilder osVersion(String osVersion) {
    this.osVersion = osVersion;
    return this;
  }

  public RefreshRequestBuilder deviceMc(String deviceMc) {
    this.deviceMc = deviceMc;
    return this;
  }

  public RefreshRequestBuilder deviceTokenType(String deviceTokenType) {
    this.deviceTokenType = deviceTokenType;
    return this;
  }

  public RefreshRequest build() {
    return new RefreshRequest(userId, appName, deviceType, deviceToken, appCode, appVersion,
        osVersion, deviceMc, deviceTokenType);
  }
}

