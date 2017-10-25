package com.open.push.transfer.token;

import com.open.push.biz.token.RefreshRequest;
import com.open.push.biz.token.RefreshRequestBuilder;
import com.open.push.biz.token.UserRefresher;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * <p>transfer层Service完成参数校验，请求调用的工作.</p>
 */
@Slf4j
@Service
@Validated
public class TokenRefreshService {

  private final UserRefresher<RefreshRequest> refresher;

  @Autowired
  public TokenRefreshService(
      final UserRefresher<RefreshRequest> refresher) {
    this.refresher = refresher;

  }

  public void refreshToken(@Valid final TokenRefreshRequest request) {

    RefreshRequestBuilder builder = new RefreshRequestBuilder()
        .userId(request.getUserId())
        .appName(request.getAppName())
        .appCode(request.getAppCode())
        .deviceType(request.getDeviceType())
        .appVersion(request.getAppVersion())
        .osVersion(request.getOsVersion())
        .deviceMc(request.getDeviceMc())
        .deviceTokenType(request.getDeviceTokenType().name())
        .deviceToken(request.getDeviceToken());

    RefreshRequest refreshRequest = builder.build();

    log.debug(ReflectionToStringBuilder.toString(refreshRequest));

    try {
      refresher.refresh(refreshRequest);
    } catch (ConstraintViolationException e) {
      log.debug("ConstraintViolationException : {}", e.getMessage(), e);
    } catch (Throwable e) {
      log.error("exception :{}", e.getMessage());
    }
  }

}
