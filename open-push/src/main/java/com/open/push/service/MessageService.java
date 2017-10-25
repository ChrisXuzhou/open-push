package com.open.push.service;

import java.util.List;

public interface MessageService {

  boolean trySave(Message msg);

  List<Message> getBy(Long traceId);

  Message getBy(Long traceId, String userId);

  void save(Message message);

}
