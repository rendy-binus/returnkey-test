package com.example.returnkeytest.model;

import java.util.*;

public enum OrderCsvHeader {
    ORDER_ID("orderId"),
    EMAIL_ADDRESS("emailAddress"),
    SKU("sku"),
    QUANTITY("quantity"),
    PRICE("price"),
    ITEM_NAME("itemName"),
    ;

    public static final List<OrderCsvHeader> ALL = new ArrayList<>();
    private static final Map<String, OrderCsvHeader> BY_VALUE = new HashMap<>();

    static {
        for (OrderCsvHeader header : values()) {
            ALL.add(header);
            BY_VALUE.put(header.val.toLowerCase(), header);
        }
    }

    private final String val;

    OrderCsvHeader(String val) {
        this.val = val;
    }

    public static OrderCsvHeader fromValue(String value) {
        return BY_VALUE.get(value.toLowerCase());
    }

    public String getVal() {
        return val;
    }
}
