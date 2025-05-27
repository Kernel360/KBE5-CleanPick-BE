package com.kdev5.cleanpick.common.file;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kdev5.cleanpick.common.file.exception.FileTypeException;
import com.kdev5.cleanpick.global.exception.ErrorCode;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/s3")
public class S3PreSignedUrlController {

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	private final S3Presigner s3Presigner;

	@GetMapping("/upload-url")
	public String getImagePreSignedUrl(@Valid @ModelAttribute PreSignedUrlRequest preSignedUrlRequest) {

		final String fileName = preSignedUrlRequest.getFilename();

		final String ext = FileUtils.getExtension(preSignedUrlRequest.getFilename());

		if(FileUtils.isImage(ext))
			throw new FileTypeException(ErrorCode.NOT_IMAGE_EXTENSION);

		return generatePreSignedUrl(preSignedUrlRequest.getType(), ext);
	}

	private String generatePreSignedUrl(String type, String ext) {
		PutObjectRequest putObjectRequest = PutObjectRequest.builder()
			.bucket(bucket)
			.key("images/" + type + "/" + UUID.randomUUID().toString() + "." + ext)
			.contentType("image/" + ext)
			.build();

		PutObjectPresignRequest preSignRequest = PutObjectPresignRequest.builder()
			.putObjectRequest(putObjectRequest)
			.signatureDuration(Duration.ofMinutes(10))
			.build();

		return s3Presigner.presignPutObject(preSignRequest).toString();
	}
}
