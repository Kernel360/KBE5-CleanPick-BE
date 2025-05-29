package com.kdev5.cleanpick.common.file;

import java.time.Duration;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kdev5.cleanpick.common.file.dto.PreSignedUrlRequest;
import com.kdev5.cleanpick.common.file.dto.PreSignedUrlResponse;
import com.kdev5.cleanpick.common.file.exception.FileTypeException;
import com.kdev5.cleanpick.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@RequiredArgsConstructor
@Service
public class PreSignedUrlService {

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	private final S3Presigner s3Presigner;

	public PreSignedUrlResponse generatePreSignedUrl(final PreSignedUrlRequest preSignedUrlRequest) {

		final String fileName = preSignedUrlRequest.getFilename();

		final String ext = FileUtils.getExtension(preSignedUrlRequest.getFilename());

		final String contentType = "image/" + ext;

		if(FileUtils.isImage(ext))
			throw new FileTypeException(ErrorCode.NOT_IMAGE_EXTENSION);

		PutObjectRequest putObjectRequest = PutObjectRequest.builder()
			.bucket(bucket)
			.key("images/" + preSignedUrlRequest.getType() + "/" +
				UUID.randomUUID().toString() + "." + ext)
			.contentType(contentType)
			.build();

		PutObjectPresignRequest preSignRequest = PutObjectPresignRequest.builder()
			.putObjectRequest(putObjectRequest)
			.signatureDuration(Duration.ofMinutes(10))
			.build();

		final String url = s3Presigner.presignPutObject(preSignRequest).url().toString();

		return new PreSignedUrlResponse(url, fileName, contentType);
	}
}
