INSERT INTO Model (name, type, description, informationurl, people, price, review_count, mpg_sum, safe_sum, space_sum, design_sum, fun_sum, work_count, long_count, drive_count, travel_count, kids_count)
VALUES
('아이오닉6', '수소/전기차', '내가 만드는 세상', 'https://www.hyundai.com/kr/ko/e/vehicles/ioniq6/intro', 5, 5200, 10, 45, 60, 70, 85, 55, 1, 1, 1, 1, 6),
('넥쏘', '수소/전기차', 'Positive Energy, NEXO', 'https://www.hyundai.com/kr/ko/e/vehicles/nexo/intro', 5, 6950, 10, 50, 65, 75, 90, 60, 2, 2, 2, 2, 2),
('디 올 뉴 코나 Electric', '수소/전기차', '새로운 차원의 라이프', 'https://www.hyundai.com/kr/ko/e/vehicles/the-all-new-kona-electric/intro', 5, 4452, 10, 55, 70, 80, 95, 65, 1, 2, 3, 1, 3),
('더 뉴 아반떼', '승용차', '지금, 더 매력적인', 'https://www.hyundai.com/kr/ko/e/vehicles/the-new-avante/intro', 5, 1975, 10, 60, 75, 85, 100, 70, 1, 2, 2, 2, 3),
('더 뉴 아반떼 Hybrid', '승용차', '지금, 더 매력적인', 'https://www.hyundai.com/kr/ko/e/vehicles/the-new-avante-hybrid/intro', 5, 2466, 10, 65, 80, 90, 80, 75, 3, 1, 2, 2, 2),
('쏘나타 디 엣지', '승용차', '익숙함도 완전히 새롭게', 'https://www.hyundai.com/kr/ko/e/vehicles/sonata-the-edge/intro', 5, 2808, 10, 70, 85, 95, 85, 80, 2, 1, 3, 2, 2),
('쏘나타 디 엣지 Hybrid', '승용차', '지속 가능한 모빌리티 라이프', 'https://www.hyundai.com/kr/ko/e/vehicles/sonata-the-edge-hybrid/intro', 5, 3187, 10, 75, 90, 100, 90, 85, 1, 3, 1, 2, 3),
('디 올 뉴 그랜저', '승용차', 'Outclass GRNDEUR', 'https://www.hyundai.com/kr/ko/e/vehicles/the-all-new-grandeur/intro', 5, 3743, 10, 80, 95, 85, 95, 90, 1, 3, 2, 1, 3),
('디 올 뉴 그랜저 Hybrid', '승용차', 'Outclass GRNDEUR Hybrid', 'https://www.hyundai.com/kr/ko/e/vehicles/the-all-new-grandeur-hybrid/intro', 5, 4266, 10, 85, 100, 90, 100, 95, 1, 1, 2, 3, 3),
('더 뉴 투싼', 'SUV', '날개를 달다', 'https://www.hyundai.com/kr/ko/e/vehicles/the-new-tucson/intro', 5, 2771, 10, 90, 85, 95, 85, 100, 1, 3, 2, 1, 3),
('더 뉴 투싼 Hybrid', 'SUV', '날개를 달다', 'https://www.hyundai.com/kr/ko/e/vehicles/the-new-tucson-hybrid/intro', 5, 3213, 10, 95, 80, 100, 80, 85,0, 0, 0, 5, 5),
('베뉴', 'SUV', '혼라이프를 즐기다', 'https://www.hyundai.com/kr/ko/e/vehicles/venue/intro', 5, 2146, 10, 100, 75, 85, 75, 70, 1, 2, 3, 2, 2),
('디 올 뉴 코나 Hybrid', 'SUV', '새로운 차원의 플레이', 'https://www.hyundai.com/kr/ko/e/vehicles/the-all-new-kona-hybrid/intro', 5, 2999, 10, 38, 72, 59, 80, 66, 1, 2, 2, 0, 5),
('디 올 뉴 코나', 'SUV', '새로운 차원의 플레이', 'https://www.hyundai.com/kr/ko/e/vehicles/the-all-new-kona/intro', 5, 2486, 10, 32, 43, 13, 55, 64, 3, 0, 0, 4, 3),
('디 올 뉴 싼타페', 'SUV', '일상과 일상 사이를 열다', 'https://www.hyundai.com/kr/ko/e/vehicles/the-all-new-santafe/intro', 7, 3546, 10, 40, 74, 61, 82, 68, 0, 2, 2, 4, 2),
('디 올 뉴 싼타페 Hybrid', 'SUV', '일상과 일상 사이를 열다', 'https://www.hyundai.com/kr/ko/e/vehicles/the-all-new-santafe-hybrid/intro', 7, 3888, 10, 42, 76, 63, 84, 70, 0, 3, 3, 1, 3),
('팰리세이드', 'SUV', '당신의 모든 세상', 'https://www.hyundai.com/kr/ko/e/vehicles/palisade/intro', 8, 3896, 10, 44, 78, 65, 86, 72, 0, 4, 1, 2, 3),
('캐스퍼', '경차', 'case by CASPER', 'https://casper.hyundai.com/vehicles/highlight', 4, 1385, 10, 46, 80, 67, 88, 74, 1, 0, 2, 4, 3),
('스타리아', 'MPV', 'Multi-Play Life', 'https://www.hyundai.com/kr/ko/e/vehicles/staria/intro', 11, 3012, 10, 48, 82, 69, 90, 76, 3, 2, 1, 0, 4);


