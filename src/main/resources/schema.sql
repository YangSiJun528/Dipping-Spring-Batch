DROP TABLE `user` IF EXISTS;

CREATE TABLE `user`  (
     user_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
     user_name VARCHAR(20) NOT NULL
);

DROP TABLE `score` IF EXISTS;

CREATE TABLE `score`  (
     score_id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
     score_value decimal(6,3) NOT NULL, -- 3자리수 소수점을 허용하는 총 6자리수의 숫자
     user_id BIGINT NOT NULL
);
