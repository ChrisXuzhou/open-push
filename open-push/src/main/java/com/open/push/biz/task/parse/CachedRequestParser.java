package com.open.push.biz.task.parse;

import com.open.push.service.PushRequestDetail;
import com.open.push.service.User;
import com.open.push.support.PushQueueService;
import java.util.List;
import java.util.StringTokenizer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.Assert;

/**
 * multi thread safe.
 */
@Slf4j
public abstract class CachedRequestParser extends AbstractRequestParser {

  private static final int CACHE_SIZE = 2000;

  public CachedRequestParser(PushQueueService queueService) {
    super(queueService);
  }

  @Override
  protected List<User> fetchCachedUsers() {
    RequestContext context = RequestContext.getCurrentContext();
    final PushRequestDetail detail = context.getPushRequestDetail();
    Assert.isTrue(ParserHelper.isPushRequestDetailNotProcessed(detail),
        "message bulk pushRequest pushRequestDetail notificationStatus must be 'INGRESS'.");

    StringTokenizer tokenizer = context.getTokenizer();
    List<String> cache = context.getCache();

    for (; ; ) {
      if (!tokenizer.hasMoreTokens()) {
        break;
      }

      cache.add(tokenizer.nextToken().replace(" ", ""));
      if (cache.size() > CACHE_SIZE - 1) {
        break;
      }
    }

    if (CollectionUtils.isEmpty(cache)) {
      return null;  //return null is safe here.
    }

    final String appName = context.getAppName();
    return getUsersFromDB(cache, appName);

  }

  protected abstract List<User> getUsersFromDB(final List<String> cache, final String appName);

}
