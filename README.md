# Jammy Server

친구들과 여행 기록을 공유하고, 여행 후 타임캡슐을 함께 열어보는 서비스 **Jammy**의 백엔드 서버입니다.

## 주요 기능

- 회원가입 및 로그인
- 여행 방 생성 및 초대 코드 기반 입장
- 공개 일기 작성 및 조회
- 타임캡슐 일기 작성 및 공개 시간 이후 조회

## 기술 스택

- Java 17
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA
- MySQL
- Gradle

## 프로젝트 구조

```text
src/main/java/com/jammy
├── global
├── user
├── room
├── roommember
└── diary
```