package com.open.push.channel.pushy;

import com.open.push.channel.ChannelException;
import com.open.push.channel.bean.Notification;
import com.open.push.channel.PostSendOperator;
import com.open.push.channel.ResourceLoader;
import com.turo.pushy.apns.ApnsClient;
import com.turo.pushy.apns.ApnsClientBuilder;
import io.netty.util.concurrent.Future;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.net.ssl.SSLException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

@Slf4j
public class PushyChannelBuilder extends ResourceLoader {

  private final int port;
  private final String host;
  private final String caPath;
  private final String p12InStream;
  private final String p12Password;

  private final PushyRegistry registry;
  private final PostSendOperator<Notification> operator;

  /**
   * <p>创建pushy channel.</p>
   */
  PushyChannelBuilder(PushyRegistry pushyRegistry, PostSendOperator<Notification> operator) {
    this.port = pushyRegistry.getPort();
    this.host = pushyRegistry.getHost();
    this.caPath = pushyRegistry.getCaPath();
    this.p12InStream = pushyRegistry.getP12InStream();
    this.p12Password = pushyRegistry.getP12Password();

    this.registry = pushyRegistry;
    this.operator = operator;
  }

  /**
   * <p>build Pushy channel which contains a pushy client.</p>
   *
   * <p>1. 创建pushy的client.</p>
   * <p>2. 连接pushy到APNS GATEWAY.</p>
   */

  public PushyChannel build() throws ChannelException {
    PushyChannel channel = null;
    for (int i = 0; i < 5; i++) {
      try {
        channel = createAndConnect();
        break;
      } catch (ChannelException e) {
        log.error("initial error.", e);
      }
    }
    return channel;
  }

  private PushyChannel createAndConnect()
      throws BuildPushyChannelException, PushyConnectionException {

    final ApnsClientBuilder builder = new ApnsClientBuilder();

    if (StringUtils.isNoneEmpty(caPath)) {
      final File ca = load(caPath);
      builder.setTrustedServerCertificateChain(ca);
    }

    try {
      final InputStream p12Stream = base64Decode(p12InStream);
      builder.setClientCredentials(p12Stream, p12Password);
    } catch (IOException e) {
      log.error("setBit client credentials(p12) error.", e);
      throw new BuildPushyChannelException("p12 file load error");
    }

    final ApnsClient client;
    try {
      client = builder.build();
    } catch (SSLException e) {
      log.error("pushy client build error. registry: {} ",
          ReflectionToStringBuilder.toString(registry), e);
      throw new BuildPushyChannelException("build apns client error");
    }

    final Future<Void> future;
    try {
      future = client.connect(host, port).await();
    } catch (InterruptedException e) {
      log.error("pushy connect APNs GATEWAY error. registry: {}",
          ReflectionToStringBuilder.toString(registry), e);
      throw new PushyConnectionException("connect to APNs GATEWAY ERROR!");
    }

    if (!future.isSuccess()) {
      log.error("pushy connect APNs GATEWAY failed. registry: {}",
          ReflectionToStringBuilder.toString(registry), future.cause());
      throw new PushyConnectionException("connect to APNs GATEWAY FAILED!");
    }

    return new PushyChannel(client, operator);
  }

}
