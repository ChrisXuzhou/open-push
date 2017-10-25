package com.open.push.service.impl;

import com.open.push.dao.RequestDetailRepository;
import com.open.push.service.PushRequestDetail;
import com.open.push.service.PushRequestDetailService;
import com.open.push.support.DozerHelper;
import com.open.push.dao.po.RequestDetailPo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PushRequestDetailServiceImpl implements PushRequestDetailService {

  private RequestDetailRepository repository;

  public PushRequestDetailServiceImpl(final RequestDetailRepository repository) {
    this.repository = repository;
  }

  @Override
  public PushRequestDetail getNextRequestDetail(final String jobId, final String status) {
    RequestDetailPo po = repository.findTopByJobIdAndStatusOrderById(jobId, status);

    if (null != po) {
      return DozerHelper.map(po, PushRequestDetail.class);
    }
    return null;
  }

  @Override
  public void save(PushRequestDetail detail) {
    final RequestDetailPo po = DozerHelper.map(detail, RequestDetailPo.class);
    repository.save(po);
  }

}
