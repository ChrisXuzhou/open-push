package com.open.push.biz.token.processor;

import com.open.push.service.User;
import com.open.push.biz.token.RefreshRequest;
import com.open.push.biz.token.RefreshRequestBuilder;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>异常情况, 以Error打印在日志中</p>
 */
public class DefaultExceptionalProcessorTest {

  private DefaultExceptionalProcessor processor;
  private RefreshRequest request;

  @Before
  public void setUp() throws Exception {
    List<User> users = new ArrayList<>(3);
    users.add(new User());
    users.add(new User());
    users.add(new User());
    request = new RefreshRequestBuilder().userId("Chris").appName("medical").deviceType("iPhone")
        .deviceToken("token").build();
    request.setUsers(users);

    this.processor = new DefaultExceptionalProcessor();
  }

  @Test(expected = ExceptionalProcessorException.class)
  public void process() throws Exception {
    processor.process(request);
  }

}