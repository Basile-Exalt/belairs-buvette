package com.it.exalt.belair.domain.order.usecase;

import com.it.exalt.belair.domain.order.Article;
import com.it.exalt.belair.domain.order.entity.Commande;
import com.it.exalt.belair.domain.order.dto.CreerCommandeRequest;
import com.it.exalt.belair.domain.order.dto.CreerCommandeResponse;
import com.it.exalt.belair.domain.order.repository.CommandeRepository;

import java.util.UUID;

public class CreerCommandeUseCaseImpl implements CreerCommandeUseCase {
    private final CommandeRepository repository;

    public CreerCommandeUseCaseImpl(CommandeRepository repository) {
        this.repository = repository;
    }

    @Override
    public CreerCommandeResponse create(CreerCommandeRequest request) {
        if (request == null || request.articles() == null || request.articles().isEmpty()) {
            throw new IllegalArgumentException("articles.empty");
        }

        String id = UUID.randomUUID().toString();
        Commande commande = new Commande(id, request.festivalgoerId(), "EN_ATTENTE");
        for (Article a : request.articles()) {
            commande.addLine(a.id(), a.quantity());
        }

        repository.save(commande);
        return new CreerCommandeResponse(commande.id(), commande.status());
    }
}
