package com.kernelsquare.memberapi.domain.answer.dto;

import com.kernelsquare.memberapi.domain.image.utils.ImageUtils;
import com.kernelsquare.domainmysql.domain.answer.entity.Answer;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.question.entity.Question;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateAnswerRequest(
	@NotNull(message = "회원 ID를 입력해 주세요.")
	Long memberId,
	@NotBlank(message = "답변 내용을 입력해 주세요.")
	@Size(min = 10, max = 10000, message = "답변 내용은 10자 이상 10000자 이하로 작성해 주세요.")
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
