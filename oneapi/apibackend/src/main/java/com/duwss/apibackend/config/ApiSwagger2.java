package com.duwss.apibackend.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
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
 * Knife4j 接口文档配置
 *
 *

 */
@Configuration
@EnableSwagger2
@EnableKnife4j
public class ApiSwagger2 {

    @Bean
    public Docket createRestBmbsApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("users")
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.duwss.apibackend.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("用户相关API")
                .version("1.0")
                .build();
    }

}
