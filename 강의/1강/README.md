# 웹 애플리케이션 이해

## 웹 서버, 웹 애플리케이션 서버
![img.png](img.png)
### 모든 것이 HTTP
> HTTP 메시지에 모든 것을 전송
* HTML, TEXT
* 이미지, 음성, 영상, 파일
* JSON, XML (API)
* 거의 모든 형태의 데이터 전송 가능
* 서버간에 데이터를 주고 받을 때도 대부분 HTTP 사용

### 웹 서버 ( Web Server )
![img_1.png](img_1.png)
* HTTP 기반으로 동작
* **정적 리소드 제공**, 기타 부가기능
* 정적(파일) HTML, CSS, JS, 이미지, 영상
* NGINX, APACHE

### 웹 애플리케이션 서버 ( Web Application Server )
![img_2.png](img_2.png)
* HTTP 기반으로 동작
* 웹 서버 기능 포함
* 프로그램 코드를 실행해서 **애플리케이션 로직 수행**
  * 동적 HTML, HTTP API (JSON)
  * 서블릿, JSP, 스프링 MVC
* Tomcat, Jetty, Undertow

### 웹 시스템 구성 - WAS, DB
![img_3.png](img_3.png)
* WAS와 DB만으로 시스템 구성 가능
* WAS는 정적 리소스, 애플리케이션 로직 모두 제공 가능
* WAS가 너무 많은 역할을 담당, 서버 과부화 우려
* 가장 비싼 애플리케이션 로직이 정적 리소스 때문에 수행이 어려울 수 있음
* WAS 장애시 오류 화면도 노출 불가능

### 웹 시스템 구성 - WEB, WAS, DB
![img_4.png](img_4.png)
* 정적 리소스는 WS가 처리
* WS는 애플리케이션 로직같은 동적인 처리가 필요하면 WAS에 요청을 위임
* WAS는 중요한 애플리케이션 로직 전담
* 효율적인 리소스 관리
  * 정적인 리소스가 많이 사용되면 WS 증설
  * 애플리케이션 리소스가 많이 사용되면 WAS 증설
* 정적 리소스만 제공하는 WS는 잘 죽지 않음
* 애플리케이션 로직이 동작하는 WAS는 잘 죽음
* WAS, DB 장애시 WS가 오류 화면 제공 가능

## 서블릿

## 동시 요청 - 멀티 쓰레드

## HTML, HTTP API, CSR, SSR

## 자바 백엔드 웹 기술 역사
