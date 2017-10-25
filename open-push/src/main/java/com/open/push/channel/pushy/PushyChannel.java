package com.open.push.channel.pushy;

import com.open.push.Helper;
import com.open.push.LifeCycle;
import com.open.push.channel.PostSendOperator;
import com.open.push.channel.PushChannel;
import com.open.push.channel.bean.Notification;
import com.open.push.channel.bean.Status;
import com.turo.pushy.apns.ApnsClient;
import com.turo.pushy.apns.ClientNotConnectedException;
import com.turo.pushy.apns.PushNotificationResponse;
import com.turo.pushy.apns.util.ApnsPayloadBuilder;
import com.turo.pushy.apns.util.SimpleApnsPushNotification;
import io.netty.util.concurrent.Future;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@Slf4j
public class PushyChannel implements PushChannel<Notification>, LifeCycle {

  private ApnsClient client;
  private PostSendOperator<Notification> operator;

  /**
   * <p>pushy channel 封装一个apns client.</p>
   */
  PushyChannel(final ApnsClient client, final PostSendOperator<Notification> operator) {
    this.client = client;
    this.operator = operator;
  }

  /**
   * <p>当失去与apns的连接后，尝试重新连接.</p>
   */
  private boolean tryReconnect() {
    final Future<Void> future = client.getReconnectionFuture();

    try {
      future.await();
    } catch (InterruptedException e) {
      log.warn("pushy reconnect interrupted.", e);
    }

    return future.isSuccess();
  }

  @Override
  public void initialize() {

  }

  @Override
  public void close() {

    if (null != client) {
      final Future<Void> future = client.disconnect();

      try {
        future.await();
      } catch (InterruptedException e) {
        log.warn("pushy close interrupted.", e);
      }

      if (future.isSuccess()) {
        log.info("close pushy channel successfully!");
      }
    }
  }


  /**
   * <p>发送push消息.</p>
   */
  public void send(final Notification notification) {

    final String topic = notification.getTopic();
    final String deviceToken = notification.getDeviceToken();

    final ApnsPayloadBuilder builder = new ApnsPayloadBuilder();
    builder
        .setAlertTitle(notification.getTitle())
        .setAlertBody(notification.getDescription())
        .addCustomProperty("traceId", String.valueOf(notification.getTraceId()));

    final Map<String, Object> properties = Helper.Json2Map(notification.getPayload());
    setProperties(properties, builder);

    final String payload = builder.buildWithDefaultMaximumLength();
    log.debug("payload: {}", payload);

    final Future<PushNotificationResponse<SimpleApnsPushNotification>> notificationResponseFuture
        = client.sendNotification(new SimpleApnsPushNotification(deviceToken, topic, payload));

    notificationResponseFuture.addListener(future -> {

      Status status = Status.FAILED;
      if (future.isSuccess()) {

        final PushNotificationResponse<SimpleApnsPushNotification> response =
            (PushNotificationResponse<SimpleApnsPushNotification>) future.get();
        if (response.isAccepted()) {
          status = Status.RECEIVED;
        } else {

          log.info("failed reason: {} ,notification: {}", response.getRejectionReason(),
              ReflectionToStringBuilder.toString(notification));

          notification.setFailedReason(response.getRejectionReason());
        }

      } else {
        status = Status.RETRY;
        final Throwable throwable = future.cause();
        log.warn("pushy send notification failed.. {}", throwable.getMessage());

        if (throwable instanceof ClientNotConnectedException) {
          log.warn("APNs Gateway: lost connection... try to reconnect...");
          if (tryReconnect()) {
            log.info("APNs Gateway: reconnected.");
          }
        }

        Helper.sleep(200);

      }

      notification.setStatus(status);
      operator.postSendOperate(notification);
    });

  }

  private void setProperties(final Map<String, Object> properties,
      final ApnsPayloadBuilder builder) {
    if (null != properties) {
      for (Map.Entry<String, Object> entry : properties.entrySet()) {
        final String key = entry.getKey();
        final Object value = entry.getValue();

        try {
          if (StringUtils.isEmpty(key) || null == value) {
            continue;
          }
          if (StringUtils.equals("action-loc-key", key)) {
            builder.setActionButtonLabel(value.toString());
            continue;
          }
          if (StringUtils.equals("badge", key)) {
            if (value instanceof Double) {
              builder.setBadgeNumber(((Double) value).intValue());
            }else {
              builder.setBadgeNumber(Integer.parseInt(value.toString()));
            }
            continue;
          }
          if (StringUtils.equals("sound", key)) {
            builder.setSoundFileName(value.toString());
            continue;
          }
        } catch (Exception e) {
          log.warn("specific key[{}, {}] error", key, value, e);
        }

        builder.addCustomProperty(entry.getKey(), value);
      }
    }

  }

}
