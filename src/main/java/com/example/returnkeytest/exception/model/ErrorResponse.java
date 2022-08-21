package com.example.returnkeytest.exception.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse implements Serializable {
    private static final long serialVersionUID = -9148098136756678434L;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ", shape = JsonFormat.Shape.STRING)
    private OffsetDateTime timestamp;

    private Status status;

    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String debugMessage;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<SubError> subErrors;

    public Map<String, Object> toAttributeMap() {
        return Map.of(
                "timestamp", this.timestamp,
                "status", this.status,
                "message", this.message,
                "debugMessage", this.debugMessage,
                "subErrors", this.subErrors
        );
    }

    public static class ErrorResponseBuilder {
        private OffsetDateTime timestamp = OffsetDateTime.now();
        private Status status;

        public ErrorResponseBuilder status(String code, String description, HttpStatus httpStatus) {
            this.status = Status.builder()
                    .code(code)
                    .description(description)
                    .httpStatus(httpStatus)
                    .build();
            return this;
        }

        public ErrorResponseBuilder status(HttpStatus httpStatus) {
            this.status = Status.builder()
                    .httpStatus(httpStatus)
                    .build();
            return this;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Status {
        private String code;
        private String description;
        @JsonIgnore
        private HttpStatus httpStatus;

        public String getCode() {
            if (code == null) {
                code = String.valueOf(httpStatus.value());
            }
            return code;
        }

        public String getDescription() {
            if (description == null) {
                description = httpStatus.getReasonPhrase();
            }
            return description;
        }
    }
}
