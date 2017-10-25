package com.open.push.service;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class PushRequestDetail {

  private String id;

  @NonNull
  private String jobId;
  @NonNull
  private String status;

  @NonNull
  private String criteria;
  @NonNull
  private String criteriaDescription;

  private Integer version;
}
