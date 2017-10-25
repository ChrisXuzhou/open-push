package com.open.push.service.impl;

import com.open.push.dao.UserJdbcRepository;
import com.open.push.dao.UserRepository;
import com.open.push.dao.po.UserPo;
import com.open.push.service.User;
import com.open.push.service.UserService;
import com.open.push.support.DozerHelper;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>提供User相关查询、更新、插入等实现.</p>
 */
@Transactional
@Service
@Slf4j
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserJdbcRepository userJdbcRepository;

  private final DozerBeanMapper dozerBeanMapper;

  @Autowired
  public UserServiceImpl(final UserRepository repository, final UserJdbcRepository userJdbcRepository) {
    this.userRepository = repository;
    this.userJdbcRepository = userJdbcRepository;
    this.dozerBeanMapper = new DozerBeanMapper();
  }

  @Override
  public User getBy(final String userId, final String appName, final String deviceType) {

    UserPo po = null;
    try {
      po = userRepository.findByUserIdAndAppNameAndDeviceType(userId, appName, deviceType);
    } catch (Exception e) {
      log.error("SYSTEM ERROR: getBit User By user messageId and device type [{}]", e);
    }

    final User user = new User();
    if (null != po) {
      dozerBeanMapper.map(po, user);
      return user;
    }
    return null;
  }

  @Override
  public List<User> getListBy(final String deviceToken, final String userId, final String appName,
      final String deviceType) {

    List<UserPo> pos = userRepository.findBy(deviceToken, userId, appName, deviceType);
    return DozerHelper.map(dozerBeanMapper, pos, User.class);
  }

  @Override
  public List<User> getListBy(final String deviceToken, final String userId, final String appName,
      final String deviceType, final String deviceMc) {

    List<UserPo> pos = userJdbcRepository.findBy(deviceToken, userId, appName, deviceType, deviceMc);
    return DozerHelper.map(dozerBeanMapper, pos, User.class);
  }

  @Override
  public List<User> getListBy(final List<String> userIds, final String appName) {
    List<UserPo> pos = userRepository.findByUserIdInAndAndAppName(userIds, appName);
    if (CollectionUtils.isNotEmpty(pos)) {
      return DozerHelper.map(pos, User.class);
    }

    return null;
  }


  @Override
  public List<User> getListByMcsAndAppName(final List<String> mcs, final String appName) {
    List<UserPo> pos = userRepository.findByDeviceMcInAndAppName(mcs, appName);
    if (CollectionUtils.isNotEmpty(pos)) {
      return DozerHelper.map(pos, User.class);
    }

    return null;
  }

  @Override
  public List<User> getListBy(final Integer startId, final Integer endId, final String appName) {
    List<UserPo> pos = userRepository
        .findByIdGreaterThanAndIdLessThanAndAndAppName(startId, endId, appName);
    if (CollectionUtils.isNotEmpty(pos)) {
      return DozerHelper.map(pos, User.class);
    }

    return null;
  }

  @Override
  public User getLastUserOrderByIdBy(final String appName) {

    UserPo po = null;
    try {
      po = userRepository.findTopByAppNameOrderByIdDesc(appName);
    } catch (Exception e) {
      log.error("SYSTEM ERROR: getBit User By app name [{}]", appName, e);
    }

    if (null != po) {
      return DozerHelper.map(po, User.class);
    }
    return null;
  }

  @Override
  public void save(final User user) {
    if (null != user) {
      UserPo po = new UserPo();
      dozerBeanMapper.map(user, po);
      userRepository.save(po);
    }
  }

  @Override
  public void delete(final User user) {
    if (null != user) {
      UserPo po = new UserPo();
      dozerBeanMapper.map(user, po);
      userRepository.delete(po);

    }
  }


}
