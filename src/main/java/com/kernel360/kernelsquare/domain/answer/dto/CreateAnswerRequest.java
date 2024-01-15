package com.kernel360.kernelsquare.domain.answer.dto;

import com.kernel360.kernelsquare.domain.answer.entity.Answer;
import com.kernel360.kernelsquare.domain.image.utils.ImageUtils;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.question.entity.Question;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateAnswerRequest(
	@NotNull(message = "회원 ID를 입력해 주세요.")
	Long memberId,
	@NotBlank(message = "답변 내용을 입력해 주세요.")
	String content,
	String imageUrl
) {
    public static Answer toEntity(
            CreateAnswerRequest createAnswerRequest,
            Question question,
            Member member
    ) {
        return Answer.builder()
                .imageUrl(ImageUtils.parseFilePath(createAnswerRequest.imageUrl()))
                .member(member)
                .question(question)
                .content(createAnswerRequest.content())
                .voteCount(0L)
                .build();
    }
}
