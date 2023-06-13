package com.crypted.kssupport.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket

@Configuration
class SwaggerConfig {
    @Bean
    fun api(): Docket{
        return Docket(DocumentationType.OAS_30)
            .apiInfo(apiInfo())
            .useDefaultResponseMessages(false)
            .select()
            .apis(RequestHandlerSelectors.basePackage("com.crypted.ks.support.kssupport"))
            .paths(PathSelectors.any())
            .build()

    }

    fun apiInfo() : ApiInfo {
        return ApiInfoBuilder()
            .title("API 문서 제목")
            .description("API 상세소개 및 사용법")
            .contact(Contact("Aiden", "x", "aiden.lim@crypted.co.kr"))
            .version("1.0")
            .build();
    }
}