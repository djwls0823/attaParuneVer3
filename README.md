# 🍽️ AttaParune (아따빠르네)

> 식권대장과 테이블링을 결합한 식당 예약 및 결제 플랫폼

## 📌 프로젝트 소개

식당 예약 서비스(테이블링)와 회사 식대 관리 시스템(식권대장)을 결합한  
식당 예약 및 포인트 기반 결제 플랫폼입니다.

짧은 점심시간 동안 발생하는 대기 시간과 결제 불편을 해결하기 위해,  
예약 기능과 포인트 기반 간편 결제 시스템을 통합하여  
점심시간 내 식사 효율을 높이는 것을 목표로 합니다.

프론트엔드와 협업하여 실제 서비스 형태로 구현하고,  
HTTPS 기반 도메인 환경에서 배포까지 진행했습니다.

## 🧩 주요 기능

- 역할 기반 사용자/관리자 시스템
- 포인트 기반 식대 결제 및 정산
- 식당 예약 및 현장 결제

## 🏗 서비스 구조

- 사용자: 식당 예약, 결제, 함께 결제 기능
- 식당 관리자: 메뉴 관리, 예약 승인/거절
- 회사 관리자: 포인트 충전 및 사원 관리
- 시스템 관리자: 식당 및 회사 등록

## 🙋‍♂️ 담당 역할

### 📊 데이터 조회 및 사용자 기능
- 메인 화면에서 식당 리스트를 조회하는 기능 구현
- DB 기반으로 음식점 데이터를 조회하여 사용자에게 제공
- 사용자의 진행 중 주문 및 주문 내역 조회 기능 구현

### 💳 결제 및 포인트 기능
- 다수 사용자와 함께 결제 시 승인 요청 기능 구현
- 회사 관리자의 사원 포인트 지급 기능 개발

### 🛠 관리자 기능
- 회사 관리자 페이지에서 직원 및 포인트 관리 CRUD 기능 구현
- 식당 관리자 페이지에서 음식점 이미지 등록 및 수정 기능 구현

### 🔐 배포 및 보안
- Let's Encrypt를 활용하여 SSL 인증서를 발급하고 HTTPS 환경 구축
- 사용자와 서버 간 통신을 암호화하여 보안성 강화

## 🖼 실행 화면

### 메인 화면
<img src="https://github.com/user-attachments/assets/5f8406f9-8c1c-4010-b578-b5b0ffe964c3" width="200"/>

### 주문 내역
<img src="https://github.com/user-attachments/assets/c78c6f53-f101-4a16-8bf3-5e5661e32e87" width="200"/>
<img src="https://github.com/user-attachments/assets/4384a4f2-5ae7-4628-b86c-b9f71883a06e" width="200"/>

### 함께 결제
<img src="https://github.com/user-attachments/assets/c16bd081-33e9-4cbc-a2f2-13a7c7197926" width="200"/>
<img src="https://github.com/user-attachments/assets/b7f6a936-7f87-4501-9027-22d527d1ddac" width="200"/>

### 관리자 페이지
<img src="https://github.com/user-attachments/assets/a3dc12e5-f338-44a7-932b-f869feeb520e" height="300"/>
<br>
<img src="https://github.com/user-attachments/assets/a79c61d7-84d6-4318-b07c-c1eaf0914402" height="300"/>
