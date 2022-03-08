# instagram-clone

# 기술스택
Spring Boot, JPA, AWS, SWAGGER

 /swagger-ui/index.html로 문서화 볼 수 있습니다.

 AWS로 배포 했다가 KEY를 노출해 버려서 내림...

기본적인 CRUD는 모두 구현했고, DM은 구현하지 않았습니다.
Image가 곧 글인데 웹버전 인스타그램에서는 한장씩만 업로드가 가능해서 
저도 사진 한장씩만 업로드 가능하게 했습니다.

로그인은 컨트롤러가 아니라 필터에 만들었습니다.

리소스 파일은
application.yml 과 aws.yml 두개로 구성했고
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
