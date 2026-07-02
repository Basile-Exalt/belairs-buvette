package com.it.exalt.belair.infrastructure.notification;

import com.it.exalt.belair.domain.order.NotificationPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LoggingNotificationAdapter implements NotificationPort {

    private static final Logger log = LoggerFactory.getLogger(LoggingNotificationAdapter.class);

    @Override
    public void notifyOrderAcknowledged(String festivalierId, String commandeId, int etaMinutes) {
        log.info("Notification [order acknowledged] festivalierId={} commandeId={} etaMinutes={}",
                festivalierId, commandeId, etaMinutes);
    }
}
