# instagram-clone

# /swagger-ui/index.html로 문서화 볼 수 있습니다.

# AWS로 배포 했다가 KEY를 노출해 버려서 내림...

기본적인 CRUD는 모두 구현했고, DM은 구현하지 않았습니다.
Image가 곧 글인데 웹버전 인스타그램에서는 한장씩만 업로드가 가능해서 
저도 사진 한장씩만 업로드 가능하게 했습니다.

로그인은 컨트롤러가 아니라 필터에 만들었습니다.

리소스 파일인
application.yml은 아래 처럼

spring:
  profiles:
    group:
      "dev": "test, oauth"
      "prod": "prod, oauth"


---

spring:
  servlet:
    multipart:
      max-request-size: 10MB
      max-file-size: 10MB
  config:
    activate:
      on-profile: "prod"
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

cloud:
  aws:
    s3:
      bucket:
        region:
          static: ap-northeast-2
        stack:
          auto: false
        credentials:
          instanceProfile: true


server:
  port: 5000


---
spring:
  config:
    activate:
      on-profile: "dev"

  datasource:
    driver-class-name: org.mariadb.jdbc.Driver
    url: jdbc:mariadb://localhost:3307/{}
    username: {}
    password: {}

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        show_sql: true

  mvc:
    hiddenmethod:
      filter:
        enabled: true

  servlet:
    multipart:
      enabled: true
      location: C:\\upload\\image_storage
      max-file-size: 10MB
      max-request-size: 20MB

logging:
  level:
    org:
      springframework:
        security:
          web: trace
    com:
      example: debug

instagram:
  upload:
    path: C:\\upload\\image_storage

---

spring:
  config:
    activate:
      on-profile: "oauth"

  security:
    oauth2:
      client:
        registration:
          google:
            client-id: 
            client-secret: 
            scope: email
            
            
aws.yml은 다음과 같이 구성 했습니다.
cloud:
  aws:
    credentials:
      accessKey:
      secretKey:
      
메인함수에 

@SpringBootApplication
@EnableJpaAuditing
public class Instagram2Application {

    public static final String APPLICATION_LOCATIONS = "spring.config.location="
            + "classpath:application.yml,"
            + "classpath:aws.yml";

    public static void main(String[] args) {
        new SpringApplicationBuilder(Instagram2Application.class)
                .properties(APPLICATION_LOCATIONS)
                .run(args);
    }
    
   다음과 같이 추가하셔야 합니다.
