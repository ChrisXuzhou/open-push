package com.open.push.biz.token.processor;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 */
public class ExceptionalProcessorException extends RuntimeException {

  public ExceptionalProcessorException(final String message) {
    super(message);
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }
}
