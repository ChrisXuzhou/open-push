package com.open.push.dao.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * <p>Entity of TABLE:Users.</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "push_user")
public class UserPo extends BasePo {

  @Column(length = 100)
  private String userId;

  @NonNull
  @Column(length = 30)
  private String appName;

  @NonNull
  @Column(length = 60)
  private String appCode;

  @NonNull
  @Column(length = 50)
  private String appVersion;

  @NonNull
  @Column(length = 100)
  private String osVersion;

  @NonNull
  @Column(length = 32)
  private String deviceType;

  @NonNull
  @Column(length = 200)
  private String deviceMc;

  @NonNull
  @Column(length = 32)
  private String deviceTokenType;

  @NonNull
  @Column(length = 200)
  private String deviceToken;

  @Version
  private Integer version;


}
