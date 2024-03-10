package com.kernelsquare.domainmysql.domain.coding_meeting.repository;

import com.kernelsquare.domainmysql.domain.coding_meeting.entity.CodingMeeting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CodingMeetingReader {
    CodingMeeting findCodingMeeting(String codingMeetingToken);
    Page<CodingMeeting> findAllCodingMeeting(Pageable pageable, String filterParameter, Long memberId);
}
