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

@Entity(name = "CodingMeetingLocation")
@Table(name = "coding_meeting_location")
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CodingMeetingLocation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "coding_meeting_id", columnDefinition = "bigint", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private CodingMeeting codingMeeting;

    @Column(nullable = false, name = "coding_meeting_location_longitude", columnDefinition = "varchar(50)")
    private String codingMeetingLocationLongitude;

    @Column(nullable = false, name = "coding_meeting_location_latitude", columnDefinition = "varchar(50)")
    private String codingMeetingLocationLatitude;

    @Column(nullable = false, name = "coding_meeting_location_item_id", columnDefinition = "varchar(50)")
    private String codingMeetingLocationItemId;

    @Column(nullable = false, name = "coding_meeting_location_place_name", columnDefinition = "varchar(200)")
    private String codingMeetingLocationPlaceName;

    @Builder
    public CodingMeetingLocation(
            String codingMeetingLocationLongitude,
            String codingMeetingLocationLatitude,
            String codingMeetingLocationItemId,
            String codingMeetingLocationPlaceName,
            CodingMeeting codingMeeting
    ) {
        if (StringUtils.isBlank(codingMeetingLocationLongitude))
            throw new InvalidParamException("CodingMeetingLocation.codingMeetingLocationLongitude");
        if (StringUtils.isBlank(codingMeetingLocationLatitude))
            throw new InvalidParamException("CodingMeetingLocation.codingMeetingLocationLatitude");
        if (StringUtils.isBlank(codingMeetingLocationItemId))
            throw new InvalidParamException("CodingMeetingLocation.codingMeetingLocationItemId");
        if (StringUtils.isBlank(codingMeetingLocationPlaceName))
            throw new InvalidParamException("CodingMeetingLocation.codingMeetingLocationPlaceName");
        if (Objects.isNull(codingMeeting))
            throw new InvalidParamException("CodingMeetingLocation.codingMeeting");

        this.codingMeetingLocationLongitude = codingMeetingLocationLongitude;
        this.codingMeetingLocationLatitude = codingMeetingLocationLatitude;
        this.codingMeetingLocationItemId = codingMeetingLocationItemId;
        this.codingMeetingLocationPlaceName = codingMeetingLocationPlaceName;
        this.codingMeeting = codingMeeting;
    }
}
