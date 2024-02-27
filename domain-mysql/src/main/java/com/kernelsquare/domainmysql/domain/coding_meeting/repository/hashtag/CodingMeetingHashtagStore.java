package com.kernelsquare.domainmysql.domain.coding_meeting.repository.hashtag;

import com.kernelsquare.domainmysql.domain.coding_meeting.entity.CodingMeeting;
import com.kernelsquare.domainmysql.domain.coding_meeting.entity.CodingMeetingHashtag;

public interface CodingMeetingHashtagStore {
    CodingMeetingHashtag store(CodingMeetingHashtag initCodingMeetingHashtag);
    void deleteAll(Long codingMeetingId);
}
