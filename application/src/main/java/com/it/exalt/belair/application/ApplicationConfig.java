package com.it.exalt.belair.application;

import com.it.exalt.belair.domain.order.ArticleCategoryPort;
import com.it.exalt.belair.domain.order.EtaCalculator;
import com.it.exalt.belair.domain.order.NotificationPort;
import com.it.exalt.belair.domain.order.repository.CommandeRepository;
import com.it.exalt.belair.domain.order.usecase.AccuserReceptionCommandeUseCase;
import com.it.exalt.belair.domain.order.usecase.AccuserReceptionCommandeUseCaseImpl;
import com.it.exalt.belair.domain.order.usecase.CreerCommandeUseCase;
import com.it.exalt.belair.domain.order.usecase.CreerCommandeUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Bean
    public CreerCommandeUseCase createOrderUseCase(CommandeRepository repository) {
        return new CreerCommandeUseCaseImpl(repository);
    }

    @Bean
    public EtaCalculator etaCalculator() {
        return new EtaCalculator();
    }

    @Bean
    public AccuserReceptionCommandeUseCase acknowledgeOrderUseCase(CommandeRepository commandeRepository,
                                                                   ArticleCategoryPort articleCategoryPort,
                                                                   NotificationPort notificationPort,
                                                                   EtaCalculator etaCalculator) {
        return new AccuserReceptionCommandeUseCaseImpl(commandeRepository, articleCategoryPort,
                notificationPort, etaCalculator);
    }
}
