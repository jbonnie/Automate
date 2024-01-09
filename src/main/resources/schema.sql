-- 멤버
DROP TABLE IF EXISTS Member;

CREATE TABLE Member
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    login_id varchar(255) not null,
    nickname varchar(255) not null,
    password varchar(255) not null,
    car_id bigint,
    primary key (id)
);