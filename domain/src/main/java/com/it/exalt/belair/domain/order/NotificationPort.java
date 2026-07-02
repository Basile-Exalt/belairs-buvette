package com.it.exalt.belair.domain.order;

public interface NotificationPort {
    void notifyOrderAcknowledged(String festivalierId, String commandeId, int etaMinutes);
}
