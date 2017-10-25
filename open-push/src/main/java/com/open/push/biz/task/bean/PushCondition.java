package com.open.push.biz.task.bean;

import lombok.Data;

@Data
public class PushCondition {

  /**
   * <p>只能是iOS或Android.</p>
   */
  private String platform;

  private String appVersion;

  private String deviceTypes;

  private Boolean checkFrequency;
}
