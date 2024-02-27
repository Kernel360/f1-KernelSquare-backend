package com.kernelsquare.domainmysql.domain.coding_meeting.repository.hashtag;

import com.kernelsquare.domainmysql.domain.coding_meeting.command.CodingMeetingCommand;
import com.kernelsquare.domainmysql.domain.coding_meeting.entity.CodingMeeting;
import com.kernelsquare.domainmysql.domain.coding_meeting.entity.CodingMeetingHashtag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CodingMeetingHashtagFactoryImpl implements CodingMeetingHashtagFactory{
    private final CodingMeetingHashtagStore codingMeetingHashtagStore;

    @Override
    public List<CodingMeetingHashtag> store(CodingMeetingCommand.CreateCommand command, CodingMeeting codingMeeting) {
        List<String> createdHashtags = command.codingMeetingHashtags();
        return createdHashtags.stream()
                .map(
                        hashtag -> {
                            CodingMeetingHashtag initCodingMeetingHashtag = CodingMeetingHashtag
                                    .builder()
                                    .codingMeetingHashtagContent(hashtag)
                                    .codingMeeting(codingMeeting)
                                    .build();
                            return codingMeetingHashtagStore.store(initCodingMeetingHashtag);
                        }
                ).toList();
    }

    @Override
    public void update(CodingMeetingCommand.UpdateCommand command, CodingMeeting codingMeeting) {
        List<String> updatedHashtags = command.codingMeetingHashtags();
        codingMeetingHashtagStore.deleteAll(codingMeeting.getId());
        for (String hashtag : updatedHashtags) {
            CodingMeetingHashtag initCodingMeetingHashtag = CodingMeetingHashtag.builder()
                    .codingMeetingHashtagContent(hashtag)
                    .codingMeeting(codingMeeting)
                    .build();
            codingMeetingHashtagStore.store(initCodingMeetingHashtag);
        }
    }

    @Override
    public void delete(CodingMeeting codingMeeting) {
        codingMeetingHashtagStore.deleteAll(codingMeeting.getId());
    }
}
