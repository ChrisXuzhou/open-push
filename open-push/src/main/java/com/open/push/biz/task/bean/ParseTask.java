package com.open.push.biz.task.bean;

import com.open.push.biz.task.RequestParser;
import com.open.push.service.PushRequest;
import com.open.push.service.PushRequestDetail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParseTask {

  @NonNull
  private PushRequest pushRequest;

  @NonNull
  private PushRequestDetail pushRequestDetail;

  @NonNull
  private String appName;

  @NonNull
  private String topic;

  @NonNull
  private RequestParser<ParseTask> requestParser;

}
