package com.open.push.dao;

import com.open.push.dao.po.RequestPo;
import java.util.Date;
import org.springframework.data.repository.CrudRepository;

public interface RequestRepository extends CrudRepository<RequestPo, Long> {

  RequestPo findByJobId(String jobId);

  RequestPo findTopByStatusAndPushTimeBeforeOrderById(String status, Date time);
}
