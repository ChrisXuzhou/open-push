package com.open.push.channel;

public interface PostSendOperator<T> {

  void postSendOperate(T notification);
}
