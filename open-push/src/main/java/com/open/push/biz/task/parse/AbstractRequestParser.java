package com.open.push.biz.task.parse;

import com.open.push.biz.NotificationPackage;
import com.open.push.biz.task.RequestParser;
import com.open.push.biz.task.bean.ParseTask;
import com.open.push.biz.task.bean.RequestParseRecord;
import com.open.push.biz.task.pack.AbstractNotificationPacker;
import com.open.push.service.User;
import com.open.push.support.PushQueueService;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@Slf4j
public abstract class AbstractRequestParser implements RequestParser<ParseTask> {

  private final PushQueueService queueService;

  public AbstractRequestParser(PushQueueService queueService) {
    this.queueService = queueService;
  }


  @Override
  public void parse(final ParseTask parseTask) {

    try {
      doParse(parseTask);
    } catch (Exception e) {
      log.error("parse task error {}",
          ReflectionToStringBuilder.toString(parseTask), e);
    }
  }

  private void doParse(final ParseTask parseTask) {

    long total = 0L;

    // #1 initial context.
    RequestContext context = RequestContext.getCurrentContext();
    context.initializeFromParseTask(parseTask);

    for (; ; ) {
      // #2 get cached users
      final List<User> cachedUsers = fetchCachedUsers();

      if (CollectionUtils.isEmpty(cachedUsers)) {
        break;
      }

      total += cachedUsers.size();

      // #3 begin to pack and push to queue.
      doPackage();
    }

    context.put("total", total);
    postParseProcess();
  }


  private void postParseProcess() {

    RequestContext context = RequestContext.getCurrentContext();
    RequestParseRecord requestParseRecord = context.getRequestParseRecord();
    Long total = (Long) context.get("total");
    requestParseRecord.total(total).setSent(true);

    if (requestParseRecord.finish()) {
      log.info("parse bean: {}", ReflectionToStringBuilder.toString(requestParseRecord));
    }
    context.remove();
  }

  abstract List<User> fetchCachedUsers();

  private void doPackage() {

    RequestContext context = RequestContext.getCurrentContext();
    final List<User> users = context.getCachedUsers();

    for (User user : users) {

      if (ParserHelper.isConditionSatisfied(user, context)) {
        final String tokenType = user.getDeviceTokenType();
        Map<String, AbstractNotificationPacker> packers = context.getPackers();

        if (packers != null) {
          final AbstractNotificationPacker packer = packers.get(tokenType);
          if (null != packer) {
            NotificationPackage notificationPackage = packer.pack(user);
            queueService.put(notificationPackage);
          }
        }
      }
    }
  }


}
