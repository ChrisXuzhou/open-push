package com.open.push.biz.token.processor;

import com.open.push.biz.token.Condition;
import com.open.push.biz.token.RefreshRequest;
import com.open.push.biz.token.UserProcessor;
import com.open.push.service.User;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * <p>异常情况, 以Error打印在日志中</p>
 */
@Slf4j
public class DefaultExceptionalProcessor implements UserProcessor<RefreshRequest> {

  private Condition<RefreshRequest> condition;

  public DefaultExceptionalProcessor() {
    condition = new DefaultCondition();
  }

  @Override
  public void process(RefreshRequest request) {

    if (condition.check(request)) {
      log.error("SYSTEM ERROR: Exceptional Situation happened! Request: {}",
          ToStringBuilder.reflectionToString(request));
      throw new ExceptionalProcessorException("Exceptional Situation happened!");
    }
  }

  class DefaultCondition implements Condition<RefreshRequest> {

    @Override
    public boolean check(RefreshRequest request) {
      final List<User> users = request.getUsers();

      return 2 < users.size();
    }
  }
}
