package com.kernelsquare.domainmysql.domain.coding_meeting.entity;

import com.kernelsquare.core.common_response.error.exception.InvalidParamException;
import com.kernelsquare.domainmysql.domain.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Objects;

@Entity(name = "CodingMeetingHashtag")
@Table(name = "coding_meeting_hashtag")
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CodingMeetingHashtag extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "coding_meeting_hashtag_content", columnDefinition = "varchar(30)")
    private String codingMeetingHashtagContent;

    @ManyToOne
    @JoinColumn(name = "coding_meeting_id", columnDefinition = "bigint", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private CodingMeeting codingMeeting;

    @Builder
    public CodingMeetingHashtag(
            String codingMeetingHashtagContent,
            CodingMeeting codingMeeting
    ) {

        if (StringUtils.isBlank(codingMeetingHashtagContent))
            throw new InvalidParamException("CodingMeetingHashtag.codingMeetingHashtagContent");
        if (Objects.isNull(codingMeeting))
            throw new InvalidParamException("CodingMeetingHashtag.codingMeeting");

        this.codingMeetingHashtagContent = codingMeetingHashtagContent;
        this.codingMeeting = codingMeeting;
    }
}
