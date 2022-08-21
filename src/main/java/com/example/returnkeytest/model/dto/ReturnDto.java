package com.example.returnkeytest.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class ReturnDto implements Serializable {
    private static final long serialVersionUID = 4230717248810153347L;

    @Schema(example = "NIKE-7")
    @NotBlank
    private String sku;

    @Schema(example = "1", required = true)
    @Min(value = 1)
    private int quantity;
}
