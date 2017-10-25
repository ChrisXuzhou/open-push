package com.open.push.biz.token;

/**
 * interface for User info refreshing operation.
 */
public interface UserRefresher<T extends RefreshRequest> {

  void refresh(T t);
}
