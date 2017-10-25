package com.open.push.channel.upush;

public enum DisplayType {

  NOTIFICATION {
    public String getValue() {
      return "notification";
    }
  },///通知:消息送达到用户设备后，由友盟SDK接管处理并在通知栏上显示通知内容。
  MESSAGE {
    public String getValue() {
      return "message";
    }
  };///消息:消息送达到用户设备后，消息内容透传给应用自身进行解析处理。

  public abstract String getValue();
}
