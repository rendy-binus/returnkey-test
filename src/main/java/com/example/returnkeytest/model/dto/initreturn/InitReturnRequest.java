package com.example.returnkeytest.model.dto.initreturn;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class InitReturnRequest implements Serializable {
    private static final long serialVersionUID = 8761993241787909696L;

    @Schema(example = "RK-478")
    @NotBlank
    private String orderId;

    @Schema(example = "john@example.com")
    @NotBlank
    @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String emailAddress;
}
