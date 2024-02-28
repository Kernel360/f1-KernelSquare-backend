package com.kernelsquare.domainmysql.domain.coding_meeting.repository.location;

import com.kernelsquare.domainmysql.domain.coding_meeting.entity.CodingMeetingLocation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CodingMeetingLocationStoreImpl implements CodingMeetingLocationStore {
    private final CodingMeetingLocationRepository codingMeetingLocationRepository;

    @Override
    public CodingMeetingLocation store(CodingMeetingLocation codingMeetingLocation) {
        return codingMeetingLocationRepository.save(codingMeetingLocation);
    }

    @Override
    public void delete(Long codingMeetingId) {
        codingMeetingLocationRepository.deleteAllByCodingMeetingId(codingMeetingId);
    }
}
