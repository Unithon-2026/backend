package com.unithon.meetroute.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MeetRoute API")
                        .description("축산/식자재 영업사원을 위한 지역별 음식점 데이터베이스 형상 관리 서비스 API")
                        .version("v1"));
    }
}
