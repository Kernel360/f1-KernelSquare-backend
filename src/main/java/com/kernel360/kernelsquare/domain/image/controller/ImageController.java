package com.kernel360.kernelsquare.domain.image.controller;

import com.kernel360.kernelsquare.domain.image.service.ImageService;
import com.kernel360.kernelsquare.global.common_response.ApiResponse;
import com.kernel360.kernelsquare.global.common_response.ResponseEntityFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.kernel360.kernelsquare.global.common_response.response.code.ImageResponseCode.IMAGE_UPLOAD_COMPLETED;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping("/images/upload")
    public ResponseEntity<ApiResponse<String>> uploadFile(
        @RequestParam
        String category,
        @RequestPart(value = "file")
        MultipartFile multipartFile
    ) {
        String url = imageService.uploadImage(category, multipartFile);

        return ResponseEntityFactory.toResponseEntity(IMAGE_UPLOAD_COMPLETED, url);
    }
}
