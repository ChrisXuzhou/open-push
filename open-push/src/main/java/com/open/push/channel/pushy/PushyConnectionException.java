package com.open.push.channel.pushy;

import com.open.push.channel.ChannelException;

class PushyConnectionException extends ChannelException {

  PushyConnectionException(String errorMessage) {
    super(errorMessage);
  }
}
