package com.kernelsquare.domainmysql.domain.coding_meeting.info;

import com.kernelsquare.core.util.ImageUtils;
import com.kernelsquare.domainmysql.domain.coding_meeting.entity.CodingMeeting;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class CodingMeetingInfo {
    @Getter
    public static class Info {
        private final Long memberId;
        private final Long memberLevel;
        private final String memberNickname;
        private final String memberProfileUrl;
        private final String memberLevelImageUrl;
        private final String codingMeetingTitle;
        private final String codingMeetingToken;
        private final String codingMeetingContent;
        private final List<String> codingMeetingHashtags;
        private final String codingMeetingLocationId;
        private final String codingMeetingLocationPlaceName;
        private final String codingMeetingLocationLongitude;
        private final String codingMeetingLocationLatitude;
        private final Long codingMeetingMemberUpperLimit;
        private final LocalDateTime codingMeetingStartTime;
        private final LocalDateTime codingMeetingEndTime;
        private final Boolean codingMeetingClosed;

        @Builder
        public Info(CodingMeeting codingMeeting) {
            this.memberId = codingMeeting.getMember().getId();
            this.memberLevel = codingMeeting.getMember().getLevel().getName();
            this.memberNickname = codingMeeting.getMember().getNickname();
            this.memberProfileUrl = ImageUtils.makeImageUrl(codingMeeting.getMember().getImageUrl());
            this.memberLevelImageUrl = ImageUtils.makeImageUrl(codingMeeting.getMember().getLevel().getImageUrl());

            this.codingMeetingTitle = codingMeeting.getCodingMeetingTitle();
            this.codingMeetingToken = codingMeeting.getCodingMeetingToken();
            this.codingMeetingContent = codingMeeting.getCodingMeetingContent();
            this.codingMeetingHashtags = codingMeeting.getCodingMeetingHashtagStringList();

            this.codingMeetingLocationId = codingMeeting.getCodingMeetingLocation().getCodingMeetingLocationItemId();
            this.codingMeetingLocationPlaceName = codingMeeting.getCodingMeetingLocation().getCodingMeetingLocationPlaceName();
            this.codingMeetingLocationLongitude = codingMeeting.getCodingMeetingLocation().getCodingMeetingLocationLongitude();
            this.codingMeetingLocationLatitude = codingMeeting.getCodingMeetingLocation().getCodingMeetingLocationLatitude();

            this.codingMeetingMemberUpperLimit = codingMeeting.getCodingMeetingMemberUpperLimit();
            this.codingMeetingStartTime = codingMeeting.getCodingMeetingStartTime();
            this.codingMeetingEndTime = codingMeeting.getCodingMeetingEndTime();
            this.codingMeetingClosed = codingMeeting.getCodingMeetingClosed();
        }

        public static Info of(CodingMeeting codingMeeting) {
            return Info.builder()
                    .codingMeeting(codingMeeting)
                    .build();
        }
    }

    @Getter
    public static class ListInfo {
        private final Long memberId;
        private final Long memberLevel;
        private final String memberNickname;
        private final String memberProfileUrl;
        private final String memberLevelImageUrl;

        private final String codingMeetingTitle;
        private final String codingMeetingToken;
        private final List<String> codingMeetingHashtags;

        private final LocalDateTime codingMeetingStartTime;
        private final LocalDateTime codingMeetingEndTime;
        private final Boolean codingMeetingClosed;
        private final LocalDateTime createdDate;

        @Builder
        public ListInfo(CodingMeeting codingMeeting) {
            this.memberId = codingMeeting.getMember().getId();
            this.memberLevel = codingMeeting.getMember().getLevel().getName();
            this.memberNickname = codingMeeting.getMember().getNickname();
            this.memberProfileUrl = ImageUtils.makeImageUrl(codingMeeting.getMember().getImageUrl());
            this.memberLevelImageUrl = ImageUtils.makeImageUrl(codingMeeting.getMember().getLevel().getImageUrl());

            this.codingMeetingTitle = codingMeeting.getCodingMeetingTitle();
            this.codingMeetingToken = codingMeeting.getCodingMeetingToken();
            this.codingMeetingHashtags = codingMeeting.getCodingMeetingHashtagStringList();

            this.codingMeetingStartTime = codingMeeting.getCodingMeetingStartTime();
            this.codingMeetingEndTime = codingMeeting.getCodingMeetingEndTime();
            this.codingMeetingClosed = codingMeeting.getCodingMeetingClosed();
            this.createdDate = codingMeeting.getCreatedDate();
        }

        public static ListInfo of(CodingMeeting codingMeeting) {
            return ListInfo.builder()
                    .codingMeeting(codingMeeting)
                    .build();
        }
    }

    @Getter
    public static class TokenInfo {
        private final String codingMeetingToken;

        @Builder
        public TokenInfo(CodingMeeting codingMeeting) {
            this.codingMeetingToken = codingMeeting.getCodingMeetingToken();
        }

        public static TokenInfo of(CodingMeeting codingMeeting) {
            return TokenInfo.builder()
                    .codingMeeting(codingMeeting)
                    .build();
        }
    }
}
