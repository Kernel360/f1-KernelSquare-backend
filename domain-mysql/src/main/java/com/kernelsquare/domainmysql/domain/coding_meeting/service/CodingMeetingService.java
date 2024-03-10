package com.kernelsquare.domainmysql.domain.coding_meeting.service;

import com.kernelsquare.domainmysql.domain.coding_meeting.command.CodingMeetingCommand;
import com.kernelsquare.domainmysql.domain.coding_meeting.info.CodingMeetingInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CodingMeetingService {
    CodingMeetingInfo.TokenInfo createCodingMeeting(CodingMeetingCommand.CreateCommand command, Long memberId);
    void updateCodingMeeting(CodingMeetingCommand.UpdateCommand command, String codingMeetingToken);
    void closeCodingMeeting(String codingMeetingToken);
    void deleteCodingMeeting(String codingMeetingToken);
    CodingMeetingInfo.Info findCodingMeeting(String codingMeetingToken);
    Page<CodingMeetingInfo.ListInfo> findAllCodingMeeting(Pageable pageable, String filterParameter, Long memberId);
}
