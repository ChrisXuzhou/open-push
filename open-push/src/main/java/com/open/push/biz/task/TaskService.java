package com.open.push.biz.task;

import com.open.push.service.PushRequest;

public interface TaskService {

  void parse(String jobId);

  void fire(PushRequest pushRequest);
}
