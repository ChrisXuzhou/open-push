package com.open.push.transfer.token;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p> 更新token信息的请求POJO.</p>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenRefreshRequest {

  @Size(min = 1, max = 100)
  private String userId;
  @NotNull
  @Size(min = 1, max = 60)
  private String appCode;
  @NotNull
  @Size(min = 1, max = 50)
  private String appVersion;
  @NotNull
  @Size(min = 1, max = 32)
  private String osVersion;
  @NotNull
  @Size(min = 1, max = 32)
  private String deviceType;
  @NotNull
  @Size(min = 1, max = 200)
  private String deviceMc;
  @NotNull
  @Size(min = 1, max = 200)
  private String deviceToken;
  @NotNull
  @Size(min = 1, max = 60)
  private String appName;

  private DeviceTokenType deviceTokenType;

  private String androidDeviceType;

}
