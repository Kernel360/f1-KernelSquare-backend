package com.kernelsquare.domainmysql.domain.coding_meeting.info;

import com.kernelsquare.domainmysql.domain.coding_meeting.entity.CodingMeeting;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CodingMeetingTokenInfo {
    private final String codingMeetingToken;
    @Builder
    public CodingMeetingTokenInfo(CodingMeeting codingMeeting) {
        this.codingMeetingToken = codingMeeting.getCodingMeetingToken();
    }
    public static CodingMeetingTokenInfo of(CodingMeeting codingMeeting) {
        return CodingMeetingTokenInfo.builder()
                .codingMeeting(codingMeeting)
                .build();
    }
}
