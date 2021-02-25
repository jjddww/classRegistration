# classRegistration
# 수강신청 도움 앱

### 개발환경
*서버 - Linux Ubuntu, Node.js

*클라이언트 - Android Studio, Java

*데이터베이스 - MYSQL

*통신 - Retrofit2


### 1. 로그인 및 로그아웃
서버로부터 받은 응답에 따라 처리하도록 설계

로그인 성공 시

![로그인](https://user-images.githubusercontent.com/50095740/109131934-a1e6f680-7796-11eb-8668-2a7963c51d9b.gif)  

정보가 틀렸을 경우

![비번틀림](https://user-images.githubusercontent.com/50095740/109130261-dce82a80-7794-11eb-9c4f-72e2981e516c.gif)  

로그아웃

![로그아웃](https://user-images.githubusercontent.com/50095740/109129950-8975dc80-7794-11eb-91aa-5002149384ed.gif)




### 2. 세션 만료 시 화면
로그인 성공 시 서버로부터 15분의 유효시간을 가진 토큰을 받아 사용. (Node.js의 JWT 라이브러리 사용하여 토큰 발급)

통신을 할 때 마다 Http 헤더에 토큰을 붙여 자료를 요청. 

서버는 앱으로부터 받은 토큰이 유효한지 검사하여 토큰이 만료되었다면 401코드를 전송.
사용자가 재로그인 하도록 유도.

![세션만료](https://user-images.githubusercontent.com/50095740/109131452-2a18cc00-7796-11eb-8c17-9163a515b07a.gif)


### 3. 과목 조회
Spinner가 자신의 전공으로 미리 셋팅 되어 있음. 

Retrofit통신으로 서버로부터 받아온 json 데이터를 파싱한 후 리사이클러뷰를 사용하여 목록을 보임.


![과목조회](https://user-images.githubusercontent.com/50095740/109138959-4caee300-779e-11eb-832b-67500e00b7be.gif)


### 4. 장바구니 담기, 삭제. 
리사이클러뷰를 사용하여 목록을 보임.
이미 장바구니에 담은 과목을 담으려 시도할 경우 메시지를 띄움.

조회 화면


강의 신청완료, 중복신청 메시지



### 5. 수강신청 및 시간표 확인. 

리사이클러뷰를 사용하여 목록을 보임.


