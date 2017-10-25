package com.open.push.dao;

import com.open.push.dao.po.MessagePo;
import java.util.Date;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface MessageRepository extends CrudRepository<MessagePo, Long> {

  List<MessagePo> getByTraceId(final Long traceId);

  MessagePo getByTraceIdAndUserId(final Long traceId, final String userId);

  @Transactional
  Long deleteByIdBetween(final Integer begin, final Integer end);

  MessagePo findTopBySendTimeBeforeOrderById(final Date expiryDate);

  MessagePo findTopBySendTimeBeforeOrderByIdDesc(final Date expiryDate);

}
