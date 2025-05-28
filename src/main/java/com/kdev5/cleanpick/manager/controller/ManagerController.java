package com.kdev5.cleanpick.manager.controller;

import com.kdev5.cleanpick.global.response.ApiResponse;
import com.kdev5.cleanpick.global.response.PageResponse;
import com.kdev5.cleanpick.manager.domain.enumeration.SortType;
import com.kdev5.cleanpick.manager.service.ManagerService;
import com.kdev5.cleanpick.manager.service.dto.response.ManagerSearchResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/manager")
public class ManagerController {

    private final ManagerService managerService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<ManagerSearchResponseDto>>> search (
            @RequestParam(required = false)
            String cleaning,
            @RequestParam(required = false)
            String region,
            @RequestParam(required = false)
            String keyword,                             // 검색어
            @RequestParam(defaultValue = "RECOMMENDATION")
            SortType sortType,
            Pageable pageable
    ) {
        Page<ManagerSearchResponseDto> result = managerService.searchManagers(cleaning, region, keyword, sortType, pageable);
        return ResponseEntity.ok(ApiResponse.ok(new PageResponse<>(result)));
    }
}
