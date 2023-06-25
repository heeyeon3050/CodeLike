# CodeLike
인스타 아이디를 기반의 익명 호감 표시 서비스
- 본인의 인스타 아이디를 입력하면, 현재 자신에게 호감을 느끼고 있는 사람이 몇 명인지 알 수 있다.
- 나를 좋아하는 상대방이 나의 어떤 매력포인트 때문에 좋아하는지 알 수 있다.
- 나를 좋아하는 상대방의 성별도 알 수 있다.

<br>

## 주요기능
- Auoth2를 이용한 로그인 기능
    - 카카오 로그인
    - 네이버 로그인
    - 구글 로그인
    - 페이스북 로그인
- 호감 표시, 수정, 삭제 기능
    - 5명까지 호감 표시 가능
    - 호감 사유 선택(외모, 성격, 능력)
    - 최근 수정한 시간으로부터 3시간 이후에 수정 및 삭제 가능
- 호감 확인 기능
    - 나를 좋아하는 사람의 수 확인
    - 나를 외모때문에 좋아하는 사람의 수 확인
    - 나를 성격때문에 좋아하는 사람의 수 확인
    - 나를 능력때문에 좋아하는 사람의 수 확인
- 알림 기능
    - 나를 호감 표시한 내역이 있으면 알림
    - 읽지 않은 내용은 🔥표시
<br>

## 배포 링크

<a href="https://www.ouo.ac/">코드라이크 배포 링크</a>

## 기술스택

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


## 프로젝트 실행 방법

1. 프로젝트 다운받기

2. src/main/resources 에 application-secret.yml 작성한다.
```yml
spring:
  security:
    oauth2:
      client:
        registration:
          naver:
            clientId: 'CLIENT ID'
            client-secret: 'CLIENT PASSWORD'
          kakao:
            clientId: 'CLIENT ID'
          google:
            clientId: 'CLIENT ID'
            client-secret: 'CLIENT PASSWORD'
          facebook:
            clientId: 'CLIENT ID'
            client-secret: 'CLIENT PASSWORD'
          instagram:
            clientId: 'CLIENT ID'
            client-secret: 'CLIENT PASSWORD'
custom:
  security:
    oauth2:
      client:
        registration:
          naver:
            devUserOauthId: 'oauthId'
          kakao:
            devUserOauthId: 'oauthId'
          google:
            devUserOauthId: 'oauthId'
          facebook:
            devUserOauthId: 'oauthId'
```

<br><br>

## 화면 구성
![image](https://github.com/heeyeon3050/HiddenHearts/assets/111184269/071fe108-be4c-4715-90c8-8e2f37e023f7)
![image](https://github.com/heeyeon3050/HiddenHearts/assets/111184269/3ba94210-74c8-4584-9d3d-7e4a92a1eb62)
