package com.kernel360.kernelsquare.domain.answer.dto;

import com.kernel360.kernelsquare.domain.answer.entity.Answer;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.question.entity.Question;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateAnswerRequest(
	@NotNull(message = "등록 되지 않은 회원입니다.")
	Long memberId,
	@NotBlank(message = "답변 내용은 필수 입력값입니다.")
	String content,
	String imageUrl
) {
	public static Answer toEntity(
		CreateAnswerRequest createAnswerRequest,
		Question question,
		Member member
	) {
		return Answer.builder()
			.imageUrl(createAnswerRequest.imageUrl())
			.member(member)
			.question(question)
			.content(createAnswerRequest.content())
			.voteCount(0L)
			.build();
	}
}
