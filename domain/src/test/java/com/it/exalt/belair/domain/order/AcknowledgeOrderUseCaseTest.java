package com.it.exalt.belair.domain.order;

import com.it.exalt.belair.domain.order.dto.AccuserReceptionCommandeCommand;
import com.it.exalt.belair.domain.order.dto.AccuserReceptionCommandeResult;
import com.it.exalt.belair.domain.order.entity.Commande;
import com.it.exalt.belair.domain.order.exception.CommandeNonTrouveeException;
import com.it.exalt.belair.domain.order.usecase.AccuserReceptionCommandeUseCaseImpl;
import com.it.exalt.belair.domain.testutil.InMemoryArticleCategoryAdapter;
import com.it.exalt.belair.domain.testutil.InMemoryCommandeRepository;
import com.it.exalt.belair.domain.testutil.InMemoryNotificationAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AcknowledgeOrderUseCaseTest {

    private InMemoryCommandeRepository commandeRepository;
    private InMemoryArticleCategoryAdapter articleCategoryAdapter;
    private InMemoryNotificationAdapter notificationAdapter;
    private AccuserReceptionCommandeUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        commandeRepository = new InMemoryCommandeRepository();
        articleCategoryAdapter = new InMemoryArticleCategoryAdapter();
        notificationAdapter = new InMemoryNotificationAdapter();
        useCase = new AccuserReceptionCommandeUseCaseImpl(
                commandeRepository,
                articleCategoryAdapter,
                notificationAdapter,
                new EtaCalculator()
        );
    }

    // -------------------------------------------------------------------------
    // Happy path
    // -------------------------------------------------------------------------

    @Test
    void givenOrderWithMealsAndNormalDrinks_whenBartenderAcknowledges_thenEtaIsComputedAndGoerIsNotified() {
        // Given an order with 1 meal type and 1 normal drink type
        Commande commande = new Commande("order-1", "goer-42", "EN_ATTENTE");
        commande.addLine("Burger", 1);
        commande.addLine("Bière", 1);
        commandeRepository.save(commande);

        articleCategoryAdapter.register("Burger", ArticleCategory.MEAL);
        articleCategoryAdapter.register("Bière", ArticleCategory.NORMAL_ALCOHOLIC_DRINK);

        // When the bartender acknowledges the order
        AccuserReceptionCommandeResult result = useCase.acknowledge(
                new AccuserReceptionCommandeCommand("order-1"));

        // Then ETA = max(2, 10+2) = 12 minutes
        assertThat(result.commandeId()).isEqualTo("order-1");
        assertThat(result.etaMinutes()).isEqualTo(12);
    }

    @Test
    void givenOrderWithMealsAndNormalDrinks_whenBartenderAcknowledges_thenOrderStatusIsUpdatedToEnPreparation() {
        // Given an order in EN_ATTENTE state
        Commande commande = new Commande("order-2", "goer-7", "EN_ATTENTE");
        commande.addLine("Pizza", 2);
        commandeRepository.save(commande);
        articleCategoryAdapter.register("Pizza", ArticleCategory.MEAL);

        // When the bartender acknowledges the order
        useCase.acknowledge(new AccuserReceptionCommandeCommand("order-2"));

        // Then the order status is updated to EN_PREPARATION
        Commande updated = commandeRepository.findById("order-2").orElseThrow();
        assertThat(updated.status()).isEqualTo("EN_PREPARATION");
    }

    @Test
    void givenOrderWithMealsAndNormalDrinks_whenBartenderAcknowledges_thenFestivalGoerReceivesNotificationWithEta() {
        // Given an order for goer-99
        Commande commande = new Commande("order-3", "goer-99", "EN_ATTENTE");
        commande.addLine("Mojito", 1);
        commandeRepository.save(commande);
        articleCategoryAdapter.register("Mojito", ArticleCategory.NORMAL_ALCOHOLIC_DRINK);

        // When acknowledged
        useCase.acknowledge(new AccuserReceptionCommandeCommand("order-3"));

        // Then goer-99 receives a notification for order-3
        assertThat(notificationAdapter.hasNotificationFor("goer-99", "order-3")).isTrue();
        InMemoryNotificationAdapter.SentNotification notification = notificationAdapter.getSent().get(0);
        assertThat(notification.etaMinutes()).isEqualTo(2);
    }

    @Test
    void givenOrderWithOnlyNonAlcoholicDrinks_whenAcknowledged_thenEtaIsOneMinutePerType() {
        // Given an order with 2 distinct non-alcoholic drink types
        Commande commande = new Commande("order-4", "goer-5", "EN_ATTENTE");
        commande.addLine("Eau plate", 1);
        commande.addLine("Jus d'orange", 1);
        commandeRepository.save(commande);
        articleCategoryAdapter.register("Eau plate", ArticleCategory.NON_ALCOHOLIC_DRINK);
        articleCategoryAdapter.register("Jus d'orange", ArticleCategory.NON_ALCOHOLIC_DRINK);

        AccuserReceptionCommandeResult result = useCase.acknowledge(
                new AccuserReceptionCommandeCommand("order-4"));

        // Then ETA = 2 * 1 = 2 minutes
        assertThat(result.etaMinutes()).isEqualTo(2);
    }

    @Test
    void givenOrderWithPremiumDrinksAndSnacks_whenAcknowledged_thenEtaIsSumOfDrinkAndSnackTimes() {
        // Given 1 premium drink type + 2 snack types
        Commande commande = new Commande("order-5", "goer-3", "EN_ATTENTE");
        commande.addLine("Champagne", 1);
        commande.addLine("Chips", 1);
        commande.addLine("Cacahuetes", 1);
        commandeRepository.save(commande);
        articleCategoryAdapter.register("Champagne", ArticleCategory.PREMIUM_ALCOHOLIC_DRINK);
        articleCategoryAdapter.register("Chips", ArticleCategory.SNACK);
        articleCategoryAdapter.register("Cacahuetes", ArticleCategory.SNACK);

        AccuserReceptionCommandeResult result = useCase.acknowledge(
                new AccuserReceptionCommandeCommand("order-5"));

        // Then ETA = 3 (premium) + 4 (2 snacks * 2) = 7 minutes
        assertThat(result.etaMinutes()).isEqualTo(7);
    }

    // -------------------------------------------------------------------------
    // Error cases
    // -------------------------------------------------------------------------

    @Test
    void givenNonExistentOrderId_whenAcknowledging_thenCommandeNonTrouveeExceptionIsThrown() {
        // Given no order exists with id "unknown-id"
        // When acknowledging
        assertThatThrownBy(() ->
                useCase.acknowledge(new AccuserReceptionCommandeCommand("unknown-id")))
                .isInstanceOf(CommandeNonTrouveeException.class)
                .hasMessageContaining("COMMANDE_NON_TROUVEE");
    }
}
