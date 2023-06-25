# CodeLike
인스타 아이디를 기반의 익명 호감 표시 서비스

<br>

## 주요기능
- Auoth2를 이용한 로그인 기능
- 호감 표시 기능
- 호감 확인 기능
- 알림 기능

<br>

## 배포 링크

<a href="https://ouo.ac/">코드라이크 배포 링크</a>

## 기술스택
![image](https://github.com/heeyeon3050/HiddenHearts/assets/111184269/c8365c3b-3b0c-4f0c-8f5d-63e1b3d131bf)
![image](https://github.com/heeyeon3050/HiddenHearts/assets/111184269/145629fc-3875-47dc-8a34-a4da78ef3432)
![image](https://github.com/heeyeon3050/HiddenHearts/assets/111184269/765708b4-e25c-41ee-b80d-2477d521d416)
![image](https://github.com/heeyeon3050/HiddenHearts/assets/111184269/910d15be-e088-476a-9b56-966bf026d239)
![image](https://github.com/heeyeon3050/HiddenHearts/assets/111184269/a8212f0c-d506-4691-bafa-79f8fba3c827)


### Tech
<img src="https://img.shields.io/badge/Java-FC4C02?style=flat-square&logo=java&logoColor=white"/> <img src="https://img.shields.io/badge/Spring boot-6DB33F?style=flat-square&logo=Spring boot&logoColor=white"/>
        
<img src="https://img.shields.io/badge/gradle-02303A?logo=gradle&logoWidth=25"/> <img src="https://img.shields.io/badge/Spring security-6DB33F?style=flat-square&logo=springsecurity&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Data JPA-2596BE?style=flat-square&logo=&logoColor=white"/> <img src="https://img.shields.io/badge/MariaDB-2596BE?style=flat-square&logo=MariaDB&logoColor=white"/>
        
       

### Deploy
<img src="https://img.shields.io/badge/Nave Cloud Platform-03C75A?style=flat-square&logo=naver&logoColor=white"/> <img src="https://img.shields.io/badge/Github Actions-2AB1AC?style=flat-square&logo=github&logoColor=black"/> <img src="https://img.shields.io/badge/Jenkins-D24939?style=flat-square&logo=Jenkins&logoColor=white"/> <img src="https://img.shields.io/badge/Docker-%230db7ed.svg?style=flat-square&logo=docker&logoColor=white"/> 
        

### Tool
<img src="https://img.shields.io/badge/IntelliJ IDEA-0052CC?style=flat-square&logo=IntelliJ IDEA&logoColor=black"/> <img src="https://img.shields.io/badge/Github-000000?style=flat-square&logo=Github&logoColor=white"/> <img src="https://img.shields.io/badge/Notion-FFFFFF?style=flat-square&logo=Notion&logoColor=black"/> 


<br>


## 아키텍쳐
<img width="874" alt="image" src="https://github.com/LikeLion-team5/Grabit/assets/92236489/ffc48237-dfb6-4924-8e99-aa5d12be0947">

<br><br>

## 요구사항 분석



## 프로젝트 실행 방법

1. 프로젝트 다운받기

2. src/main/resources 에 application-secret.yml 작성한다.
```yml
spring:
  datasource:
    url: jdbc:mariadb://DB주소:포트번호/DB명?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul
    driver-class-name: org.mariadb.jdbc.Driver
    username: 계정이름
    password: 비밀번호

  jpa:
    hibernate:
      ddl-auto: create
    properties:
      hibernate:
        show_sql: true
        format_sql: true
        use_sql_comments: true

  security:
    oauth2:
      client:
        registration:
          naver:
            clientId: '네이버에서 발급받은 id'
            client-secret: '네이버에서 발급받은 시크릿 키'
          kakao:
            clientId: '카카오에서 발급받은 키'
    s3:
      endpoint: https://kr.object.ncloudstorage.com
      bucket: '버킷명'
      dir: '버킷안에서 사용할 디렉토리명'
```
