package com.kernel360.kernelsquare.domain.member_answer_vote.dto;

import com.kernel360.kernelsquare.domain.answer.entity.Answer;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.member_answer_vote.entity.MemberAnswerVote;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

public record CreateMemberAnswerVoteRequest(
	@NotNull(message = "등록 되지 않은 회원입니다.")
	Long memberId,
	@NotNull(message = "유효 하지 않은 투표 상태입니다.")
	int status
) {
	@AssertTrue(message = "Status must be either 1 or -1")
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
