package com.open.push.biz.task.parse;

import com.open.push.Helper;
import com.open.push.biz.NotificationPackage;
import com.open.push.biz.task.bean.ParseTask;
import com.open.push.biz.task.bean.PushCondition;
import com.open.push.biz.task.bean.RequestParseRecord;
import com.open.push.biz.task.pack.AbstractNotificationPacker;
import com.open.push.biz.task.pack.PackerHelper;
import com.open.push.service.PushRequest;
import com.open.push.service.PushRequestDetail;
import com.open.push.service.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class RequestContext extends ConcurrentHashMap<String, Object> {

  private PushRequest pushRequest;

  private PushRequestDetail pushRequestDetail;

  private String jobId;

  private String appName;

  private String topic; //topic is 1:1 to appName

  private PushCondition pushCondition;

  private RequestParseRecord requestParseRecord;

  private NotificationPackage template;

  private Map<String, AbstractNotificationPacker> packers;


  private transient List<User> cachedUsers;

  //for cached userId usage.

  private transient StringTokenizer tokenizer;

  private transient List<String> cache = null;

  //for global usage.

  private transient Integer startId = null;

  private transient Integer endId = null;

  StringTokenizer getTokenizer() {
    if (null == tokenizer) {
      final String rawCache = pushRequestDetail.getCriteriaDescription();
      tokenizer = new StringTokenizer(rawCache, ",");
    }
    return tokenizer;
  }

  private static final int CACHE_SIZE = 2000;

  List<String> getCache() {
    if (null == cache) {
      cache = new ArrayList<>(CACHE_SIZE + 10);
    }
    cache.clear();
    return cache;
  }

  void initializeFromParseTask(ParseTask parseTask) {

    this.pushRequest = parseTask.getPushRequest();
    this.pushRequestDetail = parseTask.getPushRequestDetail();
    this.topic = parseTask.getTopic();
    this.jobId = pushRequest.getJobId();
    this.appName = parseTask.getAppName();

    this.pushCondition = Helper.fromJson(pushRequest.getCriteriaDescription(), PushCondition.class);

    this.requestParseRecord = new RequestParseRecord(appName, jobId);

    this.template = ParserHelper.buildNotificationTemplate(parseTask);

    this.packers = PackerHelper.buildNotificationPackers(template);

    this.cache = null;
    this.startId = null;
    this.endId = null;

  }

  protected static final ThreadLocal<? extends RequestContext> threadLocal = ThreadLocal
      .withInitial(() -> {
        try {
          return RequestContext.class.newInstance();
        } catch (Throwable e) {
          throw new RuntimeException(e);
        }
      });


  static RequestContext getCurrentContext() {
    RequestContext context = threadLocal.get();
    return context;
  }

  void remove() {
    threadLocal.remove();
  }
}
