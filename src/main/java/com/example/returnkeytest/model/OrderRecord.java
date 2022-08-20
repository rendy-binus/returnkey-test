package com.example.returnkeytest.model;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRecord {
    private String orderId;
    private String emailAddress;
    private String sku;
    private int quantity;
    private BigDecimal price;
    private String itemName;
}
