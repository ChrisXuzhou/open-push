package com.open.push.channel.mi;


import static com.open.push.channel.mi.MiConstants.MI_MAX_TOKEN_NUMBER;

import com.open.push.Helper;
import com.open.push.channel.PostSendOperator;
import com.open.push.channel.PushChannel;
import com.open.push.channel.bean.BatchNotification;
import com.google.common.base.Splitter;
import com.xiaomi.push.sdk.ErrorCode;
import com.xiaomi.xmpush.server.Constants;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Message.Builder;
import com.xiaomi.xmpush.server.Result;
import com.xiaomi.xmpush.server.Sender;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

@Slf4j
public class MiChannel implements PushChannel<BatchNotification> {

  private final Sender sender;
  private final PostSendOperator<BatchNotification> postSendOperator;

  MiChannel(final Sender sender, PostSendOperator<BatchNotification> postSendOperator) {
    this.sender = sender;
    this.postSendOperator = postSendOperator;
  }

  @Override
  public void send(final BatchNotification notification) throws MiSendNotificationException {

    check(notification);

    final Message message = generateMiMessage(notification);

    Constants.useOfficial();

    Result result;
    try {
      result = sender.send(message, notification.getDeviceTokens(), 1);
    } catch (IOException | ParseException e) {
      log.error("Mi pushRequest send message bulk error. ", e);
      throw new MiSendNotificationException(e);
    }
    parseResult(result, notification);
    postSendOperator.postSendOperate(notification);
  }

  private void check(BatchNotification notification) {
    final List<String> tokens = notification.getDeviceTokens();

    if (MI_MAX_TOKEN_NUMBER < tokens.size()) {
      log.error("MI pushRequest tokens' amount exceed max number: {} [{}]", tokens.size(),
          MI_MAX_TOKEN_NUMBER);
      throw new MiSendNotificationException("tokens' size exceed max number[1000].");
    }
  }

  private Message generateMiMessage(BatchNotification notification) {

    final String traceId = String.valueOf(notification.getTraceId());

    final Message.Builder builder = new Builder();

    builder.title(notification.getTitle())
        .restrictedPackageName(notification.getTopic())
        .description(notification.getDescription())
        .extra("traceId", traceId)
        .passThrough(0)
        .notifyType(1)
        .timeToLive(24 * 60 * 60 * 1000)
        .extra("flow_control", "4000");     // 设置平滑推送, 推送速度4000每秒(qps=4000

    final Map<String, String> properties = Helper.Json2Map(notification.getPayload());
    setProperties(properties, builder);

    return builder.build();
  }

  private void parseResult(final Result result, final BatchNotification batchNotification) {

    if (null != result) {
      log.debug("Mi notification Result: {}", ToStringBuilder.reflectionToString(result));

      if (ErrorCode.Success.equals(result.getErrorCode()) && StringUtils
          .isNotEmpty(result.getMessageId())) {
        JSONObject data = result.getData();
        if (null != data) {
          final String raw = String.valueOf(data.get("bad_regids"));
          batchNotification.setBadTokens(parseBadTokens(raw));
        }
        return;
      }

      log.info("Mi response error: {}", ReflectionToStringBuilder.toString(result.getReason()));
    }
  }

  private List<String> parseBadTokens(String badTokens) {
    final List<String> results = new ArrayList<>();

    if (StringUtils.isNotEmpty(badTokens)) {
      for (String badToken : Splitter.on(',').trimResults().omitEmptyStrings().split(badTokens)) {
        results.add(badToken);
      }
    }
    return results;
  }

  private void setProperties(final Map<String, String> properties,
      final Builder builder) {
    if (null != properties) {
      for (Map.Entry<String, String> entry : properties.entrySet()) {
        final String key = entry.getKey();

        try {
          final String value = entry.getValue();
          if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            continue;
          }
          if (StringUtils.equals("passThrough", key)) {
            builder.passThrough(Integer.parseInt(value));
            continue;
          }
          if (StringUtils.equals("timeToLive", key)) {
            builder.timeToLive(Long.valueOf(value));
            continue;
          }
          if (StringUtils.equals("payload", key)) {
            builder.payload(value);
            continue;
          }

          builder.extra(entry.getKey(), value);
        } catch (Exception e) {
          log.warn("specific key[{}] error", key, e);
        }

      }
    }

  }


}
