package com.example.returnkeytest.model.dto.createreturn;

import com.example.returnkeytest.model.dto.ItemDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

@Data
@NoArgsConstructor
public class CreateReturnRequest implements Serializable {
    private static final long serialVersionUID = 958327384634951156L;

    @Schema(example = "5qQqHDQvDQfTNmCKC7o5Ye", description = "token value from init return")
    @NotBlank
    private String token;

    @Schema(required = true)
    @Size(min = 1)
    private Set<ReturnItem> returnItems;

    @Getter
    @Setter
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    public static class ReturnItem extends ItemDto {
        @Schema(required = true, example = "3")
        @Min(value = 1)
        private int quantity;
    }
}
