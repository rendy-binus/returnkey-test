package com.example.returnkeytest.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public abstract class ItemDto {
    @Schema(example = "MENS-156")
    @NotBlank
    private String sku;
}
