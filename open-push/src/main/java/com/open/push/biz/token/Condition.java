package com.open.push.biz.token;

/**
 * <p>作为逻辑单元的条件校验接口，见 DefaultInsertTokenProcessor</p>
 */
public interface Condition<T> {

  boolean check(T t);
}
