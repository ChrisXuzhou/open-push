package com.open.push.channel.upush;

import com.open.push.Helper;
import com.open.push.channel.PostSendOperator;
import com.open.push.channel.PushChannel;
import com.open.push.channel.bean.BatchNotification;
import com.open.push.channel.mi.MiSendNotificationException;
import com.open.push.channel.upush.HttpPostBase.Result.Info;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@Slf4j
public class UPushChannel extends HttpPostBase implements PushChannel<BatchNotification> {

  private String appKey;

  private final PostSendOperator<BatchNotification> postSendOperator;

  UPushChannel(String appKey, String secret,
      PostSendOperator<BatchNotification> postSendOperator) {
    super(secret);
    this.appKey = appKey;
    this.postSendOperator = postSendOperator;
  }

  @Override
  public void send(BatchNotification notification) throws UPushSendNotificationException {

    final UPushPostBodyBuilder builder = new UPushPostBodyBuilder();
    builder.timeStamp(Integer.toString((int) (System.currentTimeMillis() / 1000)));

    final String traceId = String.valueOf(notification.getTraceId());
    builder
        .appkey(appKey)
        .traceId(traceId)
        .displayType("notification")
        .setProductionMode(true)
        .afterOpen("go_custom")
        .ticker("ticker")
        .custom("notification")
        .title(notification.getTitle())
        .description(notification.getDescription())
        .type("listcast");

    final Map<String, Object> properties = Helper.Json2Map(notification.getPayload());
    builder.setProperties(properties);

    final List<String> tokens = notification.getDeviceTokens();
    if (UPushConstants.UPUSH_MAX_TOKEN_NUMBER < tokens.size()) {
      log.error("UPush pushRequest tokens' amount exceed max number: {} [{}]", tokens.size(),
          UPushConstants.UPUSH_MAX_TOKEN_NUMBER);
      throw new MiSendNotificationException("tokens' size exceed max number[500].");
    }
    builder.deviceTokens(tokens);

    Result result;
    try {
      result = post(builder.build());
    } catch (Exception e) {
      log.error("send uPush notification error!", e);
      throw new UPushSendNotificationException(e);
    }
    parseResult(result);
    postSendOperator.postSendOperate(notification);
  }


  private void parseResult(final Result result) {
    if (StringUtils.equals(Ret.SUCCESS.name(), result.getRet())) {
      final Info data = result.getData();
      if (null != data && StringUtils.isNotEmpty(data.getMsg_id())) {
        log.debug("message id: {}", data.getMsg_id());
      }
    }
    log.info("failed result: {}", ReflectionToStringBuilder.toString(result));

  }


}
