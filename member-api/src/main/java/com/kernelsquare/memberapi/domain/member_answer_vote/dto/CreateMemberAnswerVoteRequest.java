package com.kernelsquare.memberapi.domain.member_answer_vote.dto;

import com.kernelsquare.domainmysql.domain.answer.entity.Answer;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import com.kernelsquare.domainmysql.domain.member_answer_vote.entity.MemberAnswerVote;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CreateMemberAnswerVoteRequest(
	@NotNull(message = "회원 ID를 입력해 주세요.")
	Long memberId,
	@NotNull(message = "투표 상태를 입력해 주세요.")
	Integer status
) {
	@AssertTrue(message = "Status는 1 또는 -1 이어야 합니다.")
	private boolean isStatusValid() {
		return status == 1 || status == -1;
	}

	public static MemberAnswerVote toEntity(
		CreateMemberAnswerVoteRequest createMemberAnswerVoteRequest,
		Member member,
		Answer answer
	) {
		return MemberAnswerVote.builder()
			.member(member)
			.answer(answer)
			.status(createMemberAnswerVoteRequest.status())
			.build();
	}
}
