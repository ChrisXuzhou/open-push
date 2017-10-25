package com.open.push.biz.task;

import com.open.push.ConfigHelper;
import com.open.push.Helper;
import com.open.push.ThreadPoolHelper;
import com.open.push.biz.request.PushCriteria;
import com.open.push.biz.request.PushRequestDetailStatus;
import com.open.push.biz.request.PushRequestStatus;
import com.open.push.biz.task.bean.ParseTask;
import com.open.push.service.PushRequest;
import com.open.push.service.PushRequestDetail;
import com.open.push.service.PushRequestDetailService;
import com.open.push.service.PushRequestService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TaskServiceImpl implements TaskService {

  private RequestParser<ParseTask> byUserIdRequestParser;
  private RequestParser<ParseTask> globalRequestParser;
  private RequestParser<ParseTask> mcRequestParser;

  private PushRequestService modelPushRequestService;
  private PushRequestDetailService modelPushRequestDetailService;

  public TaskServiceImpl(
      @Qualifier("byUserIdRequestParser") RequestParser<ParseTask> byUserIdRequestParser,
      @Qualifier("globalRequestParser") RequestParser<ParseTask> globalRequestParser,
      @Qualifier("byMcRequestParser") RequestParser<ParseTask> mcRequestParser,
      PushRequestService modelPushRequestService,
      PushRequestDetailService modelPushRequestDetailService) {
    this.byUserIdRequestParser = byUserIdRequestParser;
    this.globalRequestParser = globalRequestParser;
    this.mcRequestParser = mcRequestParser;
    this.modelPushRequestService = modelPushRequestService;
    this.modelPushRequestDetailService = modelPushRequestDetailService;
  }

  @Override
  public void fire(PushRequest pushRequest) {
    parse(pushRequest.getJobId());
  }

  @Override
  public void parse(final String jobId) {
    final PushRequest pushRequest = getConfirmedPushRequest(jobId);
    if (null == pushRequest) {
      return;
    }
    final String criteria = pushRequest.getCriteria();

    switch (PushCriteria.valueOf(criteria)) {
      case BY_USER_ID:
        ParseStrategies.PARSE_WITH_DETAIL
            .parse(pushRequest, byUserIdRequestParser, modelPushRequestDetailService);
        break;
      case GLOBAL:
        ParseStrategies.PARSE_WITHOUT_DETAIL
            .parse(pushRequest, globalRequestParser, modelPushRequestDetailService);
        break;
      case BY_MC:
        ParseStrategies.PARSE_WITH_DETAIL
            .parse(pushRequest, mcRequestParser, modelPushRequestDetailService);
        break;
      default:
        break;
    }
  }


  private PushRequest getConfirmedPushRequest(final String jobId) {
    final String updatedStatus = PushRequestStatus.INGRESS.name();

    PushRequest pushRequest;
    for (; ; ) {
      try {
        pushRequest = modelPushRequestService.getBy(jobId);
        if (null == pushRequest
            || !StringUtils.equals(
            PushRequestStatus.CONFIRMED.name(), pushRequest.getStatus())) {
          return null;
        }

        pushRequest.setStatus(updatedStatus);
        modelPushRequestService.save(pushRequest);
        break;
      } catch (ObjectOptimisticLockingFailureException e) {
        log.warn("Multiple thread fetch one message pushRequest at same time. {}", e);
      }
    }
    return pushRequest;
  }


  enum ParseStrategies implements ParseStrategy {

    PARSE_WITH_DETAIL {
      @Override
      public void parse(
          PushRequest pushRequest,
          RequestParser<ParseTask> parser,
          PushRequestDetailService modelPushRequestDetailService) {

        final String jobId = pushRequest.getJobId();

        for (; ; ) {
          PushRequestDetail detail =
              getNextDetail(jobId, modelPushRequestDetailService);
          if (null == detail) {
            break;
          }

          final String appNamesInString = pushRequest.getAppNames();
          final List<String> appNames = Helper.parseList(appNamesInString);

          for (String appName : appNames) {
            final String topic = ConfigHelper.getTopic(appName);

            if (StringUtils.isEmpty(topic)) {
              log.warn("unregistered app name input. {}", appName);
              continue;
            }

            Unsafe.submit(
                new ParseTask(pushRequest, detail, appName, topic, parser));
          }
        }

      }

      private PushRequestDetail getNextDetail(
          final String jobId,
          final PushRequestDetailService modelPushRequestDetailService) {
        PushRequestDetail requestDetail;
        final String status = PushRequestDetailStatus.CREATED.name();
        final String updatedStatus = PushRequestDetailStatus.INGRESS.name();

        for (; ; ) {
          try {
            requestDetail = modelPushRequestDetailService.getNextRequestDetail(jobId, status);
            if (null == requestDetail) {
              break;
            }
            requestDetail.setStatus(updatedStatus);
            modelPushRequestDetailService.save(requestDetail);
            break;
          } catch (ObjectOptimisticLockingFailureException e) {
            log.warn(
                "Multiple thread fetch one message bulk pushRequest pushRequestDetail at same time.");
          }
        }
        return requestDetail;
      }
    },
    PARSE_WITHOUT_DETAIL {
      @Override
      public void parse(
          PushRequest pushRequest,
          RequestParser<ParseTask> parser,
          PushRequestDetailService modelPushRequestDetailService) {

        final String raw = pushRequest.getAppNames();
        final List<String> appNames = Helper.parseList(raw);

        for (String appName : appNames) {

          final String topic = ConfigHelper.getTopic(appName);
          if (StringUtils.isEmpty(topic)) {
            log.warn("could not find topic for {}", appName);
            continue;
          }

          Unsafe.submit(
              new ParseTask(pushRequest, new PushRequestDetail(), appName, topic, parser));
        }

      }
    }
  }

  static class Unsafe {

    static void submit(final ParseTask parseTask) {
      RequestParser<ParseTask> requestParser = parseTask.getRequestParser();

      for (; ; ) {
        try {
          ThreadPoolHelper.submitTask(() -> requestParser.parse(parseTask));
          break;
        } catch (Exception e) {
          log.warn("too busy.", e);
          Helper.sleep(1000);
        }
      }
    }

  }

}
