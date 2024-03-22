package com.kernelsquare.memberapi.domain.search.dto;

import com.kernelsquare.core.dto.PageResponse;
import com.kernelsquare.core.dto.Pagination;
import com.kernelsquare.memberapi.domain.coding_meeting.dto.CodingMeetingDto;

import java.util.List;

public record SearchCodingMeetingResponse(
    Long totalCount,
    Pagination pagination,
    List<CodingMeetingDto.FindAllResponse> reservationArticleList
) {
    public static SearchCodingMeetingResponse of(Long totalCount, PageResponse<CodingMeetingDto.FindAllResponse> pageResponse) {
        return new SearchCodingMeetingResponse(totalCount, pageResponse.pagination(), pageResponse.list());
    }
}
