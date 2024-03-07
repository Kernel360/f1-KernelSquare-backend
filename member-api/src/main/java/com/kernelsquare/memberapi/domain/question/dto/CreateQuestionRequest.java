package com.kernelsquare.memberapi.domain.question.dto;

import java.util.List;

import com.kernelsquare.core.util.ImageUtils;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.question.entity.Question;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record CreateQuestionRequest(
	@NotNull(message = "회원 ID를 입력해 주세요.")
	Long memberId,
	@NotBlank(message = "질문 제목을 입력해 주세요.")
	@Size(min = 5, max = 100, message = "질문 제목은 5자 이상 100자 이하로 작성해 주세요.")
	String title,
	@NotBlank(message = "질문 내용을 입력해 주세요.")
	@Size(min = 10, max = 10000, message = "질문 내용은 10자 이상 10000자 이하로 작성해 주세요.")
	String content,
	String imageUrl,
	@NotNull(message = "최소 빈 리스트로 입력해 주세요.")
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
