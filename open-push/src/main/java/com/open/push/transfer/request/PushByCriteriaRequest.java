package com.open.push.transfer.request;

import com.open.push.biz.request.PushCriteria;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Value;

/**
 * <p>来自业务app的push请求.</p>
 * 1、消息的标题
 * 2、消息的摘要（非必填）
 * 3、消息的Payload_Json
 * 4、推送的时间点 Datetime 如：2017-05-12 19：00
 * 5、推送的批次（非必填）
 * 6、推送的APP （具体推送的目标APP，如丁香园App、丁香医生App、医学时间 App等）
 * 7、推送的范围
 */
@Value
public class PushByCriteriaRequest {

  @Size(min = 1, max = 36)
  private String jobId;


  //notification information.
  @NotNull
  @Size(min = 1, max = 16)
  private String title;
  @NotNull
  @Size(min = 1, max = 4000)
  private String payload;
  @Size(min = 1, max = 128)
  private String description;
  @Size(min = 1, max = 36)
  private String messageId;
  @Size(min = 1, max = 48)
  private String messageType;


  //pushRequest range criteria
  @NotNull
  private PushCriteria criteria;
  @Size(min = 1, max = 200)
  private String criteriaDescription;
  @NotNull
  @Size(min = 1, max = 200)
  private String appNames;

  private Long timeToSend;

}
