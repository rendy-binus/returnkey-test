package com.example.returnkeytest.controller;

import com.example.returnkeytest.model.dto.initreturn.InitReturnRequest;
import com.example.returnkeytest.model.dto.initreturn.InitReturnResponse;
import com.example.returnkeytest.service.ReturnService;
import io.swagger.v3.oas.annotations.Operation;
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

    private final ReturnService service;

    @Operation(summary = "Initiate Return", description = "Returns token to be used for creating Order Return")
    @PostMapping
    public ResponseEntity<InitReturnResponse> initReturn(@RequestBody @Valid InitReturnRequest request) {
        return ResponseEntity.ok(service.initReturn(request));
    }

}
