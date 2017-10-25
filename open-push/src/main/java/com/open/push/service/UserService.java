package com.open.push.service;

import java.util.List;

public interface UserService {

  User getBy(final String userId, final String appName, final String deviceType);

  List<User> getListBy(String deviceToken, String userId, String appName,
      String deviceType, String deviceMc);

  List<User> getListBy(final List<String> userIds, final String appName);

  List<User> getListBy(final String deviceToken, final String userId, final String appName,
      final String deviceType);

  List<User> getListByMcsAndAppName(List<String> mcs, String appName);

  List<User> getListBy(Integer startId, Integer endId, String appName);

  User getLastUserOrderByIdBy(String appName);

  void save(final User user);

  void delete(final User user);

}
