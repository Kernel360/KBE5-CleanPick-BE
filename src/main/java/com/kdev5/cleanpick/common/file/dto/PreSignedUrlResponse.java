package com.kdev5.cleanpick.common.file.dto;

import lombok.Getter;


@Getter
public class PreSignedUrlResponse {

	private final String url;

	private final String filename;

	private final String contentType;

	public PreSignedUrlResponse(String url, String filename, String contentType) {
		this.url = url;
		this.filename = filename;
		this.contentType = contentType;
	}
}
