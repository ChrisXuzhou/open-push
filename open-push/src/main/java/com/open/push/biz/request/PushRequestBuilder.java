package com.open.push.biz.request;


import com.open.push.service.PushRequest;
import java.util.Date;

public class PushRequestBuilder {

  private static final String EMPTY_STRING = "";

  //pushRequest information
  private String jobId = EMPTY_STRING;
  private String status = PushRequestStatus.CREATED.name();

  //app information
  private String appNames;

  //notification information
  private String title;
  private String payload;
  private String description = EMPTY_STRING;
  private String messageId = EMPTY_STRING;
  private String messageType = EMPTY_STRING;


  //pushRequest range information
  private String criteria;
  private String criteriaDescription = EMPTY_STRING;

  private Date pushTime = new Date();


  public PushRequestBuilder jobId(String jobId) {
    this.jobId = jobId;
    return this;
  }

  public PushRequestBuilder appNames(String appNames) {
    this.appNames = appNames;
    return this;
  }

  public PushRequestBuilder title(String title) {
    this.title = title;
    return this;
  }

  public PushRequestBuilder payload(String payload) {
    this.payload = payload;
    return this;
  }

  public PushRequestBuilder description(String description) {
    this.description = description;
    return this;
  }

  public PushRequestBuilder messageId(String messageId) {
    this.messageId = messageId;
    return this;
  }


  public PushRequestBuilder messageType(String messageType) {
    this.messageType = messageType;
    return this;
  }


  public PushRequestBuilder criteria(String criteria) {
    this.criteria = criteria;
    return this;
  }

  public PushRequestBuilder criteriaDescription(String criteriaDescription) {
    this.criteriaDescription = criteriaDescription;
    return this;
  }

  public PushRequestBuilder pushTime(Date pushTime) {
    this.pushTime = pushTime;
    return this;
  }


  public PushRequest build() {
    return new PushRequest(jobId, status, appNames, title, payload, description,
        messageId, messageType, criteria, criteriaDescription, pushTime);
  }
}
