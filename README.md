# softee5 워밍업 프로젝트
## # antifragile BE
----------------------------------------

### # 팀원
1. 강예지 - 서비스 기획
2. 박지원A - 디자인
3. 장보경 (팀장) - 백엔드
4. 박동민 - 백엔드
5. 이치호 - 안드로이드

### # 주제
- 리뷰 기반 현대 자동차 추천 어플

### # 주요 기능
- 나에게 맞는 차량 추천 기능
  - 랭킹 매길 때 우선 순위 : 인원수 - 예산 - 고려사항 - 주요 이용 목적
- 소비자들의 차량 리뷰 커뮤니티
  - 리뷰글 및 댓글 기능
- 키워드별 차량 랭킹 시스템
  - 예산 범위 필터링 & (고려사항 + 주요 이용 목적) 기준 랭킹 시스템 제공

### # 기술 스택
- Spring boot 3.2.1
- Java 17
- IntelliJ
- H2
- Spring Data JPA

-------------------------------------------
## # REST API 명세서

#### 유저 관련

| Method | URI | Description |
|:------:|----:|------------:|
|**POST**| /signup|회원가입 처리 (차량 정보 제외)|
|**POST**| /login|로그인 처리|
|**GET**| /logout|로그아웃 처리|
|**GET**| /user|유저 정보 반환 (유저 아이디, 비밀번호, 닉네임, 소유 차량 정보)|
|**GET**| /user/reviews|유저가 작성한 리뷰글 목록 반환|
|**GET**| /user/comments|유저가 작성한 댓글 목록 반환|
|**PATCH**| /user|유저 정보 수정 요청 처리 (아이디, 비밀번호, 닉네임)|
|**POST**| /user/car|유저 소유 차량 생성 또는 수정 요청 처리|
|**DELETE**| /user|회원 탈퇴 처리 (작성한 게시물, 댓글 삭제)|

#### 리뷰글 관련
| Method | URI | Description |
|:------:|----:|------------:|
|**GET**| /reviews|리뷰글 목록 반환 (최신순 정렬)|
|**GET**| /reviews/{post_id}|해당 게시물 하나 반환 (댓글 제외 모든 항목)|
|**GET**| /reviews/{post_id}/comments|해당 게시물의 댓글 목록 반환 (최신순 정렬)|
|**GET**| /reviews/filter?model=&type=&purpose=&minPrice=&maxPrice=&keyword=&sort=|필터링 기반 게시물 목록 반환 요청 처리 (정렬 : 최신순 / 오래된 순 / 댓글 많은 순)|
|**POST**| /reviews|게시물 생성|
|**PATCH**| /reviews/{post_id}|게시물 수정|
|**DELETE**| /reviews/{post_id}|게시물 삭제 (해당 게시물의 댓글 삭제)|
|**POST**| /reviews/{post_id}/comments|댓글 생성 (해당 게시물 댓글 개수 + 1)|
|**PATCH**| /reviews/{post_id}/comments/{comment_id}|댓글 수정|
|**DELETE**| /reviews/{post_id}/comments/{comment_id}|댓글 삭제 (해당 게시물 댓글 개수 - 1)|

#### 차량 순위 관련
| Method | URI | Description |
|:------:|----:|------------:|
|**GET**| /reviews/ranking?keyword=&minPrice=&maxPrice=|필터링 기반 차량 랭킹 반환 요청 처리 (키워드 점수 높은 순 - 리뷰 많은 순 - 모델명 순)|

#### 차량 추천하기 관련
| Method | URI | Description |
|:------:|----:|------------:|
|**POST**| /recommend|유저의 선택 기반 추천 모델 2가지 반환|
|**GET**| /recommend/{model_id}|해당 모델에 대한 현대 공식 홈페이지 링크 반환|
