package com.example.returnkeytest.controller;

import com.example.returnkeytest.exception.model.ErrorResponse;
import com.example.returnkeytest.model.dto.createreturn.CreateReturnRequest;
import com.example.returnkeytest.model.dto.createreturn.CreateReturnResponse;
import com.example.returnkeytest.service.ReturnService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
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
}
