package com.kernelsquare.domainmysql.domain.coding_meeting.repository.location;

import com.kernelsquare.domainmysql.domain.coding_meeting.command.CodingMeetingCommand;
import com.kernelsquare.domainmysql.domain.coding_meeting.entity.CodingMeeting;
import com.kernelsquare.domainmysql.domain.coding_meeting.entity.CodingMeetingLocation;

public interface CodingMeetingLocationFactory {
    CodingMeetingLocation store(CodingMeetingCommand.CreateCommand createCommand, CodingMeeting codingMeeting);
    void update(CodingMeetingCommand.UpdateCommand updateCommand, CodingMeeting codingMeeting);
    void delete(CodingMeeting codingMeeting);
}
