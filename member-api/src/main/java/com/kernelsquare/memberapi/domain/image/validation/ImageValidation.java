package com.kernelsquare.memberapi.domain.image.validation;

import com.kernelsquare.core.common_response.error.code.CategoryErrorCode;
import com.kernelsquare.core.common_response.error.code.ImageErrorCode;
import com.kernelsquare.core.common_response.error.exception.BusinessException;
import com.kernelsquare.core.type.ImageCategory;
import com.kernelsquare.core.type.ImageExtensionType;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

public class ImageValidation {
    private static final List<String> CATEGORYLIST = ImageCategory.getCategoryList();
    private static final List<String> EXTENSION_LIST = ImageExtensionType.getExtensionList();
    
    public static void validateCategory(String category) {
        if (!CATEGORYLIST.contains(category)) {
            throw new BusinessException(CategoryErrorCode.CATEGORY_NOT_VALID);
        }
    }

    public static void validateFileExists(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new BusinessException(ImageErrorCode.IMAGE_IS_EMPTY);
        }
    }

    public static void validateFileExtension(MultipartFile multipartFile) {
        String extension = StringUtils.getFilenameExtension(multipartFile.getOriginalFilename());

        if(Objects.isNull(extension) || !EXTENSION_LIST.contains(extension)) {
            throw new BusinessException(ImageErrorCode.FILE_EXTENSION_NOT_VALID);
        }
    }
}
