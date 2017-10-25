package com.open.push.biz.request.resolver;

import com.open.push.biz.request.PushRequestStatus;
import com.open.push.biz.request.RequestConstant;
import com.open.push.service.PushRequest;
import com.open.push.biz.request.RequestException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class PushRequestCondition {

  protected void checkUpdatePushRequestCondition(final PushRequest innerRecord, final String jobId) {

    //checkUpdatePushRequestCondition if job id is legal.
    if (null == innerRecord) {

      StringBuilder err = new StringBuilder();
      err.append(RequestConstant.JOB_ID_IS_ILLEGAL)
          .append("job ID: ")
          .append(jobId);
      log.warn(err.toString());

      throw new RequestException(err.toString());
    }

    //checkUpdatePushRequestCondition if job has been in process.
    final String status = innerRecord.getStatus();
    if (0 == StringUtils.compare(status, PushRequestStatus.INGRESS.name())) {

      StringBuilder err = new StringBuilder();
      err.append(RequestConstant.JOB_ID_IS_ALREADY_IN_PROCESS)
          .append("job ID: ")
          .append(jobId);
      log.warn(err.toString());

      throw new RequestException(err.toString());
    }
  }
}
