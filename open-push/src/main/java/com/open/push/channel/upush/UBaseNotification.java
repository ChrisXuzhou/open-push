package com.open.push.channel.upush;

import java.util.Objects;
import lombok.Data;
import org.json.JSONObject;

@Data
abstract class UBaseNotification {

  private final JSONObject data = new JSONObject();

  String getPostBody() {
    return data.toString();
  }

  void property(String key, Object value) throws Exception {
    if (UPushConstants.ROOT_KEYS.contains(key)) {
      data.put(key, value);
    } else if (UPushConstants.PAYLOAD_KEYS.contains(key)) {
      JSONObject payloadJson;
      if (data.has("payload")) {
        payloadJson = data.getJSONObject("payload");
      } else {
        payloadJson = new JSONObject();
        data.put("payload", payloadJson);
      }
      payloadJson.put(key, value);
    } else if (UPushConstants.BODY_KEYS.contains(key)) {
      JSONObject bodyJson;
      JSONObject payloadJson;
      if (data.has("payload")) {
        payloadJson = data.getJSONObject("payload");
      } else {
        payloadJson = new JSONObject();
        data.put("payload", payloadJson);
      }
      if (payloadJson.has("body")) {
        bodyJson = payloadJson.getJSONObject("body");
      } else {
        bodyJson = new JSONObject();
        payloadJson.put("body", bodyJson);
      }
      bodyJson.put(key, value);
    } else if (UPushConstants.POLICY_KEYS.contains(key)) {
      JSONObject policyJson;
      if (data.has("policy")) {
        policyJson = data.getJSONObject("policy");
      } else {
        policyJson = new JSONObject();
        data.put("policy", policyJson);
      }
      policyJson.put(key, value);
    } else {
      if (Objects.equals(key, "payload") || Objects.equals(key, "body") || Objects
          .equals(key, "policy") || Objects.equals(key, "extra")) {
        throw new Exception("You don't need to setBit value for " + key
            + " , just setBit values for the sub keys in it.");
      } else {
        throw new Exception("Unknown key: " + key);
      }
    }
  }

  void extra(String key, String value) throws Exception {
    JSONObject payloadJson;
    JSONObject extraJson;
    if (data.has("payload")) {
      payloadJson = data.getJSONObject("payload");
    } else {
      payloadJson = new JSONObject();
      data.put("payload", payloadJson);
    }

    if (payloadJson.has("extra")) {
      extraJson = payloadJson.getJSONObject("extra");
    } else {
      extraJson = new JSONObject();
      payloadJson.put("extra", extraJson);
    }
    extraJson.put(key, value);
  }

}
