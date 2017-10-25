package com.open.push.biz.push;

public interface NotificationValidator<T> {

  /**
   * <p>校验推送消息是否合法.</p>
   */
  void validate(T obj);
}
