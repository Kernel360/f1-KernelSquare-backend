package com.kernel360.kernelsquare.domain.answer.dto;

import com.kernel360.kernelsquare.domain.answer.entity.Answer;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.question.entity.Question;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateAnswerRequest(
	@NotNull
	Long memberId,
	@NotBlank
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
