package com.open.push.transfer.request;

import javax.validation.constraints.Size;
import lombok.Value;

@Value
public class ModifyPushRequestStatusRequest {

  @Size(min = 1, max = 36)
  private String jobId;

}