INSERT INTO Car (car_num, model_id, car_age, buy_year, buy_month)
VALUES
('12바 3456', 5, 2005, 2013, 3),
('123나 4567', 3, 2009, 2010, 2),
('134나 5678', 7, 2003, 2004, 11),
('45마 6789', 2, 2004, 2005, 1),
('56가 7890', 11, 2006, 2013, 2),
('67하 8901', 15, 2002, 2005, 1),
('78누 9012', 4, 2020, 2020, 12),
('89두 0123', 9, 2022, 2023, 10),
('90마 1234', 6, 2002, 2003, 2),
('101가 2345', 8, 2010, 2013, 2);


INSERT INTO Member(car_id, login_id, password, nickname)
VALUES
(1,'aaa123','1234','user1'),
(2,'bbb123','1234','user2'),
(3,'ccc123','1234','user3'),
(4,'ddd123','1234','user4'),
(5,'aaa111','1234','user5'),
(6,'bbb111','1234','user6'),
(7,'ccc111','1234','user7'),
(8,'ddd111','1234','user8'),
(9,'eee222','1234','user9'),
(10,'ddd333','1234','user10');

INSERT INTO Post(writer_id, car_id, title, contents, mgp, safe, space, design, fun, purpose)
VALUES (1, 1, '좋은거 같아요', '편하다 승차감 좋다', 9, 8, 7, 6, 5, '출퇴근용'),
(2, 2, '별로더라', '별로던데요', 5, 6, 7, 8, 9, '주말여행'),
(3, 3, '추천합니다', '드라이브용으로 좋아요', 9, 8, 3, 4, 9, '드라이브'),
(4, 4, '좁아요', '생각보다 좁아요', 4, 4, 0, 1, 3, '장거리 운전'),
(5, 5, '연비가 좋아요', '연비 미쳤다', 9, 9, 1, 1, 1, '주말여행'),
(6, 6, 'ㅁ', 'ㅁ', 8, 1, 8, 1, 8, '주말여행'),
(7, 7, 'ㅁ', 'ㅁ', 1, 2, 4, 5, 6, '장거리 운전'),
(8, 8, 'ㅁ', 'ㅁ', 1, 1, 1, 1, 1, '자녀와 함께'),
(9, 9, 'ㅁ', 'ㅁ', 9, 2, 4, 1, 5, '장거리 운전');