# 블로그 검색 서비스

## API 명세 (Swagger)
> API 명세와 Swagger 테스트가 가능합니다. (GCP 배포 App Engine 배포 완료)  
> Swagger URL: https://blog-search-381304.du.r.appspot.com/swagger-ui/index.html
 
## 개발 환경
> Framework : Spring-boot 2.5.5  
> Java Version : Java11

## 과제 기능 구현

1. 블로그 검색 (구현 O)
2. 인기 검색어 목록 (구현 O)
3. 카카오 API 요청실패시 네이버 API 요청 (구현 O)
    > * Request QUERY Parameter는 카카오 기반으로 동작합니다.
4. Testcase 작성 (구현 O)
5. 에러 처리 (구현 O)

## 외부 라이브러리
   > * reactor : non-blocking webclient 사용을 위해 도입  
> * lombok : 반복되는 메소드를 Annotaion으로 사용해서 자동 작성  
> * swagger : API 명세 및 Testing
>* jsr305 : 버그 탐지 도구
>* log4j : 로깅 모듈

## 빌드 결과물 다운로드 링크 (.jar)

>https://drive.google.com/file/d/1XHpcpabXI0xPPurY2rQ6DX1_URmt9fXZ/view?usp=share_link