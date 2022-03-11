# instagram-clone

# 사용한 기술들
## spring boot, jpa, mariaDB, swagger, jwt, H2

-----------------------------------------

 AWS로 배포 했다가 KEY를 노출해 버려서 내림...

기본적인 CRUD는 모두 구현했고, DM은 구현하지 않았습니다.
Image가 곧 글인데 웹버전 인스타그램에서는 한장씩만 업로드가 가능해서 
저도 사진 한장씩만 업로드 가능하게 했습니다.

로그인은 컨트롤러가 아니라 필터에 만들었습니다.

인증은 jwt를 사용합니다.

리소스 파일은 개발용, 배포용, oauth 세개 만들어 주시면 됩니다. (실수로 aws키와 구글키를 노출해 버려서 일단 내렸습니다.
oauth는 현재 구글만 되어있으며 id, secret, scope=email을 적어주세요


- 3/8
S3 버킷등의 키값들을 올리지 않아서 배포용으로 프로파일을 설정하고 실행하면 실행이되지 않아서
지금은 로컬용으로 구성되어있습니다.
S3을 사용하시고 싶으시면 본인의 버킷 키값등을 application.yml에 입력한 뒤 util에서 S3Uploader 주석을 푸시고의 
MemberServiceImpl 에서 49~52번째, 58, 77번째줄 주석을 푸시고 59번째줄(로컬에 저장하는 코드)를 주석처리 하시면 됩니다.
ImageServiceImpl 에서도 마찬가지로 53~57번째 , 127번줄 주석을 푸시고 58번째 줄을 주석처리 하시면 됩니다.
ㄴ 몇개를 수정하면서 줄이 계속 바껴서 각 클래스에 주석으로 자세하게 적어놓았습니다.

mariaDB로 개발했지만 누군가 파일을 받고 실행하면 제 db아이디가 제공되지 않기 때문에 실행이 안 돼서, 처음에는 h2로 해주시길 바라겠습니다.
다운받고 localhost:8080/swagger-ui/index.html에서 문서화 볼 수 있습니다.

테스트코드는 랜덤한 더미데이터들을 추가해서 확인하는 식으로 해서, 제 테이블이 없으면 돌아가지 않을겁니다. sql로 추출해서 그거까지 올릴까 하다가
오히려 이해하는데 방해가 될거같아 테스트코드에 @DisplayName으로 설명을 적었습니다. 테스트코드는 어떤식으로 진행됐는지만 참조해주시면 감사하겠습니다.
테스트코드는 h2로 실행해도 전부 적용가능하게 차차 수정해 나가겠습니다.
테스트 커버리지는 73%까지 진행했습니다. (dto getter setter에 대한 테스트도 점차 추가하겠습니다.)

3/11 추가
설정에 어려움이 있으실거같아 추가합니다.
배포용 yml은 이런형식으로 진행되고 버킷에 대한 정보는 따로 추가해주셔야 합니다. 
spring:
  profiles:
    include: oauth
  jpa:
    database: mysql
    show-sql: true
    database-platform: org.hibernate.dialect.MariaDB103Dialect
    hibernate:
      ddl-auto: update
  datasource:
    url: jdbc:mariadb://${rds.hostname}:${rds.port}/${rds.db.name}
    username: ${rds.username}
    password: ${rds.password}

server:
  port: 5000

