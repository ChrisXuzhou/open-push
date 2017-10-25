DROP TABLE IF EXISTS push_user;

CREATE TABLE push_user
(
  id                INT AUTO_INCREMENT NOT NULL
  COMMENT '主键',
  user_id           CHAR(40)                    DEFAULT NULL
  COMMENT '用户账户标识',
  app_name          CHAR(30)           NOT NULL
  COMMENT '应用识别码',
  app_code          CHAR(60)           NOT NULL
  COMMENT '应用识别码',
  app_version       CHAR(50)           NOT NULL
  COMMENT '应用版本号',
  os_version        CHAR(32)           NOT NULL
  COMMENT '系统版本号',
  device_type       CHAR(32)           NOT NULL
  COMMENT '设备类型',
  device_mc         CHAR(200)          NOT NULL
  COMMENT '设备唯一标识',
  device_token_type CHAR(32)           NOT NULL
  COMMENT '平台类型',
  device_token      CHAR(180)          NOT NULL
  COMMENT '推送消息的路由标识',
  create_time       DATETIME                    DEFAULT current_timestamp,
  modify_time       DATETIME AS now(),
  version           INT                NOT NULL DEFAULT 0
  COMMENT 'version信息，为乐观锁更新准备',

  PRIMARY KEY (id),
  UNIQUE (user_id, app_name, device_type),
  UNIQUE (device_token)
);

CREATE TABLE push_request (
  id                   INT AUTO_INCREMENT NOT NULL
  COMMENT '主键',

  job_id               VARCHAR(36)        NOT NULL
  COMMENT '请求标识',
  status               VARCHAR(32)        NOT NULL
  COMMENT '该请求的执行状态',

  app_key              VARCHAR(36)        NOT NULL
  COMMENT '请求方Id',


  title                VARCHAR(16)        NOT NULL
  COMMENT '消息标题',
  payload              VARCHAR(4000)      NOT NULL
  COMMENT '消息的Payload_Json',
  description          VARCHAR(128)       NOT NULL
  COMMENT '消息摘要',

  message_id           VARCHAR(36)        NOT NULL
  COMMENT '消息标识',
  message_type         VARCHAR(48)        NOT NULL
  COMMENT '消息标识',

  criteria             VARCHAR(32)        NOT NULL
  COMMENT '发送范围标准，如全局发送',
  criteria_description VARCHAR(200)       NOT NULL
  COMMENT '发送范围描述,仅限于抽象条件描述',

  push_time            DATETIME           NOT NULL DEFAULT current_timestamp
  COMMENT '批次创建时间',

  create_time          DATETIME           NOT NULL DEFAULT current_timestamp
  COMMENT '批次创建时间',
  modify_time          DATETIME AS now(),

  version              INT                NOT NULL DEFAULT 0
  COMMENT 'version信息，为乐观锁更新准备',

  PRIMARY KEY (id),
  UNIQUE (job_id)
);


CREATE TABLE push_request_detail (
  id                   INT AUTO_INCREMENT NOT NULL
  COMMENT '主键',

  job_id               VARCHAR(36)        NOT NULL
  COMMENT '请求标识',
  status               VARCHAR(32)        NOT NULL
  COMMENT '该请求的执行状态',

  criteria             VARCHAR(32)        NOT NULL
  COMMENT '发送范围标准，如全局发送',
  criteria_description MEDIUMTEXT         NOT NULL
  COMMENT '发送范围描述,仅限于抽象条件描述',


  create_time          DATETIME           NOT NULL DEFAULT current_timestamp
  COMMENT '批次创建时间',
  modify_time          DATETIME AS now(),

  version              INT                NOT NULL DEFAULT 0
  COMMENT 'version信息，为乐观锁更新准备',

  PRIMARY KEY (id),
);


CREATE INDEX index_jobId
  ON push_request_detail (job_id);


CREATE TABLE push_unit (
  id                 BIGINT AUTO_INCREMENT NOT NULL
  COMMENT '主键',
  unit_status        VARCHAR(10)           NOT NULL
  COMMENT '该请求的执行状态',


  channel_code       CHAR(36)              NOT NULL
  COMMENT '平台类型',
  device_platform    CHAR(32)              NOT NULL
  COMMENT '平台类型',
  bulk_targets       MEDIUMBLOB            NOT NULL
  COMMENT '推送目标列表',


  job_id             VARCHAR(36)           NOT NULL
  COMMENT '请求标识',


  topic              VARCHAR(60)           NOT NULL
  COMMENT '发起请求的应用名称',
  title              VARCHAR(16)           NOT NULL
  COMMENT '消息标题',
  payload            VARCHAR(4000)         NOT NULL
  COMMENT '消息的Payload_Json',
  description        VARCHAR(128)          NOT NULL
  COMMENT '消息的摘要（非必填)',


  channel_message_id VARCHAR(36)           NOT NULL
  COMMENT '渠道方的消息标识',
  channel_trace_id   VARCHAR(36)           NOT NULL
  COMMENT '渠道方的消息追踪标识',

  resolved           INT                   NOT NULL DEFAULT 0
  COMMENT '计划推送消息数',
  recipients         INT                   NOT NULL DEFAULT 0
  COMMENT '送达消息数',
  clicks             INT                   NOT NULL DEFAULT 0
  COMMENT '消息点击数',


  create_time        DATETIME              NOT NULL DEFAULT current_timestamp
  COMMENT '批次创建时间',
  modify_time        DATETIME AS now(),

  version            INT                   NOT NULL DEFAULT 0
  COMMENT 'version信息，为乐观锁更新准备',

  PRIMARY KEY (id)
);

CREATE TABLE push_message (
  id              BIGINT AUTO_INCREMENT NOT NULL
  COMMENT '发送消息标识',
  trace_id        BIGINT                NOT NULL
  COMMENT '回填id',
  device_platform VARCHAR(32)           NOT NULL
  COMMENT '平台类型',
  device_token    VARCHAR(180)          NOT NULL
  COMMENT '推送消息的路由标识',
  status          VARCHAR(32)           NOT NULL
  COMMENT '消息状态',
  user_id         VARCHAR(40)           NOT NULL
  COMMENT '用户账户标识',
  app_version     VARCHAR(50)           NOT NULL
  COMMENT '应用版本号',
  os_version      VARCHAR(32)           NOT NULL
  COMMENT '系统版本号',
  job_id          VARCHAR(36)           NOT NULL
  COMMENT '请求标识',
  send_time       DATETIME              NOT NULL DEFAULT current_timestamp
  COMMENT '消息失效时间',
  click_time      DATETIME              NOT NULL DEFAULT current_timestamp
  COMMENT '消息失效时间',


  create_time     DATETIME              NOT NULL DEFAULT current_timestamp,
  modify_time     DATETIME AS NOW(),

  PRIMARY KEY (id)
);

CREATE INDEX index_traceId_deviceToken
  ON push_message (trace_id, device_token);

