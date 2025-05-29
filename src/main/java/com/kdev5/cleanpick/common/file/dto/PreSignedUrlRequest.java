package com.kdev5.cleanpick.common.file.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PreSignedUrlRequest {

	@NotBlank
	@Pattern(regexp = "^(reviews|profiles)$", message = "유효하지 않은 타입의 파일입니다.")
	private String type;

	@NotBlank(message = "파일명은 비어있을 수 없습니다.")
	private String filename;

}
