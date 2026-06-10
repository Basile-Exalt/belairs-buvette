package com.it.exalt.belair.domain.order.usecase;

import com.it.exalt.belair.domain.order.dto.CreerCommandeRequest;
import com.it.exalt.belair.domain.order.dto.CreerCommandeResponse;

public interface CreerCommandeUseCase {
    CreerCommandeResponse create(CreerCommandeRequest request);
}
