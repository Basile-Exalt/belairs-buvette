package com.it.exalt.belair.domain.order.exception;

public class CommandeNonTrouveeException extends RuntimeException {
    public CommandeNonTrouveeException(String message) {
        super(message);
    }
}
