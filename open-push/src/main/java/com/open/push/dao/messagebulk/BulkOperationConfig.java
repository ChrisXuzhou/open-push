package com.open.push.dao.messagebulk;

import javax.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BulkOperationConfig {

  @Bean
  public FlushingBulkOperations getFlushingBulkOperations(EntityManager em) {
    return new FlushingBulkOperations(em, getBatchSize());
  }

  public int getBatchSize() {
    return 3000;
  }
}
