package com.open.push.transfer.request;


import com.open.push.biz.request.PushCriteria;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Value;


/**
 * <p>来自业务app的push请求.</p>
 * 推送的范围由criteria、criteriaDescription描述
 */
@Value
@AllArgsConstructor
public class PushByCriteriaRequestDetail {

  @NotNull
  @Size(min = 1, max = 36)
  private String jobId;

  //pushRequest range criteria
  @NotNull
  private PushCriteria criteria;
  @NotNull
  @Size(min = 1)
  private String criteriaDescription;

}
