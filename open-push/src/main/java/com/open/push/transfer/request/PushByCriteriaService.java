package com.open.push.transfer.request;

import com.open.push.biz.request.PushRequestBuilder;
import com.open.push.biz.request.PushRequestDetailBuilder;
import com.open.push.biz.request.RequestResolverService;
import com.open.push.service.PushRequest;
import com.open.push.service.PushRequestDetail;
import java.util.Date;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PushByCriteriaService {

  private final RequestResolverService resolverService;

  public PushByCriteriaService(RequestResolverService resolverService) {
    this.resolverService = resolverService;
  }

  public String handleRequest(@Valid PushByCriteriaRequest request) {

    final PushRequestBuilder builder = new PushRequestBuilder();

    builder.appNames(request.getAppNames());

    if (StringUtils.isNotEmpty(request.getJobId())) {
      builder.jobId(request.getJobId());
    }

    if (StringUtils.isNotEmpty(request.getDescription())) {
      builder.description(request.getDescription());
    }

    builder.title(request.getTitle())
        .payload(request.getPayload())
        .criteria(request.getCriteria().name());

    if (StringUtils.isNotEmpty(request.getMessageId())) {
      builder.messageId(request.getMessageId());
    }

    if (StringUtils.isNotEmpty(request.getMessageType())) {
      builder.messageType(request.getMessageType());
    }

    if (StringUtils.isNotEmpty(request.getCriteriaDescription())) {
      builder.criteriaDescription(request.getCriteriaDescription());
    }
    if (null != request.getTimeToSend()) {
      builder.pushTime(new Date(request.getTimeToSend()));
    }

    final PushRequest pushRequest = builder.build();
    resolverService.resolve(pushRequest);

    // postResolvePushRequest(pushRequest);

    return pushRequest.getJobId();
  }


  public void handleRequestDetail(@Valid PushByCriteriaRequestDetail request) {
    final PushRequestDetailBuilder builder = new PushRequestDetailBuilder();

    builder.jobId(request.getJobId())
        .criteria(request.getCriteria().name())
        .criteriaDescription(request.getCriteriaDescription());

    final PushRequestDetail detail = builder.build();
    resolverService.resolve(detail);
  }

  public void confirmRequest(@Valid ModifyPushRequestStatusRequest request) {
    resolverService.confirm(request.getJobId());
  }

  public void cancelRequest(@Valid ModifyPushRequestStatusRequest request) {
    resolverService.cancel(request.getJobId());
  }


}
