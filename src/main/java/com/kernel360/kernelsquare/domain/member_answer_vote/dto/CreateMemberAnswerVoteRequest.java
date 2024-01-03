package com.kernel360.kernelsquare.domain.member_answer_vote.dto;

import com.kernel360.kernelsquare.domain.answer.entity.Answer;
import com.kernel360.kernelsquare.domain.member.entity.Member;
import com.kernel360.kernelsquare.domain.member_answer_vote.entity.MemberAnswerVote;
import jakarta.validation.constraints.*;

public record CreateMemberAnswerVoteRequest (
    @NotNull
    Long memberId,
    @NotNull
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
