package com.example.progettoflesca.authentication;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
//@Valiated
@Configuration
@ConfigurationProperties(prefix = "jwt.auth.converter")
public  class  JwtAuthConverterProperties {

    private String resourceId;
    private String principalAttribute;
}
