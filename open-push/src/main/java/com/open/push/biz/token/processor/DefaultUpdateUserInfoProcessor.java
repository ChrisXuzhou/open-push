package com.open.push.biz.token.processor;

import com.open.push.service.User;
import com.open.push.service.User.Operation;
import com.open.push.biz.token.Condition;
import com.open.push.biz.token.RefreshRequest;
import com.open.push.biz.token.UserProcessor;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * <p>通常情况3.1：同一设备不同用户启动,更新token对应user信息, 但该用户之前没有token信息.（有两种相似情况见 3.1 3.2 ）</p>
 *
 * <p>Condition: (1) users仅有1条信息. (2) userId 不同. (3)  appName, deviceType, deviceToken 相同. </p>
 *
 * <p>users有1条信息： 更新用户deviceType, userId信息.</p>
 */
@Slf4j
public class DefaultUpdateUserInfoProcessor implements UserProcessor<RefreshRequest> {

  private Condition<RefreshRequest> condition;

  public DefaultUpdateUserInfoProcessor() {
    this.condition = new DefaultCondition();
  }

  @Override
  public void process(RefreshRequest request) {
    final List<User> users = request.getUsers();

    if (condition.check(request)) {
      final User user = users.get(0);
      final String userId = request.getUserId();

      user.setUserId(userId);
      refreshUserBasicInfo(request, user);
      user.setOp(Operation.UPDATE);

      request.setServiced(true);
      log.debug(
          "Situation 3.1: update user's information based on same device token: user : {}",
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

      final String appName = request.getAppName();
      final String deviceType = request.getDeviceType();
      final String deviceToken = request.getDeviceToken();

      return !StringUtils.equals(userId, user.getUserId())
          && StringUtils.equals(appName, user.getAppName())
          && StringUtils.equals(deviceType, user.getDeviceType())
          && StringUtils.equals(deviceToken, user.getDeviceToken());

    }
  }


}
