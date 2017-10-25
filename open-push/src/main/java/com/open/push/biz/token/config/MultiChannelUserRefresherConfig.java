package com.open.push.biz.token.config;

import com.open.push.biz.token.DefaultUserRefresher;
import com.open.push.biz.token.RefreshRequest;
import com.open.push.biz.token.UserProcessor;
import com.open.push.biz.token.UserRefresher;
import com.open.push.biz.token.processor.multichannel.MultiChannelBeginProcessor;
import com.open.push.biz.token.processor.multichannel.MultiChannelEndProcessor;
import com.open.push.biz.token.processor.multichannel.MultiChannelSize0Processor;
import com.open.push.biz.token.processor.multichannel.MultiChannelSize1Processor;
import com.open.push.biz.token.processor.multichannel.MultiChannelSize2Processor;
import com.open.push.biz.token.processor.multichannel.MultiChannelSize3Processor;
import com.open.push.service.UserService;
import com.open.push.biz.token.processor.multichannel.MultiChannelSizeMoreProcessor;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>MultiChannelUserRefresher</p>
 */
@Configuration
public class MultiChannelUserRefresherConfig {

  @Bean
  public UserRefresher<RefreshRequest> getMultiChannelUserRefresher(UserService userService) {
    DefaultUserRefresher refresher = new DefaultUserRefresher();

    List<UserProcessor<RefreshRequest>> processors = new ArrayList<>();
    processors.add(new MultiChannelBeginProcessor(userService));
    addUserProcessors(processors);
    processors.add(new MultiChannelEndProcessor(userService));
    refresher.setProcessors(processors);

    return refresher;
  }

  private void addUserProcessors(List<UserProcessor<RefreshRequest>> processors) {

    processors.add(new MultiChannelSize0Processor());
    processors.add(new MultiChannelSize1Processor());
    processors.add(new MultiChannelSize2Processor());
    processors.add(new MultiChannelSize3Processor());
    processors.add(new MultiChannelSizeMoreProcessor());
    //TODO 装配processor
  }
}
