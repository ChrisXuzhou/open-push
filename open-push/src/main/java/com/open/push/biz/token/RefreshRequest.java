package com.open.push.biz.token;

import com.open.push.service.User;
import java.util.List;
import lombok.Data;
import lombok.NonNull;

@Data
public class RefreshRequest {

  private final String userId;
  @NonNull
  private final String appName;
  @NonNull
  private final String deviceType;
  @NonNull
  private final String deviceToken;
  private final String appCode;
  private final String appVersion;
  private final String osVersion;
  private final String deviceMc;
  private final String deviceTokenType;
  private List<User> users;
  private transient boolean serviced = false;
}
