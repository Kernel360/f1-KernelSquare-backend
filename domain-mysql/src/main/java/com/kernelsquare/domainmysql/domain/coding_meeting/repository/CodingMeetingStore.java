package com.kernelsquare.domainmysql.domain.coding_meeting.repository;

import com.kernelsquare.domainmysql.domain.coding_meeting.entity.CodingMeeting;

public interface CodingMeetingStore {
    CodingMeeting store(CodingMeeting initCodingMeeting);
    void delete(String CodingMeetingToken);
}
