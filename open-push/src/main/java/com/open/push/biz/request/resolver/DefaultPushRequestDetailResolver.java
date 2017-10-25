package com.open.push.biz.request.resolver;

import com.open.push.service.PushRequest;
import com.open.push.service.PushRequestDetail;
import com.open.push.service.PushRequestDetailService;
import com.open.push.service.PushRequestService;
import com.open.push.biz.request.Resolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DefaultPushRequestDetailResolver extends PushRequestCondition implements
    Resolver<PushRequestDetail> {

  private PushRequestService pushRequestService;
  private PushRequestDetailService pushRequestDetailService;

  public DefaultPushRequestDetailResolver(final PushRequestService pushRequestService,
      final PushRequestDetailService pushRequestDetailService) {
    this.pushRequestService = pushRequestService;
    this.pushRequestDetailService = pushRequestDetailService;
  }

  @Override
  public void resolve(PushRequestDetail request) {

    final String jobId = request.getJobId();
    final PushRequest inner = pushRequestService.getBy(jobId);

    checkUpdatePushRequestCondition(inner, jobId);
    pushRequestDetailService.save(request);
  }
}
