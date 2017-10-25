package com.open.push.biz.token.processor;

import com.open.push.biz.token.RefreshRequest;
import com.open.push.biz.token.UserProcessor;
import com.open.push.service.User;
import com.open.push.service.User.Operation;
import com.open.push.biz.token.Condition;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * <p>通常情况3.2：同一设备不同用户启动,更新token对应user信息, 该用户之前有token信息.（有两种相似情况见 3.1 3.2 ）</p>
 *
 * <p>Condition: (1) users有2条信息. </p>
 *
 * <p>(2) 其中一条userId 不同，deviceType, deviceToken 相同.</p>
 *
 * <p>(3）另一条 userId, deviceType 相同， deviceToken 不同.</p>
 *
 * <p>Operation: users有2条信息： 更新用户(2)token信息, 删除另一条(3)的信息.</p>
 */
@Slf4j
public class DefaultDuplicateUserInfoProcessor implements UserProcessor<RefreshRequest> {

  private Condition<RequestWrapper> condition;

  public DefaultDuplicateUserInfoProcessor() {
    this.condition = new DefaultCondition();
  }

  @Override
  public void process(RefreshRequest request) {
    RequestWrapper wrapper = new RequestWrapper(request);

    if (condition.check(wrapper)) {
      final String deviceToken = request.getDeviceToken();
      wrapper.getUser2Update().setDeviceToken(deviceToken);
      wrapper.getUser2Update().setOp(Operation.UPDATE);
      refreshUserBasicInfo(request, wrapper.getUser2Update());
      wrapper.getUser2Delete().setOp(Operation.DELETE);
      reassembleUserList(wrapper);

      request.setServiced(true);
      log.debug(
          "Situation 3.2: Update and delete user's information based on device token. user-to-update: {} ; user-to-delete: {}",
          ToStringBuilder.reflectionToString(wrapper.getUser2Update()),
          ToStringBuilder.reflectionToString(wrapper.getUser2Delete()));
    }
  }

  private void reassembleUserList(RequestWrapper wrapper) {
    final List<User> users = new ArrayList<>(2);
    users.add(wrapper.getUser2Delete());
    users.add(wrapper.getUser2Update());
    final RefreshRequest request = wrapper.getRequest();
    request.setUsers(users);
  }

  class DefaultCondition implements Condition<RequestWrapper> {

    @Override
    public boolean check(RequestWrapper wrapper) {
      final RefreshRequest request = wrapper.getRequest();
      if (request.isServiced()) {
        return false;
      }

      final List<User> users = request.getUsers();

      if (2 != users.size()) {
        return false;
      }

      final String userId = request.getUserId();
      final String appName = request.getAppName();
      final String deviceType = request.getDeviceType();
      final String deviceToken = request.getDeviceToken();

      for (User user : users) {
        if (StringUtils.equals(userId, user.getUserId())
            && StringUtils.equals(appName, user.getAppName())
            && StringUtils.equals(deviceType, user.getDeviceType())
            && !StringUtils.equals(deviceToken, user.getDeviceToken())) {
          wrapper.setUser2Update(user);
        }
        if (!StringUtils.equals(userId, user.getUserId())
            && StringUtils.equals(appName, user.getAppName())
            && StringUtils.equals(deviceType, user.getDeviceType())
            && StringUtils.equals(deviceToken, user.getDeviceToken())) {
          wrapper.setUser2Delete(user);
        }
      }

      return null != wrapper.getUser2Delete() && null != wrapper.getUser2Update();

    }
  }

  @Data
  class RequestWrapper {

    @NonNull
    private final RefreshRequest request;

    private User user2Update = null;
    private User user2Delete = null;
  }
}
