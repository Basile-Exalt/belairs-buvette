package com.it.exalt.belair.domain.order.usecase;

import com.it.exalt.belair.domain.order.ArticleCategory;
import com.it.exalt.belair.domain.order.ArticleCategoryPort;
import com.it.exalt.belair.domain.order.EtaCalculator;
import com.it.exalt.belair.domain.order.NotificationPort;
import com.it.exalt.belair.domain.order.dto.AccuserReceptionCommandeCommand;
import com.it.exalt.belair.domain.order.dto.AccuserReceptionCommandeResult;
import com.it.exalt.belair.domain.order.entity.Commande;
import com.it.exalt.belair.domain.order.exception.CommandeNonTrouveeException;
import com.it.exalt.belair.domain.order.repository.CommandeRepository;

import java.util.List;
import java.util.Objects;

public class AccuserReceptionCommandeUseCaseImpl implements AccuserReceptionCommandeUseCase {

    static final String STATUS_EN_PREPARATION = "EN_PREPARATION";

    private final CommandeRepository commandeRepository;
    private final ArticleCategoryPort articleCategoryPort;
    private final NotificationPort notificationPort;
    private final EtaCalculator etaCalculator;

    public AccuserReceptionCommandeUseCaseImpl(CommandeRepository commandeRepository,
                                               ArticleCategoryPort articleCategoryPort,
                                               NotificationPort notificationPort,
                                               EtaCalculator etaCalculator) {
        this.commandeRepository = Objects.requireNonNull(commandeRepository, "commandeRepository must not be null");
        this.articleCategoryPort = Objects.requireNonNull(articleCategoryPort, "articleCategoryPort must not be null");
        this.notificationPort = Objects.requireNonNull(notificationPort, "notificationPort must not be null");
        this.etaCalculator = Objects.requireNonNull(etaCalculator, "etaCalculator must not be null");
    }

    @Override
    public AccuserReceptionCommandeResult acknowledge(AccuserReceptionCommandeCommand command) {
        Objects.requireNonNull(command, "command must not be null");

        Commande commande = commandeRepository.findById(command.commandeId())
                .orElseThrow(() -> new CommandeNonTrouveeException("COMMANDE_NON_TROUVEE"));

        List<ArticleCategory> categories = commande.lines().stream()
                .map(line -> articleCategoryPort.getCategory(line.article()))
                .toList();

        int etaMinutes = etaCalculator.compute(categories);

        commandeRepository.updateStatus(commande.id(), STATUS_EN_PREPARATION);

        notificationPort.notifyOrderAcknowledged(commande.festivalierId(), commande.id(), etaMinutes);

        return new AccuserReceptionCommandeResult(commande.id(), etaMinutes);
    }
}
