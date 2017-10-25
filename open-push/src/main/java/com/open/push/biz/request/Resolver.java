package com.open.push.biz.request;

public interface Resolver<T> {

  void resolve(T t);
}
