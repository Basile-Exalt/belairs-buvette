package com.it.exalt.belair.infrastructure;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Configuration
@EnableJpaRepositories(basePackages = "com.it.exalt.belair.infrastructure")
@EntityScan(basePackages = "com.it.exalt.belair.infrastructure")
public class InfrastructureConfig {

}
