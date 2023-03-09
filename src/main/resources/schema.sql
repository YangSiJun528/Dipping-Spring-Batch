DROP TABLE `user` IF EXISTS;

CREATE TABLE `user`  (
     person_id BIGINT NOT NULL PRIMARY KEY,
     first_name VARCHAR(20),
     last_name VARCHAR(20)
);

DROP TABLE `score` IF EXISTS;

CREATE TABLE `score`  (
     score_id BIGINT NOT NULL PRIMARY KEY,
     score_value decimal(6,3), -- 3자리수 소수점을 허용하는 총 6자리수의 숫자
     user_id BIGINT
);

