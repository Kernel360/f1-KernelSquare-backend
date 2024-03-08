package com.kernelsquare.domainmysql.domain.coding_meeting.entity;

import com.kernelsquare.core.common_response.error.exception.InvalidParamException;
import com.kernelsquare.core.util.TokenGenerator;
import com.kernelsquare.domainmysql.domain.base.BaseEntity;
import com.kernelsquare.domainmysql.domain.coding_meeting.command.CodingMeetingCommand;
import com.kernelsquare.domainmysql.domain.coding_meeting_comment.entity.CodingMeetingComment;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Entity(name = "CodingMeeting")
@Table(name = "coding_meeting")
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CodingMeeting extends BaseEntity {
    @Transient
    private final String CODING_MEETING_PREFIX = "cm_";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "coding_meeting_title", columnDefinition = "varchar(100)")
    private String codingMeetingTitle;

    @Column(nullable = false, unique = true, name = "coding_meeting_token", columnDefinition = "varchar(50)")
    private String codingMeetingToken;

    @Column(nullable = false, name = "coding_meeting_start_time", columnDefinition = "datetime")
    private LocalDateTime codingMeetingStartTime;

    @Column(nullable = false, name = "coding_meeting_end_time", columnDefinition = "datetime")
    private LocalDateTime codingMeetingEndTime;

    @Column(nullable = false, name = "coding_meeting_content", columnDefinition = "text")
    private String codingMeetingContent;

    @Column(nullable = false, name = "coding_meeting_member_upper_limit", columnDefinition = "bigint")
    private Long codingMeetingMemberUpperLimit;

    @Column(nullable = false, name = "coding_meeting_closed", columnDefinition = "tinyint")
    private Boolean codingMeetingClosed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", columnDefinition = "bigint", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "codingMeeting", cascade = CascadeType.PERSIST)
    private CodingMeetingLocation codingMeetingLocation;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "codingMeeting", cascade = CascadeType.PERSIST)
    private List<CodingMeetingHashtag> codingMeetingHashtags;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "codingMeeting", cascade = CascadeType.PERSIST)
    private List<CodingMeetingComment> codingMeetingComments;

    @Builder
    public CodingMeeting(
            String codingMeetingTitle,
            String codingMeetingContent,
            LocalDateTime codingMeetingStartTime,
            LocalDateTime codingMeetingEndTime,
            Long codingMeetingMemberUpperLimit,
            Member member
    ) {

        if (StringUtils.isBlank(codingMeetingTitle))
            throw new InvalidParamException("CodingMeeting.codingMeetingTitle");
        if (StringUtils.isBlank(codingMeetingContent))
            throw new InvalidParamException("CodingMeeting.codingMeetingContent");
        if (Objects.isNull(codingMeetingStartTime))
            throw new InvalidParamException("CodingMeeting.codingMeetingStartTime");
        if (Objects.isNull(codingMeetingEndTime))
            throw new InvalidParamException("CodingMeeting.codingMeetingEndTime");
        if (Objects.isNull(codingMeetingMemberUpperLimit))
            throw new InvalidParamException("CodingMeeting.codingMeetingMemberUpperLimit");
        if (Objects.isNull(member))
            throw new InvalidParamException("CodingMeeting.member");

        this.codingMeetingToken = TokenGenerator.randomCharacterWithPrefix(CODING_MEETING_PREFIX);
        this.codingMeetingTitle = codingMeetingTitle;
        this.codingMeetingContent = codingMeetingContent;
        this.codingMeetingStartTime = codingMeetingStartTime;
        this.codingMeetingEndTime = codingMeetingEndTime;
        this.codingMeetingMemberUpperLimit = codingMeetingMemberUpperLimit;
        this.codingMeetingClosed = false;
        this.member = member;
    }

    public void update(CodingMeetingCommand.UpdateCommand command) {
        this.codingMeetingTitle = command.codingMeetingTitle();
        this.codingMeetingContent = command.codingMeetingContent();
        this.codingMeetingStartTime = command.codingMeetingStartTime();
        this.codingMeetingEndTime = command.codingMeetingEndTime();
        this.codingMeetingMemberUpperLimit = command.codingMeetingMemberUpperLimit();
    }

    public void close() {
        this.codingMeetingClosed = true;
    }

    public List<String> getCodingMeetingHashtagStringList() {
        return Optional.ofNullable(codingMeetingHashtags)
                .map(hashTag -> hashTag.stream()
                        .map(CodingMeetingHashtag::getCodingMeetingHashtagContent)
                        .toList())
                .orElse(Collections.emptyList());
    }
}