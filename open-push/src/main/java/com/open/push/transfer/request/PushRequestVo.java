package com.open.push.transfer.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class PushRequestVo {

  @NonNull
  private String jobId;
  @NonNull
  private String status;
  @NonNull
  private String topic;
  @NonNull
  private String title;
  @NonNull
  private String payload;
  @NonNull
  private String description;
  @NonNull
  private String messageId;
  @NonNull
  private String criteria;
  @NonNull
  private Integer resolved;
  @NonNull
  private Integer recipients;
  @NonNull
  private Integer clicks;

}
