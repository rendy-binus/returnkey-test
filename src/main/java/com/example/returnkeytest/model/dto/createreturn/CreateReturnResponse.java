package com.example.returnkeytest.model.dto.createreturn;

import com.example.returnkeytest.model.dto.ItemDto;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class CreateReturnResponse implements Serializable {
    private static final long serialVersionUID = -7428508696630090919L;

    @Schema(example = "123")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long id;

    @Schema(example = "105.75")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private BigDecimal totalRefundAmount;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ValidItem> validItems;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<InvalidItem> invalidItems;

    @Getter
    @Setter
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public static class ValidItem extends ItemDto {
        @Schema(example = "3")
        private int quantityToReturn;

        @Schema(example = "35.25")
        private BigDecimal pricePerUnit;
    }

    @Getter
    @Setter
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public static class InvalidItem extends ItemDto {
        @Schema(enumAsRef = true, example = "QUANTITY_INVALID")
        private Reason reason;

        @Schema(example = "5")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Integer quantityToReturn;

        @Schema(example = "3")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Integer quantityAvailable;

        public enum Reason {
            SKU_NOT_FOUND,
            QUANTITY_INVALID,
        }
    }
}
