package com.it.exalt.belair.domain.testutil;

import java.util.HashMap;
import java.util.Map;

public class InMemoryStock {
    private static final Map<String, Integer> MAP = new HashMap<>();

    public void setQuantity(String item, int qty) { MAP.put(item, qty); }

    public int getQuantity(String item) { return MAP.getOrDefault(item, 0); }

    public static int getQuantityStatic(String item) { return MAP.getOrDefault(item, 0); }

    public static void decrement(String item, int qty) { MAP.put(item, getQuantityStatic(item) - qty); }
}
