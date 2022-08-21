package com.example.returnkeytest.controller;

import com.example.returnkeytest.exception.model.ErrorResponse;
import com.example.returnkeytest.model.dto.OrderReturnDto;
import com.example.returnkeytest.model.dto.createreturn.CreateReturnRequest;
import com.example.returnkeytest.model.dto.createreturn.CreateReturnResponse;
import com.example.returnkeytest.model.dto.updateqcstatus.UpdateQCStatusRequest;
import com.example.returnkeytest.service.ReturnService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/returns")
@RequiredArgsConstructor
@Tag(name = "return", description = "Return Process")
public class ReturnController {
    private final ReturnService service;

    @Operation(summary = "Create Order Return", description = "Returns estimated refund amount")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = CreateReturnResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<CreateReturnResponse> createReturn(@RequestBody @Valid CreateReturnRequest request) {
        return ResponseEntity.ok(service.createReturn(request));
    }

    @Operation(summary = "Get Order Return", description = "Returns Order Return")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = OrderReturnDto.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<OrderReturnDto> getReturn(
            @Parameter(description = "Order Return ID", required = true, schema = @Schema(type = "integer", format = "int64", example = "123"))
            @PathVariable("id") long id) {
        return ResponseEntity.ok(service.getOrderReturn(id));
    }

    @Operation(summary = "Set QC Status", description = "Change Refund Item QC Status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PutMapping(value = "/{id}/items/{itemId}/qc/status", produces = "application/json")
    public ResponseEntity<?> setQCStatus(
            @Parameter(description = "Order Return ID", required = true, schema = @Schema(type = "integer", format = "int64", example = "123"))
            @PathVariable("id") long id,
            @Parameter(description = "Refund Item ID", required = true, schema = @Schema(type = "integer", format = "int64", example = "312"))
            @PathVariable("itemId") long itemId,
            @RequestBody @Valid UpdateQCStatusRequest request) {
        service.updateQCStatus(id, itemId, request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
