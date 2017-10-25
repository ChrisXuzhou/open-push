package com.open.push.dao.po;

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
@Table(name = "push_request_detail")
public class RequestDetailPo extends BasePo {


  @Column(length = 36)
  private String jobId;

  @Column(length = 32)
  private String status;


  @Column(length = 32)
  private String criteria;

  @Column
  private String criteriaDescription;


  @Version
  private Integer version;
}
