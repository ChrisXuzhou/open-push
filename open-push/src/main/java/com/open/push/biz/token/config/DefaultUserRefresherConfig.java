package com.open.push.biz.token.config;

import com.open.push.biz.token.RefreshRequest;
import com.open.push.biz.token.UserProcessor;
import com.open.push.biz.token.processor.DefaultBeginProcessor;
import com.open.push.biz.token.processor.DefaultDuplicateUserInfoProcessor;
import com.open.push.biz.token.processor.DefaultInsertTokenProcessor;
import com.open.push.biz.token.processor.DefaultUpdateTokenProcessor;
import com.open.push.biz.token.processor.DefaultUpdateUserInfoProcessor;
import com.open.push.biz.token.DefaultUserRefresher;
import com.open.push.biz.token.UserRefresher;
import com.open.push.biz.token.processor.DefaultEndProcessor;
import com.open.push.biz.token.processor.DefaultExceptionalProcessor;
import com.open.push.service.UserService;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>装配DefaultUserRefresher</p>
 */
//@Configuration
public class DefaultUserRefresherConfig {

  //@Bean
  public UserRefresher<RefreshRequest> getDefaultUserRefresher(UserService userService) {
    DefaultUserRefresher refresher = new DefaultUserRefresher();

    List<UserProcessor<RefreshRequest>> processors = new ArrayList<>();
    processors.add(new DefaultBeginProcessor(userService));
    addUserProcessors(processors);
    processors.add(new DefaultEndProcessor(userService));
    refresher.setProcessors(processors);

    return refresher;
  }

  private void addUserProcessors(List<UserProcessor<RefreshRequest>> processors) {

    processors.add(new DefaultInsertTokenProcessor());
    processors.add(new DefaultUpdateTokenProcessor());
    processors.add(new DefaultUpdateUserInfoProcessor());
    processors.add(new DefaultDuplicateUserInfoProcessor());
    processors.add(new DefaultExceptionalProcessor());
    //TODO 装配processor
  }


}
