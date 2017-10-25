package com.open.push.channel.upush;

import com.open.push.channel.BatchNotificationService;
import com.open.push.channel.PostSendOperator;
import com.open.push.channel.bean.BatchNotification;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@Slf4j
public class UPushNotificationService implements BatchNotificationService {

  private UPushChannel uPushChannel;

  private String appKey;
  private String secret;

  private PostSendOperator<BatchNotification> postSendOperator;

  public UPushNotificationService(String appKey, String secret,
      PostSendOperator<BatchNotification> postSendOperator) {
    this.appKey = appKey;
    this.secret = secret;
    this.postSendOperator = postSendOperator;
  }

  @Override
  public void initialize() {
    uPushChannel = new UPushChannel(appKey, secret, postSendOperator);
  }

  @Override
  public void close() {
    throw new UnsupportedOperationException();
  }

  /**
   * <p>对小米的批量发送做特殊处理. </p>
   */
  @Override
  public void send(final BatchNotification batchNotification) {

    log.debug("buffered notification: {}",
        ReflectionToStringBuilder.toString(batchNotification));
    uPushChannel.send(batchNotification);
  }
}
