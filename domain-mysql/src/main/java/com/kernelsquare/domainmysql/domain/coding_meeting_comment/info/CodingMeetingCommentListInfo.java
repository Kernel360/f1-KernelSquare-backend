package com.kernelsquare.domainmysql.domain.coding_meeting_comment.info;

import com.kernelsquare.core.util.ImageUtils;
import com.kernelsquare.domainmysql.domain.coding_meeting_comment.entity.CodingMeetingComment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CodingMeetingCommentListInfo {
    private final Long memberId;
    private final Long memberLevel;
    private final String memberNickname;
    private final String memberProfileUrl;
    private final String memberLevelImageUrl;
    private final LocalDateTime createdDate;
    private final String codingMeetingCommentToken;
    private final String codingMeetingCommentContent;

    @Builder
    public CodingMeetingCommentListInfo(CodingMeetingComment codingMeetingComment) {
        this.memberId = codingMeetingComment.getMember().getId();
        this.memberLevel = codingMeetingComment.getMember().getLevel().getName();
        this.memberNickname = codingMeetingComment.getMember().getNickname();
        this.memberProfileUrl = ImageUtils.makeImageUrl(codingMeetingComment.getMember().getImageUrl());
        this.memberLevelImageUrl = ImageUtils.makeImageUrl(codingMeetingComment.getMember().getLevel().getImageUrl());
        this.createdDate = codingMeetingComment.getCreatedDate();
        this.codingMeetingCommentToken = codingMeetingComment.getCodingMeetingCommentToken();
        this.codingMeetingCommentContent = codingMeetingComment.getCodingMeetingCommentContent();
    }

    public static CodingMeetingCommentListInfo of(CodingMeetingComment codingMeetingComment) {
        return CodingMeetingCommentListInfo.builder()
                .codingMeetingComment(codingMeetingComment)
                .build();
    }
}
