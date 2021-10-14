package com.them.orderrelay.framework.config.swagger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Value("${spring.profiles.domain}")
    private String realServerUrl;
    @Value("${spring.profiles.checkHost}")
    private String realServerCheckHosts;

    @Bean
    public Docket swaggerApi() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2).apiInfo(swaggerInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("com.them.orderrelay"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false) // 기본으로 세팅되는 200,401,403,404 메시지를 표시 하지 않음
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()))
                ;
        setIsRealServer(docket);

        return docket;
    }

    private void setIsRealServer(Docket docket) {
        try {
            if(isRealServer())  docket.host(realServerUrl);
        }catch(Exception e){}
    }

    private boolean isRealServer() {
        return getIp().contains(realServerCheckHosts);
    }

    public String getIp(){
        String result = null;
        try {
            result = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            result = "";
        }
        return result;
    }

    private ApiInfo swaggerInfo() {
        return new ApiInfoBuilder().title("포스 API Documentation")
                .description("앱 개발시 사용되는 서버 API에 대한 연동 문서입니다")
                .build();
    }

    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title("Them Pos REST API")
                .description("Delivery Pos Api")
                .version("0.1.0")
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("JWT", "authorization", "header");
    }
    private SecurityContext securityContext() {
        return SecurityContext
                .builder()
                .securityReferences(defaultAuth()).forPaths(PathSelectors.any()).build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
    }
}
