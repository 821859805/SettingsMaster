package com.k8smaster.config;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusPropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {

    @Bean
    public MybatisPlusPropertiesCustomizer mybatisPlusPropertiesCustomizer() {
        return properties -> {
            if (properties.getConfiguration() != null) {
                properties.getConfiguration().setMapUnderscoreToCamelCase(true);
            }
            properties.setTypeAliasesPackage("com.k8smaster.domain.entity");
        };
    }
}
