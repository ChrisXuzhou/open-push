CREATE TABLE push_request (
  id                  INT UNSIGNED  NOT NULL AUTO_INCREMENT
  COMMENT '主键',
  jobId               VARCHAR(36)   NOT NULL DEFAULT ''
  COMMENT '请求标识',
  status              VARCHAR(32)   NOT NULL DEFAULT ''
  COMMENT '该请求的执行状态',
  appName             VARCHAR(200)  NOT NULL DEFAULT ''
  COMMENT '请求方key,标记请求方身份',
  title               VARCHAR(16)   NOT NULL DEFAULT ''
  COMMENT '消息标题',
  payload             VARCHAR(4000) NOT NULL DEFAULT ''
  COMMENT '消息的Payload_Json',
  description         VARCHAR(128)  NOT NULL DEFAULT ''
  COMMENT '消息摘要',

  messageId           VARCHAR(36)   NOT NULL DEFAULT ''
  COMMENT '消息标识',
  messageType         VARCHAR(48)   NOT NULL DEFAULT ''
  COMMENT '消息标识',

  criteria            VARCHAR(32)   NOT NULL DEFAULT ''
  COMMENT '发送范围标准，如全局发送',
  criteriaDescription VARCHAR(200)  NOT NULL DEFAULT ''
  COMMENT '发送范围描述,仅限于抽象条件描述',

  pushTime            DATETIME      NOT NULL DEFAULT current_timestamp
  COMMENT '批次发送时间',

  createTime          DATETIME      NOT NULL DEFAULT current_timestamp
  COMMENT '创建时间',
  modifyTime          DATETIME      NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp
  COMMENT '更新时间',
  version             INT           NOT NULL DEFAULT 0
  COMMENT 'version信息，为乐观锁更新准备',

  PRIMARY KEY (id),
  UNIQUE KEY uniq_jobId(jobId)
)
  ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COMMENT = '推送请求记录表';


CREATE TABLE push_request_detail (
  id                  INT UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '主键',
  jobId               VARCHAR(36)  NOT NULL DEFAULT ''
  COMMENT '请求标识',
  status              VARCHAR(32)  NOT NULL DEFAULT ''
  COMMENT '该请求的执行状态',
  criteria            VARCHAR(32)  NOT NULL DEFAULT ''
  COMMENT '发送范围标准，如全局发送',
  criteriaDescription MEDIUMTEXT
  COMMENT '发送范围描述,仅限于抽象条件描述',
  createTime          DATETIME     NOT NULL DEFAULT current_timestamp
  COMMENT '批次创建时间',
  modifyTime          DATETIME     NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp
  COMMENT '更新时间',
  version             INT          NOT NULL DEFAULT 0
  COMMENT 'version信息，为乐观锁更新准备',

  PRIMARY KEY (id),
  INDEX idx_jobId(jobId)
)
  ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COMMENT = '推送请求详情表';


CREATE TABLE push_message (
  id             BIGINT UNSIGNED NOT NULL AUTO_INCREMENT
  COMMENT '发送消息标识',
  traceId        BIGINT          NOT NULL DEFAULT 0
  COMMENT '回填id',
  devicePlatform VARCHAR(32)     NOT NULL DEFAULT ''
  COMMENT '平台类型',
  deviceToken    VARCHAR(180)    NOT NULL DEFAULT ''
  COMMENT '推送消息的路由标识',
  status         VARCHAR(32)     NOT NULL DEFAULT ''
  COMMENT '消息状态',
  userId         VARCHAR(40)     NOT NULL DEFAULT ''
  COMMENT '用户账户标识',
  appVersion     VARCHAR(50)     NOT NULL DEFAULT ''
  COMMENT '应用版本号',
  osVersion      VARCHAR(32)     NOT NULL DEFAULT ''
  COMMENT '系统版本号',
  jobId          VARCHAR(36)     NOT NULL DEFAULT ''
  COMMENT '请求标识',
  sendTime       DATETIME        NOT NULL DEFAULT current_timestamp
  COMMENT '消息失效时间',
  clickTime      DATETIME        NOT NULL DEFAULT current_timestamp
  COMMENT '消息失效时间',


  createTime     DATETIME        NOT NULL DEFAULT current_timestamp
  COMMENT '创建时间',
  modifyTime     DATETIME        NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp
  COMMENT '更新时间',
  PRIMARY KEY (id),
  INDEX idx_traceId_deviceToken(traceId)
)
  ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COMMENT = '推送日志表';
