package com.open.push.service;

import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class MonitorRecord {

  @NonNull
  private String jobId;
  @NonNull
  private Long startTime;

  @NonNull
  private Long amount;
  private Long sent;

  private boolean isFinished;

}
