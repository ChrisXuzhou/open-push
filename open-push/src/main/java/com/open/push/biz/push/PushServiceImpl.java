package com.open.push.biz.push;

import com.open.push.biz.push.delivery.BufferedDefaultPostOperator;
import com.open.push.biz.push.delivery.DeliveryStrategyInitializer;
import com.open.push.biz.push.delivery.ImmediateAPNSPostOperator;
import com.open.push.biz.NotificationPackage;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Slf4j
public class PushServiceImpl extends DeliveryStrategyInitializer implements PushService{

  private List<NotificationValidator<NotificationPackage>> validators;

  public PushServiceImpl(
      BufferedDefaultPostOperator bufferedDefaultPostOperator,
      ImmediateAPNSPostOperator immediateAPNSPostOperator,
      @Qualifier("notificationValidators")
      List<NotificationValidator<NotificationPackage>> validators) {

    super(bufferedDefaultPostOperator, immediateAPNSPostOperator);
    this.validators = validators;
  }

  @Override
  public void push(final NotificationPackage notificationPackage) {
    try {
      doDeliver(notificationPackage);
    } catch (Exception e) {
      log.error("deliver error! package: {}",
          ReflectionToStringBuilder.toString(notificationPackage), e);
    }
  }

  private void doDeliver(final NotificationPackage notificationPackage) {
    Assert.notNull(validators, "Validators for acceptor must not be null!");

    for (NotificationValidator<NotificationPackage> notificationValidator : validators) {
      notificationValidator.validate(notificationPackage);
    }

    final String code = notificationPackage.getChannelCode();
    final Delivery<NotificationPackage> delivery = strategies.map(code);
    if (null == delivery) {
      log.warn("illegal channel code input: {}, deliver package: {}", code,
          ReflectionToStringBuilder.toString(notificationPackage));
      return;
    }
    delivery.handle(notificationPackage);
  }


}
