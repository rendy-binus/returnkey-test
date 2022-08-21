package com.example.returnkeytest.model.dto;

import com.example.returnkeytest.model.QCStatus;
import com.example.returnkeytest.model.ReturnStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderReturnDto implements Serializable {
    private static final long serialVersionUID = 4230717248810153347L;

    @Schema(example = "123")
    private Long id;

    @Schema(example = "105.75")
    private BigDecimal totalRefundAmount;

    private List<RefundItem> refundItems;

    @Schema(enumAsRef = true, example = "COMPLETE")
    private ReturnStatus status;

    @Getter
    @Setter
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public static class RefundItem extends ItemDto {
        @Schema(example = "312")
        private long id;

        @Schema(example = "Small Black T-Shirt")
        private String itemName;

        @Schema(example = "3")
        private int quantity;

        @Schema(example = "35.25")
        private BigDecimal pricePerUnit;

        @Schema(enumAsRef = true, example = "ACCEPTED")
        private QCStatus status;
    }
}
