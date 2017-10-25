package com.open.push.biz.token.processor.multichannel;

import com.open.push.biz.token.RefreshRequest;
import com.open.push.biz.token.UserProcessor;
import com.open.push.service.User;
import com.open.push.service.UserService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>完成准备工作，从db中获取需要更新的users信息</p>
 *
 * <p>从（1）userId, appName, deviceType (2) deviceToken (3) deviceMc 三个纬度获取需要更新的user信息</p>
 */
@Slf4j
public class MultiChannelBeginProcessor implements UserProcessor<RefreshRequest> {

  private UserService userService;

  public MultiChannelBeginProcessor(UserService userService) {
    this.userService = userService;
  }

  @Override
  public void process(RefreshRequest request) {
    final String token = request.getDeviceToken();

    final String userId = request.getUserId();
    final String appName = request.getAppName();
    final String type = request.getDeviceType();

    final String deviceMc = request.getDeviceMc();

    final List<User> users = userService.getListBy(token, userId, appName, type, deviceMc);
    request.setUsers(users);

    log.debug("Multi Channel begin user processor processed.\n Request: {}", request);
  }
}
