package com.open.push.channel.bean;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class BatchNotification {

  @NonNull
  private Long traceId;

  @NonNull
  private List<String> deviceTokens;

  @NonNull
  private String title;
  @NonNull
  private String payload;
  @NonNull
  private String topic;
  @NonNull
  private String description;

  private Status status = Status.NONE;


  private transient List<String> badTokens;

}
