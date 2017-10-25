package com.open.push.dao;

import com.open.push.dao.po.RequestDetailPo;
import org.springframework.data.repository.CrudRepository;

public interface RequestDetailRepository extends CrudRepository<RequestDetailPo, Long> {

  RequestDetailPo findTopByJobIdAndStatusOrderById(String jobId, String status);
}
