package com.open.push.biz.task.parse;


import com.open.push.Helper;
import com.open.push.biz.request.PushRequestDetailStatus;
import com.open.push.biz.task.bean.ParseTask;
import com.open.push.biz.task.bean.PushCondition;
import com.open.push.biz.task.bean.RequestParseRecord;
import com.open.push.service.PushPriority;
import com.open.push.service.PushRequest;
import com.open.push.service.PushRequestDetail;
import com.open.push.service.User;
import com.open.push.support.DozerHelper;
import com.open.push.biz.NotificationPackage;
import com.open.push.transfer.token.DeviceTokenType;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

class ParserHelper {

  static boolean isPushRequestDetailNotProcessed(PushRequestDetail detail) {
    final String status = detail.getStatus();
    return StringUtils.equals(status, PushRequestDetailStatus.INGRESS.name());
  }

  static NotificationPackage buildNotificationTemplate(ParseTask requestParseContext) {

    final PushRequest request = requestParseContext.getPushRequest();
    final NotificationPackage template = DozerHelper.map(request, NotificationPackage.class);

    template.setChannelCode(requestParseContext.getAppName());
    template.setTopic(requestParseContext.getTopic());

    final String priority = parsePriority(request);
    template.setPriority(priority);

    return template;
  }


  private static String parsePriority(PushRequest pushRequest) {
    String priority = null;
    final String criteriaDescription = pushRequest.getCriteriaDescription();

    if (StringUtils.isNotEmpty(criteriaDescription)) {
      final Map<String, String> properties = Helper.Json2Map(criteriaDescription);
      priority = properties.get("priority");
    }

    if (StringUtils.isEmpty(priority)) {
      return PushPriority.NORMAL.name();
    }

    return priority;
  }


  public static boolean isConditionSatisfied(final User user, final RequestContext context) {
    final PushCondition pushCondition = context.getPushCondition();
    final RequestParseRecord parseRecord = context.getRequestParseRecord();

    /*
     //   频率过滤

    if (!frequencyCheck(user, context.getChecker())) {
      parseRecord.frequencyFiltered(parseRecord.getFrequencyFiltered() + 1);
      return false;
    }
    */

    if (null == pushCondition) {
      return true;
    }

    if (StringUtils.isNotEmpty(pushCondition.getPlatform())) {

      DeviceTokenType deviceTokenType = DeviceTokenType.of(user.getDeviceTokenType());
      if (null != deviceTokenType &&
          !StringUtils.equals(deviceTokenType.getDevicePlatform().name(),
              pushCondition.getPlatform())) {
        parseRecord.platformNotMatched(parseRecord.getPlatformNotMatched() + 1);
        return false;
      }
    }

    if (StringUtils.isNotEmpty(pushCondition.getAppVersion())) {
      final String appVersions = pushCondition.getAppVersion();
      if (!StringUtils.contains(appVersions, user.getAppVersion())) {
        parseRecord.appVersionNotMatched(parseRecord.getAppVersionNotMatched() + 1);
        return false;
      }
    }

    if (StringUtils.isNotEmpty(pushCondition.getDeviceTypes())) {
      final String deviceTypes = pushCondition.getDeviceTypes();
      if (!StringUtils.contains(deviceTypes, user.getDeviceType())) {
        parseRecord.deviceTypesNotMatched(parseRecord.getDeviceTypesNotMatched() + 1);
        return false;
      }
    }

    return true;
  }
}
