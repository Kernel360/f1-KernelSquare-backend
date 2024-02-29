package com.kernelsquare.domainmysql.domain.coding_meeting.command;

import com.kernelsquare.domainmysql.domain.coding_meeting.entity.CodingMeeting;
import com.kernelsquare.domainmysql.domain.coding_meeting.entity.CodingMeetingHashtag;
import com.kernelsquare.domainmysql.domain.coding_meeting.entity.CodingMeetingLocation;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

public class CodingMeetingCommand {
    @Builder
    public record CreateCommand(
            String codingMeetingTitle,
            String codingMeetingContent,
            LocalDateTime codingMeetingStartTime,
            LocalDateTime codingMeetingEndTime,
            Long codingMeetingMemberUpperLimit,
            String codingMeetingLocationId,
            String codingMeetingLocationPlaceName,
            String codingMeetingLocationLongitude,
            String codingMeetingLocationLatitude,
            List<String> codingMeetingHashtags
    ) {
        public CodingMeeting toEntity(Member member) {
            return CodingMeeting.builder()
                    .codingMeetingTitle(codingMeetingTitle)
                    .codingMeetingContent(codingMeetingContent)
                    .codingMeetingStartTime(codingMeetingStartTime)
                    .codingMeetingEndTime(codingMeetingEndTime)
                    .codingMeetingMemberUpperLimit(codingMeetingMemberUpperLimit)
                    .member(member)
                    .build();
        }
        public CreateLocationCommand toCodingMeetingLocation() {
            return CreateLocationCommand.builder()
                    .codingMeetingLocationId(codingMeetingLocationId)
                    .codingMeetingLocationPlaceName(codingMeetingLocationPlaceName)
                    .codingMeetingLocationLongitude(codingMeetingLocationLongitude)
                    .codingMeetingLocationLatitude(codingMeetingLocationLatitude)
                    .build();
        }
    }

    @Builder
    public record CreateHashtagCommand (
            String CodingMeetingHashtagContent
    ) {
        public CodingMeetingHashtag toEntity(CodingMeeting codingMeeting) {
            return CodingMeetingHashtag.builder()
                    .codingMeeting(codingMeeting)
                    .codingMeetingHashtagContent(CodingMeetingHashtagContent)
                    .build();
        }
    }

    @Builder
    public record CreateLocationCommand (
            String codingMeetingLocationId,
            String codingMeetingLocationPlaceName,
            String codingMeetingLocationLongitude,
            String codingMeetingLocationLatitude
    ) {
        public CodingMeetingLocation toEntity(CodingMeeting codingMeeting) {
            return CodingMeetingLocation.builder()
                    .codingMeeting(codingMeeting)
                    .codingMeetingLocationItemId(codingMeetingLocationId)
                    .codingMeetingLocationPlaceName(codingMeetingLocationPlaceName)
                    .codingMeetingLocationLongitude(codingMeetingLocationLongitude)
                    .codingMeetingLocationLatitude(codingMeetingLocationLatitude)
                    .build();
        }
    }

    @Builder
    public record UpdateCommand(
            String codingMeetingTitle,
            String codingMeetingContent,
            LocalDateTime codingMeetingStartTime,
            LocalDateTime codingMeetingEndTime,
            Long codingMeetingMemberUpperLimit,
            String codingMeetingLocationId,
            String codingMeetingLocationPlaceName,
            String codingMeetingLocationLongitude,
            String codingMeetingLocationLatitude,
            List<String> codingMeetingHashtags
    ) {
        public CreateLocationCommand toCodingMeetingLocation() {
            return CreateLocationCommand.builder()
                    .codingMeetingLocationId(codingMeetingLocationId)
                    .codingMeetingLocationPlaceName(codingMeetingLocationPlaceName)
                    .codingMeetingLocationLongitude(codingMeetingLocationLongitude)
                    .codingMeetingLocationLatitude(codingMeetingLocationLatitude)
                    .build();
        }
    }
}
