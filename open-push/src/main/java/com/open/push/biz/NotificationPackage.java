package com.open.push.biz;

import com.open.push.channel.bean.Notification;
import com.open.push.service.PushPriority;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class NotificationPackage extends Notification {

  //used to map certain channel delivery strategy.
  @NonNull
  private String channelCode;
  //pushRequest notification information.
  @NonNull
  private String deviceTokenType;
  @NonNull
  private String jobId;
  private String userId = "";
  private String appVersion = "";
  private String osVersion = "";

  private String deviceType = "";
  private String deviceMc = "";

  private String priority = PushPriority.NORMAL.name();


}
