package com.open.push.channel;

public interface PushChannel<T> {

  void send(T t);

}
