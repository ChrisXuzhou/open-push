package com.open.push.biz.push;

import com.open.push.biz.NotificationPackage;

/**
 * Created by xuzhou on 2017/10/20.
 */
public interface PushService {

  void push(NotificationPackage notificationPackage);
}
