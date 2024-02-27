package com.kernelsquare.domainmysql.domain.coding_meeting.service;

import com.kernelsquare.domainmysql.domain.coding_meeting.command.CodingMeetingCommand;
import com.kernelsquare.domainmysql.domain.coding_meeting.info.CodingMeetingInfo;
import com.kernelsquare.domainmysql.domain.coding_meeting.info.CodingMeetingListInfo;
import com.kernelsquare.domainmysql.domain.coding_meeting.info.CodingMeetingTokenInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CodingMeetingService {
    CodingMeetingTokenInfo createCodingMeeting(CodingMeetingCommand.CreateCommand command, Long memberId);
    void updateCodingMeeting(CodingMeetingCommand.UpdateCommand command, String codingMeetingToken);
    void closeCodingMeeting(String codingMeetingToken);
    void deleteCodingMeeting(String codingMeetingToken);
    CodingMeetingInfo findCodingMeeting(String codingMeetingToken);
    Page<CodingMeetingListInfo> findAllCodingMeeting(Pageable pageable, String filterParameter);
}
