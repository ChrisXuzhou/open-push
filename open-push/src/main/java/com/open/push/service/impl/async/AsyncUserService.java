package com.open.push.service.impl.async;

public interface AsyncUserService {

  boolean tryDelete(String deviceToken);
}
