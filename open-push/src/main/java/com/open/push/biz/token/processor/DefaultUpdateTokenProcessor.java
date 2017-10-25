package com.open.push.biz.token.processor;

import com.open.push.biz.token.RefreshRequest;
import com.open.push.biz.token.UserProcessor;
import com.open.push.service.User;
import com.open.push.service.User.Operation;
import com.open.push.biz.token.Condition;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * <p>通常情况2：用户更新设备token时，user信息的逻辑实现.</p>
 *
 * <p>Condition: (1) 正常情况下用户更新自己的token, users仅有1条信息. </p>
 *
 * <p>(2) userId, appName, deviceType 相同, 且userId != null. </p>
 *
 * <p>(3) deviceToken 不同.</p>
 *
 * <p>users有1条信息： 更新用户token信息.</p>
 */
@Slf4j
public class DefaultUpdateTokenProcessor implements UserProcessor<RefreshRequest> {

  private Condition<RefreshRequest> condition;

  public DefaultUpdateTokenProcessor() {
    this.condition = new DefaultCondition();
  }

  @Override
  public void process(RefreshRequest request) {
    final List<User> users = request.getUsers();

    if (condition.check(request)) {
      final User user = users.get(0);
      final String deviceToken = request.getDeviceToken();

      user.setDeviceToken(deviceToken);
      refreshUserBasicInfo(request, user);
      user.setOp(Operation.UPDATE);

      request.setServiced(true);
      log.debug("Situation 2: update user's device token: user : {}",
          ToStringBuilder.reflectionToString(user));
    }
  }

  class DefaultCondition implements Condition<RefreshRequest> {

    @Override
    public boolean check(RefreshRequest request) {
      if (request.isServiced()) {
        return false;
      }

      final List<User> users = request.getUsers();
      if (1 != users.size()) {
        return false;
      }

      final User user = users.get(0);
      final String userId = request.getUserId();
      if (null == userId) {
        return false;
      }

      final String deviceType = request.getDeviceType();
      final String deviceToken = request.getDeviceToken();
      final String appName = request.getAppName();

      return StringUtils.equals(userId, user.getUserId())
          && StringUtils.equals(appName, user.getAppName())
          && StringUtils.equals(deviceType, user.getDeviceType())
          && !StringUtils.equals(deviceToken, user.getDeviceToken());

    }
  }

}
