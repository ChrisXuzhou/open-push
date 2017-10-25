package com.open.push.biz.request;

import com.open.push.biz.request.resolver.CancelPushRequestResolver;
import com.open.push.biz.request.resolver.ConfirmPushRequestResolver;
import com.open.push.biz.request.resolver.DefaultPushRequestDetailResolver;
import com.open.push.biz.request.resolver.DefaultPushRequestResolver;
import com.open.push.biz.request.resolver.UpdatePushRequestResolver;
import com.open.push.service.PushRequest;
import com.open.push.service.PushRequestDetail;
import com.open.push.service.PushRequestService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class RequestResolverService {

  private PushRequestService modelService;

  private DefaultPushRequestResolver createResolver;
  private UpdatePushRequestResolver updateResolver;
  private DefaultPushRequestDetailResolver detailResolver;
  private ConfirmPushRequestResolver confirmResolver;
  private CancelPushRequestResolver cancelResolver;

  public RequestResolverService(DefaultPushRequestResolver createResolver,
      UpdatePushRequestResolver updateResolver,
      DefaultPushRequestDetailResolver detailResolver,
      ConfirmPushRequestResolver confirmResolver,
      CancelPushRequestResolver cancelResolver,
      PushRequestService modelService) {
    this.createResolver = createResolver;
    this.updateResolver = updateResolver;
    this.detailResolver = detailResolver;
    this.confirmResolver = confirmResolver;
    this.cancelResolver = cancelResolver;
    this.modelService = modelService;
  }

  public void resolve(final PushRequest request) {
    Resolver<PushRequest> resolver = createResolver;

    final String jobId = request.getJobId();
    if (StringUtils.isNotEmpty(jobId)) {
      resolver = updateResolver;
    }

    resolver.resolve(request);
  }

  public void resolve(final PushRequestDetail request) {
    detailResolver.resolve(request);
  }

  public void confirm(final String jobId) {
    confirmResolver.resolve(jobId);
  }

  public void cancel(final String jobId) {
    cancelResolver.resolve(jobId);
  }

  public PushRequest viewPushRequest(final String jobId) {
    return modelService.getBy(jobId);
  }
}
