package com.open.push.dao.po;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "push_request")
public class RequestPo extends BasePo {

  @Column(length = 36)
  private String jobId;

  @Column(length = 32)
  private String status;

  @Column(length = 200)
  private String appNames;

  @Column(length = 16)
  private String title;

  @Column(length = 4000)
  private String payload;

  @Column(length = 128)
  private String description;

  @Column(length = 36)
  private String messageId;

  @Column(length = 48)
  private String messageType;

  @Column(length = 32)
  private String criteria;

  @Column(length = 200)
  private String criteriaDescription;

  @Column
  private Date pushTime;

  @Version
  private Integer version;
}
