package com.open.push.dao.po;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "push_message")
public class MessagePo extends BasePo {

  @NonNull
  private Long traceId;
  @NonNull
  @Column(length = 32)
  private String devicePlatform;
  @NonNull
  @Column(length = 180)
  private String deviceToken;
  @NonNull
  @Column(length = 32)
  private String status;
  @NonNull
  @Column(length = 40)
  private String userId;
  @NonNull
  @Column(length = 50)
  private String appVersion;
  @NonNull
  @Column(length = 32)
  private String osVersion;
  @NonNull
  @Column(length = 36)
  private String jobId;
  @NonNull
  @Column
  private Date sendTime;
  @NonNull
  @Column
  private Date clickTime;
}
