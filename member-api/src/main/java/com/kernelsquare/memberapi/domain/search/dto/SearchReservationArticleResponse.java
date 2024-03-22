package com.kernelsquare.memberapi.domain.search.dto;

import com.kernelsquare.core.dto.PageResponse;
import com.kernelsquare.core.dto.Pagination;
import com.kernelsquare.memberapi.domain.reservation_article.dto.FindAllReservationArticleResponse;

import java.util.List;

public record SearchReservationArticleResponse (
    Long totalCount,
    Pagination pagination,
    List<FindAllReservationArticleResponse> reservationArticleList
) {
    public static SearchReservationArticleResponse of(Long totalCount, PageResponse<FindAllReservationArticleResponse> pageResponse) {
        return new SearchReservationArticleResponse(totalCount, pageResponse.pagination(), pageResponse.list());
    }
}
