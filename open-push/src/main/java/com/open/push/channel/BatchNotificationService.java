package com.open.push.channel;

import com.open.push.LifeCycle;
import com.open.push.channel.bean.BatchNotification;

public interface BatchNotificationService extends LifeCycle {

  void send(BatchNotification batchNotification);
}
