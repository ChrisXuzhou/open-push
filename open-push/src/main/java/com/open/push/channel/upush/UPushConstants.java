package com.open.push.channel.upush;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class UPushConstants {

  // Keys can be setBit in the root level
  static final HashSet<String> ROOT_KEYS = new HashSet<>(
      Arrays
          .asList("appkey", "timestamp", "type", "device_tokens", "alias", "alias_type", "file_id",
              "filter", "production_mode", "feedback", "description", "thirdparty_id"));

  // Keys can be setBit in the policy level
  static final HashSet<String> POLICY_KEYS = new HashSet<>(
      Arrays.asList("start_time", "expire_time", "max_send_num"));

  // Keys can be setBit in the payload level
  static final HashSet<String> PAYLOAD_KEYS = new HashSet<>(
      Collections.singletonList("display_type"));

  // Keys can be setBit in the body level
  static final HashSet<String> BODY_KEYS = new HashSet<>(Arrays
      .asList("ticker", "title", "text", "builder_id", "icon", "largeIcon", "img", "play_vibrate",
          "play_lights", "play_sound", "sound", "after_open", "url", "activity", "custom"));

 public static final int UPUSH_MAX_TOKEN_NUMBER = 500;
}
