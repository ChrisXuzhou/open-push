package com.open.push.channel;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.util.Assert;

@Slf4j
public class ResourceLoader {

  protected File load(String path) {

    File resource = null;

    // final URL url = Thread.currentThread().getContextClassLoader().getResource(path.replace("/", ""));

    final URL url = ResourceLoader.class.getResource(path);
    Assert.notNull(url, "load resource error. path: " + path);

    try {
      resource = new File(url.toURI());
    } catch (URISyntaxException e) {
      log.error("url to uri error. url :{}", ReflectionToStringBuilder.reflectionToString(url));
    }
    return resource;
  }

  protected InputStream loadInStream(String path) {
    final URL url = ResourceLoader.class.getResource(path);
    InputStream input = null;
    try {
      input = url.openStream();
    } catch (IOException e) {
      log.error("url to uri error. url :{}", ReflectionToStringBuilder.reflectionToString(url));
    }
    return input;
  }

  protected InputStream base64Decode(String content) {
    byte[] contentInBytes = Base64.decodeBase64(content);
    return new ByteArrayInputStream(contentInBytes);
  }
}
