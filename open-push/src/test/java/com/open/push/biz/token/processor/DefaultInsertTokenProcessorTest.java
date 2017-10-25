package com.open.push.biz.token.processor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.open.push.biz.token.RefreshRequest;
import com.open.push.service.User;
import com.open.push.service.User.Operation;
import com.open.push.biz.token.RefreshRequestBuilder;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * 对Situation 1 单元测试
 *
 * <p>通常情况1：用户新增设备token时，user信息的逻辑实现.</p>
 *
 * <p>Condition: 正常情况下用户新增自己的token, users仅有0条信息.</p>
 *
 * <p>users有0条信息： 新增用户token信息.</p>
 */
public class DefaultInsertTokenProcessorTest {

  private DefaultInsertTokenProcessor processor;

  @Before
  public void setUp() throws Exception {
    processor = new DefaultInsertTokenProcessor();
  }

  @Test
  public void testProcess() {
    List<User> users = new ArrayList<>(1);
    RefreshRequest request = new RefreshRequestBuilder().userId("Chris").appName("medical")
        .deviceType("iphone")
        .deviceToken("token").build();
    request.setUsers(users);

    processor.process(request);

    assertTrue(request.isServiced());
    final User user = users.get(0);
    assertNotNull(user);
    assertEquals("Chris", user.getUserId());
    assertEquals("medical", user.getAppName());
    assertEquals("iphone", user.getDeviceType());
    assertEquals("token", user.getDeviceToken());
    Assert.assertEquals(Operation.INSERT, user.getOp());
  }

  @After
  public void tearDown() throws Exception {
  }

}