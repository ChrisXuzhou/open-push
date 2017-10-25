package com.open.push.dao.po;

import java.util.Date;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
@MappedSuperclass
public class BasePo {

  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;

  private Date createTime;

  private Date modifyTime;
}
