package com.open.push.biz.task.parse;

import com.open.push.service.User;
import com.open.push.service.UserService;
import com.open.push.support.PushQueueService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * multi thread safe.
 */
@Slf4j
@Service("byMcRequestParser")
public class ByMcRequestParser extends CachedRequestParser {

  private final UserService modelUserService;

  public ByMcRequestParser(PushQueueService queueService, UserService modelUserService) {
    super(queueService);
    this.modelUserService = modelUserService;
  }

  @Override
  protected List<User> getUsersFromDB(final List<String> cache, final String appName) {
    return modelUserService.getListByMcsAndAppName(cache, appName);
  }
}
