package com.open.push.biz.task.bean;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class RequestParseRecord {

  @NonNull
  private String jobId;
  @NonNull
  private String appName;

  private long total = 0L;

  private long platformNotMatched = 0L;

  private long appVersionNotMatched = 0L;

  private long deviceTypesNotMatched = 0L;

  private long frequencyFiltered = 0L;

  private boolean isSent = false;

  public RequestParseRecord total(Long total) {
    this.total = total;
    return this;
  }

  public RequestParseRecord platformNotMatched(Long notMatched) {
    this.platformNotMatched = notMatched;
    return this;
  }

  public RequestParseRecord appVersionNotMatched(Long notMatched) {
    this.appVersionNotMatched = notMatched;
    return this;
  }

  public RequestParseRecord deviceTypesNotMatched(Long notMatched) {
    this.deviceTypesNotMatched = notMatched;
    return this;
  }

  public RequestParseRecord frequencyFiltered(Long frequencyFiltered) {
    this.frequencyFiltered = frequencyFiltered;
    return this;
  }

  public boolean finish() {

    boolean ret = false;

    if (this.total > 0L) {
      //RedisResourceLoader.saveParseRecord(jobId + appName, this);
      ret = true;
    }
    return ret;
  }

}
