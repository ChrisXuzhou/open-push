package com.open.push.service;

import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Message {

  private Integer id;

  @NonNull
  private Long traceId;
  @NonNull
  private String devicePlatform;
  @NonNull
  private String deviceToken;
  @NonNull
  private String status;
  @NonNull
  private String userId;
  @NonNull
  private String appVersion;
  @NonNull
  private String osVersion;
  @NonNull
  private String jobId;
  @NonNull
  private Date sendTime = new Date();
  @NonNull
  private Date clickTime = new Date();

  private String deviceType = "";

  private String deviceMc = "";

}
