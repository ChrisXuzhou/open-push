package com.open.push.biz.request.resolver;

import com.open.push.biz.request.Resolver;
import com.open.push.service.PushRequest;
import com.open.push.service.PushRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.util.Assert;

@Slf4j
public abstract class ModifyPushRequestResolver extends PushRequestCondition implements
    Resolver<String> {

  private PushRequestService modelService;

  public ModifyPushRequestResolver(final PushRequestService pushRequestService) {
    this.modelService = pushRequestService;
  }

  @Override
  public void resolve(final String jobId) {

    Assert.notNull(jobId, "job Id must not be null.");

    for (; ; ) {

      try {
        doResolve(jobId);
        break;

      } catch (ObjectOptimisticLockingFailureException e) {
        log.info("Multiple thread update pushRequest pushRequest at same time. {}", e);
      }
    }

    log.debug("pushRequest pushRequest job id[{}] confirmed.", jobId);
  }

  private void doResolve(final String jobId) {

    final PushRequest inner = modelService.getBy(jobId);

    //check if inner pushRequest pushRequest's bean satisfy update condition.
    checkUpdatePushRequestCondition(inner, jobId);
    modify(inner);
    modelService.save(inner);
    postModifyProcess(inner);
  }

  abstract void modify(PushRequest inner);

  protected void postModifyProcess(PushRequest inner) {

  }

  ;
}
