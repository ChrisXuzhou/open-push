package com.open.push.biz.push;

public interface Buffered {

  /**
   * <p>配合Delivery使用, 当是Buffered Delivery时，需要周期更新buffer cache，保证消息推送的及时性.</p>
   */
  void refresh();
}
