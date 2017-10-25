package com.open.push.biz.push.delivery;

import com.open.push.ConfigHelper;
import com.open.push.Helper;
import com.open.push.LifeCycle;
import com.open.push.biz.push.Buffered;
import com.open.push.biz.push.Delivery;
import com.open.push.biz.push.delivery.buffer.BufferedDelivery;
import com.open.push.biz.push.delivery.immediate.ImmediateDelivery;
import com.open.push.channel.mi.MiNotificationService;
import com.open.push.channel.pushy.PushyNotificationService;
import com.open.push.channel.pushy.PushyRegistry;
import com.open.push.channel.upush.UPushNotificationService;
import com.open.push.biz.NotificationPackage;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

@Slf4j
public class DeliveryStrategyInitializer implements LifeCycle {

  protected DeliveryStrategies strategies;
  private ImmediateAPNSPostOperator immediateAPNSPostOperator;
  private BufferedDefaultPostOperator bufferedDefaultPostOperator;

  public DeliveryStrategyInitializer(
      BufferedDefaultPostOperator bufferedDefaultPostOperator,
      ImmediateAPNSPostOperator immediateAPNSPostOperator) {
    this.immediateAPNSPostOperator = immediateAPNSPostOperator;
    this.bufferedDefaultPostOperator = bufferedDefaultPostOperator;
  }

  private volatile boolean start = false;

  @Override
  public void initialize() {

    start = false;

    loadCertificates();

    strategies.initialize();

    start = true;

    flushBufferPeriod();
  }

  @Override
  public void close() {

    start = false;
  }

  private void flushBufferPeriod() {

    Executors.newSingleThreadExecutor().submit((Runnable) () -> {
      for (; ; ) {
        if (!start) {
          break;
        }

        log.debug("flush all cache data.. ");
        synchronized (DeliveryStrategyInitializer.class) {
          strategies.flush();
        }
        Helper.sleep(1000 * 2);
      }
    });

  }


  private void loadCertificates() {

    final Builder<String, Delivery<NotificationPackage>> builder = ImmutableMap.builder();
    final List<String> registeredApps = ConfigHelper.getRegisteredApps();

    for (final String app : registeredApps) {
      final PushyRegistry registry = ConfigHelper.getPushyRegistry(app);
      if (null != registry) {
        builder.put(app + "pushy",
            new ImmediateDelivery(
                new PushyNotificationService(registry, immediateAPNSPostOperator)));
      }

      final String secret = ConfigHelper.getMiSecret(app);
      if (StringUtils.isNotEmpty(secret)) {
        builder.put(app + "mi",
            new BufferedDelivery(
                new MiNotificationService(secret, bufferedDefaultPostOperator), 1000));
      }

      final String appKey = ConfigHelper.getUpushAppKey(app);
      final String uPushSecret = ConfigHelper.getUpushSecret(app);

      if (StringUtils.isNotEmpty(appKey) && StringUtils.isNotEmpty(uPushSecret)) {
        builder.put(app + "uPush",
            new BufferedDelivery(
                new UPushNotificationService(appKey, uPushSecret, bufferedDefaultPostOperator),
                500));
      }
    }

    this.strategies = new DeliveryStrategies(builder.build());
  }


  @Getter
  @Setter
  public static class DeliveryStrategies implements LifeCycle {

    private Map<String, Delivery<NotificationPackage>> mapper;

    DeliveryStrategies(Map<String, Delivery<NotificationPackage>> mapper) {
      this.mapper = mapper;
    }

    public Delivery<NotificationPackage> map(String channelCode) {
      Assert.notNull(mapper, "mapper must not be null! ");
      return mapper.get(channelCode);
    }

    @Override
    public void initialize() {
      for (Map.Entry<String, Delivery<NotificationPackage>> entry : mapper.entrySet()) {
        final Delivery<NotificationPackage> delivery = entry.getValue();
        delivery.initialize();
      }
    }

    @Override
    public void close() {
      for (Map.Entry<String, Delivery<NotificationPackage>> entry : mapper.entrySet()) {
        final Delivery<NotificationPackage> delivery = entry.getValue();
        delivery.close();
      }
    }

    public void flush() {
      for (Map.Entry<String, Delivery<NotificationPackage>> entry : mapper.entrySet()) {
        final Delivery<NotificationPackage> delivery = entry.getValue();
        if (delivery instanceof Buffered) {
          final BufferedDelivery bufferedDelivery = (BufferedDelivery) delivery;
          bufferedDelivery.refresh();
        }
      }
    }
  }

}
