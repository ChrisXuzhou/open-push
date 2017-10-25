package com.open.push.biz.token.processor.multichannel;

import com.open.push.biz.token.Condition;
import com.open.push.biz.token.RefreshRequest;
import com.open.push.biz.token.UserProcessor;
import com.open.push.service.User;
import com.open.push.service.User.Operation;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;


/**
 * <p>Condition: (1) users有多于3条信息. </p>
 *
 * <p>Operation: users有3条信息： 保留1条，其余删除.</p>
 */
@Slf4j
public class MultiChannelSizeMoreProcessor implements UserProcessor<RefreshRequest> {

  private Condition<RefreshRequest> condition;

  public MultiChannelSizeMoreProcessor() {
    this.condition = new DefaultCondition();
  }

  @Override
  public void process(RefreshRequest request) {

    if (condition.check(request)) {

      final List<User> users = request.getUsers();

      for (User user : users) {
        user.setOp(Operation.DELETE);
      }

      log.debug(
          "Multi Channel Situation size more: delete illegal users. ",
          ReflectionToStringBuilder.toString(request));
    }
  }

  class DefaultCondition implements Condition<RefreshRequest> {

    @Override
    public boolean check(RefreshRequest request) {
      final List<User> users = request.getUsers();

      return 3 < users.size();
    }
  }

}
