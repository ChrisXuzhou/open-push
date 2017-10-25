package com.open.push.biz.task.pack;

import com.open.push.support.DozerHelper;
import com.open.push.biz.NotificationPackage;
import com.google.common.collect.ImmutableMap;
import java.util.Map;

/**
 * Created by xuzhou on 2017/10/20.
 */
public class PackerHelper {


  public static Map<String, AbstractNotificationPacker> buildNotificationPackers(

      NotificationPackage notificationPackageTemplate) {
    // for APNS push use.
    final PushyNotificationPacker pushyPacker = new PushyNotificationPacker(
        DozerHelper.map(notificationPackageTemplate, NotificationPackage.class));

    // for mi push channel use.
    final MiNotificationPacker miPacker = new MiNotificationPacker(
        DozerHelper.map(notificationPackageTemplate, NotificationPackage.class));

    // for u-push channel use.
    final UPushNotificationPacker uPushPacker = new UPushNotificationPacker(
        DozerHelper.map(notificationPackageTemplate, NotificationPackage.class));

    return ImmutableMap.<String, AbstractNotificationPacker>builder()
        .put("iOS", pushyPacker)
        .put("UPush", uPushPacker)
        .put("Mi", miPacker).build();
  }

}
