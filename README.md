<p align="center">
  <div align="center"><img src="https://avatars.githubusercontent.com/u/88327986?s=200&v=4" width="20%"/></div>
  <br>  
</p>


<p align="center">책 읽는 순간을 담아내는,<br> 독서 기록 어플리케이션 Ourpage📚의 서버입니다</p>
<br>

### 홈페이지: https://litt.ly/ourpage
### 인스타그램: https://www.instagram.com/ourpage_app/
### 다운로드 AOS: https://play.google.com/store/apps/details?id=com.mangpo.bookclub
### 다운로드 IOS: https://apps.apple.com/kr/app/our-page/id1602314034
<br>

### 서버 구조도
<p align="center">
  <div align="center"><img src="https://raw.githubusercontent.com/CYC0227/mangpo-ERD/main/archi.png" width="100%"/></div>
  <br>  
</p>


### 사용 기술, 개발 환경

* Java, Spring Boot, Gradle
* JPA(Spring Data, Hibernate), QueryDSL
* JUnit5, AssertJ, Mockito
* MariaDB, H2
* GitHub Actions
* AWS (Elastic Beanstalk)
* IntelliJ, Postman

### ERD
<p align="center">
  <div align="center"><img src="https://raw.githubusercontent.com/CYC0227/mangpo-ERD/main/mangpoERD.png" width="100%"/></div>
  <br>  
</p>


### 프로젝트 목표

* 독서 기록 SNS 서비스 운영
* 백엔드 개발의 전체적 프로세스 경험
* 장기간의 운영을 통한 끝없는 고민


### 핵심 기능

* 회원 가입, 탈퇴, 비밀번호 재발급 (메일링 서비스 이용)
* 로그인, 로그아웃 (JWT 이용)
* 게시물 작성/수정/삭제/조회
* todo list 기능
* 좋아요, 댓글(대댓글) 기능
* 클럽원 초대 기능

### 프로젝트를 통해 얻은 경험

* 기획자(디자이너) & 클라이언트 개발자와 커뮤니케이션  
* 기획에서 요구사항 도출 및 데이터베이스 설계
* 기능 추가에 따른 테이블 추가 및 수정(반정규화)
* HTTP API 개발
* 좋은 아키텍처에 대한 고민
* 기능 테스트 작성
* Jacoco 이용한 테스트 커버리지 분석
* 더 좋은 코드 품질과 성능을 위한 지속적 리팩토링
* AWS Beanstalk을 이용한 배포
* Github actions를 이용한 배포 자동화
* nGrinder 이용한 성능 테스트
* 기타: 홍보 영상 제작, 사용자 경험 설문조사


### 해보고 싶은 작업
* 부하 분산을 위한 DB Replication 구성 - 당장은 필요없지만 대용량 트래픽이 발생한다 가정했을때 필요하다 생각함
* 캐시 도입 (Redis) - 비싼 쿼리가 발생하는 부분이 있어서 캐싱하면 좋겠다고 생각함
* 메일링 관련 API 비동기로 전환 - 현재는 이메일이 발송되는걸 기다리는데, 그럴 필요가 없다 생각함
* notification 기능 개발 및 메세지 큐 적용


### 문제 해결 & 학습 블로깅
* 테스트 코드 학습<br>https://choibulldog.tistory.com/67
* 테스트 코드 리팩토링<br>https://choibulldog.tistory.com/50
* Jacoco 이용<br>https://choibulldog.tistory.com/66
* 서버 성능 테스트 학습<br>https://choibulldog.tistory.com/61
* nGrinder 이용한 성능 테스트<br>https://choibulldog.tistory.com/60
* Controller와 Service의 역할에 대한 고민<br>https://choibulldog.tistory.com/52
* DB table 개선<br>https://choibulldog.tistory.com/49
* @Builder 관련 NPE 해결<br>https://choibulldog.tistory.com/43


### UI

* 인스타그램을 참조해 주세요

### 기타 자료 & TMI 모음
* 사용가이드: https://coconut-cheese-fcb.notion.site/9ef0cda163df45c0b81f100b5a22bc95
* 린캔버스: https://coconut-cheese-fcb.notion.site/e1eb8b3f053a40008a9cc7160a26095d

* 2021년 정부지원 창업 동아리 선정 당시 사진
<p align="center">
  <div align="center"><img src="https://github.com/CYC0227/mangpo-ERD/blob/main/KakaoTalk_20211020_172331193.png?raw=true" width="70%"/></div>
  <br>  
</p>
