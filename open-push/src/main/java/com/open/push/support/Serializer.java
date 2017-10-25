package com.open.push.support;


public interface Serializer {

  byte[] serialize(Object obj);

  <T> T deserialize(byte[] bytes);
}
