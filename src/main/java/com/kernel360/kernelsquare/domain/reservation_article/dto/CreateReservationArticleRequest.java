package com.kernel360.kernelsquare.domain.reservation_article.dto;

import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.reservation_article.entity.ReservationArticle;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;


public record CreateReservationArticleRequest(
        @NotNull(message = "멤버 ID는 필수 항목입니다.")
        Long memberId,
        @NotBlank(message = "제목을 입력해주세요.")
        String title,
        @NotBlank(message = "내용을 작성해주세요.")
        String content,
        List<String> hashTags,
        @NotNull(message = "시간을 선택해주세요.")
        List<LocalDateTime> dateTimes
)  {
        public static ReservationArticle toEntity(CreateReservationArticleRequest createReservationArticleRequest, Member member) {
                return ReservationArticle.builder()
                        .title(createReservationArticleRequest.title())
                        .content(createReservationArticleRequest.content())
                        .member(member)
                        .build();
        }

}