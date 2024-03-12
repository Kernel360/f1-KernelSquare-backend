package com.kernelsquare.adminapi.domain.image.mapper;

import com.kernelsquare.adminapi.domain.image.dto.ImageDto;
import com.kernelsquare.domainmysql.domain.image.command.ImageCommand;
import com.kernelsquare.domainmysql.domain.image.info.ImageInfo;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface ImageDtoMapper {
    ImageCommand.FindAllImages toCommand(ImageDto.FindAllRequest request);

    ImageDto.FindAllResponse toFindAllResponse(ImageInfo imageInfo);
}
