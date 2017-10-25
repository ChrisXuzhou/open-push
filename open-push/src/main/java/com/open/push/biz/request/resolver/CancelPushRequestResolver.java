package com.open.push.biz.request.resolver;

import com.open.push.biz.request.PushRequestStatus;
import com.open.push.service.PushRequest;
import com.open.push.service.PushRequestService;
import org.springframework.stereotype.Service;

@Service
public class CancelPushRequestResolver extends ModifyPushRequestResolver {

  public CancelPushRequestResolver(
      PushRequestService pushRequestService) {
    super(pushRequestService);
  }

  @Override
  void modify(PushRequest inner) {
    inner.setStatus(PushRequestStatus.CANCELED.name());
  }

}
