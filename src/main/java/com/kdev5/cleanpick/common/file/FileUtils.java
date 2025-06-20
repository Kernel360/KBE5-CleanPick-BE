package com.kdev5.cleanpick.common.file;

import java.util.Arrays;


public class FileUtils {

	private static final String[] imageExtensions = {
		"jpg", "jpeg", "png", "gif", "bmp", "webp", "tiff", "svg"
	};

	public static String getExtension(String filename) {
		if (filename == null || !filename.contains(".")) {
			throw new IllegalArgumentException("유효하지 않은 파일명입니다: " + filename);
		}
		return filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
	}

	public static boolean isImage(String ext) {
		return Arrays.asList(imageExtensions).contains(ext);
	}
}
