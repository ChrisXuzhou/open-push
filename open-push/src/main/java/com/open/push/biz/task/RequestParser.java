package com.open.push.biz.task;

public interface RequestParser<T> {

  //@Async
  void parse(T chunk);
}
