package com.example.returnkeytest.exception.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ValidationError extends SubError implements Serializable {
    private static final long serialVersionUID = 4050230412492309678L;

    private String object;
    private String field;
    private Object rejectedValue;
}
