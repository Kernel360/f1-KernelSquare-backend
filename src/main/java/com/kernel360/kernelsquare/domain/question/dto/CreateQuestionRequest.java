package com.kernel360.kernelsquare.domain.question.dto;

import com.kernel360.kernelsquare.domain.image.utils.ImageUtils;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.question.entity.Question;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreateQuestionRequest(
	@NotNull(message = "멤버 Id는 필수 항목입니다.")
	Long memberId,
	@NotBlank(message = "빈 제목은 안됩니다.")
	String title,
	@NotBlank(message = "빈 내용은 안됩니다.")
	String content,
	String imageUrl,
	@NotNull(message = "최소 빈 리스트로 들어와야 합니다.")
	List<String> skills
) {
    public static Question toEntity(CreateQuestionRequest createQuestionRequest, Member member) {
        return Question.builder()
            .title(createQuestionRequest.title())
            .content(createQuestionRequest.content())
            .imageUrl(ImageUtils.parseFilePath(createQuestionRequest.imageUrl()))
            .viewCount(0L)
            .closedStatus(false)
            .member(member)
            .build();
    }
}
