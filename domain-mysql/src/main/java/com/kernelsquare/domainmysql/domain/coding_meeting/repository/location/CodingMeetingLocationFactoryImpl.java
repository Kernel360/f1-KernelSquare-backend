package com.kernelsquare.domainmysql.domain.coding_meeting.repository.location;

import com.kernelsquare.domainmysql.domain.coding_meeting.command.CodingMeetingCommand;
import com.kernelsquare.domainmysql.domain.coding_meeting.entity.CodingMeeting;
import com.kernelsquare.domainmysql.domain.coding_meeting.entity.CodingMeetingLocation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CodingMeetingLocationFactoryImpl implements CodingMeetingLocationFactory{
    private final CodingMeetingLocationStore codingMeetingLocationStore;
    @Override
    public CodingMeetingLocation store(CodingMeetingCommand.CreateCommand createCommand, CodingMeeting codingMeeting) {
        CodingMeetingCommand.CreateLocationCommand createdLocation = createCommand.toCodingMeetingLocation();
        return codingMeetingLocationStore.store(createdLocation.toEntity(codingMeeting));
    }

    @Override
    public void update(CodingMeetingCommand.UpdateCommand updateCommand, CodingMeeting codingMeeting) {
        CodingMeetingCommand.CreateLocationCommand createdLocation = updateCommand.toCodingMeetingLocation();
        codingMeetingLocationStore.delete(codingMeeting.getId());
        codingMeetingLocationStore.store(createdLocation.toEntity(codingMeeting));
    }

    @Override
    public void delete(CodingMeeting codingMeeting) {
        codingMeetingLocationStore.delete(codingMeeting.getId());
    }
}
