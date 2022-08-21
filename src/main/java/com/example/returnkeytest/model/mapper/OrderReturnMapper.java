package com.example.returnkeytest.model.mapper;

import com.example.returnkeytest.model.QCStatus;
import com.example.returnkeytest.model.dto.OrderReturnDto;
import com.example.returnkeytest.model.entity.OrderReturn;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderReturnMapper {
    public OrderReturnDto toDto(OrderReturn entity) {
        OrderReturnDto dto = OrderReturnDto.builder()
                .id(entity.getId())
                .status(entity.getStatus())
                .build();

        List<OrderReturnDto.RefundItem> refundItems = new ArrayList<>();

        entity.getItems().forEach(item -> {
            OrderReturnDto.RefundItem refundItem = OrderReturnDto.RefundItem.builder()
                    .id(item.getId())
                    .sku(item.getSku())
                    .itemName(item.getItemName())
                    .pricePerUnit(item.getPrice())
                    .quantity(item.getQuantity())
                    .status(item.getStatus())
                    .build();

            refundItems.add(refundItem);
        });

        dto.setRefundItems(refundItems);

        BigDecimal totalRefundAmount = refundItems.stream()
                .filter(item -> item.getStatus() == QCStatus.ACCEPTED)
                .map(item -> item.getPricePerUnit().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        dto.setTotalRefundAmount(totalRefundAmount);

        return dto;
    }
}
