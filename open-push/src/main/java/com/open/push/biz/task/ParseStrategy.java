package com.open.push.biz.task;

import com.open.push.biz.task.bean.ParseTask;
import com.open.push.service.PushRequest;
import com.open.push.service.PushRequestDetailService;

public interface ParseStrategy {

  void parse(
      PushRequest pushRequest,
      RequestParser<ParseTask> parser,
      PushRequestDetailService modelPushRequestDetailService);

}
