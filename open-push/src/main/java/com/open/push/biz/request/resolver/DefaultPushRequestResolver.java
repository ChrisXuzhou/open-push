package com.open.push.biz.request.resolver;

import com.open.push.Helper;
import com.open.push.service.PushRequest;
import com.open.push.service.PushRequestService;
import com.open.push.biz.request.Resolver;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * <p>插入新push request请求逻辑.</p>
 */
@Slf4j
@Service
public class DefaultPushRequestResolver implements Resolver<PushRequest> {

  private PushRequestService modelService;

  public DefaultPushRequestResolver(final PushRequestService pushRequestService) {
    this.modelService = pushRequestService;
  }

  @Override
  public void resolve(PushRequest request) {

    final String jobId = request.getJobId();
    Assert.isTrue(StringUtils.isEmpty(jobId), "job ID must be null or empty!");

    request.setJobId(generateJobID());
    modelService.save(request);

    log.debug("new pushRequest pushRequest landed. pushRequest:{}", ReflectionToStringBuilder.toString(request));
  }

  private String generateJobID() {
    return Helper.generateUUId();
  }

}
