package com.kernelsquare.domains3.domain.image.repository;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStore {
    String store(String category, MultipartFile multipartFile);

    void delete(String imageUrl);
}
