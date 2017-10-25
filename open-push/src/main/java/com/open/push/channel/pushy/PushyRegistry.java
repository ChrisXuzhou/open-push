package com.open.push.channel.pushy;

import lombok.Value;

@Value
public class PushyRegistry {

  private final int port;
  private final String host;
  private final String caPath;
  private final String p12InStream;
  private final String p12Password;



}
