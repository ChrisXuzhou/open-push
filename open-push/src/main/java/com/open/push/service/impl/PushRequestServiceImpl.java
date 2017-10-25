package com.open.push.service.impl;

import com.open.push.support.DozerHelper;
import com.open.push.service.PushRequest;
import com.open.push.service.PushRequestService;
import com.open.push.dao.po.RequestPo;
import com.open.push.dao.RequestRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PushRequestServiceImpl implements PushRequestService {

  private final RequestRepository requestRepository;

  public PushRequestServiceImpl(RequestRepository requestRepository) {
    this.requestRepository = requestRepository;
  }

  @Override
  public PushRequest getBy(String jobId) {
    RequestPo po = null;
    if (StringUtils.isNotEmpty(jobId)) {
      po = requestRepository.findByJobId(jobId);
    }

    if (null != po) {
      return DozerHelper.map(po, PushRequest.class);
    }

    return null;
  }

  @Override
  public void save(PushRequest request) {

    final RequestPo po = DozerHelper.map(request, RequestPo.class);
    requestRepository.save(po);
  }
}
