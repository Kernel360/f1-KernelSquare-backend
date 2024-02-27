package com.kernelsquare.domainmysql.domain.coding_meeting.info;

import com.kernelsquare.domainmysql.domain.coding_meeting.entity.CodingMeeting;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class CodingMeetingInfo {
    private final Long memberId;
    private final Long memberLevel;
    private final String memberNickname;
    private final String memberProfileUrl;
    private final String memberLevelImageUrl;

    private final String codingMeetingTitle;
    private final String codingMeetingToken;
    private final String codingMeetingContent;
    private final List<String> codingMeetingHashtags;

    private final String codingMeetingLocationId;
    private final String codingMeetingLocationPlaceName;
    private final String codingMeetingLocationLongitude;
    private final String codingMeetingLocationLatitude;

    private final Long codingMeetingMemberUpperLimit;
    private final LocalDateTime codingMeetingStartTime;
    private final LocalDateTime codingMeetingEndTime;
    private final Boolean codingMeetingClosed;

    @Builder
    public CodingMeetingInfo(CodingMeeting codingMeeting) {

        this.memberId = codingMeeting.getMember().getId();
        this.memberLevel = codingMeeting.getMember().getLevel().getName();
        this.memberNickname = codingMeeting.getMember().getNickname();
        this.memberProfileUrl = codingMeeting.getMember().getImageUrl();
        this.memberLevelImageUrl = codingMeeting.getMember().getLevel().getImageUrl();

        this.codingMeetingTitle = codingMeeting.getCodingMeetingTitle();
        this.codingMeetingToken = codingMeeting.getCodingMeetingToken();
        this.codingMeetingContent = codingMeeting.getCodingMeetingContent();
        this.codingMeetingHashtags = codingMeeting.getCodingMeetingHashtags();

        this.codingMeetingLocationId = codingMeeting.getCodingMeetingLocation().getCodingMeetingLocationItemId();
        this.codingMeetingLocationPlaceName = codingMeeting.getCodingMeetingLocation().getCodingMeetingLocationPlaceName();
        this.codingMeetingLocationLongitude = codingMeeting.getCodingMeetingLocation().getCodingMeetingLocationLongitude();
        this.codingMeetingLocationLatitude = codingMeeting.getCodingMeetingLocation().getCodingMeetingLocationLatitude();

        this.codingMeetingMemberUpperLimit = codingMeeting.getCodingMeetingMemberUpperLimit();
        this.codingMeetingStartTime = codingMeeting.getCodingMeetingStartTime();
        this.codingMeetingEndTime = codingMeeting.getCodingMeetingEndTime();
        this.codingMeetingClosed = codingMeeting.getCodingMeetingClosed();
    }

    public static CodingMeetingInfo of(CodingMeeting codingMeeting) {
        return CodingMeetingInfo.builder()
                .codingMeeting(codingMeeting)
                .build();
    }
}
