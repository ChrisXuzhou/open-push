INSERT INTO push_user (user_id, app_name, app_code, app_version, os_version, device_type, device_mc, device_token_type, device_token)
VALUES
  ('test-user-1', 'MedTime', '2789371372193719', '3.12', 'iOS.3.12', 'test-device-type-1',
   'test-device-mc-1',
   'test-token-type-1',
   'qyrMumIBPiqSnCgRsdsXi/aFqBFHxdBRkwAwch+u2A8=');

INSERT INTO push_user (user_id, app_name, app_code, app_version, os_version, device_type, device_mc, device_token_type, device_token)
VALUES
  ('test-user-2', 'MedTime', '2789371372193719', '3.12', 'iOS.3.12', 'test-device-type-1',
   'test-device-mc-2',
   'test-token-type-1',
   'qyrMumIBPiqSnCgRsdsXi/aFqBFHxdBRkwAwch+u2A8/');

INSERT INTO push_user (user_id, app_name, app_code, app_version, os_version, device_type, device_mc, device_token_type, device_token)
VALUES
  ('test-user-3', 'MedTime', '2789371372193719', '3.12', 'iOS.3.12', 'test-device-type-1',
   'test-device-mc-3',
   'test-token-type-1',
   'qyrMumIBPiqSnCgRsdsXi/aFqBFHxdBRkwAwch+u2A80');


INSERT INTO push_user (user_id, app_name, app_code, app_version, os_version, device_type, device_mc, device_token_type, device_token)
VALUES
  ('test-user-4', 'MedTime', '2789371372193719', '3.12', 'iOS.3.12', 'test-device-type-1',
   'test-device-mc-4',
   'test-token-type-1',
   'qyrMumIBPiqSnCgRsdsXi/aFqBFHxdBRkwAwch+u2A8A');

INSERT INTO push_user (user_id, app_name, app_code, app_version, os_version, device_type, device_mc, device_token_type, device_token)
VALUES
  ('test-user-5', 'appcode_medtime_android', '2789371372193719', '3.12', 'iOS.3.12', 'test-device-type-1',
   'test-device-mc-4',
   'ANDROID',
   'qyrMumIBPiqSnCgRsdsXi/aFqBFHxdBRkwAwch+u2A8B');


INSERT INTO push_user (user_id, app_name, app_code, app_version, os_version, device_type, device_mc, device_token_type, device_token)
VALUES
  ('test-user-6', 'appcode_medtime_ios', '2789371372193719', '3.12', 'iOS.3.12', 'test-device-type-1',
   'test-device-mc-4',
   'iOS',
   'qyrMumIBPiqSnCgRsdsXi/aFqBFHxdBRkwAwch+u2A8C');

INSERT INTO push_user (user_id, app_name, app_code, app_version, os_version, device_type, device_mc, device_token_type, device_token)
VALUES
  ('test-user-6', 'appcode_medtime_ios1', '278937137219373', '3.12', 'iOS.3.12', 'test-device-type-1',
   'test-device-mc-4',
   'iOS',
   'qyrMumIBPiqSnCgRsdsXi/aFqBFHxdBRkwAwch+u2A8D');

INSERT INTO push_user (user_id, app_name, app_code, app_version, os_version, device_type, device_mc, device_token_type, device_token)
VALUES
  ('test-user-7', 'appcode_medtime_ios', '278937137219373', '3.12', 'iOS.3.12', 'test-device-type-1',
   'test-device-mc-4',
   'iOS',
   'qyrMumIBPiqSnCgRsdsXi/aFqBFHxdBRkwAwch+u2A8E');


insert INTO push_request(job_id, status, app_key, title, payload, description, message_id, message_type, criteria, criteria_description)
values ('job_id', 'CONFIRMED', 'medical', 'title', 'payload_json', 'description', 'msg id','msg type','GLOBAL','');

insert INTO push_request(job_id, status, app_key, title, payload, description, message_id, message_type, criteria, criteria_description)
values ('job_id_1', 'CONFIRMED', 'medical', 'title', 'payload_json', 'description', 'msg id','msg type','BY_USER_ID','');

insert INTO push_request_detail(job_id, status, criteria, criteria_description)
values ('job_id_1', 'CREATED', 'BY_USER_ID','test-user-1,test-user-2,test-user-3,test-user-4,test-user-5,test-user-6, test-user-7');

