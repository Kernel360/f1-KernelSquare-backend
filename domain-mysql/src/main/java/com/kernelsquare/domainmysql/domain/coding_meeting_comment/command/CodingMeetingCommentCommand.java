package com.kernelsquare.domainmysql.domain.coding_meeting_comment.command;

import com.kernelsquare.domainmysql.domain.coding_meeting.entity.CodingMeeting;
import com.kernelsquare.domainmysql.domain.coding_meeting_comment.entity.CodingMeetingComment;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import lombok.Builder;

public class CodingMeetingCommentCommand {

    @Builder
    public record CreateCommand(
            String codingMeetingToken,
            String codingMeetingCommentContent
    ) {
        public CodingMeetingComment toEntity(Member member, CodingMeeting codingMeeting) {
            return CodingMeetingComment.builder()
                    .member(member)
                    .codingMeeting(codingMeeting)
                    .codingMeetingCommentContent(codingMeetingCommentContent)
                    .build();
        }
    }

    @Builder
    public record UpdateCommand(
            String codingMeetingCommentContent
    ) {
    }
}
