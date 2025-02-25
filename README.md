# 🏬 NTS Leica Korea WebSite

## 📚 프로젝트 개요
- 지인 부모님이 기존에 사용하시던 네이버 블로그 대신 사용할 수 있는 사이트 개발 요청을 받음
- 가능한 네이버 블로그처럼 사용 가능할 수 있는 편의성 있는 웹사이트를 구상함
- 어드민(허가된 사용자)만 게시물 생성, 삭제, 수정을 할 수 있고 나머지 인원들은 읽기만 허용된 웹 사이트를 구상하게 됨

## 🛠️ 기술 스택

### 💻 Back-End
- Java
- Spring Framework
- Spring Security
- Restful API

### 💻 Database
- MySQL
- Redis

### 💻 DevOps
- AWS EC2, RDS, Route53, S3
- Docker
- Nginx
- Jenkins

### 💻 Tools
- Git
- Github
- Postman
- Swagger

## ERD
![image](https://github.com/user-attachments/assets/8099461e-7aa1-4a88-880e-4da5c210f6bf)

## 📱 서비스 소개

### Screen UI
----

### 로그인
- 서버 관리자가 지정한 계정으로 로그인을 진행한다.
- 관리자를 제외한 일반사용자는 게시글을 읽기 권한만 소유한다.

< 이미지 대기.. >

----

### 게시물 조회
- 메인 페이지에서는 가장 최신 게시물 먼저 상위 5개가 조회 된다.
- 카테고리를 누르게되면 해당 카테고리에 속해있는 게시물들이 나타난다.
- 하나의 게시물에는 제목, 내용이 존재하며 이미지가 존재한다. 이미지는 S3를 활용하여 사용한다.
<img width="400" alt="image" src="https://github.com/user-attachments/assets/cddc1492-b6b5-4b06-8e19-acfa1186a64e" />
<img width="400" alt="image" src="https://github.com/user-attachments/assets/d3284594-4ca6-4a73-99d4-ade28630f547" />

----

### 서버 배포 & CI/CD
<img width="925" alt="image" src="https://github.com/user-attachments/assets/b4346665-aa42-4969-9afa-200ab1f6a091" />


----

  
### 🍀 프론트 Github

Front Github : <https://github.com/DonggyunKim00/LeicaBlog>



