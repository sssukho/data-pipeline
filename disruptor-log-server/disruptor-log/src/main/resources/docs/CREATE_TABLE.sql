-- event_log
CREATE TABLE event_log (
    log_id varchar(46),
    user_id varchar(64),
    user_name varchar(128),
    event_type varchar(64),
    log_date bigint,
    process_time bigint,
    primary key(log_id)
);

CREATE INDEX event_log_idx_1 ON event_log (log_date, user_id, user_name, event_type);
CREATE INDEX event_log_idx_2 ON event_log (process_time);














-- ) PARTITION BY RANGE (partition_date); => 추후 테이블 파티셔닝을 위한 쿼리