package com.kernelsquare.domainmysql.domain.coding_meeting.info;

import com.kernelsquare.domainmysql.domain.coding_meeting.entity.CodingMeeting;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CodingMeetingListInfo {
    private final Long memberId;
    private final Long memberLevel;
    private final String memberNickname;
    private final String memberProfileUrl;
    private final String memberLevelImageUrl;

    private final String codingMeetingTitle;
    private final String codingMeetingToken;
    private final List<String> codingMeetingHashtags;

    private final LocalDateTime codingMeetingStartTime;
    private final LocalDateTime codingMeetingEndTime;
    private final Boolean codingMeetingClosed;
    private final LocalDateTime createdDate;

    @Builder
    public CodingMeetingListInfo(CodingMeeting codingMeeting) {
        this.memberId = codingMeeting.getMember().getId();
        this.memberLevel = codingMeeting.getMember().getLevel().getName();
        this.memberNickname = codingMeeting.getMember().getNickname();
        this.memberProfileUrl = codingMeeting.getMember().getImageUrl();
        this.memberLevelImageUrl = codingMeeting.getMember().getLevel().getImageUrl();

        this.codingMeetingTitle = codingMeeting.getCodingMeetingTitle();
        this.codingMeetingToken = codingMeeting.getCodingMeetingToken();
        this.codingMeetingHashtags = codingMeeting.getCodingMeetingHashtags();

        this.codingMeetingStartTime = codingMeeting.getCodingMeetingStartTime();
        this.codingMeetingEndTime = codingMeeting.getCodingMeetingEndTime();
        this.codingMeetingClosed = codingMeeting.getCodingMeetingClosed();
        this.createdDate = codingMeeting.getCreatedDate();
    }
    public static CodingMeetingListInfo of(CodingMeeting codingMeeting) {
        return CodingMeetingListInfo.builder()
                .codingMeeting(codingMeeting)
                .build();
    }
}
