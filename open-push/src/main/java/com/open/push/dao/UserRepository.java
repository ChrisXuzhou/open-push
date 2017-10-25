package com.open.push.dao;

import com.open.push.dao.po.UserPo;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * UserPo Repository 对应表USERS的操作.
 */
public interface UserRepository extends CrudRepository<UserPo, Long> {

  UserPo findByUserIdAndAppNameAndDeviceType(String userId, String appName, String deviceType);

  @Deprecated
  @Query("select u from UserPo u where (u.deviceToken = :deviceToken) or (u.userId = :userId and u.appName = :appName and u.deviceType = :deviceType)")
  List<UserPo> findBy(
      @Param("deviceToken") String deviceToken,
      @Param("userId") String userId,
      @Param("appName") String appName,
      @Param("deviceType") String deviceType);

  @Deprecated
  @Query("select u from UserPo u where (u.deviceToken = :deviceToken) or (u.userId = :userId and u.appName = :appName and u.deviceType = :deviceType) or (u.deviceMc = :deviceMc and u.appName = :appName)")
  List<UserPo> findBy(
      @Param("deviceToken") String deviceToken,
      @Param("userId") String userId,
      @Param("appName") String appName,
      @Param("deviceType") String deviceType,
      @Param("deviceMc") String deviceMc);


  List<UserPo> findByUserIdInAndAndAppName(List<String> userIds, String appName);

  List<UserPo> findByDeviceMcInAndAppName(List<String> deviceMcs, String appName);

  List<UserPo> findByIdGreaterThanAndIdLessThanAndAndAppName(Integer startId, Integer endId,
      String appName);

  UserPo findTopByAppNameOrderByIdDesc(String appName);

  @Transactional
  void deleteUserPoByDeviceTokenIn(List<String> deviceTokens);

}
