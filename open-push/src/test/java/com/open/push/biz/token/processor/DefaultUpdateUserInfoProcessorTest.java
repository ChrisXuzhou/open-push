package com.open.push.biz.token.processor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.open.push.biz.token.RefreshRequest;
import com.open.push.service.User;
import com.open.push.service.User.Operation;
import com.open.push.biz.token.RefreshRequestBuilder;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>通常情况3.1：同一设备不同用户启动,更新token对应user信息, 但该用户之前没有token信息.（有两种相似情况见 3.1 3.2 ）</p>
 *
 * <p>Condition: (1) users仅有1条信息. (2) userId 不同. (3) appName, deviceType, deviceToken 相同. </p>
 *
 * <p>users有1条信息： 更新用户deviceType, userId信息.</p>
 */
public class DefaultUpdateUserInfoProcessorTest {

  private RefreshRequest request;
  private DefaultUpdateUserInfoProcessor processor;

  @Before
  public void setUp() throws Exception {
    request = new RefreshRequestBuilder().userId("Chris").appName("med").deviceType("iPhone")
        .deviceToken("token").build();

    List<User> users = new ArrayList<>(1);
    users.add(
        new User("James", "med", "1213", "1.01", "iOS.1.01", "iPhone", "mc", "iOS", "token"));
    request.setUsers(users);

    processor = new DefaultUpdateUserInfoProcessor();
  }

  @Test
  public void testProcess() {
    processor.process(request);

    assertTrue(request.isServiced());
    final List<User> users = request.getUsers();
    assertEquals(1, users.size());
    final User user = users.get(0);
    assertEquals("Chris", user.getUserId());
    assertEquals("iPhone", user.getDeviceType());
    assertEquals("med", user.getAppName());
    assertEquals("token", user.getDeviceToken());
    Assert.assertEquals(Operation.UPDATE, user.getOp());
  }
}