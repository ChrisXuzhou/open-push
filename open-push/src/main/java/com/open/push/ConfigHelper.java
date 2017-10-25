package com.open.push;


import com.open.push.channel.pushy.PushyRegistry;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class ConfigHelper {

  public static PushyRegistry getPushyRegistry(final String appName) {
    if (!(StringUtils.endsWith(appName, "ios") || StringUtils.endsWith(appName, "iphone")
        || StringUtils.endsWith(appName, "ipad"))) {
      return null;
    }

    /*
    final String appShortName = shortName(appName);

    final String host = CoreUtil.getPropertyValue(PUSH_PUSHY_REGISTRY_HOST);
    Assert.isTrue(StringUtil.isNotEmpty(host), "host must not be empty.");

    final String p12key = PUSH_PUSHY_REGISTRY_P12.replace("{}", appShortName);
    final String p12 = CoreUtil.getPropertyValue(p12key);
    if (StringUtil.isEmpty(p12)) {
      log.error("app name: {}'s p12 is empty!", appName);
      return null; //null is ok to return here.
    }

    final String passwordKey = PUSH_PUSHY_REGISTRY_PASSWORD.replace("{}", appShortName);
    final String password = CoreUtil.getPropertyValue(passwordKey);
    if (StringUtil.isEmpty(password)) {
      log.warn("app name: {}'s password is empty!", appName);
      return null; //null is ok to return here.
    }

    return new PushyRegistry(PUSH_PUSHY_REGISTRY_PORT, host, null, p12, password);
    */

    return null;
  }


  public static List<String> getRegisteredApps() {
/*
    final String registeredApps = CoreUtil.getPropertyValue(PUSH_APP_REGISTERED);
    if (StringUtil.isNotEmpty(registeredApps)) {
      return Lists.newArrayList(Splitter.on(',')
          .trimResults()
          .omitEmptyStrings()
          .split(registeredApps));
    }
    return Lists.newArrayList();
    */

    return Collections.singletonList("");
  }


  public static String getMiSecret(final String appName) {
    if (!StringUtils.endsWith(appName, "android")) {
      return null;
    }
    /*
    final String appShortName = shortName(appName);

    final String secretKey = PUSH_MI_REGISTRY_SECRET.replace("{}", appShortName);
    final String secret = CoreUtil.getPropertyValue(secretKey);
    if (StringUtil.isEmpty(secret)) {
      log.warn("app name: {}'s secret is empty!", appName);
      return null; //null is ok to return here.
    }
    return secret;
    */
    return "";
  }

  public static String getUpushAppKey(final String app) {
    return "";
  }

  public static String getUpushSecret(final String app) {
    return "";
  }

  public static String getTopic(final String appName) {

    /*
    final String topicKey = PUSH_TOPIC.replace("{}", appName);
    final String topic = CoreUtil.getPropertyValue(topicKey);

    if (StringUtil.isEmpty(topic)) {
      log.error("app name: {}'s topic is empty!", appName);
      return null; //null is ok to return here.
    }

    return topic;
    */
    return "";
  }

  private static final String REFRESH_STRATEGY = "dxy.push.refresh.strategy";


}
