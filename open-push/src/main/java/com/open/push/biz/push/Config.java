package com.open.push.biz.push;


import com.open.push.biz.NotificationPackage;
import com.open.push.biz.push.delivery.BufferedDefaultPostOperator;
import com.open.push.biz.push.delivery.ImmediateAPNSPostOperator;
import java.util.Collections;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

  @Bean("notificationValidators")
  public List<NotificationValidator<NotificationPackage>> getNotificationValidators() {
    return Collections.singletonList(new DefaultValidator());
  }

  @Bean
  public PushService getPushService(
      BufferedDefaultPostOperator bufferedDefaultPostOperator,
      ImmediateAPNSPostOperator immediateAPNSPostOperator) {

    return new PushServiceImpl(bufferedDefaultPostOperator, immediateAPNSPostOperator,
        getNotificationValidators());
  }

}
