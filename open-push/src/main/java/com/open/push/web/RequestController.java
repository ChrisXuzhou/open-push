package com.open.push.web;

import com.open.push.transfer.request.ModifyPushRequestStatusRequest;
import com.open.push.transfer.request.PushByCriteriaRequest;
import com.open.push.transfer.request.PushByCriteriaRequestDetail;
import com.open.push.transfer.request.PushByCriteriaService;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/request/")
public class RequestController {

  private PushByCriteriaService pushByCriteriaService;

  public RequestController(final PushByCriteriaService pushByCriteriaService) {
    this.pushByCriteriaService = pushByCriteriaService;
  }

  @RequestMapping(value = "/create", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  public Vo submitPushRequest(@RequestBody @Valid PushByCriteriaRequest request) {
    log.debug("push request {}", ReflectionToStringBuilder.toString(request));
    String jobId = pushByCriteriaService.handleRequest(request);
    Vo vo = new Vo();
    vo.put("jobId", jobId);
    return vo;
  }

  @RequestMapping(value = "/add", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  public Vo submitPushRequestDetail(
      @RequestBody @Valid PushByCriteriaRequestDetail request) {
    log.debug("push request detail {}",
        ReflectionToStringBuilder.toString(request));

    pushByCriteriaService.handleRequestDetail(request);
    return new Vo();
  }

  @RequestMapping(value = "/confirm", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  public Vo confirmPushRequest(
      @RequestBody @Valid ModifyPushRequestStatusRequest request) {
    log.debug("confirm push request, job Id: {}", request.getJobId());

    pushByCriteriaService.confirmRequest(request);
    return new Vo();
  }

  @RequestMapping(value = "/cancel", method = RequestMethod.POST)
  @ResponseStatus(HttpStatus.OK)
  public Vo cancelPushRequest(
      @RequestBody @Valid ModifyPushRequestStatusRequest request) {
    log.debug("cancel push request, job Id: {}", request.getJobId());

    pushByCriteriaService.cancelRequest(request);
    return new Vo();
  }


}
