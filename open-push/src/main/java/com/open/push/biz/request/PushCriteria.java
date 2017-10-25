package com.open.push.biz.request;

/**
 * <p>发送push的定制条件，如以版本、全局发送</p>
 */
public enum PushCriteria {

  GLOBAL, //全局发送
  BY_USER_ID,
  CUSTOMIZE, //定制化
  BY_MC,

}
