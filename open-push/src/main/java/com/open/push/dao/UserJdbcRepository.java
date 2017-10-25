package com.open.push.dao;

import com.open.push.dao.po.UserPo;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserJdbcRepository {

  private final EntityManager em;

  public UserJdbcRepository(EntityManager em) {
    this.em = em;
  }

  @Transactional
  public List<UserPo> findBy(
      String deviceToken, String userId, String appName, String deviceType, String deviceMc) {

    final List<UserPo> userPos = new LinkedList<>();

    try {

      final Session session = em.unwrap(Session.class);
      session.doWork(connection -> {

        final String sql = assembleSql();
        PreparedStatement statement = connection.
            prepareStatement(sql);

        statement.setString(1, deviceToken);
        statement.setString(2, userId);
        statement.setString(3, appName);
        statement.setString(4, deviceType);
        statement.setString(5, deviceMc);
        statement.setString(6, appName);

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
          UserPo po = new UserPo();
          po.setId(resultSet.getLong("id"));
          po.setAppCode(resultSet.getString("app_code"));
          po.setAppName(resultSet.getString("app_name"));
          po.setAppVersion(resultSet.getString("app_version"));
          po.setDeviceMc(resultSet.getString("device_vc"));
          po.setDeviceToken(resultSet.getString("device_token"));
          po.setDeviceTokenType(resultSet.getString("device_token_type"));
          po.setDeviceType(resultSet.getString("device_type"));
          po.setOsVersion(resultSet.getString("os_version"));
          po.setUserId(resultSet.getString("user_id"));
          po.setVersion(resultSet.getInt("version"));
          po.setCreateTime(resultSet.getTime("create_time"));

          userPos.add(po);
        }
        resultSet.close();
        statement.close();

      });

    } catch (Exception e) {
      log.error("exception thrown.", e);

    }


    return userPos;
  }

  String assembleSql() {
    return baseSql + tokenSql + " union " +
        baseSql + userIdAndAppNameAndDeviceType + " union " +
        baseSql + deviceMcAndAppName;

  }

  private String baseSql = "(select userpo.id as id, \n"
      + "userpo.create_time as createTime, \n"
      + "userpo.app_code as appCode, \n"
      + "userpo.app_name as appName, \n"
      + "userpo.app_version as appVersion, \n"
      + "userpo.device_mc as deviceMc, \n"
      + "userpo.device_token as deviceToken, \n"
      + "userpo.device_token_type as deviceTokenType, \n"
      + "userpo.device_type as deviceType, \n"
      + "userpo.os_version as osVersion, \n"
      + "userpo.user_id as userId, \n"
      + "userpo.version as version \n"
      + "from push_user userpo ";

  private String tokenSql = "where \n"
      + "userpo.device_token= ?)";

  private String userIdAndAppNameAndDeviceType = "where \n"
      + "userpo.user_id= ? and userpo.app_name= ? and userpo.device_type= ? )";


  private String deviceMcAndAppName = "where\n"
      + "userpo.device_mc= ? and userpo.app_name= ?)";

}
