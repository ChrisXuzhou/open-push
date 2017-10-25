package com.open.push.service;

import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class PushRequest {

  private Long id;

  //pushRequest information
  @NonNull
  private String jobId;
  @NonNull
  private String status;


  //app information
  @NonNull
  private String appNames;


  //notification information
  @NonNull
  private String title;
  @NonNull
  private String payload;
  @NonNull
  private String description;
  @NonNull
  private String messageId;
  @NonNull
  private String messageType;


  //pushRequest range information
  @NonNull
  private String criteria;
  @NonNull
  private String criteriaDescription;
  @NonNull
  private Date pushTime;



  private Integer version;

}
