package com.open.push.channel.mi;

import com.open.push.channel.BatchNotificationService;
import com.open.push.channel.PostSendOperator;
import com.open.push.channel.bean.BatchNotification;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@Slf4j
public class MiNotificationService implements BatchNotificationService {

  private MiChannel miChannel;
  private String appSecret;

  private PostSendOperator<BatchNotification> batchNotificationPostSendOperator;

  public MiNotificationService(String appSecret,
      PostSendOperator<BatchNotification> batchNotificationPostSendOperator) {
    this.appSecret = appSecret;
    this.batchNotificationPostSendOperator = batchNotificationPostSendOperator;
  }

  @Override
  public void initialize() {
    miChannel = new MiChannelBuilder(appSecret, batchNotificationPostSendOperator).build();
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
    miChannel.send(batchNotification);
  }

}
