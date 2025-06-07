package com.kdev5.cleanpick.common.file;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("s3")
class FileUtilsTest {

	@Test
	@DisplayName("올바른 확장자 추출")
	void 확장자_정상_추출() {
		assertEquals("jpg", FileUtils.getExtension("photo.jpg"));
		assertEquals("png", FileUtils.getExtension("image.folder/picture.png"));
		assertEquals("jpeg", FileUtils.getExtension("sample.name.jpeg"));
	}

	@Test
	@DisplayName("파일명이 null이거나 확장자가 없을 경우 예외 발생")
	void 확장자_비정상_파일명_예외() {
		assertThrows(IllegalArgumentException.class, () -> FileUtils.getExtension(null));
		assertThrows(IllegalArgumentException.class, () -> FileUtils.getExtension("파일이름"));
	}

	@Test
	@DisplayName("지원하는 이미지 확장자인 경우 true 반환")
	void 이미지_확장자_검사_성공() {
		assertTrue(FileUtils.isImage("jpg"));
		assertTrue(FileUtils.isImage("png"));
		assertTrue(FileUtils.isImage("gif"));
		assertTrue(FileUtils.isImage("webp"));
	}

	@Test
	@DisplayName("이미지가 아닌 확장자인 경우 false 반환")
	void 이미지_확장자_검사_실패() {
		assertFalse(FileUtils.isImage("exe"));
		assertFalse(FileUtils.isImage("txt"));
		assertFalse(FileUtils.isImage("pdf"));
	}
}
