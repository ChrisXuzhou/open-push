package com.open.push.channel;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Generic Exception for channel interface.
 */
@Getter
@Setter
public class ChannelException extends RuntimeException {

  private String errorMessage;

  public ChannelException() {
    super();
  }

  public ChannelException(String errorMessage) {
    super();
    this.errorMessage = errorMessage;
  }

  public ChannelException(Throwable e) {
    super(e);
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
