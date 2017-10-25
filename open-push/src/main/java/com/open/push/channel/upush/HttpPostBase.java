package com.open.push.channel.upush;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

@Slf4j
class HttpPostBase {

  private String secret;

  HttpPostBase(String secret) {
    this.secret = secret;
  }

  private HttpClient client = HttpClientBuilder.create().build();

  private static final String host = "http://msg.umeng.com";
  private static final String postPath = "/api/send";
  private Gson gson = new Gson();

  Result post(String postBody) throws IOException {

    log.debug("post body: {}", postBody);

    String url = host + postPath;
    String sign = DigestUtils
        .md5Hex(("POST" + url + postBody + secret).getBytes("utf8"));
    url = url + "?sign=" + sign;

    final HttpPost post = new HttpPost(url);
    String USER_AGENT = "Mozilla/5.0";
    post.setHeader("User-Agent", USER_AGENT);
    StringEntity stringEntity = new StringEntity(postBody, "UTF-8");
    post.setEntity(stringEntity);
    HttpResponse response = client.execute(post);

    return parseResponse(response);
  }

  private Result parseResponse(HttpResponse response) throws IOException {

    BufferedReader rd = new BufferedReader(
        new InputStreamReader(response.getEntity().getContent()));
    StringBuilder result = new StringBuilder();
    String line;
    while ((line = rd.readLine()) != null) {
      result.append(line);
    }
    log.debug("raw response: {}", result.toString());

    Type type = new TypeToken<Result>() {
    }.getType();
    final Result ret = gson.fromJson(result.toString(), type);
    ret.setRaw(result.toString());
    return ret;
  }

  @Data
  static class Result {

    private String ret;
    private Info data;

    private String raw;

    @Data
    static class Info {

      private String error_code;
      private String msg_id;
    }

  }

  public enum Ret {
    SUCCESS,
    FAIL
  }


}
