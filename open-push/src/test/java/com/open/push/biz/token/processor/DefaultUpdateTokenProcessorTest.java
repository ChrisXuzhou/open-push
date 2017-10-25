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
 * <p>通常情况2：用户更新设备token时，user信息的逻辑实现.</p>
 *
 * <p>Condition: (1) 正常情况下用户更新自己的token, users仅有1条信息. (2) userId, appName, deviceType 相同. (3)
 * deviceToken 不同.</p>
 *
 * <p>users有1条信息： 更新用户token信息.</p>
 */
public class DefaultUpdateTokenProcessorTest {

  private RefreshRequest request;
  private DefaultUpdateTokenProcessor processor;

  @Before
  public void setUp() throws Exception {
    final List<User> users = new ArrayList<>(1);
    users.add(new User("Chris.Test", "med", "1213", "1.01", "iOS.1.01", "iPhone", "mc", "iOS",
        "token"));
    request = new RefreshRequestBuilder().userId("Chris.Test").appName("med").deviceType("iPhone")
        .deviceToken("token-update").build();
    request.setUsers(users);

    processor = new DefaultUpdateTokenProcessor();
  }

  @Test
  public void testProcess() throws Exception {
    processor.process(request);

    assertTrue(request.isServiced());
    final List<User> users = request.getUsers();
    assertEquals(1, users.size());
    final User user = users.get(0);
    assertEquals("Chris.Test", user.getUserId());
    assertEquals("med", user.getAppName());
    assertEquals("iPhone", user.getDeviceType());
    assertEquals("token-update", user.getDeviceToken());
    Assert.assertEquals(Operation.UPDATE, user.getOp());
  }

}