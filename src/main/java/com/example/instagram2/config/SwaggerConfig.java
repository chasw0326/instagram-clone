package com.example.instagram2.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * <code>SwaggerConfig</code><br>
 * 스웨거 설정
 * @author chasw326
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    /**
     * 스웨거 2.0이 아니라 3.0이기 때문에<br>
     * http://localhost:8080/swagger-ui/index.html 링크로 접속해야 합니다.
     */
    @Bean
    public Docket api(){
        return new Docket(DocumentationType.OAS_30)
                .useDefaultResponseMessages(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.instagram2.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    /**
     * 스웨거 설정입니다.
     */
    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("Instagram2 Swagger")
                .description("controller")
                .version("1.0")
                .build();
    }

}
