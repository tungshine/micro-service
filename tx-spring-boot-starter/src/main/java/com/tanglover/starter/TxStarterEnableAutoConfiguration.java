package com.tanglover.starter;

import com.tanglover.starter.config.PersonProperties;
import com.tanglover.starter.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author TangXu
 * @create 2019-06-24 11:44
 * @description:
 */
@Configuration
@ConditionalOnClass(PersonService.class)
@EnableConfigurationProperties(PersonProperties.class)
@ConditionalOnProperty(prefix = "spring.person", value = "enabled", matchIfMissing = true)
public class TxStarterEnableAutoConfiguration {

    @Autowired
    private PersonProperties properties;

    @Bean
    @ConditionalOnMissingBean(PersonService.class)
    public PersonService personService() {
        PersonService personService = new PersonService(properties);
        return personService;
    }
}