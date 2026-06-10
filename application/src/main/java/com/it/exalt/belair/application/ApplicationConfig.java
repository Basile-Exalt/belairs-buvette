package com.it.exalt.belair.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.it.exalt.belair.domain.order.usecase.CreerCommandeUseCase;
import com.it.exalt.belair.domain.order.usecase.CreerCommandeUseCaseImpl;
import com.it.exalt.belair.domain.order.repository.CommandeRepository;

@Configuration
public class ApplicationConfig {

    @Bean
    public CreerCommandeUseCase createOrderUseCase(CommandeRepository repository) {
        return new CreerCommandeUseCaseImpl(repository);
    }
}
