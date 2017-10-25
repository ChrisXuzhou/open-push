package com.open.push.biz.token.processor;

import com.open.push.biz.token.Condition;
import com.open.push.biz.token.RefreshRequest;
import com.open.push.biz.token.UserProcessor;
import com.open.push.service.User;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * <p>通常情况1：用户新增设备token时，user信息的逻辑实现.</p>
 *
 * <p>Condition: 正常情况下用户新增自己的token, users仅有0条信息.</p>
 *
 * <p>users有0条信息： 新增用户token信息.</p>
 */
@Slf4j
public class DefaultInsertTokenProcessor implements UserProcessor<RefreshRequest> {

  private Condition<RefreshRequest> condition;

  public DefaultInsertTokenProcessor() {
    this.condition = new DefaultCondition();
  }

  @Override
  public void process(RefreshRequest request) {

    if (condition.check(request)) {
      final User user = buildNewUser(request);
      final List<User> users = request.getUsers();
      users.add(user);
      request.setServiced(true);

      log.debug("Situation 1: insert new user information: user : {}",
          ToStringBuilder.reflectionToString(user));
    }
  }

  class DefaultCondition implements Condition<RefreshRequest> {

    @Override
    public boolean check(RefreshRequest request) {
      final List<User> users = request.getUsers();

      return 0 == users.size();
    }
  }
}
