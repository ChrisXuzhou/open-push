package com.open.push;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Slf4j
public class Helper {

  private static Logger logger = LogManager.getLogger("push-message");
  private static Gson gson = new GsonBuilder()
      .disableHtmlEscaping().create();

  public static void log(Object object) {
    logger.info("{}", toJson(object));
    //log.info("{}", toJson(object));
  }

  public static String generateUUId() {
    return UUID.randomUUID().toString();
  }

  public static <T> Map<String, T> Json2Map(String payload) {
    Map<String, T> map = null;
    try {
      Type type = new TypeToken<Map<String, T>>() {
      }.getType();
      map = new Gson().fromJson(payload, type);
    } catch (Exception e) {
      log.warn("parse payload error {}", payload, e);
    }

    return map;
  }

  public static String toJson(Object object) {
    try {
      gson.toJson(object);
    } catch (Exception e) {
      log.warn("to json error", e);
    }
    return "";
  }

  public static <T> T fromJson(String json, Class<T> tClass) {
    try {
      return gson.fromJson(json, tClass);
    } catch (Exception e) {
      log.warn("to Object error", e);
    }

    return null;
  }

  public static void sleep(long time) {
    try {
      Thread.sleep(time);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static List<String> parseList(String raw) {
    Iterable<String> result = Splitter.on(',').trimResults().omitEmptyStrings()
        .split(raw);
    return Lists.newArrayList(result);
  }

  public static Long generateUniqueId() {
    return 0L;//GidUtil.next();
  }

}
