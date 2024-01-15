package com.kernel360.kernelsquare.domain.reservation_article.dto;

import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.reservation_article.entity.ReservationArticle;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record CreateReservationArticleRequest(
        @NotNull(message = "회원 ID를 입력해 주세요.")
        Long memberId,
        @NotBlank(message = "예약창 제목을 입력해 주세요.")
        String title,
        @NotBlank(message = "예약창 내용을 입력해 주세요.")
        String content,
        @NotNull(message = "최소 빈 리스트로 입력해 주세요.")
        List<String> hashTags,
        @NotNull(message = "예약 시간을 입력해 주세요.")
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