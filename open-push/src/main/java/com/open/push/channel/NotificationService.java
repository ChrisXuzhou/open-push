package com.open.push.channel;

import com.open.push.LifeCycle;
import com.open.push.biz.NotificationPackage;

public interface NotificationService extends LifeCycle {

  void send(NotificationPackage notification);
}
