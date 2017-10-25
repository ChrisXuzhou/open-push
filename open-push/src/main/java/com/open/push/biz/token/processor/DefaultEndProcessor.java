package com.open.push.biz.token.processor;

import com.open.push.biz.token.RefreshRequest;
import com.open.push.biz.token.UserProcessor;
import com.open.push.service.User;
import com.open.push.service.UserService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 */
@Slf4j
public class DefaultEndProcessor implements UserProcessor<RefreshRequest> {

  private UserService userService;

  public DefaultEndProcessor(UserService userService) {
    this.userService = userService;
  }

  @Override
  public void process(RefreshRequest request) {

    //TODO: users更新后 合法校验

    if (request.isServiced()) {
      final List<User> users = request.getUsers();

      for (User user : users) {
        operate(user);
      }
    }

    log.debug("Default end user processor processed.\n Request: {} \n",
        ToStringBuilder.reflectionToString(request));
  }

  private void operate(User user) {
    final User.Operation op = user.getOp();

    switch (op) {
      case INSERT:
      case UPDATE:
        userService.save(user);
        break;
      case DELETE:
        userService.delete(user);
        break;
      case NONE:
      default:
        break;

    }
  }

}
