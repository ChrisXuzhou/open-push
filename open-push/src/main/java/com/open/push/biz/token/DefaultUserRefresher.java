package com.open.push.biz.token;

import java.util.List;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.util.Assert;

/**
 * Default implementation of User Refresher
 */
@Slf4j
@Setter
public class DefaultUserRefresher implements UserRefresher<RefreshRequest> {

  private List<UserProcessor<RefreshRequest>> processors;

  @Override
  public void refresh(RefreshRequest request) {

    for (; ; ) {
      try {
        doRefresh(request);
        break;

      } catch (ObjectOptimisticLockingFailureException e) {
        log.debug("Multiple refresher update one item at same time. {}", e);
      }
    }

    //乐观锁事务
  }

  private void doRefresh(RefreshRequest request) {
    Assert.notNull(processors, "User Processors must not be null!");

    for (UserProcessor<RefreshRequest> processor : processors) {
      processor.process(request);
    }
  }
}
