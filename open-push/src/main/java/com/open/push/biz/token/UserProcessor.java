package com.open.push.biz.token;

import com.open.push.service.User;
import com.open.push.service.User.Operation;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>作为更新用户信息的逻辑单元</p>
 */
public interface UserProcessor<T extends RefreshRequest> {

  void process(T t);

  default void refreshUserBasicInfo(T request, User user) {

    if (null != request && null != user) {

      final String userId = request.getUserId();
      if (StringUtils.isNotEmpty(userId)) {
        user.setUserId(userId);
      }

      final String deviceToken = request.getDeviceToken();
      user.setDeviceToken(deviceToken);

      final String appVersion = request.getAppVersion();
      if (!StringUtils.equals("EMPTY", appVersion)) {
        user.setAppVersion(appVersion);
      }

      final String osVersion = request.getOsVersion();
      if (!StringUtils.equals("EMPTY", osVersion)) {
        user.setOsVersion(osVersion);
      }

      final String deviceMc = request.getDeviceMc();
      if (!StringUtils.equals("EMPTY", deviceMc)) {
        user.setDeviceMc(deviceMc);
      }

      final String deviceTokenType = request.getDeviceTokenType();
      if (!StringUtils.equals("EMPTY", deviceTokenType)) {
        user.setDeviceTokenType(deviceTokenType);
      }

      final String deviceType = request.getDeviceType();
      if (!StringUtils.equals("EMPTY", deviceType)) {
        user.setDeviceType(deviceType);
      }
    }
  }

  default User buildNewUser(RefreshRequest request) {
    User user = new User(request.getUserId(), request.getAppName(), request.getAppCode(),
        request.getAppVersion(), request.getOsVersion(), request.getDeviceType(),
        request.getDeviceMc(), request.getDeviceTokenType(), request.getDeviceToken());

    user.setOp(Operation.INSERT);
    return user;
  }


}
