package com.open.push.biz.push.delivery.buffer;

import com.open.push.Helper;
import com.open.push.support.DozerHelper;
import com.open.push.channel.bean.BatchNotification;
import com.open.push.biz.NotificationPackage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
public class NotificationBuffer {

  public final int ITEM_SIZE;

  NotificationBuffer(int size) {
    ITEM_SIZE = size;
  }

  /**
   * Map<jobId, BufferedNotificationGroup>.
   */
  private Map<String, BufferedNotificationGroup> notificationBuffers = new HashMap<>();

  /**
   * <p>clear buffer.</p>
   */
  void clear() {
    notificationBuffers.clear();
  }

  /**
   * <p>clear item.</p>
   */
  void remove(String key) {
    notificationBuffers.remove(key);
  }

  public BufferedNotificationGroup get(String key) {
    return notificationBuffers.get(key);
  }

  public int insert(NotificationPackage notificationPackage) {
    final String firstKey = notificationPackage.getJobId();
    BufferedNotificationGroup bufferedMiNotificationGroup = notificationBuffers.get(firstKey);
    if (null == bufferedMiNotificationGroup) {
      bufferedMiNotificationGroup = BufferedNotificationGroup.of(notificationPackage, ITEM_SIZE);
    }
    notificationBuffers.put(firstKey, bufferedMiNotificationGroup);
    return bufferedMiNotificationGroup.insert(notificationPackage);
  }

  @EqualsAndHashCode(callSuper = true)
  @Data
  @NoArgsConstructor
  public static class BufferedNotificationGroup extends BatchNotification {

    /**
     * Map<deviceToken, notificationPackage>.
     */
    @NonNull
    private Map<String, NotificationPackage> packages;
    /**
     * jobId.
     */
    @NonNull
    private String jobId;

    static BufferedNotificationGroup of(NotificationPackage notificationPackage, int itemSize) {
      final BufferedNotificationGroup group = DozerHelper
          .map(notificationPackage, BufferedNotificationGroup.class);
      group.setDeviceTokens(new ArrayList<>(itemSize + 10));
      group.setPackages(new HashMap<>());
      group.setTraceId(Helper.generateUniqueId());

      return group;
    }

    int insert(NotificationPackage notificationPackage) {
      if (null != packages) {
        notificationPackage.setTraceId(super.getTraceId());
        packages.put(notificationPackage.getDeviceToken(), notificationPackage);
        final List<String> deviceTokens = this.getDeviceTokens();
        deviceTokens.add(notificationPackage.getDeviceToken());
        return deviceTokens.size();
      }
      return -1;
    }

  }


}
