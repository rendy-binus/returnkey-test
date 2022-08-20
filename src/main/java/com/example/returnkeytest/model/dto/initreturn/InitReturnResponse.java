package com.example.returnkeytest.model.dto.initreturn;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InitReturnResponse implements Serializable {
    private static final long serialVersionUID = -4973893063914974987L;

    @Schema(example = "5qQqHDQvDQfTNmCKC7o5Ye")
    private String token;
}
