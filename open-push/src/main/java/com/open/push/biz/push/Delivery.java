package com.open.push.biz.push;

import com.open.push.LifeCycle;

public interface Delivery<T> extends LifeCycle {

  /**
   * <p>每个deliver与App的关系是1：1</p>
   *
   * <p>即依据channel code 找到对应app的delivery，调用handle方法，推送消息.</p>
   *
   * <p>Delivery 分为immediate和buffered两种类型.</p>
   */
  void handle(T unit);

}
