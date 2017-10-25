package com.open.push.channel.mi;

import com.open.push.channel.PostSendOperator;
import com.open.push.channel.bean.BatchNotification;
import com.xiaomi.xmpush.server.Sender;

public class MiChannelBuilder {

  private final String appSecretKey;
  private final PostSendOperator<BatchNotification> postSendOperator;

  MiChannelBuilder(final String appSecretKey,
      final PostSendOperator<BatchNotification> postSendOperator) {
    this.appSecretKey = appSecretKey;
    this.postSendOperator = postSendOperator;

  }

  public MiChannel build() {
    final Sender sender = new Sender(appSecretKey);
    return new MiChannel(sender, postSendOperator);
  }

}
