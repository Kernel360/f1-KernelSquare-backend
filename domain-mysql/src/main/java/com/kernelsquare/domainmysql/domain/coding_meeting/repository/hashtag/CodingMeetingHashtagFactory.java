package com.kernelsquare.domainmysql.domain.coding_meeting.repository.hashtag;

import com.kernelsquare.domainmysql.domain.coding_meeting.command.CodingMeetingCommand;
import com.kernelsquare.domainmysql.domain.coding_meeting.entity.CodingMeeting;
import com.kernelsquare.domainmysql.domain.coding_meeting.entity.CodingMeetingHashtag;

import java.util.List;

public interface CodingMeetingHashtagFactory {
    List<CodingMeetingHashtag> store(CodingMeetingCommand.CreateCommand createCommand, CodingMeeting codingMeeting);
    void update(CodingMeetingCommand.UpdateCommand command, CodingMeeting codingMeeting);
    void delete(CodingMeeting codingMeeting);
}
