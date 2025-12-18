# 📖 BookDam
> <b>도서 검색, 구매, 관리</b> 기능을 제공하는 웹 서비스. <b>도서판매 시스템</b>
<p align="center">
<img width="1891" height="910" alt="Image" src="https://github.com/user-attachments/assets/5f56891c-3dad-49fc-bb46-5cde25a0caaa" />
<img width="1903" height="900" alt="Image" src="https://github.com/user-attachments/assets/10629b0d-163e-470d-a415-074d6df021f9" />
</p>

</br>

## 01. 프로젝트 개요
* <b>개발 기간</b> : 2025.06. ~ 2025.07. (1개월) 
* <b>개발 인원</b> : 5명
* <b>핵심 목표</b> : 1. 수업내용을 연계한 CRUD 기능 실습 2. 도서 검색, 구매, 관리 기능을 제공하는 웹사이트 개발

<br />

## 02. 팀원 및 역할
* <b>임가영</b> : 장바구니, 결제, 환불, 마이페이지, 작가와의 채팅 구현 등
* <b>김수현</b> : 도서 검색, 도서 상세 조회, 리뷰 시스템, 작가와의 채팅 구현 등
* <b>김주희</b> : 관리자 도서 정보 및 재고 관리, 주문 내역 관리 구현 등
* <b>신혜진</b> : 로그인 및 회원가입, 회원정보 수정, 관리자 회원 정보 관리 구현 등
* <b>윤서현</b> : 베스트 셀러, 1:1 고객센터, 문화/행사 게시판 구현 등

<br />

## 03. 기술 스택
<table>
  <tr>
    <td>개발언어</td>
    <td>Java, JavaScript, HTML5, CSS3</td>
  </tr>
   <tr>
    <td>프레임워크</td>
    <td> mybatis</td>
  <tr>
    <td>라이브러리 및 API</td>
    <td>jQuery, log4j, websocket, 알라딘 도서정보 API, 도서나루 API</td>
  </tr>
   <tr>
    <td>협업 툴</td>
    <td>SVN, Redmine</td>
  </tr>
   <tr>
    <td>개발 툴</td>
    <td>Eclipse</td>
  </tr>
   <tr>
    <td>DB</td>
    <td>SQL Developer, Oracle DB</td>
</table>
  <br />

## 04. 시스템 구조 및 설계 전략

### 개발 전략
* 프로세스: Waterfall 모델 기반의 단계별 공정 준수 (기획 → 설계 → 구현 → 테스트)
* 협업 관리: Redmine을 통한 이슈 트래킹 및 마일스톤 관리

### 서비스 아키텍처
Controller → Service → DAO → Service → Controller → JSP 순서로 흐르는 전통적인 MVC 구조. 서버 사이드 렌더링(SSR) 기반으로 안정적인 화면 제공

### 데이터베이스
<p align="left">
  <img src="https://github.com/user-attachments/assets/86f8dbca-418f-4674-b77b-8f04ab1f1baf" width="1000"/>
</p>

### UI 설계
[🔗 Figma 에서 상세보기](https://www.figma.com/design/NEFjK1jRgeTG1Be6ZiJkB2/html.to.design-%E2%80%94-by-%E2%80%B9div%E2%80%BARIOTS-%E2%80%94-Import-websites-to-Figma-designs--web-html-css---Community-?node-id=0-1&t=h0iGliLnmuvYfLLr-1)
<br/><img width="800" alt="Image" src="https://github.com/user-attachments/assets/1d28b045-4691-480e-98ab-e60a7fbc1ec8" />

<br />

## 05. 주요 기능

### 🗂️ 프로젝트 관리
다양한 부서가 함께하는 프로젝트

* 프로젝트 등록 시, 다양한 부서의 사람들 초대
* 업무 및 진행률 실시간 확인
* 프로젝트 전 단계(등록–진행–완료) 관리
<br/>
  <img src="https://github.com/user-attachments/assets/8b99faae-5ced-403f-94c8-cd09adcb1e3d" width="400" />
  <br>[프로젝트 등록 시 다양한 부서원 초대 가능]<br><br>
  <img src="https://github.com/user-attachments/assets/4a390bfd-3b3e-4008-8387-1a057f11a30b" width="700" />
  <br>[진행 중인 프로젝트 목록]<br><br>
  <img src="https://github.com/user-attachments/assets/c0eff935-012e-402b-a705-1467f36a8934" width="700" />
  <br>[업무 처리 화면]<br><br>

<hr />

### 🖋️전자결재 
결재 업무 효율화 및 자동 처리

* 전자 문서 생성, 결재 요청 및 승인 프로세스 제공
* 결재 단계 및 진행 상태 실시간 확인
* 결재 완료 시 자동으로 업무에 반영
* 승인 알림 및 문서 보관 자동화
<br/>
<img width="800" alt="Image" src="https://github.com/user-attachments/assets/d1f1e4fe-d0b8-4e7d-9da6-be192d06fbff" />
<br>[전자결재 홈]<br><br>
<img width="800" alt="Image" src="https://github.com/user-attachments/assets/b39c9498-990e-4bf0-b6f1-565a756c9a4f" />
<br>[기안서 작성 페이지]<br><br>
<img width="600" alt="Image" src="https://github.com/user-attachments/assets/aac846cc-a60f-4952-b1a8-6a6815c5ad8e" />
<br>['신메뉴 기안서' 최종 승인 시, 해당 화면에 바로 반영되어 별도 관리 가능]<br><br>

<hr />

### 🤖 AI 챗봇
업무 어시스턴스

* 회사 규정·구성원 정보 검색 지원
* 자동 기안서 작성 - 휴가 신청서, 신메뉴 등록 기안서
* 회의실 예약 등 반복 업무 자동 처리
<br/>
<table>
  <tr>
    <td align="center">
      <img width="309" alt="Image" src="https://github.com/user-attachments/assets/65b1469d-404d-4546-89ef-0929841ac103" /><br>
      [회의실 자동 예약]
    </td>
    <td align="center">
      <img width="299" alt="Image" src="https://github.com/user-attachments/assets/1e0082ec-d27e-4fd7-a9eb-7062d38e4744" /><br>
      [구성원 검색]
    </td>
  </tr>
</table>






