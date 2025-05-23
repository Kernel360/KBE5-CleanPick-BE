package com.kdev5.cleanpick.review.controller;

import com.kdev5.cleanpick.global.response.ApiResponse;
import com.kdev5.cleanpick.global.response.PageResponse;
import com.kdev5.cleanpick.review.service.ReviewService;
import com.kdev5.cleanpick.review.service.dto.request.WriteReviewRequestDto;
import com.kdev5.cleanpick.review.service.dto.response.ReviewResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    private ResponseEntity<ApiResponse<ReviewResponseDto>> writeReview(
            @Valid @RequestPart(value = "writeReviewRequestDto") WriteReviewRequestDto dto,
            @RequestPart(value = "imgs", required = false) List<MultipartFile> imgs) {
        return ResponseEntity.ok(ApiResponse.ok(reviewService.writeReview(dto, imgs)));
    }

    @GetMapping
    private ResponseEntity<ApiResponse<PageResponse<ReviewResponseDto>>> readMyReview(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(new PageResponse<>(reviewService.readMyReview(pageable))));
    }
}
