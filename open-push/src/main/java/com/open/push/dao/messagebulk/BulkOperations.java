package com.open.push.dao.messagebulk;

import com.open.push.dao.po.MessagePo;
import java.util.List;

public interface BulkOperations {

  void bulkPersist(List<MessagePo> entities);

}
