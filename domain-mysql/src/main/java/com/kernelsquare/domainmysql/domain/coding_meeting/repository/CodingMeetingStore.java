package com.kernelsquare.domainmysql.domain.coding_meeting.repository;

import com.kernelsquare.domainmysql.domain.coding_meeting.entity.CodingMeeting;

public interface CodingMeetingStore {
    CodingMeeting store(CodingMeeting initCodingMeeting, Long memberId);
    void delete(String CodingMeetingToken);
}
