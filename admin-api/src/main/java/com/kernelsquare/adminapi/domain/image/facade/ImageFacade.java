package com.kernelsquare.adminapi.domain.image.facade;

import com.kernelsquare.adminapi.domain.image.dto.ImageDto;
import com.kernelsquare.adminapi.domain.image.mapper.ImageDtoMapper;
import com.kernelsquare.adminapi.domain.image.service.ImageService;
import com.kernelsquare.domainmysql.domain.image.info.ImageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageFacade {
    private final ImageDtoMapper imageDtoMapper;
    private final ImageService imageService;


    public ImageDto.FindAllResponse findAllImages(ImageDto.FindAllRequest request) {
        ImageInfo imageInfo = imageService.findAllImages(imageDtoMapper.toCommand(request));
        return imageDtoMapper.toFindAllResponse(imageInfo);
    }
}
