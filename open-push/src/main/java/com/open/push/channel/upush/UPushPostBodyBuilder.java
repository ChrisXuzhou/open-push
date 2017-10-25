package com.open.push.channel.upush;

import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class UPushPostBodyBuilder extends UBaseNotification {


  public UPushPostBodyBuilder title(String title) {
    setProperties("title", title);
    return this;
  }

  public UPushPostBodyBuilder description(String description) {
    setProperties("text", description);
    return this;
  }

  public UPushPostBodyBuilder ticker(String ticker) {
    setProperties("ticker", ticker);
    return this;
  }

  public UPushPostBodyBuilder custom(String custom) {
    setProperties("custom", custom);
    return this;
  }

  public UPushPostBodyBuilder appkey(String appkey) {
    setProperties("appkey", appkey);
    return this;
  }

  public UPushPostBodyBuilder type(String type) {
    setProperties("type", type);
    return this;
  }

  public UPushPostBodyBuilder displayType(String displayType) {
    setProperties("display_type", displayType);
    return this;
  }

  public UPushPostBodyBuilder deviceTokens(List<String> deviceTokens) {
    final StringBuilder stringBuilder = new StringBuilder();
    for (String deviceToken : deviceTokens) {
      stringBuilder.append(deviceToken).append(",");
    }
    setProperties("device_tokens", stringBuilder.toString());
    return this;
  }

  public UPushPostBodyBuilder traceId(String traceId) {
    setProperties("traceId", traceId);
    return this;
  }

  //消息过期时间,格式: "YYYY-MM-DD hh:mm:ss"。
  public UPushPostBodyBuilder setExpireTime(String expireTime) {
    setProperties("expire_time", expireTime);
    return this;
  }

  public UPushPostBodyBuilder setProductionMode(Boolean prod) {
    setProperties("production_mode", prod.toString());
    return this;
  }

  public UPushPostBodyBuilder timeStamp(String timestamp) {
    setProperties("timestamp", timestamp);
    return this;
  }

  public UPushPostBodyBuilder afterOpen(String afterOpen) {
    setProperties("after_open", afterOpen);
    return this;
  }


  public void setProperties(String key, String value) {

    try {
      if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
        return;
      }

      if (UPushConstants.ROOT_KEYS.contains(key)) {
        property(key, value);
        return;
      }
      if (UPushConstants.PAYLOAD_KEYS.contains(key)) {
        property(key, value);
        return;
      }
      if (UPushConstants.BODY_KEYS.contains(key)) {
        property(key, value);
        return;
      }
      if (UPushConstants.POLICY_KEYS.contains(key)) {
        property(key, value);
        return;
      }
      extra(key, value);

    } catch (Exception e) {
      log.warn("specific key[{}] error", key, e);
    }
  }

  public void setProperties(final Map<String, Object> properties) {
    if (null != properties) {
      for (Map.Entry<String, Object> entry : properties.entrySet()) {
        final String key = entry.getKey();
        final Object value = entry.getValue();

        try {
          setProperties(key, String.valueOf(value));
        } catch (Exception e) {
          log.error("key [{}, {}]", key, value, e);
        }
      }
    }

  }

  public String build() {
    return getPostBody();
  }

}
