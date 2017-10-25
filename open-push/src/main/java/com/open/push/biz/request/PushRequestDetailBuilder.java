package com.open.push.biz.request;

import com.open.push.service.PushRequestDetail;

public class PushRequestDetailBuilder {

  private String jobId;
  private String status = PushRequestDetailStatus.CREATED.name();

  private String criteria;
  private String criteriaDescription;

  public PushRequestDetailBuilder jobId(String jobId) {
    this.jobId = jobId;
    return this;
  }

  public PushRequestDetailBuilder criteria(String criteria) {
    this.criteria = criteria;
    return this;
  }

  public PushRequestDetailBuilder criteriaDescription(String criteriaDescription) {
    this.criteriaDescription = criteriaDescription;
    return this;
  }

  public PushRequestDetail build() {
    return new PushRequestDetail(jobId, status, criteria, criteriaDescription);
  }
}
