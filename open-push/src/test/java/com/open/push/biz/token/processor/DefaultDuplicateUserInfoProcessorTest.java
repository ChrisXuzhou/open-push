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
 * <p>通常情况3.2：同一设备不同用户启动,更新token对应user信息, 该用户之前有token信息.（有两种相似情况见 3.1 3.2 ）</p>
 *
 * <p>Condition: (1) users有2条信息. </p>
 *
 * <p>(2) 其中一条userId 不同，deviceType, deviceToken 相同.</p>
 *
 * <p>(3）另一条 userId, deviceType 相同， deviceToken 不同.</p>
 *
 * <p>Operation: users有2条信息： 更新用户(2)的token信息, 删除（3）的信息.</p>
 */
public class DefaultDuplicateUserInfoProcessorTest {

  private RefreshRequest request;
  private DefaultDuplicateUserInfoProcessor processor;

  @Before
  public void setUp() throws Exception {
    request = new RefreshRequestBuilder().userId("James").appName("med").deviceType("iPhone")
        .deviceToken("token-Chris").build();

    List<User> users = new ArrayList<>(2);
    users.add(new User("Chris", "med", "1213", "1.01", "iOS.1.01", "iPhone", "mc", "iOS",
        "token-Chris"));
    users.add(new User("James", "med", "1213", "1.01", "iOS.1.01", "iPhone", "mc", "iOS",
        "token-James"));
    request.setUsers(users);

    processor = new DefaultDuplicateUserInfoProcessor();
  }

  @Test
  public void process() throws Exception {
    processor.process(request);
    assertTrue(request.isServiced());
    final List<User> users = request.getUsers();
    for (User user : users) {
      assertEquals("iPhone", user.getDeviceType());
      assertEquals("token-Chris", user.getDeviceToken());
      switch (user.getUserId()) {
        case "Chris":
          Assert.assertEquals(Operation.DELETE, user.getOp());
          break;
        case "James":
          assertEquals(Operation.UPDATE, user.getOp());
          break;
        default:
          throw new RuntimeException("Unsupported Type");
      }
    }
  }


}
