package com.kernelsquare.domainmysql.domain.coding_meeting_comment.entity;

import com.kernelsquare.core.common_response.error.exception.InvalidParamException;
import com.kernelsquare.core.util.TokenGenerator;
import com.kernelsquare.domainmysql.domain.base.BaseEntity;
import com.kernelsquare.domainmysql.domain.coding_meeting.entity.CodingMeeting;
import com.kernelsquare.domainmysql.domain.coding_meeting_comment.command.CodingMeetingCommentCommand;
import com.kernelsquare.domainmysql.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Objects;

@Entity(name = "CodingMeetingComment")
@Table(name = "coding_meeting_comment")
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CodingMeetingComment extends BaseEntity {
    @Transient
    private final String CODING_MEETING_COMMENT_PREFIX = "cmc_";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", columnDefinition = "bigint", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coding_meeting_id", columnDefinition = "bigint", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private CodingMeeting codingMeeting;

    @Column(nullable = false, unique = true, name = "coding_meeting_comment_token", columnDefinition = "varchar(50)")
    private String codingMeetingCommentToken;

    @Column(nullable = false, name = "coding_meeting_comment_content", columnDefinition = "varchar(100)")
    private String codingMeetingCommentContent;

    @Builder
    public CodingMeetingComment(
            String codingMeetingCommentContent,
            Member member,
            CodingMeeting codingMeeting
    ) {

        if (StringUtils.isBlank(codingMeetingCommentContent))
            throw new InvalidParamException("CodingMeetingComment.codingMeetingCommentContent");
        if (Objects.isNull(member))
            throw new InvalidParamException("CodingMeetingComment.member");
        if (Objects.isNull(codingMeeting))
            throw new InvalidParamException("CodingMeetingComment.codingMeeting");

        this.codingMeetingCommentToken = TokenGenerator.randomCharacterWithPrefix(CODING_MEETING_COMMENT_PREFIX);
        this.codingMeetingCommentContent = codingMeetingCommentContent;
        this.codingMeeting = codingMeeting;
        this.member = member;
    }

    public void update(CodingMeetingCommentCommand.UpdateCommand command) {
        this.codingMeetingCommentContent = command.codingMeetingCommentContent();
    }
}
