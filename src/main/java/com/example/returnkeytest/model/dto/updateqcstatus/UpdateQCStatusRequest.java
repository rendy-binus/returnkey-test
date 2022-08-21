package com.example.returnkeytest.model.dto.updateqcstatus;

import com.example.returnkeytest.model.QCStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class UpdateQCStatusRequest implements Serializable {
    private static final long serialVersionUID = 6513169002201500798L;

    @Schema(required = true, enumAsRef = true, example = "ACCEPTED")
    private QCStatus status;
}
