package com.kernelsquare.adminapi.domain.image.utils;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.kernelsquare.adminapi.domain.image.component.ImageComponent;

public class ImageUtils {

	// 클라이언트에게 받은 이미지 파일을 받아서 s3에 저장할 file path를 만듦
	public static String makeFilePath(String category, MultipartFile multipartFile) {
		UUID uuid = UUID.randomUUID();

		return category + "/" + uuid + multipartFile.getOriginalFilename();
	}

	public static String parseFilePath(String url) {
		if (url != null) {
			String baseUrl = ImageComponent.getBaseUrl();
			return url.replaceFirst(baseUrl + "/", "");
		} else {
			return null;
		}
	}

	public static String makeImageUrl(String filePath) {
		if (filePath != null) {
			String baseUrl = ImageComponent.getBaseUrl();
			return baseUrl + "/" + filePath;
		} else {
			return null;
		}
	}
}
