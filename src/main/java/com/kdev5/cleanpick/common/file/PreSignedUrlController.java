package com.kdev5.cleanpick.common.file;

import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kdev5.cleanpick.common.file.dto.PreSignedUrlRequest;
import com.kdev5.cleanpick.common.file.dto.PreSignedUrlResponse;
import com.kdev5.cleanpick.global.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

// TODO: 추후 배포시 제거하기
@Profile("s3")
@RestController
@RequiredArgsConstructor
public class PreSignedUrlController {

	private final PreSignedUrlService preSignedUrlService;

	@PostMapping("images/presigned-url")
	public ResponseEntity<ApiResponse<PreSignedUrlResponse>> getImagePreSignedUrl(@Valid @RequestBody PreSignedUrlRequest preSignedUrlRequest) {
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(ApiResponse.ok(
				preSignedUrlService.generatePreSignedUrl(preSignedUrlRequest)
			)
		);
	}
}
