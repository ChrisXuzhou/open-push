package com.open.push.biz.token.processor.multichannel;

import com.open.push.biz.token.RefreshRequest;
import com.open.push.service.User;
import com.open.push.service.User.Operation;
import com.open.push.biz.token.Condition;
import com.open.push.biz.token.UserProcessor;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;


/**
 * <p>Condition: (1) users有3条信息. </p>
 *
 * <p>Operation: users有3条信息： 删2条，保留1条.</p>
 */
@Slf4j
public class MultiChannelSize3Processor implements UserProcessor<RefreshRequest> {

  private Condition<RefreshRequest> condition;

  public MultiChannelSize3Processor() {
    this.condition = new DefaultCondition();
  }

  @Override
  public void process(RefreshRequest request) {

    if (condition.check(request)) {

      final List<User> users = request.getUsers();
      final User user2Delete1 = users.get(0);
      user2Delete1.setOp(Operation.DELETE);

      final User user2Delete2 = users.get(1);
      user2Delete2.setOp(Operation.DELETE);

      final User user2Update = users.get(2);
      refreshUserBasicInfo(request, user2Update);
      user2Update.setOp(Operation.UPDATE);

      log.debug(
          "Multi Channel Situation size 3: Update and delete user's information based on device token. user-to-delete1: {},user-to-delete2: {}, user-to-update: {}",
          ToStringBuilder.reflectionToString(user2Delete1),
          ToStringBuilder.reflectionToString(user2Delete2),
          ToStringBuilder.reflectionToString(user2Update));
    }
  }

  class DefaultCondition implements Condition<RefreshRequest> {

    @Override
    public boolean check(RefreshRequest request) {
      final List<User> users = request.getUsers();

      return 3 == users.size();
    }
  }

}
