package com.open.push.service.impl.async;

import com.open.push.dao.messagebulk.BulkOperations;
import com.open.push.dao.po.MessagePo;
import com.open.push.service.Message;
import com.open.push.service.MessageService;
import com.open.push.support.DozerHelper;
import com.open.push.dao.MessageRepository;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AsyncMessageServiceImpl extends AbstractAsynchronousMessageOperator implements
    MessageService {

  private BlockingQueue<Message> queue;
  private static final int QUEUE_SIZE = 20000;
  private static final int DEFAULT_BATCH_SIZE = 5000;

  private ExecutorService executorService;
  private MessageRepository messageRepository;

  @Autowired
  public AsyncMessageServiceImpl(final BulkOperations bulkOperations,
      final MessageRepository messageRepository) {
    super(DEFAULT_BATCH_SIZE, bulkOperations);
    this.messageRepository = messageRepository;
  }

  public AsyncMessageServiceImpl(int batchSize, final BulkOperations bulkOperations) {
    super(batchSize, bulkOperations);
  }

  @PostConstruct
  public void initialize() {
    queue = new ArrayBlockingQueue<>(QUEUE_SIZE);

    //TODO: temporary solution
    executorService = Executors.newSingleThreadExecutor();
    executorService.submit(this);
  }

  @PreDestroy
  @Override
  public void close() {
    isOnService = false;

    while (!closable()) {
      try {
        Thread.sleep(200);

      } catch (InterruptedException e) {
        log.warn("close interrupted!", e);
      }
    }

    executorService.shutdownNow();
    log.info("async bulk message service closed..");
  }


  @Override
  public Message fetchMessage() {
    return queue.poll();
  }


  @Override
  public boolean trySave(Message msg) {
    return isOnService && queue.offer(msg);
  }

  @Override
  public List<Message> getBy(Long traceId) {
    List<MessagePo> pos = null;
    try {
      pos = messageRepository.getByTraceId(traceId);
    } catch (Exception e) {
      log.error("getBit message po error, trace Id: {}", traceId, e);
    }
    if (CollectionUtils.isNotEmpty(pos)) {
      return DozerHelper.map(pos, Message.class);
    }
    return null;
  }

  @Override
  public Message getBy(Long traceId, String userId) {
    MessagePo po = null;
    try {
      po = messageRepository.getByTraceIdAndUserId(traceId, userId);
    } catch (Exception e) {
      log.error("getBit message po error, trace Id: {}, userId: {}", traceId, userId, e);
    }
    if (null != po) {
      return DozerHelper.map(po, Message.class);
    }
    return null;
  }


  @Override
  public void save(Message message) {
    if (null != message) {
      MessagePo po = DozerHelper.map(message, MessagePo.class);
      messageRepository.save(po);
    }
  }
}
