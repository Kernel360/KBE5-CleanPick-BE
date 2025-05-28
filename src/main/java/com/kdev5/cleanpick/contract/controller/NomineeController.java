package com.kdev5.cleanpick.contract.controller;

import com.kdev5.cleanpick.contract.service.ReadNomineeService;
import com.kdev5.cleanpick.contract.service.dto.response.ReadRequestedMatchingResponseDto;
import com.kdev5.cleanpick.global.response.ApiResponse;
import com.kdev5.cleanpick.global.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/nominee")
public class NomineeController {

    private final ReadNomineeService readNomineeService;

    private static final Long managerId = 1L; // TODO

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ReadRequestedMatchingResponseDto>>> readRequestedMatching(@RequestParam("isPersonal") Boolean isPersonal, Pageable pageable) {

        return ResponseEntity.ok(ApiResponse.ok(new PageResponse<>(readNomineeService.readRequestedMatching(managerId, isPersonal, pageable))));
    }
}
