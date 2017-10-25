package com.open.push.service.impl.async;

import com.open.push.support.DozerHelper;
import com.open.push.LifeCycle;
import com.open.push.dao.messagebulk.BulkOperations;
import com.open.push.dao.po.MessagePo;
import com.open.push.service.Message;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

@Slf4j
public abstract class AbstractAsynchronousMessageOperator implements Runnable, LifeCycle {

  private final int batchSize;
  private List<MessagePo> cache;

  private BulkOperations bulkOperations;


  volatile boolean isOnService = true;

  public AbstractAsynchronousMessageOperator(int batchSize, final BulkOperations bulkOperations) {
    this.batchSize = batchSize;
    this.bulkOperations = bulkOperations;

    this.cache = new ArrayList<>(batchSize + 10);
  }

  @Override
  public void run() {

    for (; ; ) {

      if (isOnService) {
        try {
          Message msg = fetchMessage();
          if (msg == null) {
            flush();
            continue;
          }
          cache.add(DozerHelper.map(msg, MessagePo.class));

          final int size = cache.size();
          if (size > batchSize) {
            flush();
          }
        } catch (Exception e) {
          log.error("un-normal exception thrown! ", e);
        }
      }

    }
  }


  public boolean closable() {
    return CollectionUtils.isEmpty(cache);
  }

  private void flush() {
    bulkOperations.bulkPersist(cache);
    cache.clear();
  }

  public abstract Message fetchMessage();

}
