package com.example.returnkeytest.controller;

import com.example.returnkeytest.exception.model.ErrorResponse;
import com.example.returnkeytest.model.dto.initreturn.InitReturnRequest;
import com.example.returnkeytest.model.dto.initreturn.InitReturnResponse;
import com.example.returnkeytest.service.InitReturnService;
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
@RequestMapping("/pending/returns")
@RequiredArgsConstructor
@Tag(name = "init-return", description = "Initiate Return Process")
public class InitReturnController {

    private final InitReturnService service;

    @Operation(summary = "Initiate Return", description = "Returns token to be used for creating Order Return")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(schema = @Schema(implementation = InitReturnResponse.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<InitReturnResponse> initReturn(@RequestBody @Valid InitReturnRequest request) {
        return ResponseEntity.ok(service.initReturn(request));
    }

}
