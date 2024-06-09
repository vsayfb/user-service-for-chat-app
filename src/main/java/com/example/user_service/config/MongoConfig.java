package com.example.user_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
@EnableMongoRepositories(basePackages = "com.example.user_service.repository")
@EnableMongoAuditing
public class MongoConfig {

    @Bean
    ValidatingMongoEventListener validatingMongoEventListener(
            LocalValidatorFactoryBean factory) {
        return new ValidatingMongoEventListener(factory);
    }

}
