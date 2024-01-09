-- 멤버
DROP TABLE IF EXISTS Member;

CREATE TABLE Member
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    login_id varchar(255) not null,
    nickname varchar(255) not null,
    password varchar(255) not null,
    car_id BIGINT
);

-- 모델
DROP TABLE IF EXISTS Model;

CREATE TABLE Model (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    type VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    informationurl VARCHAR(255) NOT NULL,
    people INT NOT NULL,
    price INT NOT NULL,
    review_count BIGINT DEFAULT 0,
    mpg_sum BIGINT DEFAULT 0,
    safe_sum BIGINT DEFAULT 0,
    space_sum BIGINT DEFAULT 0,
    design_sum BIGINT DEFAULT 0,
    fun_sum BIGINT DEFAULT 0,
    work_count BIGINT DEFAULT 0,
    long_count BIGINT DEFAULT 0,
    drive_count BIGINT DEFAULT 0,
    travel_count BIGINT DEFAULT 0,
    kids_count BIGINT DEFAULT 0
);

-- 자동차
DROP TABLE IF EXISTS Car;

CREATE TABLE Car (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    car_num VARCHAR(255) NOT NULL,
    model_id BIGINT NOT NULL,
    car_age INT,
    buy_year INT,
    buy_month INT
);

-- 게시물
DROP TABLE IF EXISTS Post;

CREATE TABLE Post (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    writer_id BIGINT NOT NULL,
    car_id BIGINT NOT NULL,
    title VARCHAR(255),
    contents VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    mgp BIGINT DEFAULT 5,
    safe BIGINT DEFAULT 5,
    space BIGINT DEFAULT 5,
    design BIGINT DEFAULT 5,
    fun BIGINT DEFAULT 5,
    purpose VARCHAR(255),
    comment_count BIGINT DEFAULT 0
);

-- 댓글
DROP TABLE IF EXISTS Comment;

CREATE TABLE Comment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    post_id BIGINT NOT NULL,
    writer_id BIGINT NOT NULL,
    contents TEXT NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);




