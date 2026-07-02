package com.it.exalt.belair.domain.testutil;

import com.it.exalt.belair.domain.order.NotificationPort;

import java.util.ArrayList;
import java.util.List;

public class InMemoryNotificationAdapter implements NotificationPort {

    public record SentNotification(String festivalierId, String commandeId, int etaMinutes) {
    }

    private final List<SentNotification> sent = new ArrayList<>();

    @Override
    public void notifyOrderAcknowledged(String festivalierId, String commandeId, int etaMinutes) {
        sent.add(new SentNotification(festivalierId, commandeId, etaMinutes));
    }

    public List<SentNotification> getSent() {
        return List.copyOf(sent);
    }

    public boolean hasNotificationFor(String festivalierId, String commandeId) {
        return sent.stream()
                .anyMatch(n -> n.festivalierId().equals(festivalierId) && n.commandeId().equals(commandeId));
    }
}
