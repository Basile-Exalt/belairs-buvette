package com.it.exalt.belair.domain.order.usecase;

import com.it.exalt.belair.domain.order.dto.AccuserReceptionCommandeCommand;
import com.it.exalt.belair.domain.order.dto.AccuserReceptionCommandeResult;

public interface AccuserReceptionCommandeUseCase {
    AccuserReceptionCommandeResult acknowledge(AccuserReceptionCommandeCommand command);
}
