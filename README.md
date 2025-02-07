# DevInit - 개발자 커뮤니티 플랫폼 🚀

<div align="center">

[![Deploy](https://img.shields.io/badge/DevInit-배포_링크-4285F4?style=for-the-badge&logo=google-chrome&logoColor=white)](http://34.64.72.48/)

</div>

## 👥 팀 소개
DevInit은 열정 넘치는 4명의 개발자가 모여 시작한 프로젝트입니다.

|Frontend|Frontend|Backend|Backend|
|:------:|:------:|:------:|:------:|
|[<img src="https://avatars.githubusercontent.com/u/78842816?v=4" width="100px">](https://github.com/min-s9709)|[<img src="https://avatars.githubusercontent.com/u/74394824?v=4" width="100px">](https://github.com/seung365)|[<img src="https://avatars.githubusercontent.com/u/49359846?v=4" width="100px">](https://github.com/jiwon2030)|[<img src="https://avatars.githubusercontent.com/u/77718648?v=4" width="100px">](https://github.com/moonsunmean)|
|[김민성](https://github.com/min-s9709)|[백승범](https://github.com/seung365)|[민지원](https://github.com/jiwon2030)|[문선민](https://github.com/moonsunmean)|

## 📌 프로젝트 소개

**DevInit**은 개발자들의 새로운 시작을 응원합니다!

개발자 커리어를 시작하는 순간부터 함께하는 커뮤니티를 지향합니다. <br>
DevInit이란 이름에는 개발자(Developer)의 시작(Init)이라는 의미를 담았습니다.

## 🎥 시연 영상
[DevInit 소개영상 보러가기](https://www.youtube.com/watch?v=Q522KXwnJ3I)

## 🛠 기술 스택

### Backend

<div style="display: flex; flex-wrap: wrap; gap: 10px;">

![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Spring Securiey](https://img.shields.io/badge/springsecurity-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)
![JPA](https://img.shields.io/badge/jpa-6DB33F?style=for-the-badge&logo=jpa&logoColor=white)
![QUERYDSL](https://img.shields.io/badge/queryDSL-6DB33F?style=for-the-badge&logo=queryDSL&logoColor=white)
![AMAZONS3](https://img.shields.io/badge/amazons3-569A31?style=for-the-badge&logo=amazons3&logoColor=white)
![JWT](https://img.shields.io/badge/jsonwebtokens-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![STOMP](https://img.shields.io/badge/STOMP-000000?style=for-the-badge&logo=null&logoColor=white)


</div>


### Backend DevOps & Tools
<div style="display: flex; flex-wrap: wrap; gap: 10px;">

![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![GitLab](https://img.shields.io/badge/GitLab-330F63?style=for-the-badge&logo=gitlab&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white)
![OAuth](https://img.shields.io/badge/OAuth-4285F4?style=for-the-badge&logo=google&logoColor=white)
![Nginx](https://img.shields.io/badge/Nginx-009639?style=for-the-badge&logo=nginx&logoColor=white)
![SWAGGER](https://img.shields.io/badge/swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=white)


</div>


## 🏗 ARCHITECTURE

![ARCITECTURE](https://cdn.discordapp.com/attachments/1326759250592268348/1337244708442738839/image.png?ex=67a6bdaf&is=67a56c2f&hm=289a685a6f592e71617a7ce31d09b6d029ada04a406fb8a4df72846d02c451a5&)

## 🗂 ERD
![devinit (3)](https://github.com/user-attachments/assets/2d584c32-f1f2-4971-a2d0-09585cec062c)

## 🎨 WIRE FRAME

👉 https://www.figma.com/design/Rd6KQMMBOgj4wo1GZG1sKh/DevInit?node-id=0-1&p=f&t=vLnJyybdHWRWDzEo-0
  


## 📝 커밋 메시지 컨벤션
```
feat: 새로운 기능 추가
fix: 버그 수정
docs: 문서 수정
style: 코드 포맷팅
refactor: 코드 리팩토링
test: 테스트 코드
chore: 사소한 수정 및 빌드 설치 사항
build: 빌드 관련 수정
```

## 📁 백엔드 프로젝트 구조

```
main
├── java
│   └── com
│       └── team3
│           └── devinit_back
│               ├── DevinitBackApplication.java
│               ├── board # 도메인별 관리
│               │   ├── controller
│               │   ├── dto
│               │   ├── entity
│               │   ├── init
│               │   ├── repository
│               │   └── service
│               ├── comment
│               ├── follow
│               ├── global
│               │   ├── amazonS3
│               │   ├── aop
│               │   ├── common
│               │   ├── config
│               │   └── exception
│               ├── hub
│               ├── member
│               ├── profile
│               ├── resume
│               └── websocket
└── resources
    ├── application-dev.yml
    ├── application-prod.yml
    └── application.yml
```

---
<div align="center">
  
**DevInit**과 함께 당신의 개발 여정을 시작하세요! 🚀

</div>
