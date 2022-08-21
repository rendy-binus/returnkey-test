package com.example.returnkeytest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRecord {
    @JsonIgnore
    private String orderId;
    @JsonIgnore
    private String emailAddress;
    private String sku;
    private int quantity;
    private BigDecimal price;
    private String itemName;
}
