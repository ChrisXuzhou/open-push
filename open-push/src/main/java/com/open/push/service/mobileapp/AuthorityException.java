package com.open.push.service.mobileapp;

import lombok.Getter;

@Getter
public class AuthorityException extends RuntimeException {

  private final String errorCode;

  AuthorityException(String errorCode) {
    this.errorCode = errorCode;
  }

}
