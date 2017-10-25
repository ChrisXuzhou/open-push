package com.open.push.biz.request.resolver;

import com.open.push.service.PushRequest;
import com.open.push.service.PushRequestService;
import com.open.push.biz.request.Resolver;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


/**
 * <p>更新push request请求的逻辑.</p>
 */
@Slf4j
@Service
public class UpdatePushRequestResolver extends PushRequestCondition implements
    Resolver<PushRequest> {

  private PushRequestService modelService;

  public UpdatePushRequestResolver(final PushRequestService pushRequestService) {
    this.modelService = pushRequestService;
  }

  @Override
  public void resolve(final PushRequest request) {

    final String jobId = request.getJobId();
    Assert.notNull(jobId, "job Id must not be null.");

    for (; ; ) {

      try {
        doResolve(request);
        break;

      } catch (ObjectOptimisticLockingFailureException e) {
        log.info("Multiple thread update pushRequest pushRequest at same time. {}", e);
      }
    }

    log.debug("pushRequest pushRequest [{}] updated.", ReflectionToStringBuilder.toString(request));

  }

  private void doResolve(final PushRequest request) {

    final String jobId = request.getJobId();
    final PushRequest inner = modelService.getBy(jobId);

    //check if inner pushRequest pushRequest's bean satisfy update condition.
    checkUpdatePushRequestCondition(inner, jobId);

    request.setVersion(inner.getVersion());
    request.setId(inner.getId());

    modelService.save(request);

  }

}
