package com.open.push.service;

public interface PushRequestService {

  PushRequest getBy(String jobId);

  void save(PushRequest request);
}
