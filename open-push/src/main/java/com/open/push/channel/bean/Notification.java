package com.open.push.channel.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Notification {

  @NonNull
  private Long traceId;

  @NonNull
  private String deviceToken;

  @NonNull
  private String title;
  @NonNull
  private String payload;
  @NonNull
  private String topic;
  @NonNull
  private String description;

  private Status status = Status.NONE;

  private transient String failedReason = "";

}
