package com.open.push.biz.request.resolver;

import com.open.push.biz.task.TaskService;
import com.open.push.biz.request.PushRequestStatus;
import com.open.push.service.PushRequest;
import com.open.push.service.PushRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ConfirmPushRequestResolver extends ModifyPushRequestResolver {

  private TaskService parsePushRequestService;

  public ConfirmPushRequestResolver(
      PushRequestService pushRequestService, TaskService parsePushRequestService) {
    super(pushRequestService);
    this.parsePushRequestService = parsePushRequestService;
  }

  @Override
  void modify(PushRequest inner) {
    inner.setStatus(PushRequestStatus.CONFIRMED.name());
  }

  @Override
  protected void postModifyProcess(PushRequest inner) {
    parsePushRequestService.fire(inner);
  }


}
