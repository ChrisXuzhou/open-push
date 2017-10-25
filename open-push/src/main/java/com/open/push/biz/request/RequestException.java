package com.open.push.biz.request;

public class RequestException extends RuntimeException {

  public RequestException(String errorCode) {
    super(errorCode);
  }

}
