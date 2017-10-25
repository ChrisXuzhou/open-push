package com.open.push.biz.task.parse;

import com.open.push.biz.request.PushCriteria;
import com.open.push.service.PushRequest;
import com.open.push.service.User;
import com.open.push.service.UserService;
import com.open.push.support.PushQueueService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * multi thread safe.
 */
@Slf4j
@Service("globalRequestParser")
public class GlobalRequestParser extends AbstractRequestParser {

  private static final int CACHE_SIZE = 5000;
  private final UserService modelUserService;

  public GlobalRequestParser(PushQueueService queueService, UserService modelUserService) {
    super(queueService);
    this.modelUserService = modelUserService;
  }

  @Override
  protected List<User> fetchCachedUsers() {

    RequestContext context = RequestContext.getCurrentContext();

    final PushRequest request = context.getPushRequest();
    Assert.isTrue(
        StringUtils.equals(PushCriteria.GLOBAL.name(), request.getCriteria()),
        "push criteria must be global.");

    final String appName = context.getAppName();

    Integer startId = context.getStartId();
    if (null == startId) {
      startId = 0;
    }
    Integer endId = context.getEndId();
    if (null == endId) {
      endId = 0;
      User user = modelUserService.getLastUserOrderByIdBy(appName);
      if (null != user) {
        endId = user.getId().intValue() + 1;
      }
      context.setEndId(endId);
    }

    while (startId < endId) {
      context.setStartId(startId + CACHE_SIZE);
      log.debug("global fetch target users, start Id: {}, end Id: {}", startId,
          startId + CACHE_SIZE);

      List<User> users = getUsersFromDB(startId, startId + CACHE_SIZE, appName);
      if (CollectionUtils.isNotEmpty(users)) {
        return users;
      }
      startId += CACHE_SIZE;
    }
    return null;
  }

  private List<User> getUsersFromDB(final Integer startId, final Integer endId,
      final String appName) {
    return modelUserService.getListBy(startId, endId, appName);
  }

}
