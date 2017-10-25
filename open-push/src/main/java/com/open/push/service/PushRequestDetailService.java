package com.open.push.service;

public interface PushRequestDetailService {

  PushRequestDetail getNextRequestDetail(String jobId, String status);

  void save(PushRequestDetail detail);
}
