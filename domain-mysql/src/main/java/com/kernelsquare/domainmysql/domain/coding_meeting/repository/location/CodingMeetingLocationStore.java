package com.kernelsquare.domainmysql.domain.coding_meeting.repository.location;

import com.kernelsquare.domainmysql.domain.coding_meeting.entity.CodingMeetingLocation;

public interface CodingMeetingLocationStore {
    CodingMeetingLocation store(CodingMeetingLocation codingMeetingLocation);
    void delete(Long codingMeetingId);
}
