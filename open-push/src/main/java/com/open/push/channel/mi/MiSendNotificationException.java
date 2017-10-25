package com.open.push.channel.mi;

import com.open.push.channel.ChannelException;

public class MiSendNotificationException extends ChannelException {

  public MiSendNotificationException(String errorMessage) {
    super(errorMessage);
  }


  MiSendNotificationException(Throwable e) {
    super(e);
  }

}
