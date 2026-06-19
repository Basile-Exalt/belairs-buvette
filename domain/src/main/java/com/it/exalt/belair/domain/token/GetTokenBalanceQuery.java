package com.it.exalt.belair.domain.token;

import java.time.LocalDate;

public record GetTokenBalanceQuery(String festivalgoerId, LocalDate day) {
}
