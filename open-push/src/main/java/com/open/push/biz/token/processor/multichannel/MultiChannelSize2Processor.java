package com.open.push.biz.token.processor.multichannel;

import com.open.push.biz.token.Condition;
import com.open.push.biz.token.RefreshRequest;
import com.open.push.biz.token.UserProcessor;
import com.open.push.service.User;
import com.open.push.service.User.Operation;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * <p>Condition: (1) users有2条信息. </p>
 *
 * <p>Operation: users有2条信息： 删一条，保留一条.</p>
 */
@Slf4j
public class MultiChannelSize2Processor implements UserProcessor<RefreshRequest> {

  private Condition<RefreshRequest> condition;

  public MultiChannelSize2Processor() {
    this.condition = new DefaultCondition();
  }

  @Override
  public void process(RefreshRequest request) {

    if (condition.check(request)) {

      final List<User> users = request.getUsers();
      final User user2Delete = users.get(0);
      user2Delete.setOp(Operation.DELETE);
      final User user2Update = users.get(1);
      refreshUserBasicInfo(request, user2Update);
      user2Update.setOp(Operation.UPDATE);

      log.debug(
          "Multi Channel Situation 3.2: Update and delete user's information based on device token. user-to-delete: {}, user-to-update: {}",
          ToStringBuilder.reflectionToString(user2Delete),
          ToStringBuilder.reflectionToString(user2Update));
    }
  }

  class DefaultCondition implements Condition<RefreshRequest> {

    @Override
    public boolean check(RefreshRequest request) {
      final List<User> users = request.getUsers();

      return 2 == users.size();
    }
  }

}
