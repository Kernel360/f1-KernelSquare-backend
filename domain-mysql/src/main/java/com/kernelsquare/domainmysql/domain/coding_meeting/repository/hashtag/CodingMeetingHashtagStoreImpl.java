package com.kernelsquare.domainmysql.domain.coding_meeting.repository.hashtag;

import com.kernelsquare.domainmysql.domain.coding_meeting.entity.CodingMeeting;
import com.kernelsquare.domainmysql.domain.coding_meeting.entity.CodingMeetingHashtag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CodingMeetingHashtagStoreImpl implements CodingMeetingHashtagStore {

    private final CodingMeetingHashtagRepository codingMeetingHashtagRepository;
    @Override
    public CodingMeetingHashtag store(CodingMeetingHashtag initCodingMeetingHashtag) {
        return codingMeetingHashtagRepository.save(initCodingMeetingHashtag);
    }

    @Override
    public void deleteAll(Long codingMeetingId) {
        codingMeetingHashtagRepository.deleteAllByCodingMeetingId(codingMeetingId);
    }
}
