package com.open.push.biz.token.processor.multichannel;

import com.open.push.biz.token.RefreshRequest;
import com.open.push.service.User;
import com.open.push.service.User.Operation;
import com.open.push.biz.token.Condition;
import com.open.push.biz.token.UserProcessor;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * <p>Condition: users仅有1条信息, 更新用户token信息. </p>
 *
 * <p>(1) userId, deviceType, deviceMc, deviceToken 有任意不同. </p>
 */
@Slf4j
public class MultiChannelSize1Processor implements UserProcessor<RefreshRequest> {

  private Condition<RefreshRequest> condition;

  public MultiChannelSize1Processor() {
    this.condition = new DefaultCondition();
  }

  @Override
  public void process(RefreshRequest request) {
    final List<User> users = request.getUsers();

    if (condition.check(request)) {

      final User user = users.get(0);
      refreshUserBasicInfo(request, user);
      user.setOp(Operation.UPDATE);

      log.debug("Multi Channel Situation size 1: update user's device token: user : {}",
          ToStringBuilder.reflectionToString(user));
    }
  }

  class DefaultCondition implements Condition<RefreshRequest> {

    @Override
    public boolean check(RefreshRequest request) {

      final List<User> users = request.getUsers();
      if (1 != users.size()) {
        return false;
      }

      final User user = users.get(0);

      final String userId = request.getUserId();
      final String deviceMc = request.getDeviceMc();
      final String deviceType = request.getDeviceType();
      final String deviceToken = request.getDeviceToken();

      final String osVersion = request.getOsVersion();
      final String appVersion = request.getAppVersion();

      return !StringUtils.equals(userId, user.getUserId())
          || !StringUtils.equals(deviceMc, user.getDeviceMc())
          || !StringUtils.equals(deviceType, user.getDeviceType())
          || !StringUtils.equals(deviceToken, user.getDeviceToken())
          || !StringUtils.equals(osVersion, user.getOsVersion())
          || !StringUtils.equals(appVersion, user.getAppVersion());

    }
  }

}
